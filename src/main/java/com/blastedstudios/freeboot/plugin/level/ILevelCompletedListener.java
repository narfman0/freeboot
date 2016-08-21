package com.blastedstudios.freeboot.plugin.level;

import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.freeboot.world.WorldManager;

import net.xeoh.plugins.base.Plugin;

public interface ILevelCompletedListener extends Plugin{
	 void levelComplete(final boolean success, WorldManager world, GDXLevel level);
}
