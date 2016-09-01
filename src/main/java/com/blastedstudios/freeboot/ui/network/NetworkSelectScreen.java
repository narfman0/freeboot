package com.blastedstudios.freeboot.ui.network;

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
import com.blastedstudios.freeboot.ui.network.network.ChatWindow;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.INetworkWindowListener;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.MultiplayerType;
import com.blastedstudios.freeboot.ui.loading.GameplayLoadingWindowExecutor;
import com.blastedstudios.freeboot.ui.loading.LoadingWindow;
import com.blastedstudios.freeboot.world.being.Player;

public class NetworkSelectScreen extends FreebootScreen {
	private final AssetManager sharedAssets;
	private final PannerManager panner;
	private final NetworkWindow networkWindow;
	private ChatWindow chat;
	/**
	 * If we are the host or dedicated server, we use the initially loaded GDXWorld saved in the preferences.
	 * However, if we are a client, we will want to load a world via the network and set it here. So, we set
	 * the world initially, then expect to overwrite it later.
	 */
	private GDXWorld gdxWorld;

	public NetworkSelectScreen(final GDXGame game, final Player player, final GDXWorld gdxWorld, final FileHandle worldFile,
			final GDXRenderer gdxRenderer, final AssetManager sharedAssets, final PannerManager panner){
		super(game);
		this.sharedAssets = sharedAssets;
		this.panner = panner;
		this.gdxWorld = gdxWorld;
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
				GDXLevel level = NetworkSelectScreen.this.gdxWorld.getLevels().get(0);
				stage.addActor(new LoadingWindow(skin, 
					new GameplayLoadingWindowExecutor(game, player, level, NetworkSelectScreen.this.gdxWorld, worldFile, gdxRenderer, sharedAssets, 
							networkWindow.getMultiplayerType(), networkWindow.getSource())));
			}
			@Override
			public void worldSelected(GDXWorld world) {
				NetworkSelectScreen.this.gdxWorld = world;
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
					game.pushScreen(new WorldEditorScreen(game, NetworkSelectScreen.this.gdxWorld, worldFile));
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
