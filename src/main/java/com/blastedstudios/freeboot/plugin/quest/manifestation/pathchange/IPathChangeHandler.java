package com.blastedstudios.freeboot.plugin.quest.manifestation.pathchange;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;

@PluginImplementation
public interface IPathChangeHandler extends Plugin{
	CompletionEnum pathChange(String beingString, String pathString);
}
