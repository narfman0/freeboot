package com.blastedstudios.freeboot.ui.gameplay.hud;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.blastedstudios.freeboot.world.being.Player;

@PluginImplementation
public class HPBarElement extends AbstractHUDElement{
	private HPComponent hpBar;
	
	@Override public AbstractHUDElement initialize(Skin skin, BitmapFont font, Player player){
		super.initialize(skin, font, player);
		hpBar = new HPComponent(skin);
		return this;
	}
	
	@Override public void render(SpriteBatch spriteBatch) {
		float percentHP = player.getHp()/player.getMaxHp();
		hpBar.render(spriteBatch, percentHP, new Vector2(106f, 32f));
	}
}
