package com.blastedstudios.freeboot.plugin.network;

import java.util.UUID;

import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.blastedstudios.freeboot.network.Messages.Respawn;
import com.blastedstudios.freeboot.util.UUIDConvert;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.being.Player;

public class RespawnReceived extends AbstractMessageReceive<Respawn> {
	@Override public void receive(MessageType type, Respawn message) {
		UUID uuid = UUIDConvert.convert(message.getUuid());
		Being existing = worldManager.getRemotePlayer(uuid);
		if(existing != null)
			existing.respawn(worldManager.getWorld(), message.getPosX(), message.getPosY());
		else if(UUIDConvert.convert(message.getUuid()).equals(network.getUUID())){
			Player self = worldManager.getPlayer();
			self.respawn(worldManager.getWorld(), self.getPosition().x, self.getPosition().y);
		}
	}

	@Override public MessageType getSubscription() {
		return MessageType.RESPAWN;
	}
}
