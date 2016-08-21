package com.blastedstudios.freeboot.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class PauseConsole extends AbstractConsole {
	@Override public String[] getMatches() {
		return new String[]{"pause"};
	}

	@Override public void execute(String[] tokens) {
		world.pause(Boolean.parseBoolean(tokens[1]));
	}

	@Override public String getHelp() {
		return "Pause command pauses gameplay. Usage:\npause <true/false>, where true pauses the game";
	}
}
