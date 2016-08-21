package com.blastedstudios.freeboot.world.weapon;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;

public class Turret {
	private final Gun gun;
	private final Vector2 location, mountLocation;
	private final float directionLow, directionHigh;
	private final Random random;
	private final String baseResource;
	private float direction;
	private Sprite gunSprite = null, weaponBaseSprite = null;
	private long reloadTime;
	
	public Turret(Gun gun, String baseResource, Vector2 location, Vector2 mountLocation,
			float direction, float directionLow, float directionHigh, Random random){
		this.gun = gun;
		this.baseResource = baseResource;
		this.location = location;
		this.mountLocation = mountLocation;
		this.direction = direction;
		this.directionLow = directionLow;
		this.directionHigh = directionHigh;
		this.random = random;
	}
	
	public void aim(float direction){
		this.direction = Math.max(directionLow, Math.min(directionHigh, direction));
	}
	
	public void update(float dt, WorldManager worldManager) {
		if(reloadTime != 0l && System.currentTimeMillis() > reloadTime){
			gun.setCurrentRounds(gun.getRoundsPerClip());
			reloadTime = 0l;
		}
	}
	
	public void render(float dt, Batch batch, GDXRenderer gdxRenderer, WorldManager worldManager) {
		if(gunSprite == null){
			String gunPath = "data/textures/weapons/" + gun.getResource() + ".png",
				basePath = "data/textures/weapons/" + baseResource;
			gunSprite = new Sprite(worldManager.getSharedAssets().get(gunPath, Texture.class));
			gunSprite.setScale(Properties.getFloat("ragdoll.custom.scale"));
			gunSprite.setPosition(location.x - gunSprite.getWidth()/2f, location.y - gunSprite.getHeight()/2f);
			weaponBaseSprite = new Sprite(worldManager.getSharedAssets().get(basePath, Texture.class));
			weaponBaseSprite.setScale(Properties.getFloat("turret.scale", .02f));
			weaponBaseSprite.setPosition(location.x - weaponBaseSprite.getWidth()/2f, location.y - weaponBaseSprite.getHeight()/2f);
		}
		gunSprite.setRotation((float)Math.toDegrees(direction));
		gunSprite.draw(batch);
		weaponBaseSprite.draw(batch);
	}
	
	public void shoot(Being source, WorldManager worldManager){
		if(gun.canAttack())
			gun.shoot(source, random, new Vector2((float)Math.cos(direction), (float)Math.sin(direction)),
					worldManager, location);
		if(gun.getCurrentRounds() <= 0 && reloadTime == 0l)
			reloadTime = System.currentTimeMillis() + (long)gun.getReloadSpeed();
	}
	
	public Vector2 getLocation(){
		return location;
	}

	public float getDirection() {
		return direction;
	}

	public Vector2 getMountLocation() {
		return mountLocation;
	}
}
