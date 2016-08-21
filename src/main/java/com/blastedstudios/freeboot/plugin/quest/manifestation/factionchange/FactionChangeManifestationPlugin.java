package com.blastedstudios.freeboot.plugin.quest.manifestation.factionchange;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.gdxworld.plugin.mode.quest.IQuestComponent.IQuestComponentManifestation;
import com.blastedstudios.gdxworld.world.quest.ICloneable;

@PluginImplementation
public class FactionChangeManifestationPlugin implements IQuestComponentManifestation{
	@Override public String getBoxText() {
		return "Faction Change";
	}

	@Override public ICloneable getDefault() {
		return FactionChangeManifestation.DEFAULT.clone();
	}

	@Override public Table createTable(Skin skin, Object object) {
		return new FactionChangeManifestationTable(skin, (FactionChangeManifestation) object);
	}
}
