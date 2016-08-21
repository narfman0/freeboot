package com.blastedstudios.freeboot.plugin.quest.manifestation.beingstatus;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;

@PluginImplementation
public interface IBeingStatusHandler extends Plugin{
	CompletionEnum statusBeing(String being, float dmg, boolean kill, String textureAtlas,
			boolean remove, boolean doAim, float aim, String attackTarget);
}
