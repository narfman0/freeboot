package com.blastedstudios.freeboot.world.weapon;

import java.util.Random;

import net.xeoh.plugins.base.Plugin;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.freeboot.physics.PhysicsEnvironment;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.weapon.shot.GunShot;

public class Gun extends Weapon {
	private static final long serialVersionUID = 1L;
	private int currentRounds;
	private AmmoTypeEnum ammoType;
	private boolean semiAutomatic;
	private long lastFireTime;
	private transient boolean attackReleased;
	
	public GunShot createGunShot(Being origin, Vector2 dir){
		return new GunShot(origin, dir, this);
	}

	public int getCurrentRounds() {
		return currentRounds;
	}

	public int setCurrentRounds(int currentRounds) {
		this.currentRounds = currentRounds;
		for(IGunListener listener : PluginUtil.getPlugins(IGunListener.class))
			listener.setCurrentRounds(this, currentRounds);
		return currentRounds;
	}
	
	public int addCurrentRounds(int currentRounds) {
		return setCurrentRounds(this.currentRounds + currentRounds);
	}
	
	public AmmoTypeEnum getAmmoType() {
		return ammoType;
	}
	
	public void setAmmoType(AmmoTypeEnum ammoType) {
		this.ammoType = ammoType;
	}
	
	@Override public long getMSSinceAttack(){
		return System.currentTimeMillis() - lastFireTime;
	}
	
	@Override public String toString(){
		return "[Gun name:" + name + " dmg: " + getDamage() + " currentRounds:" + currentRounds + "]";
	}
	
	@Override public String toStringPretty(Being owner) {
		int rightHandSide = Being.INFINITE_AMMO ? getRoundsPerClip() : owner.getAmmo().get(ammoType);
		return name + ": " + currentRounds + "/" + rightHandSide;
	}
	
	public void shoot(Being source, Random random, Vector2 direction,
			WorldManager world, Vector2 position){
		currentRounds -= 1;
		lastFireTime = System.currentTimeMillis();
		for(int i=0; i<getProjectileCount(); i++){
			float degrees = (float) (random.nextGaussian() * 20 * (1f-getAccuracy()));
			Vector2 dir = direction.cpy().rotate(degrees);
			GunShot gunshot = createGunShot(source, dir);
			Body body = PhysicsEnvironment.createBullet(world.getWorld(), gunshot, position);
			world.getGunshots().put(body, gunshot);
		}
		for(IGunListener listener : PluginUtil.getPlugins(IGunListener.class))
			listener.shoot(this, source, random, direction, world, position, world.getSharedAssets());
		attackReleased = false;
	}
	
	@Override public boolean canAttack(){
		boolean rateOfFireCheck = getMSSinceAttack()/1000f > 1f/getRateOfFire();
		return (rateOfFireCheck || (semiAutomatic && attackReleased)) && getCurrentRounds() > 0;
	}

	public static interface IGunListener extends Plugin{
		void shoot(Gun gun, Being source, Random random, Vector2 direction,
				WorldManager world, Vector2 origin, AssetManager sharedAssets);
		void setCurrentRounds(Gun gun, int currentRounds);
	}
	
	public void setAttackReleased(boolean attackReleased) {
		this.attackReleased = attackReleased;
	}

	public boolean isSemiAutomatic() {
		return semiAutomatic;
	}
}
