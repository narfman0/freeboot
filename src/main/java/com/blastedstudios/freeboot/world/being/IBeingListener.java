package com.blastedstudios.freeboot.world.being;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.weapon.DamageStruct;

public interface IBeingListener{
	public void receivedDamage(DamageStruct damage);
	public void setFixedRotation(boolean fixedRotation);
	public void stopMovement();
	public void attack(Vector2 direction);
	public void setReloading(boolean reloading);
	public void addXp(long xp);
	public void levelUp();
	public void setCurrentWeapon(int currentWeapon, World world);
	public void setHp(float hp);
	public void aim(float heading);
	public void respawn(World world, float x, float y);
	public void death(WorldManager worldManager);
	public void jump(World world);
}
