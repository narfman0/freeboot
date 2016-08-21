package com.blastedstudios.freeboot.plugin.quest.manifestation.addxp;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.blastedstudios.gdxworld.plugin.mode.quest.ManifestationTable;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class AddXPManifestationTable extends ManifestationTable{
	private final AddXPManifestation manifestation;
	private final TextField beingField, xpField;
	
	public AddXPManifestationTable(Skin skin, AddXPManifestation manifestation){
		super(skin);
		this.manifestation = manifestation;
		beingField = new TextField(manifestation.getBeing(), skin);
		beingField.setMessageText("<being name>");
		xpField = new TextField(manifestation.getXp()+"", skin);
		xpField.setMessageText("<xp>");
		add(new Label("Being Name: ", skin));
		add(beingField);
		row();
		add(new Label("XP: ", skin));
		add(xpField);
	}

	@Override public AbstractQuestManifestation apply() {
		manifestation.setBeing(beingField.getText());
		manifestation.setXp(Long.parseLong(xpField.getText()));
		return manifestation;
	}
}