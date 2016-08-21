package com.blastedstudios.freeboot.plugin.quest.handler.manifestation;

import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.world.GDXNPC;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.freeboot.plugin.quest.handler.IWorldManagerInitializer;
import com.blastedstudios.freeboot.plugin.quest.manifestation.groupspawn.IGroupSpawnHandler;
import com.blastedstudios.freeboot.world.WorldManager;

@PluginImplementation
public class GroupSpawnHandlerPlugin implements IGroupSpawnHandler, IWorldManagerInitializer{
	private WorldManager world;
	
	@Override public void setWorldManager(WorldManager world){
		this.world = world;
	}

	@Override public CompletionEnum groupSpawn(List<String> npcData, int level) {
		for(String npcDatum : npcData){
			GDXNPC npc = new GDXNPC();
			npc.getProperties().put("NPCData", npcDatum);
			npc.getProperties().put("Level", level+"");
			npc.setCoordinates(world.getFurthestSpawn().add(world.getRandom().nextFloat(), world.getRandom().nextFloat()));
			world.spawnNPC(npc, world.getAiWorld());
		}
		return CompletionEnum.COMPLETED;
	}
}
