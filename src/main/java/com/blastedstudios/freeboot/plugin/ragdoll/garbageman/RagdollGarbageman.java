package com.blastedstudios.freeboot.plugin.ragdoll.garbageman;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.freeboot.plugin.ragdoll.loader.RagdollLoader;
import com.blastedstudios.freeboot.world.being.Being;

public class RagdollGarbageman extends RagdollLoader {
	public RagdollGarbageman(World world, float x, float y, Being being, TextureAtlas atlas, FileHandle file){
		super(world, x, y, being, atlas, createBodyMap(world, file, x, y));
		for(Body body : bodies)
			for(Fixture fixture : body.getFixtureList()){
				Filter filter = fixture.getFilterData();
				filter.maskBits = being.getMask();
				filter.categoryBits = being.getCat();
				fixture.setFilterData(filter);
			}
	}
	
	@Override public void render(Batch batch, boolean dead, boolean isGrounded,
			boolean isMoving, float velX, boolean paused, boolean inputEnabled){
		super.render(batch, isGrounded, isMoving, velX, paused);
	}
}
