package com.blastedstudios.freeboot.util.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.blastedstudios.gdxworld.util.Log;

public class UIHelper {
	public static Color getColor(Skin skin, String... names){
		for(String name : names)
			try{
				return skin.getColor(name);
			}catch(GdxRuntimeException e){}
		handleError("Color", names);
		return null;
	}
	
	public static BitmapFont getFont(Skin skin, String... names){
		for(String name : names)
			try{
				return skin.getFont(name);
			}catch(GdxRuntimeException e){}
		handleError("Font", names);
		return null;
	}
	
	public static Sprite getSprite(Skin skin, String... names){
		for(String name : names)
			try{
				return skin.getSprite(name);
			}catch(GdxRuntimeException e){}
		handleError("Sprite", names);
		return null;
	}
	
	public static NinePatch getPatch(Skin skin, String... names){
		for(String name : names)
			try{
				return skin.getPatch(name);
			}catch(GdxRuntimeException e){}
		handleError("Patch", names);
		return null;
	}
	
	private static void handleError(String origin, String... names){
		StringBuffer buf = new StringBuffer("No match for name(s) ");
		for(String name : names)
			buf.append(name + ",");
		Log.debug("UIHelper.get" + origin, buf.toString());
	}
}
