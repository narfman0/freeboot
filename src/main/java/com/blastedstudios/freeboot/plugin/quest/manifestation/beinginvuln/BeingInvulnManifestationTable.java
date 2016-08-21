package com.blastedstudios.freeboot.plugin.quest.manifestation.beinginvuln;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.blastedstudios.gdxworld.plugin.mode.quest.ManifestationTable;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class BeingInvulnManifestationTable extends ManifestationTable{
	private final BeingInvulnManifestation manifestation;
	private final TextField beingField;
	private final CheckBox invulnBox;
	
	public BeingInvulnManifestationTable(Skin skin, BeingInvulnManifestation manifestation){
		super(skin);
		this.manifestation = manifestation;
		beingField = new TextField(manifestation.getBeing(), skin);
		beingField.setMessageText("<being name>");
		invulnBox = new CheckBox("Invulnerable", skin);
		invulnBox.setChecked(manifestation.isInvuln());
		add(new Label("Being Name: ", skin));
		add(beingField);
		row();
		add(invulnBox);
	}

	@Override public AbstractQuestManifestation apply() {
		manifestation.setBeing(beingField.getText());
		manifestation.setInvuln(invulnBox.isChecked());
		return manifestation;
	}
}
