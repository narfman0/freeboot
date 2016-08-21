package com.blastedstudios.freeboot.ui.gameplay.particles;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.GDXParticle;
import com.blastedstudios.gdxworld.world.shape.GDXShape;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;

public class ParticleManager {
	private static final long TIME_TO_REMOVE_SCHEDULED = Properties.getInt("particles.remove.time", 11000);
	private final HashMap<String, ParticleStruct> particles = new HashMap<>();

	public void addParticle(GDXParticle particle){
		if(!particles.containsKey(particle.getName())){
			try{
				particles.put(particle.getName(), new ParticleStruct(particle.createEffect(), particle));
				Log.log("ParticleManager.addParticle", "Particle added: " + particle);
			}catch(Exception e){
				Log.log("ParticleManager.addParticle", "Failed to add particle: " + particle);
				e.printStackTrace();
			}
		}
	}

	public void render(float dt, Camera camera, WorldManager worldManager, Batch batch){
		for(Iterator<Entry<String,ParticleStruct>> iter = particles.entrySet().iterator(); iter.hasNext();){
			Entry<String,ParticleStruct> entry = iter.next();
			ParticleStruct struct = entry.getValue();
			if(!struct.particle.getAttachedBody().isEmpty()){
				boolean found = false;
				for(Being being : worldManager.getAllBeings())
					if(being.getName().equalsIgnoreCase(struct.particle.getAttachedBody()) || 
							struct.particle.getAttachedBody().equalsIgnoreCase("player") && being == worldManager.getPlayer()){
						struct.effect.setPosition(being.getPosition().x, being.getPosition().y);
						found = true;
					}
				if(!found)
					for(Entry<GDXShape, Body> body : worldManager.getCreateLevelStruct().bodies.entrySet())
						if(body.getKey().getName().equals(struct.particle.getAttachedBody()))
							struct.effect.setPosition(body.getValue().getPosition().x, body.getValue().getPosition().y);
			}
			float time = worldManager.isPause() ? 0f : dt;
			if(struct.particle.getEmitterName().isEmpty())
				struct.effect.draw(batch, time);
			else
				struct.effect.findEmitter(struct.particle.getEmitterName()).draw(batch, time);
			
			if(struct.isRemoveScheduled() && 
					(System.currentTimeMillis() - struct.getRemoveScheduled() > TIME_TO_REMOVE_SCHEDULED)){
				disposeParticle(entry.getKey());
				iter.remove();
			}
		}
	}

	public void scheduleRemove(String name) {
		particles.get(name).scheduleRemove(name);
	}
	
	public void disposeParticle(String name){
		ParticleStruct struct = particles.get(name);
		if(struct == null)
			Log.error("ParticleManager.disposeParticle", "Particle remove requested for " + 
					name + ", but no such particle exists");
		else{
			struct.effect.dispose();
			Log.debug("ParticleManager.disposeParticle", "Disposed " + name);
		}
	}

	public void modify(String name, int duration, Vector2 position,
			String emitterName, String attachedBody) {
		ParticleStruct struct = particles.get(name);
		if(struct != null){
			if(duration != -1)
				struct.effect.setDuration(duration);
			struct.effect.setPosition(position.x, position.y);
			struct.particle.setEmitterName(emitterName);
			struct.particle.setAttachedBody(attachedBody);
		}else
			Log.error("ParticleManager.modify", "Particle modify requested for " + 
					name + ", but no such particle exists");
	}
}
