package com.blastedstudios.freeboot.ui.gameplay.hud;

import net.xeoh.plugins.base.Plugin;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.blastedstudios.freeboot.world.being.Player;

public interface IHUDElement extends Plugin{
	void render(final SpriteBatch spriteBatch);
	IHUDElement initialize(Skin skin, BitmapFont font, Player player);
}
