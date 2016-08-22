package com.blastedstudios.freeboot.ui.levelselect;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.ui.worldeditor.WorldEditorScreen;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.panner.PannerManager;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.freeboot.input.ActionEnum;
import com.blastedstudios.freeboot.plugin.quest.handler.manifestation.SoundThematicHandlerPlugin;
import com.blastedstudios.freeboot.ui.FreebootScreen;
import com.blastedstudios.freeboot.ui.levelselect.network.ChatWindow;
import com.blastedstudios.freeboot.ui.levelselect.network.NetworkWindow;
import com.blastedstudios.freeboot.ui.levelselect.network.NetworkWindow.INetworkWindowListener;
import com.blastedstudios.freeboot.ui.levelselect.network.NetworkWindow.MultiplayerType;
import com.blastedstudios.freeboot.ui.loading.GameplayLoadingWindowExecutor;
import com.blastedstudios.freeboot.ui.loading.LoadingWindow;
import com.blastedstudios.freeboot.world.being.Player;

public class LevelSelectScreen extends FreebootScreen{
	private final AssetManager sharedAssets;
	private final PannerManager panner;
	private final NetworkWindow networkWindow;
	private ChatWindow chat;

	public LevelSelectScreen(final GDXGame game, final Player player, final GDXWorld gdxWorld, final FileHandle worldFile,
			final GDXRenderer gdxRenderer, final AssetManager sharedAssets, final PannerManager panner){
		super(game);
		this.sharedAssets = sharedAssets;
		this.panner = panner;
		Log.log("LevelSelect.<init>", "Loaded world successfully");
		sharedAssets.finishLoading();
		networkWindow = new NetworkWindow(skin, player, new INetworkWindowListener() {
			@Override public void networkSelected(MultiplayerType type) {
				if(chat != null)
					chat.remove();
				chat = null;
				switch(type){
				case Client:
				case DedicatedServer:
				case Host:
					chat = new ChatWindow(skin, networkWindow.getSource());
					break;
				case Local:
					break;
				}
				if(chat != null)
					stage.addActor(chat);
			}
			@Override public void start(){
				GDXLevel level = gdxWorld.getLevels().get(0);
				stage.addActor(new LoadingWindow(skin, 
					new GameplayLoadingWindowExecutor(game, player, level, gdxWorld, worldFile, gdxRenderer, sharedAssets, 
							networkWindow.getMultiplayerType(), networkWindow.getSource())));
			}
		});
		stage.addActor(networkWindow);
		register(ActionEnum.BACK, new AbstractInputHandler() {
			public void down(){
				game.popScreen();
			}
		});
		register(ActionEnum.ACTION, new AbstractInputHandler() {
			public void down(){
				if(ActionEnum.MODIFIER.isPressed())
					game.pushScreen(new WorldEditorScreen(game, gdxWorld, worldFile));
			}
		});
	}

	@Override public void render(float delta){
		super.render(delta);
		networkWindow.render();
		SoundThematicHandlerPlugin.get().tick(delta);
		sharedAssets.update();
		panner.updatePanners(delta);
		stage.draw();
	}
}
