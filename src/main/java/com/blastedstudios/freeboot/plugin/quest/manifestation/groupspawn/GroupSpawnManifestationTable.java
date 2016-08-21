package com.blastedstudios.freeboot.plugin.quest.manifestation.groupspawn;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.blastedstudios.gdxworld.plugin.mode.quest.ManifestationTable;
import com.blastedstudios.gdxworld.util.StringUtil;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class GroupSpawnManifestationTable extends ManifestationTable{
	private final GroupSpawnManifestation manifestation;
	private final TextField npcDataField, levelField;
	
	public GroupSpawnManifestationTable(Skin skin, GroupSpawnManifestation manifestation){
		super(skin);
		this.manifestation = manifestation;
		npcDataField = new TextField(StringUtil.joinWithComma(manifestation.getNpcData()), skin);
		levelField = new TextField(manifestation.getLevel()+"", skin);
		add("NPCData: ");
		add(npcDataField);
		row();
		add("Level: ");
		add(levelField);
	}

	@Override public AbstractQuestManifestation apply() {
		manifestation.setLevel(Integer.parseInt(levelField.getText()));
		manifestation.setNpcData(StringUtil.splitOnComma(npcDataField.getText()));
		return manifestation;
	}
}