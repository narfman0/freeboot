package com.blastedstudios.freeboot.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class JetpackConsole extends AbstractConsole{
	@Override public String[] getMatches() {
		return new String[]{"player"};
	}

	@Override public void execute(String[] tokens) {
		if(tokens[1].equalsIgnoreCase("jetpack"))
			world.getPlayer().getStats().setJetpackMax(Float.parseFloat(tokens[2]));
	}

	@Override public String getHelp() {
		return "Jetpack console command changes max power for player. Usage:\n"
				+ "player jetpack <max>, where max is an integer e.g. 100";
	}
}
