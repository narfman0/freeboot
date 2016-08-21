package com.blastedstudios.freeboot.plugin.quest.manifestation.cameracontrol;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.gdxworld.plugin.mode.quest.IQuestComponent.IQuestComponentManifestation;
import com.blastedstudios.gdxworld.world.quest.ICloneable;

@PluginImplementation
public class CameraControlManifestationPlugin implements IQuestComponentManifestation{
	@Override public String getBoxText() {
		return "Camera Control";
	}

	@Override public ICloneable getDefault() {
		return CameraControlManifestation.DEFAULT;
	}

	@Override public Table createTable(Skin skin, Object object) {
		return new CameraControlManifestationTable(skin, (CameraControlManifestation) object);
	}
}
