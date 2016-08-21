package com.blastedstudios.freeboot.ui;

import java.util.HashMap;

import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.freeboot.input.ActionEnum;
import com.blastedstudios.freeboot.ui.main.MainScreen;

public abstract class FreebootScreen extends AbstractScreen {
	protected final HashMap<ActionEnum, AbstractInputHandler> inputHandlers = new HashMap<>();
	
	public FreebootScreen(GDXGame game) {
		super(game, MainScreen.SKIN_PATH);
	}
	
	public void register(ActionEnum actionType, AbstractInputHandler inputHandler){
		inputHandlers.put(actionType, inputHandler);
	}

	@Override public boolean keyDown(int key) {
		AbstractInputHandler handler = inputHandlers.get(ActionEnum.fromKey(key));
		if(handler != null)
			handler.down();
		return false;
	}

	@Override public boolean keyUp(int key) {
		AbstractInputHandler handler = inputHandlers.get(ActionEnum.fromKey(key));
		if(handler != null)
			handler.up();
		return false;
	}
	
	public abstract class AbstractInputHandler {
		public void down(){}
		public void up(){}
	}
}
