package com.blastedstudios.freeboot.plugin.ragdoll.custom;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.freeboot.physics.ragdoll.IRagdoll;
import com.blastedstudios.freeboot.physics.ragdoll.IRagdoll.IRagdollPlugin;
import com.blastedstudios.freeboot.world.being.Being;

@PluginImplementation
public class RagdollCustomPlugin implements IRagdollPlugin{
	@Override public boolean canCreate(String resource) {
		return resource == null || resource.equalsIgnoreCase("");
	}

	@Override public IRagdoll create(World world, float x, float y, Being being, TextureAtlas atlas, FileHandle file) {
		return new RagdollCustom(world, x, y, being, atlas);
	}
}
