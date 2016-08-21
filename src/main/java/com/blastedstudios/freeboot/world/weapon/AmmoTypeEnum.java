package com.blastedstudios.freeboot.world.weapon;

public enum AmmoTypeEnum {
	_00buck(".00 Buck", "00buck"),
	_38acp(".38 ACP", "38ACP"),
	_38specialJHP(".38 Special JHP", "38specialJHP"), 
	_7_62mm("7.62mm", "7.62mm"),
	_9mm("9mm", "38ACP"),
	GAS("Gasoline", "gasolineIgnited"),
	RPG("RPG-7", "rpg7HEAT");
	
	public final String prettyName, textureName;
	
	private AmmoTypeEnum(String prettyName, String textureName){
		this.prettyName = prettyName;
		this.textureName = textureName;
	}
	
	public static AmmoTypeEnum fromPrettyName(String prettyName){
		for(AmmoTypeEnum value : values())
			if(value.prettyName.equals(prettyName))
				return value;
		return null;
	}
}
