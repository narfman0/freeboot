package com.blastedstudios.freeboot.plugin.network;

import java.util.UUID;

import com.blastedstudios.entente.BaseNetwork;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.MultiplayerType;
import com.blastedstudios.freeboot.world.WorldManager;
import com.google.protobuf.Message;

public abstract class AbstractMessageReceive<T extends Message> implements IMessageReceive<T> {
	protected WorldManager worldManager;
	protected BaseNetwork network;
	protected MultiplayerType multiplayerType;
	protected UUID uuid;
	
	@Override
	public void initialize(WorldManager worldManager, BaseNetwork network, MultiplayerType multiplayerType, UUID uuid){
		this.worldManager = worldManager;
		this.network = network;
		this.multiplayerType = multiplayerType;
		this.uuid = uuid;
	}

	@Override public boolean applies(MultiplayerType multiplayerType) {
		return multiplayerType != MultiplayerType.Local;
	}
}
