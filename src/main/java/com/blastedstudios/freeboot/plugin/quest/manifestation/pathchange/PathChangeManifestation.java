package com.blastedstudios.freeboot.plugin.quest.manifestation.pathchange;

import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class PathChangeManifestation extends AbstractQuestManifestation {
	private static final long serialVersionUID = 1L;
	public static final PathChangeManifestation DEFAULT = new PathChangeManifestation();
	private String being = "", path = "";
	
	public PathChangeManifestation(){}
	public PathChangeManifestation(String being, String path){
		this.being = being;
		this.path = path;
	}
	
	@Override public CompletionEnum execute(float dt) {
		for(IPathChangeHandler handler : PluginUtil.getPlugins(IPathChangeHandler.class))
			if(handler.pathChange(being, path) == CompletionEnum.COMPLETED)
				return CompletionEnum.COMPLETED;
		return CompletionEnum.EXECUTING;
	}

	@Override public AbstractQuestManifestation clone() {
		return new PathChangeManifestation(being, path);
	}

	@Override public String toString() {
		return "[PathChangeManifestation being:" + being + " path:" + path + "]";
	}

	public String getBeing() {
		return being;
	}

	public void setBeing(String being) {
		this.being = being;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
}