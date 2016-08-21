package com.blastedstudios.freeboot.plugin.quest.manifestation.cameracontrol;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;

@PluginImplementation
public interface ICameraControlHandler extends Plugin{
	CompletionEnum playerTrack(boolean playerTrack);
}
