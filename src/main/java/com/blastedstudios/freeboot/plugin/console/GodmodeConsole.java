package com.blastedstudios.freeboot.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;

@PluginImplementation
public class GodmodeConsole extends AbstractConsole{
	@Override public String[] getMatches() {
		return new String[]{"player"};
	}

	@Override public void execute(String[] tokens) {
		if(tokens.length == 3 && tokens[1].equalsIgnoreCase("godmode")){
			boolean enabled = Boolean.parseBoolean(tokens[2]);
			Properties.set("character.godmode", enabled+"");
			Log.log("GodmodeConsole.execute", "Godmode set to: " + enabled);
		}
	}

	@Override public String getHelp() {
		return "God mode command, making player invincible to damage until unset. "
				+ "Usage:\nplayer godmode <true/false>, where true enabled invincibility";
	}
}