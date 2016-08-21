package com.blastedstudios.freeboot.world.being.component;

import net.xeoh.plugins.base.Plugin;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.being.IBeingListener;

public interface IComponent extends IBeingListener,Plugin,Cloneable{
	IComponent initialize(Being being);
	void render(float dt, Batch batch, AssetManager sharedAssets,
			GDXRenderer gdxRenderer, boolean facingLeft, boolean paused);
	public void update(float dt, boolean facingLeft, boolean paused);
	boolean keyDown(int key, WorldManager worldManager);
	boolean keyUp(int key, WorldManager worldManager);
}
