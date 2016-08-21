package com.blastedstudios.freeboot.world.weapon;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.being.Being.BodyPart;

public class DamageStruct {
	private Being target, origin;
	private float damage;
	private Vector2 dir, damagePosition;
	private BodyPart bodyPart;
	
	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public Vector2 getDir() {
		return dir;
	}

	public void setDir(Vector2 dir) {
		this.dir = dir;
	}

	public BodyPart getBodyPart() {
		return bodyPart;
	}

	public void setBodyPart(BodyPart bodyPart) {
		this.bodyPart = bodyPart;
	}

	public Being getTarget() {
		return target;
	}

	public void setTarget(Being target) {
		this.target = target;
	}

	public Being getOrigin() {
		return origin;
	}

	public void setOrigin(Being origin) {
		this.origin = origin;
	}

	public Vector2 getDamagePosition() {
		return damagePosition;
	}

	public void setDamagePosition(Vector2 damagePosition) {
		this.damagePosition = damagePosition;
	}
}
