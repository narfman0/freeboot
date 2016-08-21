package com.blastedstudios.freeboot.util.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.util.Properties;

public class FreebootTextButton extends TextButton {
	public FreebootTextButton(String name, Skin skin, EventListener... listeners){
		super(name, skin);
		try{
			setColor(UIHelper.getColor(skin, "textbutton-background", "button-background", "background", "secondary"));
		}catch(Exception e){}
		addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				Gdx.audio.newSound(Gdx.files.internal("data/sounds/ui/button4.mp3")).play(Properties.getFloat("sound.volume", 1f));
			}
		});
		for(EventListener listener : listeners)
			addListener(listener);
	}
}
