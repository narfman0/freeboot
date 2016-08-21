package com.blastedstudios.freeboot.plugin.quest.manifestation.cameracontrol;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.blastedstudios.gdxworld.plugin.mode.quest.ManifestationTable;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class CameraControlManifestationTable extends ManifestationTable{
	private final CameraControlManifestation manifestation;
	private final CheckBox playerTrackBox;
	
	public CameraControlManifestationTable(Skin skin, CameraControlManifestation manifestation){
		super(skin);
		this.manifestation = manifestation;
		playerTrackBox = new CheckBox("Player Track", skin);
		playerTrackBox.setChecked(manifestation.isPlayerTrack());
		add(playerTrackBox);
	}

	@Override public AbstractQuestManifestation apply() {
		manifestation.setPlayerTrack(playerTrackBox.isChecked());
		return manifestation;
	}
}