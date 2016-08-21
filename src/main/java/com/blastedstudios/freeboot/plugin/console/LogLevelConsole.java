package com.blastedstudios.freeboot.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.blastedstudios.gdxworld.util.Log;

@PluginImplementation
public class LogLevelConsole extends AbstractConsole{
	@Override public String[] getMatches() {
		return new String[]{"log"};
	}

	@Override public void execute(String[] tokens) {
		if(tokens.length == 3 && tokens[1].equalsIgnoreCase("level")){
			int level = Integer.parseInt(tokens[2]);
			Gdx.app.setLogLevel(level);
			Log.log("LogLevelConsole.execute", "Log level set to: " + level);
		}
	}

	@Override public String getHelp() {
		return "Log level command changes verbosity of log. Usage:\n"
				+ "log level <level>, where level is a libgdx log supported value e.g. DEBUG, INFO, WARNING";
	}
}