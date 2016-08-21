package com.blastedstudios.freeboot.ui.gameplay.hud;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.blastedstudios.freeboot.util.ui.UIHelper;

public class HPComponent {
	private final Sprite barOutsideSprite, barInsideSprite;
	
	public HPComponent(Skin skin){
		barOutsideSprite = UIHelper.getSprite(skin, "hud-hpbar-outside", "hud-hpbar", "circle");
		try{
			barOutsideSprite.setColor(UIHelper.getColor(skin, "hud-hpbar-outside", "hud-hpbar", "primary"));
		}catch(Exception e){}
		barInsideSprite = UIHelper.getSprite(skin, "hud-hpbar-inside", "hud-hpbar", "circle");
		try{
			barInsideSprite.setColor(UIHelper.getColor(skin, "hud-hpbar-inside", "hud-hpbar", "primary"));
		}catch(Exception e){}
	}
	
	public void render(SpriteBatch spriteBatch, float percentHP, Vector2 position){
		int width = barInsideSprite.getRegionWidth();
		barOutsideSprite.setPosition(position.x - barOutsideSprite.getWidth()/2f, 
				position.y - barOutsideSprite.getHeight()/2f);
		barInsideSprite.setPosition(position.x - barInsideSprite.getWidth()/2f - ((1f-percentHP)*width/2), 
				position.y - barInsideSprite.getHeight()/2f);
		barOutsideSprite.draw(spriteBatch);
		barInsideSprite.setRegionWidth((int)(width * percentHP));
		barInsideSprite.setScale(percentHP, 1f);
		barInsideSprite.draw(spriteBatch);
		barInsideSprite.setRegionWidth(width);
	}
}
