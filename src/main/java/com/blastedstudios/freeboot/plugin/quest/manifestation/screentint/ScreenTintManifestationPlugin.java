package com.blastedstudios.freeboot.plugin.quest.manifestation.screentint;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.gdxworld.plugin.mode.quest.IQuestComponent.IQuestComponentManifestation;
import com.blastedstudios.gdxworld.world.quest.ICloneable;

@PluginImplementation
public class ScreenTintManifestationPlugin implements IQuestComponentManifestation {
	@Override public String getBoxText() {
		return "Screen Tint";
	}

	@Override public ICloneable getDefault() {
		return ScreenTintManifestation.DEFAULT;
	}

	@Override public Table createTable(Skin skin, Object object) {
		return new ScreenTintManifestationTable(skin, (ScreenTintManifestation) object);
	}
}
