package com.blastedstudios.freeboot.world.weapon.shot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.weapon.Gun;

public class Flame extends GunShot {
	public final ParticleEffect flame;
	private long timeToRemove = -1l;
	private BodyType bodyType = null;
	
	public Flame(Being origin, Vector2 dir, Gun gun){
		super(origin, dir, gun);
		flame = new ParticleEffect();
		flame.load(Gdx.files.internal("data/particles/flame.p"), Gdx.files.internal("data/particles"));
		flame.start();
		flame.setDuration(99999);
	}
	
	@Override public void render(float dt, Batch batch, AssetManager assetManager, 
			Body body, WorldManager worldManager){
		super.render(dt, batch, assetManager, body, worldManager);
		flame.setPosition(body.getPosition().x, body.getPosition().y);
		flame.draw(batch, dt);
		if(isTimeToRemoveSet() && System.currentTimeMillis() >= timeToRemove){
			worldManager.transferParticles(flame);
			body.setUserData(WorldManager.REMOVE_USER_DATA);
			setCanRemove(true);
		}
		if(bodyType != null && bodyType != body.getType())
			body.setType(bodyType);
	}
	
	@Override public void beginContact(Body gunshotBody, Fixture hit, WorldManager worldManager, WorldManifold manifold){
		if(!isTimeToRemoveSet()){
			int duration = Properties.getInt("flame.contact.duration", 1500);
			timeToRemove = System.currentTimeMillis() + duration;
			bodyType = BodyType.StaticBody;
			flame.setDuration(duration);
		}
	}
	
	public boolean isTimeToRemoveSet(){
		return timeToRemove != -1l;
	}
}
