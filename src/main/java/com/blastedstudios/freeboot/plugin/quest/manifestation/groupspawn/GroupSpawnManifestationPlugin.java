package com.blastedstudios.freeboot.plugin.quest.manifestation.groupspawn;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.gdxworld.plugin.mode.quest.IQuestComponent.IQuestComponentManifestation;
import com.blastedstudios.gdxworld.world.quest.ICloneable;

@PluginImplementation
public class GroupSpawnManifestationPlugin implements IQuestComponentManifestation{
	@Override public String getBoxText() {
		return "Group Spawn";
	}

	@Override public ICloneable getDefault() {
		return GroupSpawnManifestation.DEFAULT;
	}

	@Override public Table createTable(Skin skin, Object object) {
		return new GroupSpawnManifestationTable(skin, (GroupSpawnManifestation) object);
	}
}
