package com.blastedstudios.freeboot.world.weapon;

import java.io.Serializable;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.freeboot.network.Messages.NetWeapon;
import com.blastedstudios.freeboot.physics.ragdoll.IRagdoll;
import com.blastedstudios.freeboot.world.Stats;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;

public abstract class Weapon implements Serializable{
	private static final long serialVersionUID = 1L;
	protected int cost, rolls, minLevel;
	protected Stats stats;
	protected String name, resource, fireSound, id;
	
	public float getDamage() {
		return stats.getDamage();
	}

	public void setDamage(float damage) {
		stats.setDamage(damage);
	}

	public float getAccuracy() {
		return stats.getAccuracy();
	}

	public float getRateOfFire() {
		return stats.getRateOfFire();
	}

	public float getReloadSpeed() {
		return stats.getReloadSpeed();
	}

	public float getMuzzleVelocity() {
		return stats.getMuzzleVelocity();
	}

	public float getRecoil() {
		return stats.getRecoil();
	}

	public int getProjectileCount() {
		return stats.getProjectileCount();
	}

	public int getRoundsPerClip() {
		return stats.getRoundsPerClip();
	}
	
	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override public String toString(){
		return "[Gun name:" + name + " dmg: " + getDamage() + "]";
	}
	
	public String getResource() {
		return resource;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	public float getAttack() {
		return stats.getAttack();
	}
	
	public float getDefense() {
		return stats.getDefense();
	}
	
	public float getHp() {
		return stats.getHp();
	}
	
	public float getAttackPerLevel() {
		return stats.getAttackPerLevel();
	}
	
	public float getDefensePerLevel() {
		return stats.getDefensePerLevel();
	}
	
	public float getHpPerLevel() {
		return stats.getHpPerLevel();
	}
	
	public Stats getStats() {
		return stats;
	}
	
	public void setStats(Stats stats) {
		this.stats = stats;
	}
	
	public String getFireSound() {
		return fireSound;
	}
	
	public void setFireSound(String fireSound) {
		this.fireSound = fireSound;
	}
	
	public String toStringPretty(Being owner) {
		return name;
	}

	public int getRolls() {
		return rolls;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public abstract long getMSSinceAttack();
	public abstract boolean canAttack();
	
	public void beginContact(WorldManager worldManager, Being target, 
			Fixture hit, Contact contact, float i){}
	public void activate(World world, IRagdoll ragdoll, Being owner){}
	public void dispose(World world){}
	public void death(World world){}
	
	public NetWeapon buildMessage(){
		NetWeapon.Builder builder = NetWeapon.newBuilder();
		builder.setId(id);
		return builder.build();
	}
}
