package com.blastedstudios.freeboot.plugin.quest.manifestation.cameratween;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.Camera;

public class CameraAccessor implements TweenAccessor<Camera> {
	public static final int POSITION_XY = 1;
	
	@Override public int getValues(Camera target, int tweenType, float[] returnValues) {
		returnValues[0] = target.position.x;
		returnValues[1] = target.position.y;
		return 2;
	}

	@Override public void setValues(Camera target, int tweenType, float[] newValues) {
		target.position.set(newValues[0], newValues[1], 0f);
	}
}
