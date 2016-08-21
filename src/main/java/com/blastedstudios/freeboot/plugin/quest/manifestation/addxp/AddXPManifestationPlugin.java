package com.blastedstudios.freeboot.plugin.quest.manifestation.addxp;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.gdxworld.plugin.mode.quest.IQuestComponent.IQuestComponentManifestation;
import com.blastedstudios.gdxworld.world.quest.ICloneable;

@PluginImplementation
public class AddXPManifestationPlugin implements IQuestComponentManifestation{
	@Override public String getBoxText() {
		return "Add XP";
	}

	@Override public ICloneable getDefault() {
		return AddXPManifestation.DEFAULT;
	}

	@Override public Table createTable(Skin skin, Object object) {
		return new AddXPManifestationTable(skin, (AddXPManifestation) object);
	}
}
