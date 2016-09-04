package com.blastedstudios.freeboot.ui.gameplay;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.badlogic.gdx.net.Socket;
import com.blastedstudios.entente.BaseNetwork;
import com.blastedstudios.entente.IMessageListener;
import com.blastedstudios.freeboot.network.Messages;
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
	private final HashMap<Class<?>, List<IMessageReceive<?>>> messageMap = new HashMap<>();
	private float npcStateAccumulator;
	private final UUID uuid;
	
	public GameplayNetReceiver(WorldManager worldManager, MultiplayerType multiplayerType, BaseNetwork network){
		this.worldManager = worldManager;
		this.type = multiplayerType;
		this.network = network;
		this.uuid = UUID.randomUUID();
		if(multiplayerType != MultiplayerType.Local)
			for(Class<?> messageClass : Messages.class.getClasses())
				network.subscribe(messageClass, this);
		worldManager.setSimulate(multiplayerType != MultiplayerType.Client);
		
		for(Class<?> messageType : Messages.class.getClasses())
			messageMap.put(messageType, new LinkedList<>());
		for(IMessageReceive<?> messageReceiver : PluginUtil.getPlugins(IMessageReceive.class))
			if(messageReceiver.applies(multiplayerType)){
				messageReceiver.initialize(worldManager, network, multiplayerType, uuid);
				messageMap.get(messageReceiver.getSubscription()).add(messageReceiver);
			}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override public void receive(Message object, Socket origin) {
		for(IMessageReceive messageReceiver : messageMap.get(object.getClass()))
			messageReceiver.receive(object, origin);
	}
	
	public void render(float dt){
		if(type != MultiplayerType.Local){
			network.update();
			npcStateAccumulator -= dt;
			if(type != MultiplayerType.DedicatedServer && !worldManager.getPlayer().isDead()){
				PlayerState.Builder builder = PlayerState.newBuilder();
				builder.addPlayers(worldManager.getPlayer().buildMessage(true));
				network.send(builder.build(), null);
			}
			if(type == MultiplayerType.Host || type == MultiplayerType.DedicatedServer && npcStateAccumulator < 0){
				NPCState.Builder builder = NPCState.newBuilder(); 
				for(NPC npc : worldManager.getNpcs())
					builder.addNpcs(npc.buildMessage(false));
				network.send(builder.build(), null);
				npcStateAccumulator = .033f; // 33ms before next update for npcs
			}
		}
	}
	
	public void dispose(){
		if(type != MultiplayerType.Local)
			network.unsubscribe(this);
	}

	public void send(Message message, List<Socket> destinations) {
		if(network != null)
			network.send(message, destinations);
	}
	
	public void send(Message message){
		send(message, null);
	}
}
