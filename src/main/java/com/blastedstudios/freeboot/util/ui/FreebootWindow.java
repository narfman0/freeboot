package com.blastedstudios.freeboot.util.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.blastedstudios.gdxworld.ui.AbstractWindow;

public class FreebootWindow extends AbstractWindow{
	public FreebootWindow(String title, Skin skin) {
		super(title, skin);
		try{
			setColor(UIHelper.getColor(skin, "window-background", "background", "primary"));
		}catch(Exception e){}
	}
}