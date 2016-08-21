package com.blastedstudios.freeboot.plugin.quest.manifestation.pathchange;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.blastedstudios.gdxworld.plugin.mode.quest.ManifestationTable;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class PathChangeManifestationTable extends ManifestationTable{
	private final PathChangeManifestation manifestation;
	private final TextField beingField, pathField;
	
	public PathChangeManifestationTable(Skin skin, PathChangeManifestation manifestation){
		super(skin);
		this.manifestation = manifestation;
		beingField = new TextField(manifestation.getBeing(), skin);
		beingField.setMessageText("<being name>");
		pathField = new TextField(manifestation.getPath(), skin);
		pathField.setMessageText("<path name>");
		add(new Label("Being: ", skin));
		add(beingField);
		add(new Label("Path: ", skin));
		add(pathField);
	}

	@Override public AbstractQuestManifestation apply() {
		manifestation.setBeing(beingField.getText());
		manifestation.setPath(pathField.getText());
		return manifestation;
	}
}