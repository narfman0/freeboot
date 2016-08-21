package com.blastedstudios.freeboot.world.being.component.blood;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;

class BloodStruct {
	public final long startTime;
	public final ParticleEffect effect;
	
	public BloodStruct(long startTime, ParticleEffect effect){
		this.startTime = startTime;
		this.effect = effect;
	}
}
