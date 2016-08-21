package com.blastedstudios.freeboot.plugin.quest.handler;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.freeboot.world.WorldManager;

@PluginImplementation
public interface IWorldManagerInitializer extends Plugin{
	void setWorldManager(WorldManager world);
}
