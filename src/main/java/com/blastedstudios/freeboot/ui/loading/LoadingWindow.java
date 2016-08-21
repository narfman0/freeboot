package com.blastedstudios.freeboot.ui.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.blastedstudios.freeboot.util.ui.FreebootTextButton;
import com.blastedstudios.freeboot.util.ui.FreebootWindow;

public class LoadingWindow extends FreebootWindow{
	private final ILoadingWindowExecutor executor;
	
	public LoadingWindow(Skin skin, ILoadingWindowExecutor executor) {
		super("", skin);
		this.executor = executor;
		add(new FreebootTextButton("Loading", skin));
		pack();
		setX(Gdx.graphics.getWidth()/2 - getWidth()/2);
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
	}
	
	@Override public void act(float delta){
		super.act(delta);
		if(executor != null)
			if(executor.act(delta))
				remove();
	}
	
	public interface ILoadingWindowExecutor{
		/**
		 * @return true if finished loading
		 */
		boolean act(float delta);
	}
}
