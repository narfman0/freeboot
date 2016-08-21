package com.blastedstudios.freeboot.plugin.quest.handler.manifestation;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.freeboot.plugin.quest.handler.IWorldManagerInitializer;
import com.blastedstudios.freeboot.plugin.quest.manifestation.cameracontrol.ICameraControlHandler;
import com.blastedstudios.freeboot.world.WorldManager;

@PluginImplementation
public class CameraControlHandlerPlugin implements ICameraControlHandler, IWorldManagerInitializer{
	private WorldManager world;
	
	@Override public void setWorldManager(WorldManager world){
		this.world = world;
	}

	@Override public CompletionEnum playerTrack(boolean playerTrack) {
		world.setPlayerTrack(playerTrack);
		return CompletionEnum.COMPLETED;
	}
}
