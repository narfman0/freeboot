package com.blastedstudios.freeboot.plugin.network;

import com.blastedstudios.freeboot.network.Messages.Dead;
import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.blastedstudios.freeboot.util.UUIDConvert;
import com.blastedstudios.freeboot.world.being.Being;

public class DeadReceived extends AbstractMessageReceive<Dead>{
	@Override public void receive(MessageType type, Dead message) {
		Being existing = null;
		if(message.hasUuid())
			existing = worldManager.getRemotePlayer(UUIDConvert.convert(message.getUuid()));
		else
			for(Being being : worldManager.getAllBeings())
				if(being.getName().equals(message.getName()))
					existing = being;
		if(existing != null)
			existing.death(worldManager);
	}

	@Override public MessageType getSubscription() {
		return MessageType.DEAD;
	}

}
