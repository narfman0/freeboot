package com.blastedstudios.freeboot.plugin.quest.manifestation.groupspawn;

import java.util.List;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;

@PluginImplementation
public interface IGroupSpawnHandler extends Plugin{
	CompletionEnum groupSpawn(List<String> npcData, int level);
}
