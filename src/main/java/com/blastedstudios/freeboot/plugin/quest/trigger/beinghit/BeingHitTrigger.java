package com.blastedstudios.freeboot.plugin.quest.trigger.beinghit;

import com.blastedstudios.gdxworld.world.quest.trigger.AbstractQuestTrigger;
import com.blastedstudios.freeboot.world.QuestTriggerInformationProvider;
import com.blastedstudios.freeboot.world.weapon.DamageStruct;

public class BeingHitTrigger extends AbstractQuestTrigger implements IBeingHitListener {
	private static final long serialVersionUID = 1L;
	public static final BeingHitTrigger DEFAULT = new BeingHitTrigger(); 
	private String target = ".*", origin = ".*";
	private float damageAmount = -1f, damageRatio = -1f;
	private boolean death;
	private transient boolean invoked;
	
	public BeingHitTrigger(){}
	public BeingHitTrigger(String target, String origin, float damageAmount, float damageRatio, boolean death){
		this.target = target;
		this.damageAmount = damageAmount;
		this.origin = origin;
		this.damageRatio = damageRatio;
		this.death = death;
	}

	@Override public boolean activate(float dt) {
		((QuestTriggerInformationProvider)getProvider()).addBeingHitListener(this);
		return invoked;
	}
	
	@Override public AbstractQuestTrigger clone(){
		return super.clone(new BeingHitTrigger(target, origin, damageAmount, damageRatio, death));
	}

	@Override public String toString() {
		return "[BeingHitTrigger target:" + target + " damageAmount:" + 
				damageAmount + " origin:" + origin + "]";
	}
	
	public String getTarget() {
		return target;
	}
	
	public void setTarget(String target) {
		this.target = target;
	}
	
	public String getOrigin() {
		return origin;
	}
	
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	public float getDamageAmount() {
		return damageAmount;
	}
	
	public void setDamageAmount(float damageAmount) {
		this.damageAmount = damageAmount;
	}
	
	public float getDamageRatio() {
		return damageRatio;
	}
	
	public void setDamageRatio(float damageRatio) {
		this.damageRatio = damageRatio;
	}
	
	public boolean isDeath() {
		return death;
	}
	
	public void setDeath(boolean death) {
		this.death = death;
	}
	
	@Override public void beingHit(DamageStruct damageStruct) {
		float hpAfter =  damageStruct.getTarget().getHp() - damageStruct.getDamage(),
				dmgRatio = hpAfter / damageStruct.getTarget().getMaxHp();
		boolean damageThresholdSatisfied = damageRatio == -1f ? true : dmgRatio < damageRatio;
		if(damageStruct.getDamage() >= damageAmount && damageThresholdSatisfied &&
				damageStruct.getTarget().getName().matches(target) &&
				damageStruct.getOrigin() != null && damageStruct.getOrigin().getName().matches(origin) &&
				!death ^ damageStruct.getTarget().getHp() <= 0f)
			invoked = true;
	}
}