package com.blastedstudios.freeboot.plugin.quest.handler.manifestation;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.plugin.quest.manifestation.beingspawn.IBeingSpawnHandler;
import com.blastedstudios.gdxworld.world.GDXNPC;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.freeboot.plugin.quest.handler.IWorldManagerInitializer;
import com.blastedstudios.freeboot.world.WorldManager;

@PluginImplementation
public class BeingSpawnHandlerPlugin implements IBeingSpawnHandler, IWorldManagerInitializer{
	private WorldManager world;
	
	@Override public void setWorldManager(WorldManager world){
		this.world = world;
	}

	@Override public CompletionEnum beingSpawn(GDXNPC npc) {
		if("wave".matches(npc.getName()))
			world.getSpawnPoints().add(npc.getCoordinates());
		else if("player".matches(npc.getName()))
			world.setRespawnLocation(npc.getCoordinates().cpy());
		else
			world.spawnNPC(npc, world.getAiWorld());
		return CompletionEnum.COMPLETED;
	}
}
