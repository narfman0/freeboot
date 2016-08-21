package com.blastedstudios.freeboot.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.weapon.Melee;
import com.blastedstudios.freeboot.world.weapon.shot.GunShot;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {
	private final WorldManager worldManager;
	
	public ContactListener(WorldManager worldManager){
		this.worldManager = worldManager;
	}
	
	@Override public void preSolve(Contact contact, Manifold arg1) {
		GunShot gunshot = (GunShot) (contact.getFixtureA().getBody().getUserData() instanceof GunShot ? contact.getFixtureA().getBody().getUserData() :
			contact.getFixtureB().getBody().getUserData() instanceof GunShot ? contact.getFixtureB().getBody().getUserData() : null);
		Fixture hit = contact.getFixtureA().getBody().getUserData() instanceof Being ? contact.getFixtureA() :
			contact.getFixtureB().getBody().getUserData() instanceof Being ? contact.getFixtureB() : null;
		if(gunshot != null && !gunshot.collideWithOrigin() && gunshot.getBeing().isOwned(hit))
			contact.setEnabled(false);
	}
	
	@Override public void beginContact(Contact contact) {
		Body aBody = contact.getFixtureA().getBody(), bBody = contact.getFixtureB().getBody();
		Body gunshotBody = aBody.getUserData() instanceof GunShot ? aBody :	bBody.getUserData() instanceof GunShot ? bBody : null;
		Fixture hit = aBody.getUserData() instanceof Being ? contact.getFixtureA() : bBody.getUserData() instanceof Being ? contact.getFixtureB() : null;
		Body meleeBody = aBody.getUserData() instanceof Melee ? aBody : bBody.getUserData() instanceof Melee ? bBody : null;
		if(aBody.getUserData() == WorldManager.REMOVE_USER_DATA || bBody.getUserData() == WorldManager.REMOVE_USER_DATA)
			return;
			
		if(gunshotBody != null){//handle projectile contact
			GunShot gunshot = (GunShot) gunshotBody.getUserData();
			if(gunshot.collideWithOrigin() || !gunshot.getBeing().isOwned(hit)){//skip if self
				if(hit != null){//handle getting shot
					Being target = (Being) hit.getBody().getUserData();
					worldManager.processHit(gunshot.getGun().getDamage(), target, gunshot.getBeing(), 
							hit, contact.getWorldManifold().getNormal(), contact.getWorldManifold().getPoints()[0]);
				}
				gunshot.beginContact(gunshotBody, hit, worldManager, contact.getWorldManifold());
			}
		}else if(meleeBody != null && hit != null){//handle melee attack
			Being target = (Being) hit.getBody().getUserData();
			Melee melee = (Melee) meleeBody.getUserData();
			melee.beginContact(worldManager, target, hit, contact, calculateMomentumImpulse(contact));
		}else if(hit != null){//handle physics object collision dmg
			Being target = (Being) hit.getBody().getUserData();
			float i = calculateMomentumImpulse(contact);
			if(i > Properties.getFloat("contact.impulse.threshold", 13f))
				worldManager.processHit(impulseToDamage(i), target, null, hit,
						contact.getWorldManifold().getNormal(), 
						contact.getWorldManifold().getPoints()[0]);
		}
	}
	
	public static float impulseToDamage(float impulse){
		return Math.max(0f, impulse - Properties.getFloat("contact.impulse.threshold"));
	}
	
	private static float calculateMomentumImpulse(Contact contact){
		Vector2 velocity = contact.getFixtureA().getBody().getLinearVelocity().sub(
				contact.getFixtureB().getBody().getLinearVelocity());
		return velocity.scl(contact.getWorldManifold().getNormal()).len() ;
	}
	
	@Override public void postSolve(Contact contact, ContactImpulse oldManifold) {}
	@Override public void endContact(Contact contact) {}
}
