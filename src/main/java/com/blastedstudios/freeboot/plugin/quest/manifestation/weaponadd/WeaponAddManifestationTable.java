package com.blastedstudios.freeboot.plugin.quest.manifestation.weaponadd;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.plugin.mode.quest.ManifestationTable;
import com.blastedstudios.gdxworld.ui.leveleditor.VertexTable;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;
import com.blastedstudios.freeboot.plugin.quest.manifestation.weaponadd.WeaponAddManifestation.WeaponAddType;

public class WeaponAddManifestationTable extends ManifestationTable{
	private final WeaponAddManifestation manifestation;
	private final TextField weaponField, targetField;
	private final List<WeaponAddType> weaponAddTypeField;
	private final VertexTable locationTable;
	private final Table targetTable = new Table();
	
	public WeaponAddManifestationTable(final Skin skin, final WeaponAddManifestation manifestation){
		super(skin);
		this.manifestation = manifestation;
		weaponField = new TextField(manifestation.getWeapon(), skin);
		weaponField.setMessageText("<weapon name>");
		weaponAddTypeField = new List<WeaponAddType>(skin);
		weaponAddTypeField.setItems(WeaponAddType.values());
		weaponAddTypeField.setSelectedIndex(manifestation.getWeaponAddType().ordinal());
		weaponAddTypeField.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				updateTargetTable(skin);
			}
		});
		
		targetField = new TextField(manifestation.getTarget(), skin);
		targetField.setMessageText("<target>");
		locationTable = new VertexTable(manifestation.getLocation(), skin, null);

		add(new Label("Weapon: ", skin));
		add(weaponField);
		row();
		add(new Label("Weapon Add Type: ", skin));
		add(weaponAddTypeField);
		row();
		add(targetTable).colspan(2);
		
		updateTargetTable(skin);
	}
	
	private WeaponAddType getType(){
		return weaponAddTypeField.getSelected();
	}
	
	private void updateTargetTable(final Skin skin){
		targetTable.clear();
		switch(getType()){
		case LOCATION:
			targetTable.add(new Label("Location: ", skin));
			targetTable.add(locationTable);
			break;
		case PERSON:
			targetTable.add(new Label("Person: ", skin));
			targetTable.add(targetField);
			break;
		}
	}

	@Override public AbstractQuestManifestation apply() {
		manifestation.setWeapon(weaponField.getText());
		manifestation.setLocation(locationTable.getVertex());
		manifestation.setTarget(targetField.getText());
		manifestation.setWeaponAddType(getType());
		return manifestation;
	}
	
	public void touched(Vector2 coordinates){
		
	}
}