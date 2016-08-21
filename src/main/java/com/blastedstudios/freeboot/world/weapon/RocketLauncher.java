package com.blastedstudios.freeboot.world.weapon;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.weapon.shot.GunShot;
import com.blastedstudios.freeboot.world.weapon.shot.Rocket;

public class RocketLauncher extends Gun {
	private static final long serialVersionUID = 1L;
	private float maxDistance, baseDamage, baseImpulse;

	@Override public String toString(){
		return "[RocketLauncher name:" + name + " dmg: " + getDamage() + "]";
	}
	
	@Override public GunShot createGunShot(Being origin, Vector2 dir){
		return new Rocket(origin, dir, this);
	}
	
	public float calculateLinearDmg(float distance){
		return baseDamage*( (maxDistance-distance)/maxDistance );
	}

	public float getBaseImpulse() {
		return baseImpulse;
	}

	public void setBaseImpulse(float baseImpulse) {
		this.baseImpulse = baseImpulse;
	}

	public float getBaseDamage() {
		return baseDamage;
	}

	public void setBaseDamage(float baseDamage) {
		this.baseDamage = baseDamage;
	}

	public float getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(float maxDistance) {
		this.maxDistance = maxDistance;
	}
}
