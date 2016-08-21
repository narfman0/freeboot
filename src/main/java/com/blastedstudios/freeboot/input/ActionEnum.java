package com.blastedstudios.freeboot.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.blastedstudios.gdxworld.util.Properties;

public enum ActionEnum {
	LEFT(new int[]{Properties.getInt("input.left", Keys.A), Properties.getInt("input.left2", Keys.LEFT)}),
	RIGHT(new int[]{Properties.getInt("input.right", Keys.D), Properties.getInt("input.right2", Keys.RIGHT)}),
	DOWN(new int[]{Properties.getInt("input.down", Keys.S), Properties.getInt("input.down2", Keys.DOWN)}),
	UP(new int[]{Properties.getInt("input.up", Keys.W), Properties.getInt("input.up2", Keys.UP)}),
	ACTION(new int[]{Properties.getInt("input.action", Keys.E), Properties.getInt("input.action2", Keys.ENTER)}),
	INVENTORY(new int[]{Properties.getInt("input.inventory", Keys.I), Properties.getInt("input.inventory2", Keys.O)}),
	RELOAD(new int[]{Properties.getInt("input.reload", Keys.R), Properties.getInt("input.reload2", Keys.DEL)}),
	CROUCH(new int[]{Properties.getInt("input.crouch", Keys.CONTROL_LEFT), Properties.getInt("input.crouch2", Keys.C)}),
	BACK(new int[]{Properties.getInt("input.back", Keys.ESCAPE), Properties.getInt("input.back2", Keys.BACK)}),
	CHAT(new int[]{Properties.getInt("input.talk", Keys.T), Properties.getInt("input.talk2", Keys.Y)}),
	MODIFIER(new int[]{Properties.getInt("input.modifier", Keys.SHIFT_LEFT), Properties.getInt("input.modifier2", Keys.SHIFT_RIGHT)}),
	CONSOLE(new int[]{Properties.getInt("input.console", Keys.PERIOD), Properties.getInt("input.console2", Keys.COLON)}),
	UNDEFINED(new int[]{-1,-1});
	
	public int[] keys;
	
	private ActionEnum(int[] keys){
		this.keys = keys;
	}
	
	public static ActionEnum fromKey(int key){
		if(key == -1)
			return UNDEFINED;
		for(ActionEnum actionType : values())
			if(actionType.matches(key))
				return actionType;
		return UNDEFINED;
	}
	
	public boolean matches(int key){
		for(int targetKey : keys)
			if(targetKey == key)
				return true;
		return false;
	}
	
	public boolean isPressed(){
		for(int key : keys)
			if(Gdx.input.isKeyPressed(key))
				return true;
		return false;
	}
}
