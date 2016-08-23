package com.blastedstudios.freeboot.plugin.network;

import com.blastedstudios.freeboot.world.WorldManager;
import com.google.protobuf.Message;

public abstract class AbstractMessageReceive<T extends Message>  implements IMessageReceive<T> {
	protected WorldManager worldManager;
	
	@Override
	public void initialize(WorldManager worldManager){
		this.worldManager = worldManager;
	}
}
