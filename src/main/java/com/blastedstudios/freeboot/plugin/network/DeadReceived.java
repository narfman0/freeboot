package com.blastedstudios.freeboot.plugin.network;

import java.net.Socket;
import java.util.UUID;

import com.blastedstudios.freeboot.network.Messages.Dead;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.MultiplayerType;
import com.blastedstudios.freeboot.util.UUIDConvert;
import com.blastedstudios.freeboot.world.being.Being;
import com.google.protobuf.Message;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class DeadReceived extends AbstractMessageReceive<Dead>{
	@Override public void receive(Dead message, Socket origin) {
		UUID uuid = UUIDConvert.convert(message.getUuid());
		Being existing = worldManager.getRemotePlayer(uuid);
		if(existing == null)
			existing = worldManager.getAllBeings().get(uuid);
		if(existing != null){
			existing.death(worldManager);
			if(multiplayerType == MultiplayerType.Host || multiplayerType == MultiplayerType.DedicatedServer)
				network.send(message);
		}
	}

	@Override public Class<? extends Message> getSubscription() {
		return Dead.class;
	}
}
