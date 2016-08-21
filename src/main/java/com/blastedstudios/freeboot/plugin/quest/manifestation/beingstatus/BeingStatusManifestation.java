package com.blastedstudios.freeboot.plugin.quest.manifestation.beingstatus;

import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class BeingStatusManifestation extends AbstractQuestManifestation{
	private static final long serialVersionUID = 1L;
	public static final BeingStatusManifestation DEFAULT = new BeingStatusManifestation();
	private String being = "", textureAtlas = "", attackTarget = "";
	private float dmg = 0f, aim = 0f;
	private boolean kill, remove, doAim;
	
	public BeingStatusManifestation(){}
	public BeingStatusManifestation(String being, float dmg, boolean kill, String textureAtlas,
			boolean remove, boolean doaim, float aim, String attackTarget){
		this.being = being;
		this.dmg = dmg;
		this.kill = kill;
		this.textureAtlas = textureAtlas;
		this.remove = remove;
		this.aim = aim;
		this.attackTarget = attackTarget;
		this.doAim = doaim;
	}
	
	@Override public CompletionEnum execute(float dt) {
		for(IBeingStatusHandler handler : PluginUtil.getPlugins(IBeingStatusHandler.class))
			handler.statusBeing(being, dmg, kill, textureAtlas, remove, doAim, aim, getAttackTarget());
		return CompletionEnum.COMPLETED;
	}

	@Override public AbstractQuestManifestation clone() {
		return new BeingStatusManifestation(being, dmg, kill, textureAtlas, remove, doAim, aim, attackTarget);
	}

	@Override public String toString() {
		return "[BeingStatusManifestation being:" + being + "]";
	}
	
	public String getBeing() {
		return being;
	}
	
	public void setBeing(String being) {
		this.being = being;
	}
	
	public float getDmg() {
		return dmg;
	}
	
	public void setDmg(float dmg) {
		this.dmg = dmg;
	}
	
	public boolean isKill() {
		return kill;
	}
	
	public void setKill(boolean kill) {
		this.kill = kill;
	}
	
	public String getTextureAtlas() {
		if(textureAtlas == null)
			textureAtlas = "";
		return textureAtlas;
	}
	
	public void setTextureAtlas(String textureAtlas) {
		this.textureAtlas = textureAtlas;
	}
	
	public boolean isRemove() {
		return remove;
	}
	
	public void setRemove(boolean remove) {
		this.remove = remove;
	}
	
	public float getAim() {
		return aim;
	}
	
	public void setAim(float aim) {
		this.aim = aim;
	}
	
	public String getAttackTarget() {
		if(attackTarget == null)
			attackTarget = "";
		return attackTarget;
	}
	
	public void setAttackTarget(String attackTarget) {
		this.attackTarget = attackTarget;
	}
	
	public boolean isDoAim() {
		return doAim;
	}
	
	public void setDoAim(boolean doAim) {
		this.doAim = doAim;
	}
}
