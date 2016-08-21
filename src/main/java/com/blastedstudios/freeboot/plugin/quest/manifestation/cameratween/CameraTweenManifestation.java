package com.blastedstudios.freeboot.plugin.quest.manifestation.cameratween;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;
import com.blastedstudios.freeboot.world.QuestManifestationExecutor;

public class CameraTweenManifestation extends AbstractQuestManifestation{
	private static final long serialVersionUID = 1L;
	public static final CameraTweenManifestation DEFAULT = new CameraTweenManifestation();
	private Vector2 target = new Vector2();
	public float duration = 2f;
	private CameraTweenType tweenType = CameraTweenType.CubicInOut; 

	public CameraTweenManifestation(){}
	public CameraTweenManifestation(Vector2 target, float duration, CameraTweenType tweenType){
		this.target = target;
		this.duration = duration;
		this.tweenType = tweenType;
	}
	
	@Override public CompletionEnum execute(float dt) {
		return CompletionEnum.EXECUTING;
	}
	
	@Override public CompletionEnum tick(float dt){
		return ((QuestManifestationExecutor)executor).cameraTween(CameraAccessor.POSITION_XY, duration, target, tweenType.equation);
	}

	@Override public AbstractQuestManifestation clone() {
		return new CameraTweenManifestation(target, duration, tweenType);
	}

	@Override public String toString() {
		return "[CameraTweenManifestation target:" + target + " duration:" + duration + " type:" + tweenType.getClass() + "]";
	}
	
	public Vector2 getTarget() {
		return target;
	}
	
	public void setTarget(Vector2 target) {
		this.target = target;
	}
	
	public float getDuration(){
		return duration;
	}
	
	public void setDuration(float duration){
		this.duration = duration;
	}
	
	public CameraTweenType getTweenType() {
		return tweenType;
	}
	
	public void setTweenType(CameraTweenType tweenType) {
		this.tweenType = tweenType;
	}
}
