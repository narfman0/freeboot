package com.blastedstudios.freeboot.plugin.quest.manifestation.addxp;

import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;
import com.blastedstudios.freeboot.world.QuestManifestationExecutor;

public class AddXPManifestation extends AbstractQuestManifestation {
	private static final long serialVersionUID = 1L;
	public static final AddXPManifestation DEFAULT = new AddXPManifestation();
	private String being = "player";
	private long xp;
	
	public AddXPManifestation(){}
	public AddXPManifestation(String being, long xp){
		this.being = being;
		this.xp = xp;
	}
	
	@Override public CompletionEnum execute(float dt) {
		return ((QuestManifestationExecutor)executor).addXP(being, xp);
	}

	@Override public AbstractQuestManifestation clone() {
		return new AddXPManifestation(being, xp);
	}

	@Override public String toString() {
		return "[AddXPManifestation being:" + being + " xp:" + xp + "]";
	}
	
	public String getBeing() {
		return being;
	}
	
	public void setBeing(String being) {
		this.being = being;
	}
	
	public long getXp() {
		return xp;
	}
	
	public void setXp(long xp) {
		this.xp = xp;
	}
}