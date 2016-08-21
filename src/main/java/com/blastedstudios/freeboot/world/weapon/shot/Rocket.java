package com.blastedstudios.freeboot.world.weapon.shot;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.being.Being.BodyPart;
import com.blastedstudios.freeboot.world.weapon.Gun;
import com.blastedstudios.freeboot.world.weapon.RocketLauncher;

public class Rocket extends GunShot {
	private final ParticleEffect trail, explosion;
	
	public Rocket(Being origin, Vector2 dir, Gun gun){
		super(origin, dir, gun);
		trail = new ParticleEffect();
		trail.load(Gdx.files.internal("data/particles/rocketTrail.p"), Gdx.files.internal("data/particles"));
		trail.start();
		trail.setDuration(99999);
		explosion = new ParticleEffect();
		explosion.load(Gdx.files.internal("data/particles/rocketExplosion.p"), Gdx.files.internal("data/particles"));
	}
	
	@Override public void render(float dt, Batch batch, AssetManager assetManager, 
			Body body, WorldManager worldManager){
		super.render(dt, batch, assetManager, body, worldManager);
		trail.setPosition(body.getPosition().x, body.getPosition().y);
		trail.draw(batch, dt);
		explosion.setPosition(body.getPosition().x, body.getPosition().y);
	}

	@Override public void beginContact(Body gunshotBody, Fixture hit, WorldManager worldManager, WorldManifold manifold){
		RocketLauncher launcher = (RocketLauncher) gun;//TODO: make generic
		HashMap<Being, Float> nearbyBeings = new HashMap<>();
		//provide impulse on bodies
		for(Body worldBody : worldManager.getBodiesIterable()){
			Vector2 direction = worldBody.getWorldCenter().cpy().sub(gunshotBody.getPosition());
			float distance = direction.len();
			if(distance < launcher.getMaxDistance()){
				if(worldBody.getUserData() instanceof Being){
					float closestDistance = distance;
					if(nearbyBeings.containsKey(worldBody.getUserData()))
						closestDistance = Math.min(nearbyBeings.get(worldBody.getUserData()), distance);
					nearbyBeings.put((Being) worldBody.getUserData(), closestDistance);
				}
				//if a being, this is huge because each body gets in
				Vector2 impulse = direction.nor().scl(launcher.getBaseImpulse() / (float)Math.max(1, distance));
				worldBody.applyLinearImpulse(impulse, worldBody.getPosition(), true);
			}
		}
		//handle damage on nearby players
		for(Entry<Being,Float> being : nearbyBeings.entrySet()){
			Log.log("RocketLauncher.handleContact","Calculate rocket baseDamage for " + being.getKey().getName());
			worldManager.processHit(launcher.calculateLinearDmg(being.getValue()), being.getKey(), getBeing(), 
					being.getKey().getRagdoll().getBodyPart(BodyPart.torso).getFixtureList().get(0), 
					manifold.getNormal(), manifold.getPoints()[0]);
		}
		//send off particles to particle manager
		explosion.setPosition(gunshotBody.getPosition().x, gunshotBody.getPosition().y);
		explosion.start();
		Sound sound = worldManager.getSharedAssets().get("data/sounds/smallRocketExplosion.mp3", Sound.class);
		WorldManager.playSoundTuned(sound, gunshotBody.getPosition(), worldManager.getPlayer().getPosition());
		trail.setDuration(1500);
		worldManager.transferParticles(trail, explosion);

		gunshotBody.setUserData(WorldManager.REMOVE_USER_DATA);
		setCanRemove(true);
	}
}
