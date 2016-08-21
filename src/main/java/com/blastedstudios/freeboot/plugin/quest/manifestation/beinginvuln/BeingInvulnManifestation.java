package com.blastedstudios.freeboot.plugin.quest.manifestation.beinginvuln;

import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;
import com.blastedstudios.freeboot.world.QuestManifestationExecutor;

public class BeingInvulnManifestation extends AbstractQuestManifestation{
	private static final long serialVersionUID = 1L;
	public static final BeingInvulnManifestation DEFAULT = new BeingInvulnManifestation();
	private String being = "player";
	private boolean invuln;
	
	public BeingInvulnManifestation(){}
	public BeingInvulnManifestation(String being, boolean invuln){
		this.being = being;
		this.invuln = invuln;
	}
	
	@Override public CompletionEnum execute(float dt) {
		return ((QuestManifestationExecutor)executor).beingInvuln(being, invuln);
	}

	@Override public AbstractQuestManifestation clone() {
		return new BeingInvulnManifestation(being, invuln);
	}

	@Override public String toString() {
		return "[BeingInvulnManifestation being:" + being + " invuln:" + invuln + "]";
	}
	
	public String getBeing() {
		return being;
	}
	
	public void setBeing(String being) {
		this.being = being;
	}
	
	public boolean isInvuln() {
		return invuln;
	}
	
	public void setInvuln(boolean invuln) {
		this.invuln = invuln;
	}
}
