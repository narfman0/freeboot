package com.blastedstudios.freeboot.ui.gameplay;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.blastedstudios.freeboot.network.BaseNetwork;
import com.blastedstudios.freeboot.network.IMessageListener;
import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.blastedstudios.freeboot.network.Messages.NPCState;
import com.blastedstudios.freeboot.network.Messages.PlayerState;
import com.blastedstudios.freeboot.plugin.network.IMessageReceive;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.MultiplayerType;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.NPC;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.google.protobuf.Message;

public class GameplayNetReceiver implements IMessageListener{
	private final WorldManager worldManager;
	public final MultiplayerType type;
	public final BaseNetwork network;
	private final GameplayScreen screen;
	private final HashMap<MessageType, List<IMessageReceive<?>>> messageMap = new HashMap<>();
	
	public GameplayNetReceiver(GameplayScreen screen, WorldManager worldManager, MultiplayerType type, BaseNetwork network){
		this.screen = screen;
		this.worldManager = worldManager;
		this.type = type;
		this.network = network;
		if(type != MultiplayerType.Local){
			if(type != MultiplayerType.DedicatedServer)
				worldManager.getPlayer().setUuid(network.getUUID());
			network.addListener(MessageType.TEXT, this);
		}
		worldManager.setSimulate(type != MultiplayerType.Client);
		
		for(MessageType messageType : MessageType.values())
			messageMap.put(messageType, new LinkedList<>());
		for(IMessageReceive<?> messageReceiver : PluginUtil.getPlugins(IMessageReceive.class))
			if(messageReceiver.applies(type)){
				messageReceiver.initialize(worldManager, network);
				messageMap.get(messageReceiver.getSubscription()).add(messageReceiver);
			}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override public void receive(MessageType messageType, Message object) {
		for(IMessageReceive messageReceiver : messageMap.get(messageType))
			messageReceiver.receive(messageType, object);
		switch(messageType){
		case TEXT:{
			screen.handlePause(true);
			break;
		}default:
			break;
		}
	}
	
	public void render(float dt){
		if(type != MultiplayerType.Local){
			network.render();
			if(type != MultiplayerType.DedicatedServer && !worldManager.getPlayer().isDead()){
				PlayerState.Builder builder = PlayerState.newBuilder();
				builder.addPlayers(worldManager.getPlayer().buildMessage(true));
				network.send(MessageType.PLAYER_STATE, builder.build());
			}
		}
		if(type == MultiplayerType.Host || type == MultiplayerType.DedicatedServer){
			NPCState.Builder builder = NPCState.newBuilder(); 
			for(NPC npc : worldManager.getNpcs())
				builder.addNpcs(npc.buildMessage(false));
			network.send(MessageType.NPC_STATE, builder.build());
		}
	}
	
	public void dispose(){
		if(type != MultiplayerType.Local)
			network.removeListener(this);
	}

	public void send(MessageType messageType, Message message) {
		if(network != null)
			network.send(messageType, message);
	}
}
