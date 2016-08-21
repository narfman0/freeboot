package com.blastedstudios.freeboot.plugin.console;

import com.blastedstudios.freeboot.ui.gameplay.GameplayScreen;
import com.blastedstudios.freeboot.util.IConsoleCommand;
import com.blastedstudios.freeboot.world.WorldManager;

public abstract class AbstractConsole implements IConsoleCommand{
	protected WorldManager world;
	protected GameplayScreen screen;
	
	@Override public void initialize(final WorldManager world, final GameplayScreen screen){
		this.world = world;
		this.screen = screen;
	}
}
