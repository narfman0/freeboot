package com.blastedstudios.freeboot.world;

import java.io.Serializable;

import com.blastedstudios.freeboot.world.being.NPCData;

public class Stats implements Serializable {
	private static final long serialVersionUID = 1L;
	private float hp, attack, defense, hpPerLevel, hpRegen, hpRegenPerLevel, attackPerLevel, defensePerLevel,
		damage, accuracy, rateOfFire, reloadSpeed, muzzleVelocity, recoil, 
		jetpackRecharge, jetpackMax, jetpackImpulse, distanceAware, distanceVision;
	private int projectileCount, roundsPerClip;

	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	public float getRateOfFire() {
		return rateOfFire;
	}

	public void setRateOfFire(float rateOfFire) {
		this.rateOfFire = rateOfFire;
	}

	public float getReloadSpeed() {
		return reloadSpeed;
	}

	public void setReloadSpeed(float reloadSpeed) {
		this.reloadSpeed = reloadSpeed;
	}

	public float getMuzzleVelocity() {
		return muzzleVelocity;
	}

	public void setMuzzleVelocity(float muzzleVelocity) {
		this.muzzleVelocity = muzzleVelocity;
	}

	public float getRecoil() {
		return recoil;
	}

	public void setRecoil(float recoil) {
		this.recoil = recoil;
	}

	public int getProjectileCount() {
		return projectileCount;
	}

	public void setProjectileCount(int projectileCount) {
		this.projectileCount = projectileCount;
	}

	public int getRoundsPerClip() {
		return roundsPerClip;
	}

	public void setRoundsPerClip(int roundsPerClip) {
		this.roundsPerClip = roundsPerClip;
	}

	public float getAttack() {
		return attack;
	}

	public void setAttack(float attack) {
		this.attack = attack;
	}

	public float getDefense() {
		return defense;
	}

	public void setDefense(float defense) {
		this.defense = defense;
	}

	public float getHp() {
		return hp;
	}

	public void setHp(float hp) {
		this.hp = hp;
	}

	public float getHpPerLevel() {
		return hpPerLevel;
	}

	public void setHpPerLevel(float hpPerLevel) {
		this.hpPerLevel = hpPerLevel;
	}

	public float getAttackPerLevel() {
		return attackPerLevel;
	}

	public void setAttackPerLevel(float attackPerLevel) {
		this.attackPerLevel = attackPerLevel;
	}

	public float getDefensePerLevel() {
		return defensePerLevel;
	}

	public void setDefensePerLevel(float defensePerLevel) {
		this.defensePerLevel = defensePerLevel;
	}

	public float getJetpackRecharge() {
		return jetpackRecharge;
	}

	public void setJetpackRecharge(float jetpackRecharge) {
		this.jetpackRecharge = jetpackRecharge;
	}
	
	public boolean hasJetpack(){
		return jetpackMax != 0f;
	}

	public float getJetpackMax() {
		return jetpackMax;
	}

	public void setJetpackMax(float jetpackMax) {
		this.jetpackMax = jetpackMax;
	}

	public float getJetpackImpulse() {
		return jetpackImpulse;
	}

	public void setJetpackImpulse(float jetpackImpulse) {
		this.jetpackImpulse = jetpackImpulse;
	}

	public float getDistanceAware() {
		return distanceAware;
	}

	public void setDistanceAware(float distanceAware) {
		this.distanceAware = distanceAware;
	}

	public float getDistanceVision() {
		return distanceVision;
	}

	public void setDistanceVision(float distanceVision) {
		this.distanceVision = distanceVision;
	}

	public float getHpRegen() {
		return hpRegen;
	}

	public void setHpRegen(float hpRegen) {
		this.hpRegen = hpRegen;
	}

	public float getHpRegenPerLevel() {
		return hpRegenPerLevel;
	}

	public void setHpRegenPerLevel(float hpRegenPerLevel) {
		this.hpRegenPerLevel = hpRegenPerLevel;
	}
	
	public static Stats parseNPCData(NPCData npcData){
		Stats stats = new Stats();
		stats.hp = npcData.getFloat("HP");
		stats.attack = npcData.getFloat("Attack");
		stats.defense = npcData.getFloat("Defense");
		stats.hpPerLevel = npcData.getFloat("HPPerLevel");
		stats.setHpRegen(npcData.getFloat("HPRegen"));
		stats.setHpRegenPerLevel(npcData.getFloat("HPRegenPerLevel"));
		stats.attackPerLevel = npcData.getFloat("AttackPerLevel");
		stats.defensePerLevel = npcData.getFloat("DefensePerLevel");
		stats.jetpackRecharge = npcData.getFloat("JetpackRecharge");
		stats.jetpackMax = npcData.getFloat("JetpackMax");
		stats.jetpackImpulse = npcData.getFloat("JetpackImpulse");
		stats.distanceAware = npcData.getFloat("DistanceAware");
		stats.distanceVision = npcData.getFloat("DistanceVision");
		return stats;
	}
}
