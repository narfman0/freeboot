package com.blastedstudios.freeboot.plugin.console;

import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.gdxworld.util.Log;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class BeingStateConsole extends AbstractConsole{
	@Override public String[] getMatches() {
		return new String[]{"being"};
	}

	@Override public void execute(String[] tokens) {
		if(tokens.length == 2 && tokens[1].equalsIgnoreCase("list")){
			for(Being being : world.getAllBeings().values())
				Log.log(this.getClass().getSimpleName() + ".execute", "NPC uuid: " + being.getUuid() + 
						" name: " + being.getName() + " position: " + being.getPosition());
		}
	}

	@Override public String getHelp() {
		return "Being state plugin. Usage:\nbeing list";
	}
}
