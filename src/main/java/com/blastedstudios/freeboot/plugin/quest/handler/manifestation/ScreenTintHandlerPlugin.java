package com.blastedstudios.freeboot.plugin.quest.handler.manifestation;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.gdxworld.util.GDXGameFade;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.freeboot.plugin.quest.handler.IGameplayScreenInitializer;
import com.blastedstudios.freeboot.plugin.quest.manifestation.screentint.IScreenTintHandler;
import com.blastedstudios.freeboot.plugin.quest.manifestation.screentint.ScreenTintManifestation.TintType;
import com.blastedstudios.freeboot.ui.gameplay.GameplayScreen;

@PluginImplementation
public class ScreenTintHandlerPlugin implements IGameplayScreenInitializer, IScreenTintHandler{
	private GameplayScreen screen;
	
	@Override public void setGameplayScreen(GameplayScreen screen){
		this.screen = screen;
	}

	@Override public CompletionEnum screenTint(Color tableColor, Color pixmapColor, float duration, TintType tintType) {
		Table tintTable = GDXGameFade.buildTable(tableColor, pixmapColor);
		switch(tintType){
		case FADEIN:
			tintTable.addAction(Actions.fadeIn(duration));
			break;
		case FADEOUT:
			tintTable.addAction(Actions.fadeOut(duration));
			break;
		}
		screen.applyTintTable(tintTable);
		return CompletionEnum.COMPLETED;
	}
}
