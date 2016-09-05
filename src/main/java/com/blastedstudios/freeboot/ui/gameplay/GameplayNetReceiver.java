package com.blastedstudios.freeboot.ui.gameplay;

import java.net.Socket;
import java.util.List;
import java.util.UUID;

import com.blastedstudios.entente.BaseNetwork;
import com.blastedstudios.freeboot.network.Messages.NPCState;
import com.blastedstudios.freeboot.network.Messages.PlayerState;
import com.blastedstudios.freeboot.plugin.network.IMessageReceive;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.MultiplayerType;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.NPC;
import com.blastedstudios.freeboot.world.being.Player;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.google.protobuf.Message;

public class GameplayNetReceiver{
	private final WorldManager worldManager;
	public final MultiplayerType type;
	public final BaseNetwork network;
	private float beingStateAccumulator;
	private final UUID uuid;
	
	public GameplayNetReceiver(WorldManager worldManager, MultiplayerType multiplayerType, BaseNetwork network){
		this.worldManager = worldManager;
		this.type = multiplayerType;
		this.network = network;
		this.uuid = UUID.randomUUID();
		worldManager.setSimulate(multiplayerType != MultiplayerType.Client);
		
		for(IMessageReceive<?> messageReceiver : PluginUtil.getPlugins(IMessageReceive.class))
			if(messageReceiver.applies(multiplayerType)){
				messageReceiver.initialize(worldManager, network, multiplayerType, uuid);
				network.subscribe(messageReceiver.getSubscription(), messageReceiver);
			}
	}
	
	public void update(float dt){
		if(type != MultiplayerType.Local){
			network.update();
			beingStateAccumulator -= dt;
			if(type != MultiplayerType.DedicatedServer && !worldManager.getPlayer().isDead()){
				PlayerState.Builder builder = PlayerState.newBuilder();
				builder.addPlayers(worldManager.getPlayer().buildMessage(true));
				network.send(builder.build(), null);
			}
			if(type == MultiplayerType.Host || type == MultiplayerType.DedicatedServer && beingStateAccumulator < 0){
				updateNPCs();
				updatePlayers();
				beingStateAccumulator = .033f; // 33ms before next update for npcs
			}
		}
	}
	
	/**
	 * Send message with updates for all NPCs
	 */
	private void updateNPCs(){
		NPCState.Builder builder = NPCState.newBuilder(); 
		for(NPC npc : worldManager.getNpcs())
			builder.addNpcs(npc.buildMessage(false));
		network.send(builder.build(), null);
	}
	
	/**
	 * Send message with updates for all Players
	 */
	private void updatePlayers(){
		PlayerState.Builder playerBuilder = PlayerState.newBuilder();
		for(Player player : worldManager.getAllPlayers())
			playerBuilder.addPlayers(player.buildMessage(true));
		network.send(playerBuilder.build(), null);
	}

	public void send(Message message, List<Socket> destinations) {
		if(network != null)
			network.send(message, destinations);
	}
	
	public void send(Message message){
		send(message, null);
	}
	
	public UUID getUUID(){
		return uuid;
	}
}
