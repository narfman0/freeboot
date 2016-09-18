package com.blastedstudios.freeboot.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.blastedstudios.freeboot.util.IConsoleCommand;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.PluginUtil;

public class ConsoleInput {
	private final Thread thread;
	private final List<String> lines = Collections.synchronizedList(new LinkedList<>());
	
	public ConsoleInput(WorldManager world){
		for(IConsoleCommand commandPlugin : PluginUtil.getPlugins(IConsoleCommand.class))
			commandPlugin.initialize(world);
		thread = new Thread(() -> {while(true){ checkInput(); }}, ConsoleInput.class.getSimpleName() + "Thread");
		thread.setDaemon(true);
		thread.start();
	}
	
	public void checkInput(){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			lines.add(br.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void execute(){
		List<String> result = new ArrayList<>(lines);
		lines.clear();
		for(String commandText : result){
			String[] tokens = commandText.split(" ");
			for(IConsoleCommand commandPlugin : PluginUtil.getPlugins(IConsoleCommand.class))
				for(String match : commandPlugin.getMatches())
					try{
						if(tokens[0].matches(match))
							commandPlugin.execute(tokens);
					}catch(Exception e){
						Log.error("ConsoleInput.execute", "Failed to execute command: " + commandText);
						e.printStackTrace();
					}
		}
	}
}
