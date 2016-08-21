package com.blastedstudios.freeboot.plugin.quest.manifestation.factionchange;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.blastedstudios.gdxworld.plugin.mode.quest.ManifestationTable;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;
import com.blastedstudios.freeboot.network.Messages.NetBeing.FactionEnum;

public class FactionChangeManifestationTable extends ManifestationTable{
	private final FactionChangeManifestation manifestation;
	private final TextField beingField;
	private final List<FactionEnum> factionList;
	
	public FactionChangeManifestationTable(Skin skin, FactionChangeManifestation manifestation){
		super(skin);
		this.manifestation = manifestation;
		beingField = new TextField(manifestation.getBeing(), skin);
		beingField.setMessageText("<being name>");
		factionList = new List<FactionEnum>(skin);
		factionList.setItems(FactionEnum.values());
		factionList.setSelectedIndex(manifestation.getFaction().ordinal());
		add(new Label("Being: ", skin));
		add(beingField);
		add(new Label("Faction: ", skin));
		add(factionList);
	}

	@Override public AbstractQuestManifestation apply() {
		manifestation.setBeing(beingField.getText());
		manifestation.setFaction(factionList.getSelected());
		return manifestation;
	}
}