package com.blastedstudios.freeboot.ui.gameplay.console;

import java.util.HashSet;
import java.util.LinkedList;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.graphics.Color;
import com.blastedstudios.gdxworld.util.Log.ILogHandler;
import com.blastedstudios.gdxworld.util.Properties;

@PluginImplementation
public class History implements ILogHandler{
	static final LinkedList<ConsoleOutputStruct> items = new LinkedList<>();
	private static final HashSet<IHistoryListener> listeners = new HashSet<>();

	@Override
	public void debug(String tag, String message) {
		if(Properties.getBool("history.log.debug", false))
			add(tag + ": " + message, Color.GRAY);
	}

	@Override
	public void error(String tag, String message) {
		if(Properties.getBool("history.log.error", true))
			add(tag + ": " + message, Color.MAROON);
	}

	@Override
	public void log(String tag, String message) {
		if(Properties.getBool("history.log.log", true))
			add(tag + ": " + message, Color.DARK_GRAY);
	}
	
	public static void add(String msg, Color color){
		items.add(new ConsoleOutputStruct(msg, color));
		while(items.size() > Properties.getInt("history.size", 100))
			items.removeFirst();
		for(IHistoryListener listener : listeners)
			listener.added(msg, color);
	}
	
	public static void addListener(IHistoryListener listener){
		listeners.add(listener);
	}
	
	public static void removeListener(IHistoryListener listener){
		listeners.remove(listener);
	}
}
