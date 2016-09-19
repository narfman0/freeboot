package com.blastedstudios.freeboot.ui.gameplay;

import java.awt.Dimension;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.entente.BaseNetwork;
import com.blastedstudios.freeboot.input.ActionEnum;
import com.blastedstudios.freeboot.network.Messages.Logout;
import com.blastedstudios.freeboot.network.Messages.Reload;
import com.blastedstudios.freeboot.plugin.level.ILevelCompletedListener;
import com.blastedstudios.freeboot.ui.FreebootScreen;
import com.blastedstudios.freeboot.ui.drawable.ParticleManagerDrawable;
import com.blastedstudios.freeboot.ui.drawable.WorldManagerDrawable;
import com.blastedstudios.freeboot.ui.gameplay.console.ConsoleWindow;
import com.blastedstudios.freeboot.ui.gameplay.hud.HUD;
import com.blastedstudios.freeboot.ui.gameplay.inventory.InventoryWindow;
import com.blastedstudios.freeboot.ui.gameplay.particles.ParticleManager;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.MultiplayerType;
import com.blastedstudios.freeboot.util.UUIDConvert;
import com.blastedstudios.freeboot.util.ui.FreebootWindow;
import com.blastedstudios.freeboot.world.DialogBubble;
import com.blastedstudios.freeboot.world.DialogManager;
import com.blastedstudios.freeboot.world.DialogManager.DialogStruct;
import com.blastedstudios.freeboot.world.QuestManifestationExecutor;
import com.blastedstudios.freeboot.world.QuestTriggerInformationProvider;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.activity.ReviveActivity;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.being.Player;
import com.blastedstudios.freeboot.world.being.component.IComponent;
import com.blastedstudios.gdxworld.GDXWorldEditor;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.dialog.DialogManifestation;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.ui.drawable.Drawable;
import com.blastedstudios.gdxworld.ui.drawable.DrawableSorter;
import com.blastedstudios.gdxworld.ui.drawable.ShapeDrawable;
import com.blastedstudios.gdxworld.ui.drawable.TiledMeshRendererDrawable;
import com.blastedstudios.gdxworld.ui.leveleditor.LevelEditorScreen;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.GDXGameFade;
import com.blastedstudios.gdxworld.util.GDXGameFade.IPopListener;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.util.TiledMeshRenderer;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXParticle;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.gdxworld.world.quest.GDXQuest;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;

import box2dLight.RayHandler;

public class GameplayScreen extends FreebootScreen {
	private final DialogManager dialogManager;
	private final DialogBubble dialogBubble;
	private final ParticleManager particleManager;
	private final HUD hud;
	private OrthographicCamera camera;
	private WorldManager worldManager;
	private FreebootWindow characterWindow, inventoryWindow;
	private ConsoleWindow consoleWindow;
	private final Box2DDebugRenderer renderer;
	private final GDXRenderer gdxRenderer;
	private final GDXLevel level;
	private RayHandler rayHandler;
	private final GDXWorld world;
	private final FileHandle selectedFile;
	private Vector2 touchedDirection;
	private final TiledMeshRenderer tiledMeshRenderer;
	private final SpriteBatch spriteBatch = new SpriteBatch();
	private final AssetManager sharedAssets, assetManager;
	private final LinkedList<Drawable> drawables;
	private GameplayNetReceiver receiver;
	private Table tintTable;
	
	public GameplayScreen(GDXGame game, Player player, GDXLevel level, GDXWorld world,
			FileHandle selectedFile, final GDXRenderer gdxRenderer, AssetManager sharedAssets,
			AssetManager assetManager, MultiplayerType type, BaseNetwork network){
		super(game);
		this.level = level;
		this.world = world;
		this.selectedFile = selectedFile;
		this.gdxRenderer = gdxRenderer;
		this.sharedAssets = sharedAssets;
		this.assetManager = assetManager;
		hud = new HUD(skin, player);
		dialogManager = new DialogManager(skin);
		dialogBubble = new DialogBubble(sharedAssets, skin);
		particleManager = new ParticleManager();
		for(GDXParticle particle : level.getParticles())
			particleManager.addParticle(particle);
		worldManager = new WorldManager(player, level, sharedAssets);
		if(type != MultiplayerType.Local){
			receiver = new GameplayNetReceiver(worldManager, type, network);
			worldManager.setReceiver(receiver);
		}
		player.getQuestManager().initialize(new QuestTriggerInformationProvider(this, worldManager), 
				new QuestManifestationExecutor(this, worldManager));
		player.getQuestManager().setCurrentLevel(level);
		player.getQuestManager().tick(0f);//to get "start" quest to set respawn location
		if(!player.isSpawned()){
			worldManager.setRespawnLocation(worldManager.getDirector().createSpawnPoint(worldManager.getPlayer().getFaction()));
			worldManager.respawnPlayer();
		}
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom = Properties.getFloat("gameplay.camera.zoom", .02f);
		renderer = new Box2DDebugRenderer();
		rayHandler = worldManager.getCreateLevelStruct().lights.rayHandler;
		tiledMeshRenderer = new TiledMeshRenderer(gdxRenderer, level.getPolygons());
		if(sharedAssets.getQueuedAssets() > 0)
			Log.log("GameplayScreen.<init>", "Shared assets finishing loading");
		sharedAssets.finishLoading();//unsure if queued == 0 means its done... this verifies
		registerInput();
		drawables = gdxRenderer.generateDrawables(assetManager, spriteBatch, level, camera, 
				worldManager.getCreateLevelStruct().bodies.entrySet());
		drawables.sort(new DrawableSorter());
		int startIndex = drawables.size() - 1;
		for(int i = 1; i < drawables.size(); i++)
			if(drawables.get(i-1) instanceof ShapeDrawable && !(drawables.get(i) instanceof ShapeDrawable))
				startIndex = i;
		drawables.add(startIndex, new ParticleManagerDrawable(particleManager, worldManager));
		drawables.add(startIndex, new WorldManagerDrawable(worldManager));
		drawables.add(startIndex, new TiledMeshRendererDrawable(tiledMeshRenderer));
	}
	
	private void registerInput(){
		final Player player = worldManager.getPlayer();
		AbstractInputHandler pauseHandler = new AbstractInputHandler() {
			public void down(){
				if(!worldManager.isPause())
					handlePause(true);
			}
		};
		register(ActionEnum.BACK, pauseHandler);
		register(ActionEnum.CHAT, pauseHandler);
		register(ActionEnum.INVENTORY, pauseHandler);
		register(ActionEnum.CROUCH, new AbstractInputHandler() {
			public void down(){
				worldManager.setDesireFixedRotation(false);
			}
			public void up(){
				worldManager.setDesireFixedRotation(true);
			}
		});
		register(ActionEnum.RELOAD, new AbstractInputHandler() {
			public void down(){
				if(!worldManager.isPause() && worldManager.isInputEnable()){
					player.setReloading(true);
					Reload.Builder builder = Reload.newBuilder();
					if(player.getUuid() != null)
						builder.setUuid(UUIDConvert.convert(player.getUuid()));
					if(receiver != null)
						receiver.send(builder.build());
				}
			}
		});
		register(ActionEnum.LEFT, new AbstractInputHandler() {
			public void down(){
				if(!worldManager.isPause() && worldManager.isInputEnable())
					player.setMoveLeft(true);
			}
			public void up(){
				player.setMoveLeft(false);
			}
		});
		register(ActionEnum.RIGHT, new AbstractInputHandler() {
			public void down(){
				if(!worldManager.isPause() && worldManager.isInputEnable())
					player.setMoveRight(true);
			}
			public void up(){
				player.setMoveRight(false);
			}
		});
		register(ActionEnum.UP, new AbstractInputHandler() {
			public void down(){
				if(!worldManager.isPause() && worldManager.isInputEnable())
					player.setJump(true);
			}
			public void up(){
				player.setJump(false);
			}
		});
		register(ActionEnum.ACTION, new AbstractInputHandler() {
			public void down(){
				if(debugCommandEnabled()){
					game.pushScreen(new LevelEditorScreen(game, world, selectedFile, level, assetManager));
					Log.log("GameplayScreen.render", "Edit mode entered");
				}
				DialogStruct struct = dialogManager.poll();
				if(struct != null){
					//TODO kinda nasty hack, here... on the slow side and doesn't go through GDXQuestManager
					for(GDXQuest quest : level.getQuests())
						if(quest.getManifestation() instanceof DialogManifestation){
							String converted = DialogManager.splitRenderable(((DialogManifestation)quest.getManifestation()).getDialog(), DialogManager.DIALOG_WIDTH);
							if(converted.equals(struct.dialog)){
								player.getQuestManager().setStatus(quest.getName(), CompletionEnum.COMPLETED);
								break;
							}
						}
				}else{
					if(!worldManager.isPause()){
						Being closest = worldManager.getClosestBeing(player, true, true);
						if(closest != null && closest.getPosition().dst(player.getPosition()) < Properties.getFloat("activity.revive.distance"))
							player.applyActivity(new ReviveActivity(player, closest, worldManager, receiver));
							//TODO if shot, cant revive pfft
					}
				}
			}
		});
	}
	
	public void handlePause(boolean pause){
		if(worldManager.isInputEnable()){
			worldManager.pause(pause);
			if(pause){
				if(characterWindow != null)
					cleanCharacterWindows();
				stage.addActor(characterWindow = new CharacterWindow(skin, worldManager.getPlayer()));
				stage.addActor(inventoryWindow = new InventoryWindow(skin, worldManager.getPlayer(), worldManager.getSharedAssets(), stage));
				EventListener consoleListener = new EventListener() {
					@Override public boolean handle(Event event) {
						cleanCharacterWindows();
						worldManager.pause(false);
						return false;
					}
				};
				stage.addActor(consoleWindow = new ConsoleWindow(skin, worldManager, GameplayScreen.this, consoleListener));
			}else
				cleanCharacterWindows();
		}
	}

	@Override public void render(float delta) {
		super.render(delta);
		//if disposed this up in super's render, need to drop it before rendering scene
		if(assetManager.getLoadedAssets() == 0)
			return;
		//if player is not spawned then we are in a cutscene, if input isn't enabled we shouldn't update camera
		if(worldManager.getPlayer().isSpawned() && worldManager.isPlayerTrack() && !worldManager.isPause()){
			Vector2 offset = new Vector2();
			if(Properties.getBool("camera.lead.mouse.modifier", false) ^ Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)){
				Dimension dim = GDXWorldEditor.getDimension(Properties.getBool("graphics.fullscreen"));
				offset.add(-dim.width/2f + Gdx.input.getX(), dim.height/2f - Gdx.input.getY());
				offset.scl(camera.zoom/5f);
			}
			camera.position.set(worldManager.getPlayer().getPosition().cpy().add(offset), 0);
		}
		camera.update();
		rayHandler.setCombinedMatrix(camera);

		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		for(Drawable drawable : drawables)
			drawable.render(delta, assetManager, spriteBatch, camera, gdxRenderer);
		spriteBatch.end();
		
		if(Properties.getBool("lighting.draw", true))
			rayHandler.updateAndRender();
		if(Properties.getBool("debug.draw"))
			renderer.render(worldManager.getWorld(), camera.combined);
		dialogBubble.render(sharedAssets, worldManager, dialogManager, camera);
		dialogManager.render(assetManager);
		stage.draw();
		if(worldManager.getPlayer().isSpawned())
			hud.render();
		
		int x = Gdx.input.getX(), y = Gdx.input.getY();
		worldManager.update(delta);
		if(!worldManager.isPause() && worldManager.isInputEnable() && Gdx.input.isTouched() && 
				(inventoryWindow == null || !inventoryWindow.contains(x, y)) &&
				(characterWindow == null || !characterWindow.contains(x, y)) &&
				(consoleWindow == null || !consoleWindow.contains(x, y)))
			worldManager.getPlayer().attack(touchedDirection, worldManager);
		if(receiver != null)
			receiver.update(delta);
	}
	
	public void logout(final boolean success){
		if(receiver != null){
			Logout.Builder builder = Logout.newBuilder();
			builder.setUuid(UUIDConvert.convert(worldManager.getPlayer().getUuid()));
			receiver.send(builder.build());
		}
		for(ILevelCompletedListener listener : PluginUtil.getPlugins(ILevelCompletedListener.class))
			listener.levelComplete(success, worldManager, level);
		GDXGameFade.fadeOutPopScreen(game, new IPopListener() {
			@Override public void screenPopped() {
				dispose();
			}
		});
	}

	public DialogManager getDialogManager() {
		return dialogManager;
	}

	@Override public boolean touchDown(int x, int y, int x1, int y1) {
		mouseMovedHandler(x, y);
		return false;
	}

	@Override public boolean touchDragged(int x, int y, int ptr) {
		mouseMovedHandler(x, y);
		return false;
	}
	
	@Override public boolean mouseMoved(int x, int y){
		mouseMovedHandler(x, y);
		return false;
	}
	
	@Override public boolean touchUp(int x, int y, int x1, int y1){
		worldManager.getPlayer().touchUp(x, y, x1, y1);
		return false;
	}

	private void mouseMovedHandler(int x, int y) {
		if(!worldManager.isPause() && worldManager.isInputEnable()){
			float angle = (float)Math.atan2(Gdx.graphics.getHeight()/2 - y, x - Gdx.graphics.getWidth()/2);
			touchedDirection = new Vector2((float)Math.cos(angle), (float)Math.sin(angle));
			worldManager.getPlayer().aim(angle);
		}
	}
	
	@Override public boolean scrolled(int amount) {
		if(Properties.getBool("camera.zoom.enable", true) && Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)){
			camera.zoom += (amount*camera.zoom/10f);
			camera.zoom = Math.min(.04f, Math.max(.002f, camera.zoom));
		}else if(!worldManager.isPause() && worldManager.isInputEnable()){
			int max = Math.min(Being.MAX_GUNS, worldManager.getPlayer().getGuns().size());
			if(max > 0){
				int gun = (worldManager.getPlayer().getCurrentGun() + amount + max) % max;
				worldManager.changePlayerWeapon(gun);
			}
		}
		return false;
	}
	
	@Override public boolean keyDown(int key) {
		//switch weapon
		for(int i=0; i<Being.MAX_GUNS; i++)
			if(key == Keys.NUM_1 + i && consoleWindow == null)
				if(worldManager.getPlayer().getGuns().size() > i && i != worldManager.getPlayer().getCurrentGun()){
					worldManager.changePlayerWeapon(i);
					Log.log("GameplayScreen.render","New gun selected: " + worldManager.getPlayer().getGuns().get(i));
				}
		switch(key){
		case Keys.F6:
			if(debugCommandEnabled()){
				Properties.set("debug.draw", ""+!Properties.getBool("debug.draw"));
				Log.log("GameplayScreen.render", "debug.draw: " + Properties.getBool("debug.draw"));
			}
			break;
		case Keys.F10:
			if(debugCommandEnabled()){
				Vector2 position = worldManager.getPlayer().getPosition();
				worldManager.spawnNPC(level.getClosestNPC(position.x, position.y));
			}
			break;
		case Keys.F12:
			if(debugCommandEnabled()){
				logout(true);
				Log.log("Gameplayscreen.keyDown", "beat level cheater");
			}
			break;
		}
		for(IComponent component : worldManager.getPlayer().getListeners())
			component.keyDown(key, worldManager);
		return super.keyDown(key);
	}
	
	private void cleanCharacterWindows(){
		for(FreebootWindow window : new FreebootWindow[]{characterWindow, inventoryWindow, consoleWindow})
			if(window != null)
				window.remove();
		characterWindow = null;
		inventoryWindow = null;
		consoleWindow = null;
	}
	
	private boolean debugCommandEnabled(){
		return Properties.getBool("debug.commands") && ActionEnum.MODIFIER.isPressed();
	}
	
	@Override public boolean keyUp(int key) {
		for(IComponent component : worldManager.getPlayer().getListeners())
			component.keyUp(key, worldManager);
		return super.keyUp(key);
	}

	public ParticleManager getParticleManager() {
		return particleManager;
	}

	public Camera getCamera(){
		return camera;
	}
	
	@Override public void dispose(){
		super.dispose();
		assetManager.dispose();
		if(worldManager.getPlayer() != null)
			worldManager.getPlayer().dispose(worldManager.getWorld());
		worldManager.getWorld().dispose();
	}

	public void applyTintTable(Table tintTable) {
		if(this.tintTable != null)
			this.tintTable.remove();
		stage.addActor(this.tintTable = tintTable);
		Log.log("GameplayScreen.applyTintTable", "New table applied at " + System.currentTimeMillis());
	}

	public GameplayNetReceiver getReceiver() {
		return receiver;
	}
}
