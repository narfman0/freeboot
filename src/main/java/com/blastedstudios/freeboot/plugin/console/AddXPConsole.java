package com.blastedstudios.freeboot.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.util.Log;

@PluginImplementation
public class AddXPConsole extends AbstractConsole{
	@Override public String[] getMatches() {
		return new String[]{"player"};
	}

	@Override public void execute(String[] tokens) {
		if(tokens.length == 3 && tokens[1].equalsIgnoreCase("addxp")){
			long amount = Integer.parseInt(tokens[2]);
			Log.log("AddXPConsole.execute", "Adding: " + amount + " xp to player");
			world.getPlayer().addXp(amount);
		}
	}

	@Override
	public String getHelp() {
		return "Add xp console command. Usage:\nplayer addxp <xp>, where xp is some integer value";
	}
}