package com.blastedstudios.freeboot.plugin.network;

import java.util.UUID;

import com.badlogic.gdx.net.Socket;
import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.blastedstudios.freeboot.network.Messages.Respawn;
import com.blastedstudios.freeboot.util.UUIDConvert;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.being.Player;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class RespawnReceived extends AbstractMessageReceive<Respawn> {
	@Override public void receive(MessageType type, Respawn message, Socket origin) {
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
