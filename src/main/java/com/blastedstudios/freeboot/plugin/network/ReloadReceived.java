package com.blastedstudios.freeboot.plugin.network;

import java.util.UUID;

import com.badlogic.gdx.net.Socket;
import com.blastedstudios.freeboot.network.Messages.Reload;
import com.blastedstudios.freeboot.util.UUIDConvert;
import com.blastedstudios.freeboot.world.being.Being;
import com.google.protobuf.Message;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class ReloadReceived extends AbstractMessageReceive<Reload>{
	@Override public void receive(Reload message, Socket origin) {
		UUID uuid = UUIDConvert.convert(message.getUuid());
		Being existing = worldManager.getRemotePlayer(uuid);
		if(existing != null)
			existing.reload();
	}

	@Override public Class<? extends Message> getSubscription() {
		return Reload.class;
	}
}
