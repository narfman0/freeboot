package com.blastedstudios.freeboot.plugin.gunlistener;

import java.util.Random;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.weapon.Gun;
import com.blastedstudios.freeboot.world.weapon.Gun.IGunListener;

@PluginImplementation
public class GunshotSound implements IGunListener {
	@Override public void shoot(Gun gun, Being source, Random random,
			Vector2 direction, WorldManager world, Vector2 origin, AssetManager sharedAssets) {
		if(sharedAssets == null)
			return;
		String filename = "data/sounds/guns/" + gun.getFireSound() + ".mp3";
		if(!sharedAssets.isLoaded(filename)){
			Log.error("GunshotSound.shoot", "Sound not loaded: " + filename);
			return;
		}
		final Sound sound = sharedAssets.get(filename, Sound.class);
		if(sound != null){
			WorldManager.playSoundTuned(sound, origin, world.getPlayer().getPosition());
		}else
			Log.error("Being.attack", "Sound null for fireSound: " + gun.getFireSound());
	}

	@Override public void setCurrentRounds(Gun gun, int currentRounds) {}
}
