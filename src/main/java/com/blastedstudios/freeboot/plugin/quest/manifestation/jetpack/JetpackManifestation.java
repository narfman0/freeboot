package com.blastedstudios.freeboot.plugin.quest.manifestation.jetpack;

import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;
import com.blastedstudios.freeboot.world.QuestManifestationExecutor;

public class JetpackManifestation extends AbstractQuestManifestation{
	private static final long serialVersionUID = 1L;
	public static final JetpackManifestation DEFAULT = new JetpackManifestation();
	private String beingName = "";
	private boolean changeMax, changeRecharge, changeImpulse;
	private float max, recharge, impulse;
	
	public JetpackManifestation(){}
	
	public JetpackManifestation(String beingName, boolean changeMax, float max, boolean changeRecharge, 
			float recharge, boolean changeImpulse, float impulse){
		this.beingName = beingName;
		this.changeMax = changeMax;
		this.changeRecharge = changeRecharge;
		this.changeImpulse = changeImpulse;
		this.max = max;
		this.recharge = recharge;
		this.impulse = impulse;
	}
	
	@Override public CompletionEnum execute(float dt) {
		return ((QuestManifestationExecutor)executor).jetpackManifestation(beingName,
				changeMax, max, changeRecharge, recharge, changeImpulse, impulse);
	}

	@Override public AbstractQuestManifestation clone() {
		return new JetpackManifestation(beingName,
				changeMax, max, changeRecharge, recharge, changeImpulse, impulse);
	}

	@Override public String toString() {
		return "[JetpackManifestation beingName:" + beingName + "]";
	}
	
	public boolean isChangeMax() {
		return changeMax;
	}
	
	public void setChangeMax(boolean changeMax) {
		this.changeMax = changeMax;
	}

	public boolean isChangeRecharge() {
		return changeRecharge;
	}

	public void setChangeRecharge(boolean changeRecharge) {
		this.changeRecharge = changeRecharge;
	}

	public String getBeingName() {
		return beingName;
	}

	public void setBeingName(String beingName) {
		this.beingName = beingName;
	}

	public boolean isChangeImpulse() {
		return changeImpulse;
	}

	public void setChangeImpulse(boolean changeImpulse) {
		this.changeImpulse = changeImpulse;
	}

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}

	public float getRecharge() {
		return recharge;
	}

	public void setRecharge(float recharge) {
		this.recharge = recharge;
	}

	public float getImpulse() {
		return impulse;
	}

	public void setImpulse(float impulse) {
		this.impulse = impulse;
	}
}
