package com.blastedstudios.freeboot.plugin.quest.handler.manifestation;

import java.util.HashMap;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.audio.Sound;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.sound.ISoundHandler;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.sound.SoundManifestationEnum;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.freeboot.plugin.quest.handler.IWorldManagerInitializer;
import com.blastedstudios.freeboot.world.WorldManager;

@PluginImplementation
public class SoundHandlerPlugin implements ISoundHandler, IWorldManagerInitializer{
	private final HashMap<String, Long> soundIdMap = new HashMap<>();
	private WorldManager world;
	
	@Override public void setWorldManager(WorldManager world){
		this.world = world;
	}

	@Override public CompletionEnum sound(float dt, SoundManifestationEnum manifestationType,
			String name, String filename, float volume, float pan, float pitch) {
		String path = "data/sounds/" + filename + ".mp3";
		if(!world.getSharedAssets().isLoaded(path)){
			Log.error("SoundHandlerPlugin.sound", "Sound not available: " + path);
			return CompletionEnum.COMPLETED;
		}
		Sound sound = world.getSharedAssets().get(path, Sound.class);
		switch(manifestationType){
		case PLAY:
			soundIdMap.put(name, sound.play(volume * Properties.getFloat("sound.volume", 1f), pan, pitch));
			break;
		case LOOP:
			soundIdMap.put(name, sound.loop(volume * Properties.getFloat("sound.volume", 1f), pan, pitch));
			break;
		case PAUSE:
			sound.pause(soundIdMap.get(name));
			break;
		case PITCHPAN:
			sound.setPitch(soundIdMap.get(name), pitch);
			sound.setPan(soundIdMap.get(name), pan, volume);
		case RESUME:
			sound.resume(soundIdMap.get(name));
			break;
		case STOP:
			sound.stop();
			break;
		case VOLUME:
			sound.setVolume(soundIdMap.get(name), volume * Properties.getFloat("sound.volume", 1f));
			break;
		default:
			return CompletionEnum.NOT_STARTED;
		}
		return CompletionEnum.COMPLETED;
	}

	@Override public CompletionEnum tick(float dt) {
		return CompletionEnum.NOT_STARTED;
	}
}
