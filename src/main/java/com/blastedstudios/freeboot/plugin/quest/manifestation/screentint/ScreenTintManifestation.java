package com.blastedstudios.freeboot.plugin.quest.manifestation.screentint;

import com.badlogic.gdx.graphics.Color;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class ScreenTintManifestation extends AbstractQuestManifestation{
	private static final long serialVersionUID = 1L;
	public static final ScreenTintManifestation DEFAULT = new ScreenTintManifestation();
	private Color tableColor = Color.BLACK.cpy(), pixmapColor = Color.BLACK.cpy();
	private TintType tintType = TintType.FADEIN;
	private float duration = 2f;
	
	public ScreenTintManifestation(){}
	public ScreenTintManifestation(Color color, Color pixmapColor, TintType tintType, float duration){
		this.tableColor = color;
		this.pixmapColor = pixmapColor;
		this.tintType = tintType;
		this.duration = duration;
	}
	
	@Override public CompletionEnum execute(float dt) {
		for(IScreenTintHandler handler : PluginUtil.getPlugins(IScreenTintHandler.class))
			handler.screenTint(tableColor, pixmapColor, duration, tintType);
		return CompletionEnum.COMPLETED;
	}

	@Override public AbstractQuestManifestation clone() {
		return new ScreenTintManifestation(tableColor, pixmapColor, tintType, duration);
	}

	@Override public String toString() {
		return "[ScreenTintManifestation tableColor:" + tableColor + " pixmapColor: " + pixmapColor + 
				" tintType: " + tintType + " duration: " + duration + "]";
	}
	
	public Color getTableColor() {
		return tableColor;
	}
	
	public void setTableColor(Color color) {
		this.tableColor = color;
	}
	
	public TintType getTintType() {
		return tintType;
	}
	
	public void setTintType(TintType tintType) {
		this.tintType = tintType;
	}

	public float getDuration() {
		return duration;
	}
	
	public void setDuration(float duration) {
		this.duration = duration;
	}

	public Color getPixmapColor() {
		return pixmapColor;
	}
	
	public void setPixmapColor(Color pixmapColor) {
		this.pixmapColor = pixmapColor;
	}

	public static enum TintType{
		FADEIN, FADEOUT
	}
}
