package com.blastedstudios.freeboot.plugin.quest.trigger.beinghit;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.gdxworld.plugin.mode.quest.IQuestComponent.IQuestComponentTrigger;
import com.blastedstudios.gdxworld.world.quest.ICloneable;

@PluginImplementation
public class BeingHitPlugin implements IQuestComponentTrigger{
	@Override public String getBoxText() {
		return "Being Hit";
	}

	@Override public ICloneable getDefault() {
		return BeingHitTrigger.DEFAULT;
	}

	@Override public Table createTable(Skin skin, Object object) {
		return new BeingHitTable(skin, (BeingHitTrigger) object);
	}
}