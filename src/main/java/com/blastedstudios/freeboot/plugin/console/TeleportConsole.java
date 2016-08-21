package com.blastedstudios.freeboot.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class TeleportConsole extends AbstractConsole{
	@Override public String[] getMatches() {
		return new String[]{"teleport","tp"};
	}

	@Override public void execute(String[] tokens) {
		float x, y;
		if(tokens[1].contains(",")){
			x = Float.parseFloat(tokens[1].split(",")[0].trim());
			y = Float.parseFloat(tokens[1].split(",")[1].trim());
		}else{
			x = Float.parseFloat(tokens[1]);
			y = Float.parseFloat(tokens[2]);
		}
		world.getPlayer().setPosition(x, y, 0);
	}

	@Override public String getHelp() {
		return "Teleport command places the player in a location. Can be shortened to 'tp'. Usage:\n"
				+ "teleport <location>, where <location> is an x,y comma separated value e.g. 100,100";
	}
}
