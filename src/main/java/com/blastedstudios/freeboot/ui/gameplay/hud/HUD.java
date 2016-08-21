package com.blastedstudios.freeboot.ui.gameplay.hud;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.freeboot.util.ui.UIHelper;
import com.blastedstudios.freeboot.world.being.Player;

/**
 * Class manages HUD elements. All HUD elements must extend AbstractHUDElement
 * (and therefore implement IHUDElement) and annotate @PluginImplementation,
 * then will be automatically added to the list.
 */
public class HUD {
	private final ArrayList<IHUDElement> elements = new ArrayList<>();
	private final SpriteBatch spriteBatch = new SpriteBatch();
	private final BitmapFont font;
	
	public HUD(Skin skin, Player player){
		font = UIHelper.getFont(skin, "hud", "blur", "default");
		try{
			font.setColor(UIHelper.getColor(skin, "hud-font", "secondary"));
		}catch(Exception e){}
		for(IHUDElement element : PluginUtil.getPlugins(IHUDElement.class))
			elements.add(element.initialize(skin, font, player));
	}
	
	public void render() {
		spriteBatch.begin();
		for(IHUDElement element : elements)
			element.render(spriteBatch);
		spriteBatch.end();
	}
}
