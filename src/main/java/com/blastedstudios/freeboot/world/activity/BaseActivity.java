package com.blastedstudios.freeboot.world.activity;

public abstract class BaseActivity {
	/**
	 * @return true if activity is still being performed
	 */
	public abstract boolean render(float dt);
}
