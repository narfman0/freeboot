package com.blastedstudios.freeboot.plugin.quest.handler.manifestation;

import java.util.LinkedList;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.world.GDXPath;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.freeboot.plugin.quest.handler.IWorldManagerInitializer;
import com.blastedstudios.freeboot.plugin.quest.manifestation.pathchange.IPathChangeHandler;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.being.NPC;

@PluginImplementation
public class PathChangeHandlerPlugin implements IPathChangeHandler, IWorldManagerInitializer{
	private WorldManager world;
	
	@Override public void setWorldManager(WorldManager world){
		this.world = world;
	}
	
	public CompletionEnum pathChange(String beingString, String pathString) {
		final GDXPath path;
		if(pathString.equals("player")){
			path = new GDXPath();
			path.getNodes().add(world.getPlayer().getPosition());
		}else
			path = world.getPath(pathString);
		
		boolean found = false;
		LinkedList<String> names = new LinkedList<>();
		if(beingString.contains(","))
			for(String name : beingString.split(","))
				names.add(name.trim());
		else
			names.add(beingString);
		for(Being being : world.getAllBeings())
			for(String name : names)
				if(being.getName().matches(name)){
					((NPC)being).setPath(path);
					found = true;
				}
		
		if(path == null)
			Log.error("QuestManifestationExecutor.pathChange", "Path null " +
					"for quest manifestation! path:" + pathString + " being:" + beingString);
		if(!found)
			Log.error("QuestManifestationExecutor.pathChange", "Being null " +
					"for quest manifestation! path:" + pathString + " being:" + beingString);
		return CompletionEnum.COMPLETED;
	}
}
