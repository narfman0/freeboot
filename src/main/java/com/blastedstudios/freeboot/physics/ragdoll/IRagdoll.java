package com.blastedstudios.freeboot.physics.ragdoll;

import net.xeoh.plugins.base.Plugin;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.being.Being.BodyPart;
import com.blastedstudios.freeboot.world.weapon.DamageStruct;

public interface IRagdoll{
	static interface IRagdollPlugin extends Plugin{
		boolean canCreate(String resource);
		IRagdoll create(World world, float x, float y, Being being, TextureAtlas atlas, FileHandle file);
	}
	
	void setFriction(float friction);
	boolean standingOn(Contact contact);
	boolean aim(float heading);
	void death(World world, DamageStruct shotDamage);
	Vector2 getPosition();
	Vector2 getLinearVelocity();
	void setLinearVelocity(float x, float y);
	void applyLinearImpulse(float i, float j, float x, float y);
	void applyForceAtCenter(float x, float y);
	void setTransform(float x, float y, float angle);
	boolean isOwned(Fixture fixture);
	boolean isFoot(Fixture fixture);
	void dispose(World world);
	BodyPart getBodyPart(Fixture fixture);
	Body getBodyPart(BodyPart part);
	void render(Batch batch, boolean dead, boolean isGrounded,
			boolean isMoving, float velX, boolean paused, boolean inputEnabled);
	boolean isFixedRotation();
	void setFixedRotation(boolean fixedRotation);
	void applyTorque(float torque);
	boolean isFacingLeft();
	void breakAppendage(BodyPart bodyPart, World world, Vector2 dir);
	void setTextureAtlas(final TextureAtlas atlas);
	Body getHandFacing();
	/**
	 * @return position of the hand according to which direction the ragdoll is facing.
	 * If facing left, will return the right hand (as it is "behind" and further out)
	 */
	Vector2 getHandFacingPosition();
	void setAlpha(float alpha);
}