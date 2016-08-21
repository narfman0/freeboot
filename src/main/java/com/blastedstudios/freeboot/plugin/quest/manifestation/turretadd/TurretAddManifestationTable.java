package com.blastedstudios.freeboot.plugin.quest.manifestation.turretadd;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.plugin.mode.quest.ManifestationTable;
import com.blastedstudios.gdxworld.ui.leveleditor.VertexTable;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class TurretAddManifestationTable extends ManifestationTable{
	private final TurretAddManifestation manifestation;
	private final TextField weaponField, baseResourceField, directionField, directionLowField, directionHighField;
	private final VertexTable locationTable, mountLocationTable;
	private VertexTable selected = null;

	public TurretAddManifestationTable(final Skin skin, final TurretAddManifestation manifestation){
		super(skin);
		this.manifestation = manifestation;
		weaponField = new TextField(manifestation.getWeapon(), skin);
		weaponField.setMessageText("<weapon name>");
		baseResourceField = new TextField(manifestation.getBaseResource()+"", skin);
		baseResourceField.setMessageText("<base resource>");
		directionField = new TextField(manifestation.getDirection()+"", skin);
		directionField.setMessageText("<direction>");
		directionLowField = new TextField(manifestation.getDirectionLow()+"", skin);
		directionLowField.setMessageText("<direction low>");
		directionHighField = new TextField(manifestation.getDirectionHigh()+"", skin);
		directionHighField.setMessageText("<direction high>");
		locationTable = new VertexTable(manifestation.getLocation(), skin, null);
		Button locationButton = new TextButton("+", skin);
		locationButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				selected = locationTable;
			}
		});
		mountLocationTable = new VertexTable(manifestation.getMountLocation(), skin, null);
		Button mountLocationButton = new TextButton("+", skin);
		mountLocationButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				selected = mountLocationTable;
			}
		});

		add(new Label("Location: ", skin));
		add(locationTable);
		add(locationButton);
		row();
		add(new Label("Mount Location: ", skin));
		add(mountLocationTable);
		add(mountLocationButton);
		row();
		add(new Label("Weapon: ", skin));
		add(weaponField);
		row();
		add(new Label("Base Resource: ", skin));
		add(baseResourceField);
		row();
		add(new Label("Direction: ", skin));
		add(directionField);
		row();
		add(new Label("Direction Low: ", skin));
		add(directionLowField);
		row();
		add(new Label("Direction High: ", skin));
		add(directionHighField);
		row();
	}

	@Override public AbstractQuestManifestation apply() {
		manifestation.setLocation(locationTable.getVertex());
		manifestation.setMountLocation(mountLocationTable.getVertex());
		manifestation.setWeapon(weaponField.getText());
		manifestation.setBaseResource(baseResourceField.getText());
		manifestation.setDirection(Float.parseFloat(directionField.getText()));
		manifestation.setDirectionLow(Float.parseFloat(directionLowField.getText()));
		manifestation.setDirectionHigh(Float.parseFloat(directionHighField.getText()));
		return manifestation;
	}

	@Override public void touched(Vector2 coordinates) {
		if(selected != null){
			selected.setVertex(coordinates.x, coordinates.y);
			selected = null;
		}
	}
}
