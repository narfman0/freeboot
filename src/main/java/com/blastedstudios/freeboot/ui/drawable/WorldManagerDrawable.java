package com.blastedstudios.freeboot.ui.drawable;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.ui.drawable.Drawable;
import com.blastedstudios.freeboot.world.WorldManager;

public class WorldManagerDrawable extends Drawable {
	private final WorldManager world;
	
	public WorldManagerDrawable(WorldManager world){
		this.world = world;
	}

	@Override public void render(float dt, AssetManager assetManager, Batch batch, Camera camera, GDXRenderer renderer) {
		world.render(dt, renderer, camera, batch);
	}
}
