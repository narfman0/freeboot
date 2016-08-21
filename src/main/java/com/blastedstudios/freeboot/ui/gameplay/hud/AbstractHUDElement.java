package com.blastedstudios.freeboot.ui.gameplay.hud;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.world.being.NPC;
import com.blastedstudios.freeboot.world.being.Player;

public abstract class AbstractHUDElement implements IHUDElement{
	private static GlyphLayout GLYPH_LAYOUT = new GlyphLayout();
	protected static final int INSET = Properties.getInt("hud.inset", 4);
	protected BitmapFont font;
	protected Player player;
	protected Skin skin;
	
	public AbstractHUDElement initialize(Skin skin, BitmapFont font, Player player){
		this.skin = skin;
		this.font = font;
		this.player = player;
		return this;
	}
	
	protected void drawString(SpriteBatch spriteBatch, String string, float x, 
			float y, XAlign xAlign, YAlign yAlign){
		GLYPH_LAYOUT.setText(font, string);
		switch(xAlign){
		case LEFT:
			x -= GLYPH_LAYOUT.width;
			break;
		case MIDDLE:
			x -= (GLYPH_LAYOUT.width / 2);
			break;
		case RIGHT:
			break;
		}
		switch(yAlign){
		case UP:
			y += GLYPH_LAYOUT.height;
			break;
		case DOWN:
			break;
		}
		font.draw(spriteBatch, string, x, y);
	}

	public abstract void render(final SpriteBatch spriteBatch);
	public void npcAdded(NPC npc){}
	protected enum XAlign{LEFT,MIDDLE,RIGHT}
	protected enum YAlign{UP,DOWN}
}
