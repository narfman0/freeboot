package com.blastedstudios.freeboot.plugin.quest.manifestation.beingstatus;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.blastedstudios.gdxworld.plugin.mode.quest.ManifestationTable;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class BeingStatusManifestationTable extends ManifestationTable{
	private final BeingStatusManifestation manifestation;
	private final TextField beingField, dmgField, spriteAtlasField, aimField, attackField;
	private final CheckBox killCheckbox, removeCheckbox, doAimCheckbox;
	
	public BeingStatusManifestationTable(Skin skin, BeingStatusManifestation manifestation){
		super(skin);
		this.manifestation = manifestation;
		beingField = new TextField(manifestation.getBeing(), skin);
		beingField.setMessageText("<being name(s) or regex>");
		dmgField = new TextField(manifestation.getDmg()+"", skin);
		dmgField.setMessageText("<apply damage>");
		aimField = new TextField(manifestation.getAim()+"", skin);
		aimField.setMessageText("<aim in radians>");
		attackField = new TextField(manifestation.getAttackTarget(), skin);
		attackField.setMessageText("<attack target>");
		spriteAtlasField = new TextField(manifestation.getTextureAtlas(), skin);
		spriteAtlasField.setMessageText("<sprite atlas>");
		killCheckbox = new CheckBox("Kill", skin);
		killCheckbox.setChecked(manifestation.isKill());
		removeCheckbox = new CheckBox("Remove", skin);
		removeCheckbox.setChecked(manifestation.isRemove());
		doAimCheckbox = new CheckBox("Do Aim", skin);
		doAimCheckbox.setChecked(manifestation.isDoAim());
		add("Being(s): ");
		add(beingField);
		row();
		add("Damage: ");
		add(dmgField);
		row();
		add("Sprite Atlas: ");
		add(spriteAtlasField);
		row();
		add("Attack: ");
		add(attackField);
		row();
		add(doAimCheckbox);
		add("Aim (rads): ");
		add(aimField);
		row();
		add(killCheckbox);
		row();
		add(removeCheckbox);
	}

	@Override public AbstractQuestManifestation apply() {
		manifestation.setBeing(beingField.getText());
		manifestation.setDmg(Float.parseFloat(dmgField.getText()));
		manifestation.setKill(killCheckbox.isChecked());
		manifestation.setTextureAtlas(spriteAtlasField.getText());
		manifestation.setRemove(removeCheckbox.isChecked());
		manifestation.setDoAim(doAimCheckbox.isChecked());
		manifestation.setAim(Float.parseFloat(aimField.getText()));
		manifestation.setAttackTarget(attackField.getText());
		return manifestation;
	}
}
