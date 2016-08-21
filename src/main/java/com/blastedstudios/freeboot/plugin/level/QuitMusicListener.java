package com.blastedstudios.freeboot.plugin.level;

import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.freeboot.plugin.quest.handler.manifestation.SoundThematicHandlerPlugin;
import com.blastedstudios.freeboot.world.WorldManager;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class QuitMusicListener implements ILevelCompletedListener{
	@Override public void levelComplete(boolean success, WorldManager world, GDXLevel level) {
		if(!success)
			SoundThematicHandlerPlugin.get().applyMusic(SoundThematicHandlerPlugin.getMainMusic());
	}
}
