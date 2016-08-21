package com.blastedstudios.freeboot.plugin.quest.manifestation.cameratween;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.gdxworld.plugin.mode.quest.IQuestComponent.IQuestComponentManifestation;
import com.blastedstudios.gdxworld.world.quest.ICloneable;

@PluginImplementation
public class CameraTweenManifestationPlugin implements IQuestComponentManifestation{
	@Override public String getBoxText() {
		return "Camera Tween";
	}

	@Override public ICloneable getDefault() {
		return CameraTweenManifestation.DEFAULT.clone();
	}

	@Override public Table createTable(Skin skin, Object object) {
		return new CameraTweenManifestationTable(skin, (CameraTweenManifestation) object);
	}
}