package com.blastedstudios.freeboot.plugin.quest.manifestation.beingrotation;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.gdxworld.plugin.mode.quest.IQuestComponent.IQuestComponentManifestation;
import com.blastedstudios.gdxworld.world.quest.ICloneable;

@PluginImplementation
public class BeingRotationManifestationPlugin implements IQuestComponentManifestation{
	@Override public String getBoxText() {
		return "Being Rotation";
	}

	@Override public ICloneable getDefault() {
		return BeingRotationManifestation.DEFAULT;
	}

	@Override public Table createTable(Skin skin, Object object) {
		return new BeingRotationManifestationTable(skin, (BeingRotationManifestation) object);
	}
}
