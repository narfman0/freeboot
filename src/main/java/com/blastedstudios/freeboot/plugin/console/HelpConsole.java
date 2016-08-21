package com.blastedstudios.freeboot.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.freeboot.util.IConsoleCommand;

@PluginImplementation
public class HelpConsole extends AbstractConsole{
	@Override public String[] getMatches() {
		return new String[]{"help", "h"};
	}

	@Override public void execute(String[] tokens) {
		for(IConsoleCommand command : PluginUtil.getPlugins(IConsoleCommand.class))
			Log.log("HelpConsole.execute", command.getHelp());
		Log.log("HelpConsole.execute", "Direct debug commands are enabled with the debug.commands property."
				+ " They are activated by holding LEFT_CONTROL and pressing a key. They are:");
		Log.log("HelpConsole.execute", "F6: toggles debug.draw property");
		Log.log("HelpConsole.execute", "F9: generate a drop");
		Log.log("HelpConsole.execute", "F10: spawn NPC");
		Log.log("HelpConsole.execute", "F12: beat current level");
	}

	@Override
	public String getHelp() {
		return "Help command prints information on available console commands. Usage:\nhelp";
	}
}