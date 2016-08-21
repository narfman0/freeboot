package com.blastedstudios.freeboot.plugin.quest.handler.manifestation;

import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.physics.box2d.Body;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.physics.IPhysicsManifestationHandler;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.physics.PhysicsManifestation;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.freeboot.plugin.quest.handler.IWorldManagerInitializer;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.being.Being.BodyPart;

@PluginImplementation
public class PhysicsManifestationHandlerPlugin implements	IPhysicsManifestationHandler, IWorldManagerInitializer {
	private WorldManager world;
	
	@Override public void setWorldManager(WorldManager world){
		this.world = world;
	}
	
	@Override public CompletionEnum execute(PhysicsManifestation manifestation) {
		List<Body> bodies = world.matchPhysicsObject(manifestation.getName());
		if(manifestation.getExecutor() != null){
			Body body = manifestation.getExecutor().getPhysicsObject(manifestation.getName());
			if(body != null && !bodies.contains(body))
				bodies.add(body);
		}
		for(Being being : world.matchBeings(manifestation.getName()))
			bodies.add(being.getRagdoll().getBodyPart(BodyPart.torso));
		for(Body body : bodies){
			body.applyLinearImpulse(manifestation.getImpulse(), body.getPosition(),true);
			if(manifestation.isHasVelocity())
				body.setLinearVelocity(manifestation.isRelativeVelocity() ? 
						body.getLinearVelocity().cpy().add(manifestation.getVelocity()) :
						manifestation.getVelocity());
			if(manifestation.isHasPosition()){
				float angle = manifestation.isHasAngle() ? (manifestation.isRelativeAngle() ?
						body.getAngle() + manifestation.getAngle() : manifestation.getAngle()) : body.getAngle();
				body.setTransform(manifestation.isRelativePosition() ?
						body.getPosition().cpy().add(manifestation.getPosition()) : manifestation.getPosition(), angle);
			}
			body.setType(manifestation.getType());
			body.applyTorque(manifestation.getTorque(), true);
		}
		return CompletionEnum.COMPLETED;
	}
}