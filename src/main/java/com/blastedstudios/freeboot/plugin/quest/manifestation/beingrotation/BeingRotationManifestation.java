package com.blastedstudios.freeboot.plugin.quest.manifestation.beingrotation;

import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;
import com.blastedstudios.freeboot.world.QuestManifestationExecutor;

public class BeingRotationManifestation extends AbstractQuestManifestation {
	private static final long serialVersionUID = 1L;
	public static final BeingRotationManifestation DEFAULT = new BeingRotationManifestation();
	private boolean fixedRotation;
	private float torque;
	private String being = "";
	
	public BeingRotationManifestation(){}
	public BeingRotationManifestation(String being, boolean fixedRotation, float torque){
		this.fixedRotation = fixedRotation;
		this.torque = torque;
		this.being = being;
	}
	
	@Override public CompletionEnum execute(float dt) {
		return ((QuestManifestationExecutor)executor).beingRotation(being, fixedRotation, torque);
	}

	@Override public AbstractQuestManifestation clone() {
		return new BeingRotationManifestation(being, fixedRotation, torque);
	}

	@Override public String toString() {
		return "[BeingRotationManifestation being:" + being + " fixedRotation:" + 
				fixedRotation + " torque:" + torque + "]";
	}
	
	public boolean isFixedRotation() {
		return fixedRotation;
	}
	
	public void setFixedRotation(boolean fixedRotation) {
		this.fixedRotation = fixedRotation;
	}
	
	public float getTorque() {
		return torque;
	}
	
	public void setTorque(float torque) {
		this.torque = torque;
	}
	
	public String getBeing() {
		return being;
	}
	
	public void setBeing(String being) {
		this.being = being;
	}
}