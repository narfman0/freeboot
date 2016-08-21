package com.blastedstudios.freeboot.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;

@PluginImplementation
public class PropertyConsole extends AbstractConsole {
	@Override public String[] getMatches() {
		return new String[]{"property","prop"};
	}

	@Override public void execute(String[] tokens) {
		if(tokens.length == 2)
			Log.log("PropertyConsole.execute", "Property " + tokens[1] + 
					" has value: " + Properties.get(tokens[1]));
		else if(tokens.length == 3)
			Properties.set(tokens[1], tokens[2]);
	}

	@Override public String getHelp() {
		return "Property command prints or sets properties. Can be shortened to 'prop'. Usage:\n"
				+ "property debug.draw, shows the current value for property debug.draw\n"
				+ "property debug.draw true, sets value for property debug.draw to 'true'";
	}
}
