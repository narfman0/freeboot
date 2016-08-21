package com.blastedstudios.freeboot.ui.loading;

import net.xeoh.plugins.base.util.uri.ClassURI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.GDXGameFade;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.freeboot.ui.loading.LoadingWindow.ILoadingWindowExecutor;
import com.blastedstudios.freeboot.ui.main.MainScreen;
import com.blastedstudios.freeboot.util.AssetUtil;

public class MainLoadingScreen extends AbstractScreen{
	private final AssetManager sharedAssets = new AssetManager();
	private GDXWorld gdxWorld;
	private int iterationsToLoad = 1;
	private MainScreen mainScreen = null;
	
	public MainLoadingScreen(final GDXGame game){
		super(game, MainScreen.SKIN_PATH);
		stage.addActor(new LoadingWindow(skin, new ILoadingWindowExecutor() {
			@Override public boolean act(float delta) {
				if(mainScreen != null && mainScreen.ready()){
					game.popScreen();
					GDXGameFade.fadeInPushScreen(game, mainScreen);
					return true;
				}
				return false;
			}
		}));
		for(FileHandle handle : Gdx.files.internal("data/textures").list())
			if(!handle.isDirectory() && handle.extension().equals("png"))
				sharedAssets.load(handle.path(), Texture.class);
	}

	@Override public void render(float delta){
		super.render(delta);
		stage.draw();
		sharedAssets.update();
		if(mainScreen == null && sharedAssets.getProgress() == 1f)
			mainScreen = new MainScreen(game, sharedAssets, gdxWorld);
		if(mainScreen != null)
			mainScreen.update(delta);
		if(iterationsToLoad-- == 0){
			PluginUtil.initialize(ClassURI.CLASSPATH);//this takes 5+ seconds
			gdxWorld = GDXWorld.load(MainScreen.WORLD_FILE);
			AssetUtil.loadAssetsRecursive(sharedAssets, Gdx.files.internal("data/sounds"), Sound.class);
			AssetUtil.loadAssetsRecursive(sharedAssets, Gdx.files.internal("data/textures/ammo"), Texture.class);
			AssetUtil.loadAssetsRecursive(sharedAssets, Gdx.files.internal("data/textures/weapons"), Texture.class);
		}
	}
}
