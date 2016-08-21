package com.blastedstudios.freeboot.ui.gameplay.hud;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

@PluginImplementation
public class EquippedElement extends AbstractHUDElement {
	@Override public void render(SpriteBatch spriteBatch) {
		if(player.getEquippedWeapon() != null)
			drawString(spriteBatch, player.getEquippedWeapon().toStringPretty(player), 
					Gdx.graphics.getWidth() - INSET, INSET, XAlign.LEFT, YAlign.UP);
	}
}
