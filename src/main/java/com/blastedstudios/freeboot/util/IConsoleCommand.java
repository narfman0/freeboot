package com.blastedstudios.freeboot.util;

import net.xeoh.plugins.base.Plugin;

import com.blastedstudios.freeboot.ui.gameplay.GameplayScreen;
import com.blastedstudios.freeboot.world.WorldManager;

public interface IConsoleCommand extends Plugin {
	void initialize(final WorldManager world, final GameplayScreen screen);
	String[] getMatches();
	void execute(String[] tokens);
	String getHelp();
}
