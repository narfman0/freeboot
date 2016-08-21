package com.blastedstudios.freeboot.plugin.quest.manifestation.screentint;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.graphics.Color;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.freeboot.plugin.quest.manifestation.screentint.ScreenTintManifestation.TintType;

@PluginImplementation
public interface IScreenTintHandler extends Plugin{
	CompletionEnum screenTint(Color tableColor, Color pixmapColor, float duration, TintType tintType);
}
