package com.blastedstudios.freeboot;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.entente.BaseNetwork;
import com.blastedstudios.entente.Host;
import com.blastedstudios.freeboot.network.Messages;
import com.blastedstudios.freeboot.network.server.ConsoleInput;
import com.blastedstudios.freeboot.ui.gameplay.GameplayNetReceiver;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.MultiplayerType;
import com.blastedstudios.freeboot.ui.main.MainScreen;
import com.blastedstudios.freeboot.util.SaveHelper;
import com.blastedstudios.freeboot.world.WorldManager;

import net.xeoh.plugins.base.util.uri.ClassURI;

public class FreebootServer extends ApplicationAdapter{
	private Host host;
	private GDXWorld gdxWorld;
	private WorldManager worldManager;
	private GameplayNetReceiver receiver;
	private ConsoleInput consoleInput;
	
	@Override public void create () {
		SaveHelper.loadProperties();
		Gdx.app.setLogLevel(Properties.getInt("log.level", Application.LOG_INFO));
		PluginUtil.initialize(ClassURI.CLASSPATH);
		BaseNetwork.registerMessageOrigin(Messages.class);
		host = new Host(Properties.getInt("network.port"));
		gdxWorld = GDXWorld.load(MainScreen.WORLD_FILE);
		worldManager = new WorldManager(null, gdxWorld.getLevels().get(0), null, MultiplayerType.DedicatedServer);
		receiver = new GameplayNetReceiver(worldManager, MultiplayerType.DedicatedServer, host);
		worldManager.setReceiver(receiver);
		consoleInput = new ConsoleInput(worldManager);
	}
	
	@Override public void render() {
		float dt = Gdx.graphics.getDeltaTime();
		try {
			worldManager.update(dt);
			receiver.update(dt);
			consoleInput.update();
			Thread.sleep((long)dt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main (String[] argv) {
		new HeadlessApplication(new FreebootServer());
	}
}
