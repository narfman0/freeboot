package com.blastedstudios.freeboot.world.being;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.FileUtil;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.network.Messages.Attack;
import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.blastedstudios.freeboot.network.Messages.NetBeing;
import com.blastedstudios.freeboot.network.Messages.NetBeing.FactionEnum;
import com.blastedstudios.freeboot.network.Messages.NetWeapon;
import com.blastedstudios.freeboot.physics.PhysicsEnvironment;
import com.blastedstudios.freeboot.physics.VisibleQueryCallback;
import com.blastedstudios.freeboot.physics.ragdoll.IRagdoll;
import com.blastedstudios.freeboot.physics.ragdoll.IRagdoll.IRagdollPlugin;
import com.blastedstudios.freeboot.ui.gameplay.GameplayNetReceiver;
import com.blastedstudios.freeboot.util.UUIDConvert;
import com.blastedstudios.freeboot.util.VectorHelper;
import com.blastedstudios.freeboot.world.Stats;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.activity.BaseActivity;
import com.blastedstudios.freeboot.world.being.component.IComponent;
import com.blastedstudios.freeboot.world.weapon.AmmoTypeEnum;
import com.blastedstudios.freeboot.world.weapon.DamageStruct;
import com.blastedstudios.freeboot.world.weapon.Gun;
import com.blastedstudios.freeboot.world.weapon.Melee;
import com.blastedstudios.freeboot.world.weapon.Weapon;
import com.blastedstudios.freeboot.world.weapon.WeaponFactory;

public class Being implements Serializable{
	private static final long serialVersionUID = 1L;
	public final static int MAX_GUNS = Properties.getInt("guns.max", 4);
	public final static boolean INFINITE_AMMO = Properties.getBool("ammo.infinite", true);
	protected final static float ACCELEROMETER_ROLL_THRESHOLD = Properties.getFloat("accelerometer.roll.threshold", 3f),
			CHARACTER_IMPULSE_MAGNITUDE = Properties.getFloat("character.impulse.magnitude", 2f),
			CHARACTER_JUMP_IMPULSE = Properties.getFloat("character.jump.impulse", 70),
			MAX_VELOCITY = Properties.getFloat("character.velocity.max", 7f);
	protected static final HashMap<BodyPart,Float> bodypartDmgMap = new HashMap<>();
	protected final HashMap<AmmoTypeEnum,Integer> ammo = new HashMap<>();
	protected transient boolean jump, moveRight, moveLeft, dead, reloading, invulnerable;
	protected transient IRagdoll ragdoll = null;
	protected String name;
	protected String resource, ragdollResource;
	protected float hp;
	protected Stats stats;
	protected List<Weapon> guns, inventory;
	protected int currentWeapon, cash, level;
	protected long xp;
	protected FactionEnum faction;
	protected EnumSet<FactionEnum> friendlyFactions;
	protected transient float lastGunHeadingRadians, timeUntilReload;
	protected transient int ticksToActivateWeapon = -1;
	protected transient DamageStruct lastDamage;
	protected transient Random random;
	protected transient List<IComponent> listeners;
	protected transient AssetManager sharedAssets;
	private transient UUID uuid;
	private transient BaseActivity activity;

	public Being(String name, List<Weapon> guns, List<Weapon> inventory, Stats stats,
			int currentWeapon, int cash, int level, int xp, FactionEnum faction,
			EnumSet<FactionEnum> factions, String resource, String ragdollResource){
		this.name = name;
		this.guns = guns;
		this.inventory = inventory;
		this.currentWeapon = currentWeapon;
		this.cash = cash;
		this.level = level;
		this.xp = xp;
		this.faction = faction;
		this.friendlyFactions = factions;
		this.resource = resource;
		this.stats = stats;
		this.ragdollResource = ragdollResource;
		for(AmmoTypeEnum ammoType : AmmoTypeEnum.values()){
			for(Weapon weapon : guns)
				if(weapon instanceof Gun && ((Gun)weapon).getAmmoType() == ammoType)
					ammo.put(ammoType, ((Gun)weapon).getRoundsPerClip() * 2);
			if(!ammo.containsKey(ammoType))
				ammo.put(ammoType, 0);
		}
		if(bodypartDmgMap.isEmpty())
			initializeBodypartDmgMap();
	}
	
	public Being(NetBeing message){
		this(message.getName(), new LinkedList<>(), new LinkedList<>(), new Stats(), 
				message.getCurrentWeapon(), 0, 0, 0, FactionEnum.valueOf(message.getFaction().name()),
				EnumSet.noneOf(FactionEnum.class), message.getResource(), message.getRagdollResource());
		setUuid(UUIDConvert.convert(message.getUuid()));
		stats.setHp(message.getHp());
		for(NetWeapon netWeapon : message.getWeaponsList())
			guns.add(WeaponFactory.getWeapon(netWeapon.getId()));
	}
	
	public void update(float dt, World world, IDeathCallback deathCallback, boolean paused, 
			boolean inputEnabled, GameplayNetReceiver receiver){
		if(ticksToActivateWeapon != -1 && getEquippedWeapon() != null && ticksToActivateWeapon-- == 0)
			getEquippedWeapon().activate(world, ragdoll, this);
		Vector2 vel = ragdoll.getLinearVelocity();
		if(paused)
			return;
		if(activity != null)
			if(!activity.render(dt))
				activity = null;
		if(!dead && hp <= 0 && deathCallback != null)
			deathCallback.dead(this);
		if(dead)
			return;
		setHp(hp + getHpRegen() * dt);

		// cap max velocity on x		
		if(Math.abs(vel.x) > MAX_VELOCITY) {			
			vel.x = Math.signum(vel.x) * MAX_VELOCITY;
			ragdoll.setLinearVelocity(vel.x, vel.y);
		}

		// calculate stilltime & damp
		if(!moveLeft && !moveRight)		
			ragdoll.setLinearVelocity(vel.x * 0.8f, vel.y);

		if(jump && vel.y < CHARACTER_JUMP_IMPULSE)
			jump(world);

		// apply left impulse, but only if max velocity is not reached yet
		if(moveLeft && vel.x > -MAX_VELOCITY ||
				Gdx.input.getRoll() < -ACCELEROMETER_ROLL_THRESHOLD)
			walk(true, world);
		// apply right impulse, but only if max velocity is not reached yet
		if(moveRight && vel.x < MAX_VELOCITY ||
				Gdx.input.getRoll() > ACCELEROMETER_ROLL_THRESHOLD)
			walk(false, world);
		reload();
		timeUntilReload = Math.max(0f, timeUntilReload-dt);
	}

	public void render(float dt, World world, Batch batch, AssetManager sharedAssets,
			GDXRenderer gdxRenderer, IDeathCallback deathCallback, boolean paused, 
			boolean inputEnabled, GameplayNetReceiver receiver){
		this.sharedAssets = sharedAssets;
		Vector2 vel = ragdoll.getLinearVelocity();
		boolean facingLeft = !isDead() && ragdoll.aim(lastGunHeadingRadians);
		ragdoll.render(batch, dead, isGrounded(world), moveLeft || moveRight, vel.x, paused, inputEnabled);
		for(IComponent component : getListeners())
			component.render(dt, batch, sharedAssets, gdxRenderer, facingLeft, paused);
	}
	
	private float getHpRegen() {
		return stats.getHpRegen() + level * stats.getHpRegenPerLevel();
	}

	private void walk(boolean left, World world){
		List<Vector2> normals = getTouchedNormals(world);
		final Vector2 normal = normals.isEmpty() ? new Vector2(0f,1f) : VectorHelper.calculateAverage(normals);
		if(isTravelNormalValid(normal)){
			double desiredHeading = Math.atan2(normal.y, normal.x) + (left ? Math.PI/2.0 : Math.PI/-2.0);
			float i = (float)Math.cos(desiredHeading) * CHARACTER_IMPULSE_MAGNITUDE, 
					j = (float)Math.sin(desiredHeading) * CHARACTER_IMPULSE_MAGNITUDE;
			ragdoll.applyLinearImpulse(i, j, ragdoll.getPosition().x, ragdoll.getPosition().y);
		}
	}
	
	private List<Vector2> getTouchedNormals(World world){
		List<Vector2> normals = new LinkedList<>();
		for(Contact contact :  world.getContactList())
			if(contact.isTouching() && (
					(ragdoll.isFoot(contact.getFixtureA()) && !ragdoll.isOwned(contact.getFixtureB())) ||
					(ragdoll.isFoot(contact.getFixtureB()) && !ragdoll.isOwned(contact.getFixtureA())) )){
				Vector2 normal = contact.getWorldManifold().getNormal().cpy(); 
				//Next few lines are a hack. The contact is always NPC first since I think the solver
				//iterates on NPCs last thus see collision last. Don't know - either way, in the following case,
				//normal must always be flipped. Would be more graceful to find out more, but alas. Time.
				if(contact.getFixtureA().getUserData() != null && contact.getFixtureB().getUserData() != null &&
						ragdoll.isOwned(contact.getFixtureB()))
					normal.scl(-1f);
				normals.add(normal);
			}
		return normals;
	}
	
	public boolean sees(Being being, World world){
		VisibleQueryCallback callback = new VisibleQueryCallback(this, being);
		world.rayCast(callback, getPosition(), being.getPosition());
		return !callback.called;
	}
	
	public boolean isGrounded(World world){
		return !getTouchedNormals(world).isEmpty();
	}
	
	private void jump(World world){
		Vector2 dir = null;
		List<Vector2> normals = getTouchedNormals(world);
		if(!normals.isEmpty()){
			if(Properties.getBool("being.jump.usenormal", false)){
				dir = normals.get(0).cpy();
				for(Vector2 normal : normals)
					if(normal.y > dir.y)
						dir.set(normal);
				//	dir.add(normal);
				//dir.div(normals.size());
			}else
				for(Vector2 normal : normals)
					if(isTravelNormalValid(normal))
						dir = new Vector2(0f,1f);
		}
		if(dir != null){
			jump = false;
			jumpImmediate(dir.cpy().scl(CHARACTER_JUMP_IMPULSE));
			for(IComponent component : getListeners())
				component.jump(world);
		}
	}
	
	public void jumpImmediate(Vector2 impulse){
		ragdoll.setLinearVelocity(ragdoll.getLinearVelocity().x, 0);
		ragdoll.applyLinearImpulse(impulse.x, impulse.y, ragdoll.getPosition().x, ragdoll.getPosition().y);
	}
	
	/**
	 * @return true if being can exert force when touching the given normal
	 */
	protected boolean isTravelNormalValid(Vector2 normal){
		double normalAngle = Math.abs(Math.atan2(normal.y, normal.x)),
				anglePref = Properties.getFloat("being.jump.normal.angle", .5f);
		return normalAngle > anglePref && normalAngle < Math.PI - anglePref;
	}

	/**
	 * Reload gun if reload criteria met (reload time passed)
	 */
	public void reload(){
		Weapon weapon = getEquippedWeapon();
		if(weapon != null && !(weapon instanceof Melee) && reloading && timeUntilReload <= 0f){
			reloadImmediate((Gun)weapon);
			Log.log("Being.reload", name + "'s gun " + weapon.getName() + " reloaded to " + 
					((Gun)weapon).getCurrentRounds() + " rounds");
			reloading = false;
		}
	}
	
	private void reloadImmediate(Gun gun){
		int reloadAmount = Math.min(gun.getRoundsPerClip(), ammo.get(gun.getAmmoType()));
		if(INFINITE_AMMO)
			reloadAmount = gun.getRoundsPerClip();
		addAmmo(gun.getAmmoType(), -gun.setCurrentRounds(reloadAmount));
	}

	/**
	 * This method both returns the damage multiplier and sets the body part
	 * that received the damage form the shot
	 * @param bodyPart which got shot
	 * @param shotDamage populate with body part that was shot
	 * @return dmg multiplier
	 */
	public float handleShotDamage(Fixture bodyPart, DamageStruct shotDamage){
		shotDamage.setBodyPart(ragdoll.getBodyPart(bodyPart));
		if(bodypartDmgMap.isEmpty())
			initializeBodypartDmgMap();
		return bodypartDmgMap.get(shotDamage.getBodyPart());
	}

	public void death(WorldManager worldManager) {
		ragdoll.death(worldManager.getWorld(), lastDamage);
		if(lastDamage != null && lastDamage.getOrigin() != null && lastDamage.getOrigin() != this)
			lastDamage.getOrigin().addXp(Properties.getBool("being.xp.usestatic", false) ? xp : 
				calculateXPWorth(this, lastDamage.getOrigin().getLevel()));
		lastDamage = null;
		dead = true;
		reloading = false;
		if(getEquippedWeapon() != null)
			getEquippedWeapon().death(worldManager.getWorld());
		for(IComponent component : getListeners())
			component.death(worldManager);
	}
	
	public static long calculateXPWorth(Being mob, int charLevel){
		int xp = (charLevel * 5) + 45;
		if(charLevel < mob.getLevel())
			xp *= (1f + .05f * (mob.getLevel() - charLevel));
		else if(charLevel > mob.getLevel())
			xp *= (1f - ((charLevel - mob.getLevel()) / (5f + 2f * charLevel)));
		if(mob instanceof NPC && ((NPC)mob).isBoss())
			xp *= 3;
		return xp;
	}
	
	public boolean isSpawned(){
		return ragdoll != null;
	}

	public void respawn(World world, float x, float y) {
		random = new Random();
		dead = false;
		hp = getMaxHp();
		dispose(world);

		FileHandle atlasHandle = FileUtil.find(Gdx.files.internal("data/textures/characters"), resource);
		if(atlasHandle == null)
			atlasHandle = FileUtil.find(Gdx.files.internal("data/textures/characters"), resource + ".atlas");
		TextureAtlas atlas = new TextureAtlas(atlasHandle != null ? atlasHandle : 
			Gdx.files.internal("data/textures/characters/dummy.atlas"));
		for(IRagdollPlugin plugin : PluginUtil.getPlugins(IRagdollPlugin.class))
			if(plugin.canCreate(ragdollResource))
				ragdoll = plugin.create(world, x, y, this, atlas, Gdx.files.internal("data/world/npc/ragdoll/" + ragdollResource));
		
		for(Weapon weapon : guns){
			if(!(weapon instanceof Melee)){
				Gun gun = (Gun) weapon;
				if(gun.getCurrentRounds() == 0)
					gun.addCurrentRounds(random.nextInt(3) + 3);
				reloadImmediate(gun);
			}
		}
		if(getEquippedWeapon() != null)
			ticksToActivateWeapon = 5;
		Log.log("Being.respawn", name + " initialized at " + x + "," + y);
		for(IComponent component : getListeners())
			component.respawn(world, x, y);
	}
	
	/**
	 * Clean character from level when exiting
	 */
	public void dispose(World world){
		if(getEquippedWeapon() != null)
			getEquippedWeapon().dispose(world);
		if(ragdoll != null)
			ragdoll.dispose(world);
		ragdoll = null;
	}

	public boolean buy(Weapon weapon, World world){
		boolean afford = cash >= weapon.getCost();
		if(afford){
			cash -= weapon.getCost();
			receive(weapon, world);
		}
		return afford;
	}
	
	public void receive(Weapon weapon, World world){
		if(guns.size() < MAX_GUNS){
			guns.add(weapon);
			if(guns.size() == 1)
				setCurrentWeapon(0, world, true);
		}else
			inventory.add(weapon);
	}

	public void aim(float heading){
		lastGunHeadingRadians = heading;
		for(IComponent component : getListeners())
			component.aim(heading);
	}
	
	public void aim(Being being){
		aim(getAimAngle(this, being));
	}
	
	public static float getAimAngle(Being origin, Being target){
		return (float) Math.atan2(
				target.getPosition().y - origin.getPosition().y, 
				target.getPosition().x - origin.getPosition().x);
	}

	public void setJump(boolean jump){
		this.jump = jump;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector2 getPosition(){
		if(ragdoll == null)
			return null;
		return ragdoll.getPosition().cpy();
	}

	public void setPosition(float x, float y, float angle) {
		ragdoll.setTransform(x,y, angle);
	}

	public void setVelocity(Vector2 velocity) {
		ragdoll.setLinearVelocity(velocity.x, velocity.y);
	}

	public Vector2 getVelocity() {
		return ragdoll.getLinearVelocity().cpy();
	}

	@Override public String toString(){
		return "name:" + name + " loc=" + getPosition();
	}

	public boolean isMoveRight() {
		return moveRight;
	}

	public void setMoveRight(boolean moveRight) {
		this.moveRight = moveRight;
	}

	public boolean isMoveLeft() {
		return moveLeft;
	}

	public void setMoveLeft(boolean moveLeft) {
		this.moveLeft = moveLeft;
	}

	public float getHp() {
		return hp;
	}

	public void setHp(float hp) {
		this.hp = Math.min(Math.max(0, hp), getMaxHp());
		for(IComponent component : getListeners())
			component.setHp(hp);
	}

	public int getCurrentGun() {
		return currentWeapon;
	}

	public void setCurrentWeapon(int currentWeapon, World world, boolean force) {
		if(!force && this.currentWeapon == currentWeapon)
			return; //dont do anything if its the same, duh
		if(getEquippedWeapon() != null)
			getEquippedWeapon().dispose(world);
		this.currentWeapon = currentWeapon;
		reloading = false;
		if(getEquippedWeapon() != null)
			getEquippedWeapon().activate(world, ragdoll, this);
		for(IComponent component : getListeners())
			component.setCurrentWeapon(currentWeapon, world);
	}

	public Weapon getEquippedWeapon(){
		if(guns.size() > currentWeapon)
			return guns.get(currentWeapon);
		return null;
	}

	public List<Weapon> getGuns() {
		return guns;
	}

	public void setGuns(List<Weapon> guns) {
		this.guns = guns;
	}

	public boolean isDead(){
		return dead;
	}

	public int getCash() {
		return cash;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}

	public void addCash(int cash){
		this.cash += cash;
		Log.log("Being.addCash", name + " received " + cash + " cash");
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getXp() {
		return xp;
	}

	public long setXp(long xp) {
		return this.xp = xp;
	}

	public long addXp(long xp){
		this.xp += xp;
		Log.log("Being.addXp", name + " received " + xp + " xp, total xp is " + this.xp);
		while(this.xp > xpToLevel(level+1))
			levelUp();
		for(IComponent component : getListeners())
			component.addXp(xp);
		return this.xp;
	}
	
	public void levelUp(){
		++level;
		if(Properties.getBool("being.levelup.restorehp", false))
			hp = getMaxHp();
		Log.log("Being.levelUp", name + " now level " + level);
		for(IComponent component : getListeners())
			component.levelUp();
	}

	public boolean isOwned(Fixture fixture){
		if(getEquippedWeapon() != null && getEquippedWeapon() instanceof Melee){
			Melee meleeWeapon = (Melee) getEquippedWeapon(); 
			if(meleeWeapon.getBody() == null)
				Log.error("Being.isOwned", "Melee body null for: " + getName() + "'s weapon: " + meleeWeapon);
			else
				for(Fixture meleeFixture : meleeWeapon.getBody().getFixtureList())
					if(fixture == meleeFixture)
						return true;
		}
		// need to double check, being could be destroyed before gunshot collides 
		return isSpawned() && ragdoll.isOwned(fixture);
	}

	public boolean isReloading() {
		return reloading;
	}

	public void setReloading(boolean reloading) {
		if(!this.reloading && reloading && getEquippedWeapon() != null){
			if(sharedAssets != null)
				sharedAssets.get("data/sounds/guns/reload.mp3", Sound.class).play(Properties.getFloat("sound.volume", 1f));
			timeUntilReload = (float)getEquippedWeapon().getReloadSpeed()/1000f;
			Log.log("Being.setReloading", name + " began reloading");
			for(IComponent component : getListeners())
				component.setReloading(reloading);
		}
		this.reloading = reloading;
	}

	public IRagdoll getRagdoll(){
		return ragdoll;
	}

	public DamageStruct getLastDamage() {
		return lastDamage;
	}

	public EnumSet<FactionEnum> getFactions() {
		return friendlyFactions;
	}

	public void setFactions(EnumSet<FactionEnum> factions) {
		this.friendlyFactions = factions;
	}

	public FactionEnum getFaction() {
		return faction;
	}

	public void setFaction(FactionEnum faction) {
		this.faction = faction;
	}
	
	public boolean isFriendly(FactionEnum faction){
		return this.faction == faction || friendlyFactions.contains(faction);
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
		if(ragdoll != null)
			ragdoll.setTextureAtlas(new TextureAtlas(
					Gdx.files.internal("data/textures/characters/" + resource + ".atlas")));
	}

	public boolean canAttack(){
		Weapon equipped = getEquippedWeapon();
		return !isDead() && !reloading && equipped != null && equipped.canAttack();
	}

	public boolean attack(Vector2 direction, WorldManager world){
		if(isDead())
			return false;
		Weapon weapon = getEquippedWeapon();
		if(canAttack()){
			// send network message request first, so clients start mulling it over
			Attack.Builder builder = Attack.newBuilder();
			if(uuid != null)
				builder.setUuid(UUIDConvert.convert(uuid));
			else
				builder.setName(name);
			builder.setPosX(direction.x);
			builder.setPosY(direction.y);
			world.getReceiver().send(MessageType.ATTACK, builder.build());
			//process as normal
			if(!(weapon instanceof Melee)){
				Gun gun = (Gun) weapon;
				gun.shoot(this, random, direction, world, ragdoll.getHandFacing().getPosition());
			}
			return true;
		}else if(weapon != null && !(weapon instanceof Melee) && 
				(INFINITE_AMMO || ammo.get(((Gun)weapon).getAmmoType()) > 0) && 
				((Gun)weapon).getCurrentRounds() == 0 && !reloading)
			setReloading(true);
		for(IComponent component : getListeners())
			component.attack(direction);
		return false;
	}

	public HashMap<AmmoTypeEnum,Integer> getAmmo() {
		return ammo;
	}

	public void addAmmo(AmmoTypeEnum ammoType, int amount){
		ammo.put(ammoType, ammo.get(ammoType) + amount);
		Log.log("Being.addAmmo", name + " received " + amount + " ammo added for " + ammoType);
	}

	public enum BodyPart {
		head,
		torso,
		lArm,
		rArm,
		lLeg,
		rLeg,
		lHand,
		rHand
	}

	/**
	 * ripped from http://www.wowwiki.com/Formulas:XP_To_Level
	 * @return xp required to get to given level
	 */
	public static long xpToLevel(int level){
		return (long) (level <= 11 ? 40 * Math.pow(level, 2) + 360 * level :
			level <= 27 ? -.4 * Math.pow(level, 3) + 40.4 * Math.pow(level, 2) + 396 * level :
				/*level <= 59 ?*/ (65 * Math.pow(level, 2) - 165 * level - 6750) * .82);
	}

	/**
	 * Interface for death listener. When this being dies, the callback will be invoked
	 * with the being to be processes for death.
	 */
	public interface IDeathCallback{
		public void dead(Being being);
	}

	public float getAttack() {
		float equippedGunAddition = getEquippedWeapon() == null ? 0 : getEquippedWeapon().getAttackPerLevel() * level + getEquippedWeapon().getAttack();
		return stats.getAttack() + stats.getAttackPerLevel() * level + equippedGunAddition;
	}

	public float getDefense() {
		float equippedGunAddition = getEquippedWeapon() == null ? 0 : getEquippedWeapon().getDefensePerLevel() * level + getEquippedWeapon().getDefense();
		return stats.getDefense() + stats.getDefensePerLevel() * level + equippedGunAddition;
	}

	public float getMaxHp() {
		float equippedGunAddition = getEquippedWeapon() == null ? 0 : getEquippedWeapon().getHpPerLevel() * level + getEquippedWeapon().getHp();
		return stats.getHp() + stats.getHpPerLevel() * level + equippedGunAddition;
	}

	public List<Weapon> getInventory() {
		return inventory;
	}

	public void setInventory(List<Weapon> inventory) {
		this.inventory = inventory;
	}
	
	public short getMask(){
		return faction == FactionEnum.FRIEND ? PhysicsEnvironment.MASK_FRIEND : PhysicsEnvironment.MASK_ENEMY;
	}
	
	public short getCat(){
		return faction == FactionEnum.FRIEND ? PhysicsEnvironment.CAT_FRIEND : PhysicsEnvironment.CAT_ENEMY;
	}
	
	public void stopMovement(){
		moveLeft = false;
		moveRight = false;
		jump = false;
		for(IComponent component : getListeners())
			component.stopMovement();
	}

	public void setFixedRotation(boolean fixedRotation) {
		if(ragdoll != null && fixedRotation != ragdoll.isFixedRotation()){
			ragdoll.setFixedRotation(fixedRotation);
			for(IComponent component : getListeners())
				component.setFixedRotation(fixedRotation);
		}
	}
	
	public Stats getStats(){
		return stats;
	}

	public void receivedDamage(DamageStruct damage) {
		this.lastDamage = damage;
		for(IComponent component : getListeners())
			component.receivedDamage(damage);
	}
	
	public List<IComponent> getListeners(){
		if(listeners == null){
			listeners = new LinkedList<>();
			for(IComponent component : PluginUtil.getPlugins(IComponent.class)){
				IComponent initialized = component.initialize(this);
				if(initialized != null)
					listeners.add(initialized);
			}
		}
		return listeners;
	}
	
	public void addListener(IComponent listener){
		listeners.add(listener);
	}
	
	private void initializeBodypartDmgMap(){
		for(BodyPart bodyType : BodyPart.values())
			bodypartDmgMap.put(bodyType, Properties.getFloat("character.bodypart." + bodyType.name() + ".dmgmultiplier", 1f));
	}

	public void setInvulnerable(boolean invuln) {
		this.invulnerable = invuln;
	}

	public boolean isInvulnerable() {
		return invulnerable;
	}
	
	public NetBeing buildMessage(boolean complete){
		NetBeing.Builder builder = NetBeing.newBuilder();
		if(uuid != null)
			builder.setUuid(UUIDConvert.convert(uuid));
		builder.setName(name);
		builder.setHp(getHp());
		builder.setMaxHp(getMaxHp());
		builder.setAim(lastGunHeadingRadians);
		if(getPosition() != null){
			builder.setPosX(getPosition().x);
			builder.setPosY(getPosition().y);
			builder.setVelX(getVelocity().x);
			builder.setVelY(getVelocity().y);
		}
		if(complete){
			builder.setCurrentWeapon(currentWeapon);
			for(Weapon weapon : guns)
				builder.addWeapons(weapon.buildMessage());
			builder.setResource(resource);
			if(ragdollResource != null && !ragdollResource.isEmpty())
				builder.setRagdollResource(ragdollResource);
		}
		return builder.build();
	}
	
	public void updateFromMessage(NetBeing update){
		getRagdoll().getBodyPart(BodyPart.torso).setTransform(update.getPosX(), update.getPosY(), 0);
		setVelocity(new Vector2(update.getVelX(), update.getVelY()));
		aim(update.getAim());
		setHp(update.getHp());
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public void applyActivity(BaseActivity activity) {
		this.activity = activity;
	}
}
