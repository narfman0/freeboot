package com.blastedstudios.freeboot.ui.gameplay.hud;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.blastedstudios.freeboot.world.being.component.IComponent;
import com.blastedstudios.freeboot.world.being.component.jetpack.JetpackComponent;

@PluginImplementation
public class PowerBarElement extends AbstractHUDElement {
	@Override public void render(SpriteBatch spriteBatch) {
		if((int)player.getStats().getJetpackMax() > 0)
			for(IComponent component : player.getListeners())
				if(component instanceof JetpackComponent){
					int power = (int)((JetpackComponent)component).getJetpackPower();
					drawString(spriteBatch, "Power: " + power + "/" + (int)player.getStats().getJetpackMax(), 
							Gdx.graphics.getWidth()/2, INSET, XAlign.MIDDLE, YAlign.UP);
				}
	}
}
