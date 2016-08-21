package com.blastedstudios.freeboot.plugin.quest.manifestation.beingrotation;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.blastedstudios.gdxworld.plugin.mode.quest.ManifestationTable;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class BeingRotationManifestationTable extends ManifestationTable{
	private final BeingRotationManifestation manifestation;
	private final CheckBox fixedRotation;
	private final TextField beingField, torqueField;
	
	public BeingRotationManifestationTable(Skin skin, BeingRotationManifestation manifestation){
		super(skin);
		this.manifestation = manifestation;
		fixedRotation = new CheckBox("Fixed Rotation", skin);
		fixedRotation.setChecked(manifestation.isFixedRotation());
		beingField = new TextField(manifestation.getBeing(), skin);
		beingField.setMessageText("<being name>");
		torqueField = new TextField(manifestation.getTorque()+"", skin);
		torqueField.setMessageText("<torque>");
		add(fixedRotation);
		row();
		add(new Label("Being Name: ", skin));
		add(beingField);
		row();
		add(new Label("Torque: ", skin));
		add(torqueField);
	}

	@Override public AbstractQuestManifestation apply() {
		manifestation.setFixedRotation(fixedRotation.isChecked());
		manifestation.setBeing(beingField.getText());
		manifestation.setTorque(Float.parseFloat(torqueField.getText()));
		return manifestation;
	}
}