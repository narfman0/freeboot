package com.blastedstudios.freeboot.plugin.quest.manifestation.groupspawn;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class GroupSpawnManifestation extends AbstractQuestManifestation {
	private static final long serialVersionUID = 1L;
	public static final GroupSpawnManifestation DEFAULT = new GroupSpawnManifestation();
	private List<String> npcData = new LinkedList<>();
	private int level = 1;
	
	public GroupSpawnManifestation(){}
	
	public GroupSpawnManifestation(List<String> npcData, int level){
		this.npcData = npcData;
		this.level = level;
	}

	@Override public CompletionEnum execute(float dt) {
		for(IGroupSpawnHandler handler : PluginUtil.getPlugins(IGroupSpawnHandler.class))
			if(handler.groupSpawn(npcData, level) == CompletionEnum.COMPLETED)
				return CompletionEnum.COMPLETED;
		return CompletionEnum.COMPLETED;
	}

	@Override public AbstractQuestManifestation clone() {
		return new GroupSpawnManifestation(new ArrayList<>(npcData), level);
	}

	@Override public String toString() {
		return "[BeingSpawnManifestation npc: " + getNpcData().toString() + "]";
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<String> getNpcData() {
		return npcData;
	}

	public void setNpcData(List<String> npcData) {
		this.npcData = npcData;
	}
}
