package com.blastedstudios.freeboot.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.blastedstudios.freeboot.world.being.Being;

public class VisibleQueryCallback implements RayCastCallback {
	public boolean called = false;
	private final Being viewer, target;
	
	/**
	 * @param ragdolls which ragdolls should be omitted from query. Usually 
	 * this is the perceiver and the target
	 */
	public VisibleQueryCallback(Being viewer, Being target){
		this.viewer = viewer;
		this.target = target;
	}

	public float reportRayFixture (Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
		if(target.isOwned(fixture) || viewer.isOwned(fixture))
			return -1f;
		called = true;
		return fraction;
	}
}
