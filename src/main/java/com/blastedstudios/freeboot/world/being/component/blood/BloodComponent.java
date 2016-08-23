package com.blastedstudios.freeboot.world.being.component.blood;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.world.being.component.AbstractComponent;
import com.blastedstudios.freeboot.world.weapon.DamageStruct;

@PluginImplementation
public class BloodComponent extends AbstractComponent {
	private static Random RANDOM = new Random();
	private static final long BLOOD_TIME = Properties.getInt("character.blood.time", 10000);
	private transient LinkedList<BloodStruct> blood = new LinkedList<>();
	
	@Override public void render(float dt, Batch batch, AssetManager sharedAssets,
			GDXRenderer gdxRenderer, boolean facingLeft, boolean paused){
		for(Iterator<BloodStruct> iter = blood.iterator(); iter.hasNext();){
			BloodStruct current = iter.next();
			current.effect.draw(batch, dt);
			if(System.currentTimeMillis() - current.startTime > BLOOD_TIME){
				current.effect.dispose();
				iter.remove();
			}
		}
	}

	@Override public void receivedDamage(DamageStruct damage){
		if(Gdx.app instanceof HeadlessApplication)
			return;
		ParticleEffect effect = new ParticleEffect();
		effect.load(Gdx.files.internal("data/particles/blood.p"), Gdx.files.internal("data/particles"));
		effect.setPosition(damage.getDamagePosition().x, damage.getDamagePosition().y);
		float angle = (float)Math.atan2(damage.getDir().y, damage.getDir().x);
		ParticleEmitter emitter = effect.findEmitter("blood");
		float angleDegrees = (float)Math.toDegrees(angle), quantityScalar = 20f + damage.getDamage()/100f;
		emitter.getAngle().setHigh(angleDegrees - 20f - ((float)RANDOM.nextGaussian() * quantityScalar), 
				angleDegrees + 20f + ((float)RANDOM.nextGaussian() * quantityScalar));
		emitter.getAngle().setLow(angleDegrees);
		effect.start();
		blood.add(new BloodStruct(System.currentTimeMillis(), effect));
	}
}
