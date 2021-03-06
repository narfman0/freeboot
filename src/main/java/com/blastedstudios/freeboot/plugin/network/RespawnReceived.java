package com.blastedstudios.freeboot.plugin.network;

import java.net.Socket;
import java.util.UUID;

import com.blastedstudios.freeboot.network.Messages.Respawn;
import com.blastedstudios.freeboot.util.UUIDConvert;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.being.Player;
import com.google.protobuf.Message;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class RespawnReceived extends AbstractMessageReceive<Respawn> {
	@Override public void receive(Respawn message, Socket origin) {
		UUID uuid = UUIDConvert.convert(message.getUuid());
		Being existing = worldManager.getRemotePlayer(uuid);
		if(existing == null)
			existing = worldManager.getNpcs().get(uuid);
		if(existing != null)
			existing.respawn(worldManager, message.getPosX(), message.getPosY());
		else if(worldManager.getPlayer() != null && worldManager.getPlayer().getUuid().equals(uuid)){
			Player self = worldManager.getPlayer();
			if(self != null && self.isDead())
				self.respawn(worldManager, message.getPosX(), message.getPosY());
		}
	}

	@Override public Class<? extends Message> getSubscription() {
		return Respawn.class;
	}
}
