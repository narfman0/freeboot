package com.blastedstudios.freeboot.world.being;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.GDXPath;
import com.blastedstudios.freeboot.ai.AIWorld;
import com.blastedstudios.freeboot.network.Messages.NetBeing.FactionEnum;
import com.blastedstudios.freeboot.ui.gameplay.GameplayNetReceiver;
import com.blastedstudios.freeboot.world.Stats;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.weapon.DamageStruct;
import com.blastedstudios.freeboot.world.weapon.Gun;
import com.blastedstudios.freeboot.world.weapon.Melee;
import com.blastedstudios.freeboot.world.weapon.Weapon;

import jbt.execution.core.BTExecutorFactory;
import jbt.execution.core.ContextFactory;
import jbt.execution.core.IBTExecutor;
import jbt.execution.core.IBTLibrary;
import jbt.execution.core.IContext;

public class NPC extends Being {
	public enum AIFieldEnum{AI_WORLD, ALERT, ATTACK_TICK, OBJECTIVE, SELF, WORLD, TURRET, VISIBLE}
	public enum DifficultyEnum{HARD, MEDIUM, EASY}
	private static final long serialVersionUID = 1L;
	private IContext context;
	private IBTExecutor btExecutor;
	private GDXPath path;
	private final DifficultyEnum difficulty;
	private final boolean vendor, boss;
	private final LinkedList<Weapon> vendorWeapons;
	
	public NPC(String name, List<Weapon> guns, List<Weapon> inventory, Stats stats,
			int currentGun, int cash, int level, int xp, String behavior,
			GDXPath path, FactionEnum faction, EnumSet<FactionEnum> factions,
			WorldManager world, String resource, String ragdollResource, 
			DifficultyEnum difficulty, AIWorld aiWorld, boolean vendor,
			LinkedList<Weapon> vendorWeapons, boolean boss) {
		super(name, guns, inventory, stats, currentGun, cash, level, xp, 
				faction, factions, resource, ragdollResource);
		this.difficulty = difficulty;
		this.vendor = vendor;
		this.vendorWeapons = vendorWeapons;
		this.boss = boss;
		applyBehaviorTree(behavior, aiWorld, world, path);
	}
	
	public void applyBehaviorTree(String behavior, AIWorld aiWorld, WorldManager world, GDXPath path){
		final String basePackage = "com.blastedstudios.freeboot.ai.bt.trees";
		try{
			Class<?> btLibClass = Class.forName(basePackage+"."+behavior);
			IBTLibrary library = (IBTLibrary) btLibClass.newInstance();
			context = ContextFactory.createContext(library);
			context.setVariable(AIFieldEnum.AI_WORLD.name(), aiWorld);
			context.setVariable(AIFieldEnum.SELF.name(), this);
			context.setVariable(AIFieldEnum.WORLD.name(), world);
			setPath(path);
			btExecutor = BTExecutorFactory.createBTExecutor(library.getBT("Root"), context);
		}catch(Exception e){
			Log.error("NPC.<init>", "Error making NPC: " + name + " with message: " + e.getMessage());
		}
	}
	
	public void update(float dt, World world, IDeathCallback callback,
			boolean paused, boolean inputEnabled, boolean simulate, GameplayNetReceiver receiver){
		super.update(dt, world, callback, paused, inputEnabled, receiver);
		if(!dead && btExecutor != null && simulate)
			btExecutor.tick();
	}

	public GDXPath getPath() {
		return path;
	}
	
	public void setPath(GDXPath path){
		this.path = path;
		stopMovement();
		if(path != null)
			context.setVariable(AIFieldEnum.OBJECTIVE.name(), path.clone());
		else{
			context.clearVariable(AIFieldEnum.OBJECTIVE.name());
			context.clearVariable("CurrentObjectiveTarget");//make sure its removed
		}
	}
	
	@Override public void reload(){
		super.reload();
		Weapon gun = getEquippedWeapon();
		if(gun != null && !(gun instanceof Melee))
			((Gun)gun).addCurrentRounds(gun.getRoundsPerClip() - ((Gun)gun).getCurrentRounds());
	}
	
	@Override public float handleShotDamage(Fixture bodyPart, DamageStruct shotDamage){
		context.setVariable(AIFieldEnum.ATTACK_TICK.name(), shotDamage);
		return super.handleShotDamage(bodyPart, shotDamage);
	}

	public DifficultyEnum getDifficulty() {
		return difficulty;
	}
	
	/**
	 * Used as a handicap/difficulty slider, this function provides the value
	 * for NPCs to wait before shooting the next round so they don't slam a
	 * low level/easy difficulty player with an endless hail of bullets.
	 * @param level of the NPC shooting
	 * @param difficulty of the game, lower difficulty will increase time
	 * @return Delay before an NPC of the given level may shoot his weapon. 
	 */
	public static long shootDelay(int level, DifficultyEnum difficulty){
		return 1000 - ((2-difficulty.ordinal()) * 300) - (level * 25);
	}
	
	public float getDistanceAware(){
		return stats.getDistanceAware();
	}
	
	public float getDistanceVision(){
		return stats.getDistanceVision();
	}

	public boolean isVendor() {
		return vendor;
	}

	public LinkedList<Weapon> getVendorWeapons() {
		return vendorWeapons;
	}

	public boolean isBoss() {
		return boss;
	}
	
	@Override public long addXp(long xp){
		if(name.matches(Properties.get("npc.levelup.name.match", "noone")))
			return super.addXp(xp);
		return this.xp;
	}
}
