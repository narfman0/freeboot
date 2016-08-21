package com.blastedstudios.freeboot.plugin.quest.handler.manifestation;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.plugin.quest.manifestation.inputenable.IInputEnableHandler;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.freeboot.plugin.quest.handler.IWorldManagerInitializer;
import com.blastedstudios.freeboot.world.WorldManager;

@PluginImplementation
public class InputEnableHandlerPlugin implements IInputEnableHandler, IWorldManagerInitializer{
	private WorldManager world;
	
	@Override public void setWorldManager(WorldManager world){
		this.world = world;
	}
	
	@Override public CompletionEnum inputEnable(boolean inputEnable) {
		world.setInputEnable(inputEnable);
		return CompletionEnum.COMPLETED;
	}
}
