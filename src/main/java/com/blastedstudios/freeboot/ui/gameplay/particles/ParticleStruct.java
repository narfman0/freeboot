package com.blastedstudios.freeboot.ui.gameplay.particles;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.world.GDXParticle;

class ParticleStruct {
	public final ParticleEffect effect;
	public final GDXParticle particle;
	private long removeScheduled = -1L;
	
	public ParticleStruct(ParticleEffect effect, GDXParticle particle){
		this.effect = effect;
		this.particle = particle;
	}
	
	public long getRemoveScheduled() {
		return removeScheduled;
	}
	
	public boolean isRemoveScheduled(){
		return removeScheduled != -1L;
	}
	
	public void setRemoveScheduled(long removeScheduled) {
		this.removeScheduled = removeScheduled;
	}
	
	public void scheduleRemove(String name){
		removeScheduled = System.currentTimeMillis();
		effect.setDuration(0);
		Log.debug("ParticleStruct.scheduleRemove", "Scheduled remove for: " + name);
	}

	@Override public String toString(){
		return "[ParticleStruct particle:" + particle + " effect:" + effect + "]";
	}
}
