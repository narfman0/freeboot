package com.blastedstudios.freeboot.world.being.component;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.weapon.DamageStruct;

public abstract class AbstractComponent implements IComponent{
	protected Being being;
	
	/**
	 * Initialize with being and clone self since JSPF plugins are singletons
	 * (and every being needs their own component)
	 */
	public IComponent initialize(Being being){
		this.being = being;
		try {
			return (IComponent) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void render(float dt, Batch batch, AssetManager sharedAssets,
			GDXRenderer gdxRenderer, boolean facingLeft, boolean paused){}
	public void update(float dt, boolean facingLeft, boolean paused){}
	public void receivedDamage(DamageStruct damage){}
	public void dash(boolean right){}
	public void setFixedRotation(boolean fixedRotation){}
	public void stopMovement(){}
	public void attack(Vector2 direction){}
	public void setReloading(boolean reloading){}
	public void addXp(long xp){}
	public void levelUp(){}
	public void setCurrentWeapon(int currentWeapon, World world){}
	public void setHp(float hp){}
	public void aim(float heading){}
	public void respawn(World world, float x, float y){}
	public void death(WorldManager worldManager){}
	public void jump(World world){}
	public boolean keyDown(int key, WorldManager worldManager){return false;}
	public boolean keyUp(int key, WorldManager worldManager){return false;}
}
