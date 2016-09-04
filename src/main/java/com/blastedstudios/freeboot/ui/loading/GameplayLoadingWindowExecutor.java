package com.blastedstudios.freeboot.ui.loading;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.GDXGameFade;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.entente.BaseNetwork;
import com.blastedstudios.freeboot.ui.gameplay.GameplayScreen;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.MultiplayerType;
import com.blastedstudios.freeboot.ui.loading.LoadingWindow.ILoadingWindowExecutor;
import com.blastedstudios.freeboot.world.being.Player;

public class GameplayLoadingWindowExecutor implements ILoadingWindowExecutor{
	private final GDXGame game;
	private final AssetManager assetManager;
	private final Player player;
	private final GDXLevel level;
	private final GDXWorld world;
	private final FileHandle selectedFile;
	private final GDXRenderer gdxRenderer;
	private final AssetManager sharedAssets;
	private final MultiplayerType type;
	private final BaseNetwork network;
	private List<String> createdAssetList = null;
	private GameplayScreen screen = null;
	private int ticksToTransitionGame = 2;
	
	public GameplayLoadingWindowExecutor(final GDXGame game, Player player, GDXLevel level, GDXWorld world,
			FileHandle selectedFile, final GDXRenderer gdxRenderer, AssetManager sharedAssets, MultiplayerType type, BaseNetwork network){
		this.game = game;
		this.player = player;
		this.level = level;
		this.world = world;
		this.selectedFile = selectedFile;
		this.gdxRenderer = gdxRenderer;
		this.sharedAssets = sharedAssets;
		this.type = type;
		this.network = network;
		assetManager = new AssetManager();
		createdAssetList = level.createAssetList();
	}

	@Override public boolean act(float delta) {
		//begin loading
		if(!createdAssetList.isEmpty())
			assetManager.load(createdAssetList.remove(0), Texture.class);
		//middle of loading
		if(createdAssetList.isEmpty())
			assetManager.update();
		//done loading
		if(createdAssetList.isEmpty() && assetManager.getQueuedAssets() == 0){
			if(screen == null){
				screen = new GameplayScreen(game, player, level, world, selectedFile, 
						gdxRenderer, sharedAssets, assetManager, type, network);
			}else{
				if(ticksToTransitionGame > 0)
					ticksToTransitionGame--;
			}
			if(ticksToTransitionGame <= 0){
				GDXGameFade.fadeInPushScreen(game, screen);
				return true;
			}
		}
		return false;
	}
}
