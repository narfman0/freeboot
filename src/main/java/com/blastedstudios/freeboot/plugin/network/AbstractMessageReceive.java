package com.blastedstudios.freeboot.plugin.network;

import com.blastedstudios.freeboot.network.BaseNetwork;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.MultiplayerType;
import com.blastedstudios.freeboot.world.WorldManager;
import com.google.protobuf.Message;

public abstract class AbstractMessageReceive<T extends Message>  implements IMessageReceive<T> {
	protected WorldManager worldManager;
	protected BaseNetwork network;
	
	@Override
	public void initialize(WorldManager worldManager, BaseNetwork network){
		this.worldManager = worldManager;
		this.network = network;
	}

	@Override public boolean applies(MultiplayerType multiplayerType) {
		return multiplayerType != MultiplayerType.Local;
	}
}
