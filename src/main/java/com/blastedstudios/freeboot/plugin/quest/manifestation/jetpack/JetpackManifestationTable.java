package com.blastedstudios.freeboot.plugin.quest.manifestation.jetpack;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.blastedstudios.gdxworld.plugin.mode.quest.ManifestationTable;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class JetpackManifestationTable extends ManifestationTable{
	private final JetpackManifestation manifestation;
	private final TextField beingNameField, maxField, rechargeField, impulseField;
	private final CheckBox changeMaxBox, changeRechargeBox, changeImpulseBox;
	
	public JetpackManifestationTable(Skin skin, JetpackManifestation manifestation){
		super(skin);
		this.manifestation = manifestation;
		beingNameField = new TextField(manifestation.getBeingName(), skin);
		beingNameField.setMessageText("<being name>");
		maxField = new TextField(manifestation.getMax()+"", skin);
		maxField.setMessageText("<max>");
		rechargeField = new TextField(manifestation.getRecharge()+"", skin);
		rechargeField.setMessageText("<recharge>");
		impulseField = new TextField(manifestation.getImpulse()+"", skin);
		impulseField.setMessageText("<impulse>");
		changeMaxBox = new CheckBox("Change Max", skin);
		changeMaxBox.setChecked(manifestation.isChangeMax());
		changeRechargeBox = new CheckBox("Change Recharge", skin);
		changeRechargeBox.setChecked(manifestation.isChangeRecharge());
		changeImpulseBox = new CheckBox("Change Impulse", skin);
		changeImpulseBox.setChecked(manifestation.isChangeImpulse());

		add(new Label("Being Name: ", skin));
		add(beingNameField);
		row();
		add(changeMaxBox);
		add(maxField);
		row();
		add(changeImpulseBox);
		add(impulseField);
		row();
		add(changeRechargeBox);
		add(rechargeField);
		row();
	}

	@Override public AbstractQuestManifestation apply() {
		manifestation.setBeingName(beingNameField.getText());
		manifestation.setChangeImpulse(changeImpulseBox.isChecked());
		manifestation.setChangeMax(changeMaxBox.isChecked());
		manifestation.setChangeRecharge(changeRechargeBox.isChecked());
		manifestation.setImpulse(Float.parseFloat(impulseField.getText()));
		manifestation.setMax(Float.parseFloat(maxField.getText()));
		manifestation.setRecharge(Float.parseFloat(rechargeField.getText()));
		return manifestation;
	}
}
