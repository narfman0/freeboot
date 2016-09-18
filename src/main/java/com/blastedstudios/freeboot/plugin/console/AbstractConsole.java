package com.blastedstudios.freeboot.plugin.console;

import com.blastedstudios.freeboot.util.IConsoleCommand;
import com.blastedstudios.freeboot.world.WorldManager;

public abstract class AbstractConsole implements IConsoleCommand{
	protected WorldManager world;
	
	@Override public void initialize(final WorldManager world){
		this.world = world;
	}
}
