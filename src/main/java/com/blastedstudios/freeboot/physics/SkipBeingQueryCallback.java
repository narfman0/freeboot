package com.blastedstudios.freeboot.physics;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.blastedstudios.freeboot.world.being.Being;

public class SkipBeingQueryCallback implements QueryCallback {
	private final Being skipBeing;
	private Being collidedBeing;
	private Fixture fixture;
	
	public SkipBeingQueryCallback(Being being){
		this.skipBeing = being;
	}

	@Override public boolean reportFixture(Fixture fixture) {
		boolean skip = skipBeing.isOwned(fixture);
		if(fixture.getBody().getUserData() instanceof Being && !skip){
			collidedBeing = (Being) fixture.getBody().getUserData();
			this.fixture = fixture;
		}
		return skip;
	}

	public Being getCollidedBeing() {
		return collidedBeing;
	}

	public Fixture getFixture() {
		return fixture;
	}
}
