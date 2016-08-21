package com.blastedstudios.freeboot.plugin.quest.manifestation.weaponadd;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;
import com.blastedstudios.freeboot.world.QuestManifestationExecutor;

public class WeaponAddManifestation extends AbstractQuestManifestation {
	private static final long serialVersionUID = 1L;
	public static final WeaponAddManifestation DEFAULT = new WeaponAddManifestation();
	private String weapon = "wrench", target = "player";
	private WeaponAddType weaponAddType = WeaponAddType.LOCATION;
	private Vector2 location = new Vector2();
	
	public WeaponAddManifestation(){}
	public WeaponAddManifestation(String weapon, String target, WeaponAddType weaponAddType, Vector2 location){
		this.weapon = weapon;
		this.target = target;
		this.weaponAddType = weaponAddType;
		this.location = location;
	}
	
	@Override public CompletionEnum execute(float dt) {
		switch(weaponAddType){
		case PERSON:
			return ((QuestManifestationExecutor)executor).weaponAdd(weapon, target);
		default:
			break;
		}
		Log.log("WeaponAddManifestation.execute", "Case not matched: " + weaponAddType + ", skipping");
		return CompletionEnum.COMPLETED;
	}

	@Override public AbstractQuestManifestation clone() {
		return new WeaponAddManifestation(weapon, target, weaponAddType, location.cpy());
	}

	@Override public String toString() {
		return "[WeaponAddManifestation weapon:" + weapon + "]";
	}

	public String getWeapon() {
		return weapon;
	}

	public void setWeapon(String weapon) {
		this.weapon = weapon;
	}
	
	public WeaponAddType getWeaponAddType() {
		return weaponAddType;
	}
	public void setWeaponAddType(WeaponAddType weaponAddType) {
		this.weaponAddType = weaponAddType;
	}

	public Vector2 getLocation() {
		return location;
	}
	public void setLocation(Vector2 location) {
		this.location = location;
	}

	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}

	public static enum WeaponAddType{PERSON, LOCATION}
}