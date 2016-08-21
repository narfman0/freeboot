package com.blastedstudios.freeboot.plugin.quest.manifestation.cameratween;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.plugin.mode.quest.ManifestationTable;
import com.blastedstudios.gdxworld.ui.leveleditor.VertexTable;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class CameraTweenManifestationTable extends ManifestationTable{
	private final CameraTweenManifestation manifestation;
	private final VertexTable targetTable;
	private final TextField durationField;
	private final SelectBox<CameraTweenType> cameraTweenTypeList;
	private boolean modify;
	
	public CameraTweenManifestationTable(Skin skin, CameraTweenManifestation manifestation){
		super(skin);
		this.manifestation = manifestation;
		targetTable = new VertexTable(manifestation.getTarget(), skin);
		Button targetButton = new TextButton("+", skin);
		targetButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				modify = true;
			}
		});
		durationField = new TextField(manifestation.getDuration()+"", skin);
		cameraTweenTypeList = new SelectBox<CameraTweenType>(skin);
		cameraTweenTypeList.setItems(CameraTweenType.values());
		cameraTweenTypeList.setSelected(manifestation.getTweenType());
		add(new Label("Target: ", skin));
		add(targetTable);
		add(targetButton);
		row();
		add(new Label("Duration (s): ", skin));
		add(durationField);
		row();
		add(new Label("Tween Type: ", skin));
		add(cameraTweenTypeList);
	}

	@Override public AbstractQuestManifestation apply() {
		manifestation.setDuration(Float.parseFloat(durationField.getText()));
		manifestation.setTarget(targetTable.getVertex());
		manifestation.setTweenType(cameraTweenTypeList.getSelected());
		return manifestation;
	}

	@Override public void touched(Vector2 coordinates) {
		if(modify){
			targetTable.setVertex(coordinates.x, coordinates.y);
			modify = false;
		}
	}
}
