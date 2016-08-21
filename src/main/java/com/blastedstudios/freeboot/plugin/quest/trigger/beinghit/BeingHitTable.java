package com.blastedstudios.freeboot.plugin.quest.trigger.beinghit;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.blastedstudios.gdxworld.plugin.mode.quest.TriggerTable;
import com.blastedstudios.gdxworld.world.quest.trigger.AbstractQuestTrigger;

public class BeingHitTable extends TriggerTable {
	private final BeingHitTrigger trigger;
	private final TextField beingText, originText, damageAmountText, damageRatioText;
	private final CheckBox deathBox;

	public BeingHitTable(Skin skin, BeingHitTrigger trigger) {
		super(skin, trigger);
		this.trigger = trigger;
		beingText = new TextField(trigger.getTarget(), skin);
		beingText.setMessageText("<being name regex>");
		damageAmountText = new TextField(trigger.getDamageAmount()+"", skin);
		damageAmountText.setMessageText("<damage amount>");
		damageRatioText = new TextField(trigger.getDamageRatio()+"", skin);
		damageRatioText.setMessageText("<damage ratio>");
		originText = new TextField(trigger.getOrigin(), skin);
		originText.setMessageText("<origin name regex>");
		deathBox = new CheckBox("Death", skin);
		deathBox.setChecked(trigger.isDeath());
		add("Being: ");
		add(beingText);
		row();
		add("Damage amount: ");
		add(damageAmountText);
		row();
		add("Damage ratio: ");
		add(damageRatioText);
		row();
		add("Origin: ");
		add(originText);
		row();
		add(deathBox);
	}

	@Override public AbstractQuestTrigger apply() {
		super.apply(trigger);
		trigger.setTarget(beingText.getText());
		trigger.setDamageAmount(Float.parseFloat(damageAmountText.getText()));
		trigger.setDamageRatio(Float.parseFloat(damageRatioText.getText()));
		trigger.setOrigin(originText.getText());
		trigger.setDeath(deathBox.isChecked());
		return trigger;
	}
}
