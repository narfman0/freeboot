package com.blastedstudios.freeboot.plugin.quest.handler.manifestation;

import java.util.Iterator;
import java.util.LinkedList;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.sound.ISoundHandler;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.sound.SoundManifestationEnum;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.freeboot.plugin.quest.handler.ISharedAssetConsumer;

@PluginImplementation
public class SoundThematicHandlerPlugin implements ISoundHandler, ISharedAssetConsumer{
	private final LinkedList<MusicStruct> previous = new LinkedList<>();
	private MusicStruct current = null;
	private AssetManager assets;
	
	@Override public CompletionEnum sound(float dt, SoundManifestationEnum manifestationType,
			String name, String filename, float volume, float pan, float pitch) {
		String path = "data/sounds/" + filename + ".mp3";
		if(filename.equals("main"))
			path = getMainMusic();
		if(!assets.isLoaded(path)){
			Log.error("SoundThematicHandlerPlugin.sound", "Sound not available: " + path);
			return CompletionEnum.COMPLETED;
		}
		switch(manifestationType){
		case THEMATIC:
			previous.add(new MusicStruct(current.sound, current.id));
			Sound sound = assets.get(path, Sound.class);
			if(sound == current.sound){
				Log.log("SoundThematicHandlerPlugin.sound", "Sound currently playing, skipping");
				return CompletionEnum.COMPLETED;
			}
			current = new MusicStruct(sound, sound.loop(0f));
			Log.log("SoundThematicHandlerPlugin.sound", "Thematic sound executing: " + path);
			return CompletionEnum.EXECUTING;
		default:
			return CompletionEnum.NOT_STARTED;
		}
	}

	@Override public CompletionEnum tick(float dt) {
		for(Iterator<MusicStruct> iter = previous.iterator(); iter.hasNext();){
			MusicStruct struct = iter.next();
			struct.time -= dt;
			struct.sound.setVolume(struct.id, (struct.time/struct.duration) * Properties.getFloat("music.volume", 1f));
			if(struct.time <= 0f){
				struct.sound.stop(struct.id);
				iter.remove();
				if(previous.isEmpty())
					current.time = struct.duration;
			}
		}
		if(previous.isEmpty()){
			current.time -= dt;
			if(current.time <= 0f){
				current.sound.setVolume(current.id, Properties.getFloat("music.volume", 1f));
				return CompletionEnum.COMPLETED;
			}else{
				current.sound.setVolume(current.id, ((current.duration - current.time)/
						current.duration) * Properties.getFloat("music.volume", 1f));
				return CompletionEnum.EXECUTING;
			}
		}
		return CompletionEnum.EXECUTING;
	}
	
	public void applyMusic(String path){
		float volume = 1f * Properties.getFloat("music.volume", 1f);
		if(current != null){
			previous.add(new MusicStruct(current.sound, current.id));
			volume = 0f;
		}
		Sound sound = assets.get(path, Sound.class);
		current = new MusicStruct(sound, sound.loop(volume));
	}
	
	public static SoundThematicHandlerPlugin get(){
		//return instance of self, lame plugin way
		for(ISoundHandler handler : PluginUtil.getPlugins(ISoundHandler.class))
			if(handler instanceof SoundThematicHandlerPlugin)
				return (SoundThematicHandlerPlugin)handler;
		return null;
	}

	@Override public void setAssets(AssetManager assets) {
		this.assets = assets;
		applyMusic(getMainMusic());
	}
	
	public static String getMainMusic(){
		return Properties.get("sound.music.main", "data/sounds/music/EpicMovieGameTrailer.mp3");
	}
	
	private class MusicStruct{
		final float duration = Properties.getFloat("sound.music.fade.duration", 2f); 
		final Sound sound;
		final long id;
		float time = duration;
		
		MusicStruct(Sound sound, long id){
			this.sound = sound;
			this.id = id;
		}
	}
}
