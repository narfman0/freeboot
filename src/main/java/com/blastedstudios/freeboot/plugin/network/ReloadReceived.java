package com.blastedstudios.freeboot.plugin.network;

import java.util.UUID;

import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.blastedstudios.freeboot.network.Messages.Reload;
import com.blastedstudios.freeboot.util.UUIDConvert;
import com.blastedstudios.freeboot.world.being.Being;

public class ReloadReceived extends AbstractMessageReceive<Reload>{
	@Override public void receive(MessageType type, Reload message) {
		UUID uuid = UUIDConvert.convert(message.getUuid());
		Being existing = worldManager.getRemotePlayer(uuid);
		if(existing != null)
			existing.reload();
	}

	@Override public MessageType getSubscription() {
		return MessageType.RELOAD;
	}
}
