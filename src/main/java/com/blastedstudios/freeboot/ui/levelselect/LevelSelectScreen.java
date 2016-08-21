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
import com.blastedstudios.freeboot.network.IMessageListener;
import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.blastedstudios.freeboot.network.Messages.LevelLoad;
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
	private final Player player;
	private final GDXWorld gdxWorld;
	private final FileHandle worldFile;
	private final GDXRenderer gdxRenderer;
	private LevelInformationWindow levelWindow;
	private ChatWindow chat;

	public LevelSelectScreen(final GDXGame game, final Player player, final GDXWorld gdxWorld, final FileHandle worldFile,
			final GDXRenderer gdxRenderer, final AssetManager sharedAssets, final PannerManager panner){
		super(game);
		this.player = player;
		this.gdxWorld = gdxWorld;
		this.worldFile = worldFile;
		this.gdxRenderer = gdxRenderer;
		this.sharedAssets = sharedAssets;
		this.panner = panner;
		Log.log("LevelSelect.<init>", "Loaded world successfully");
		sharedAssets.finishLoading();
		levelWindow = new LevelInformationWindow(skin, gdxWorld, this, MultiplayerType.Local);
		stage.addActor(levelWindow);
		networkWindow = new NetworkWindow(skin, player, new INetworkWindowListener() {
			@Override public void networkSelected(MultiplayerType type) {
				levelWindow.remove();
				if(chat != null)
					chat.remove();
				chat = null;
				levelWindow = new LevelInformationWindow(skin, gdxWorld, LevelSelectScreen.this, type);
				switch(type){
				case Client:
					networkWindow.getSource().addListener(MessageType.LEVEL_LOAD, new IMessageListener() {
						@Override public void receive(MessageType messageType, Object object) {
							LevelLoad message = (LevelLoad) object;
							levelSelected(message.getName());
						}
					});
				case DedicatedServer:
				case Host:
					chat = new ChatWindow(skin, networkWindow.getSource());
					break;
				case Local:
					break;
				}
				stage.addActor(levelWindow);
				if(chat != null)
					stage.addActor(chat);
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
	
	public void levelSelected(String levelName){
		if(networkWindow.getMultiplayerType() == MultiplayerType.Host || networkWindow.getMultiplayerType() == MultiplayerType.DedicatedServer){
			LevelLoad.Builder levelLoad = LevelLoad.newBuilder();
			levelLoad.setName(levelName);
			networkWindow.getSource().send(MessageType.LEVEL_LOAD, levelLoad.build());
		}
		GDXLevel level = gdxWorld.getLevel(levelName);
		stage.addActor(new LoadingWindow(skin, 
			new GameplayLoadingWindowExecutor(game, player, level, gdxWorld, worldFile, gdxRenderer, sharedAssets, 
					networkWindow.getMultiplayerType(), networkWindow.getSource())));
	}
}
