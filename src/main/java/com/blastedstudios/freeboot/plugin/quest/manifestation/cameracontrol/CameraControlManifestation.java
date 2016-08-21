package com.blastedstudios.freeboot.plugin.quest.manifestation.cameracontrol;

import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class CameraControlManifestation extends AbstractQuestManifestation {
	private static final long serialVersionUID = 1L;
	public static final CameraControlManifestation DEFAULT = new CameraControlManifestation();
	private boolean playerTrack;
	
	public CameraControlManifestation(){}
	public CameraControlManifestation(boolean playerTrack){
		this.playerTrack = playerTrack;
	}
	
	@Override public CompletionEnum execute(float dt) {
		for(ICameraControlHandler handler : PluginUtil.getPlugins(ICameraControlHandler.class))
			if(handler.playerTrack(playerTrack) == CompletionEnum.COMPLETED)
				return CompletionEnum.COMPLETED;
		return CompletionEnum.EXECUTING;
	}

	@Override public AbstractQuestManifestation clone() {
		return new CameraControlManifestation(playerTrack);
	}

	@Override public String toString() {
		return "[CameraControlManifestation playerTrack:" + playerTrack + "]";
	}
	
	public boolean isPlayerTrack() {
		return playerTrack;
	}
	
	public void setPlayerTrack(boolean playerTrack) {
		this.playerTrack = playerTrack;
	}
}