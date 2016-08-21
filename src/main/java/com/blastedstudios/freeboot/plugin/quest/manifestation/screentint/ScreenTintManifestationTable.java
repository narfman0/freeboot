package com.blastedstudios.freeboot.plugin.quest.manifestation.screentint;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.blastedstudios.gdxworld.plugin.mode.light.ColorTable;
import com.blastedstudios.gdxworld.plugin.mode.quest.ManifestationTable;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;
import com.blastedstudios.freeboot.plugin.quest.manifestation.screentint.ScreenTintManifestation.TintType;

public class ScreenTintManifestationTable extends ManifestationTable{
	private final ScreenTintManifestation manifestation;
	private final ColorTable tableColorTable, pixmapColorTable;
	private final TextField durationField;
	private final List<TintType> tintTypeList;
	
	public ScreenTintManifestationTable(Skin skin, ScreenTintManifestation manifestation){
		super(skin);
		this.manifestation = manifestation;
		tableColorTable = new ColorTable(manifestation.getTableColor(), skin);
		pixmapColorTable = new ColorTable(manifestation.getPixmapColor(), skin);
		durationField = new TextField(manifestation.getDuration()+"", skin);
		durationField.setMessageText("<duration (s)>");
		tintTypeList = new List<>(skin);
		tintTypeList.setItems(TintType.values());
		tintTypeList.setSelected(manifestation.getTintType());
		add("Table Color: ");
		add(tableColorTable);
		row();
		add("Pixmap Color: ");
		add(pixmapColorTable);
		row();
		add("Duration (s): ");
		add(durationField);
		add("Tint Type: ");
		add(tintTypeList);
	}

	@Override public AbstractQuestManifestation apply() {
		manifestation.setTableColor(tableColorTable.parseColor());
		manifestation.setPixmapColor(pixmapColorTable.parseColor());
		manifestation.setDuration(Float.parseFloat(durationField.getText()));
		manifestation.setTintType(tintTypeList.getSelected());
		return manifestation;
	}
}
