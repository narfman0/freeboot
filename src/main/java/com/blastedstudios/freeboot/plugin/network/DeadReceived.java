package com.blastedstudios.freeboot.plugin.network;

import com.badlogic.gdx.net.Socket;
import com.blastedstudios.freeboot.network.Messages.Dead;
import com.blastedstudios.freeboot.util.UUIDConvert;
import com.blastedstudios.freeboot.world.being.Being;
import com.google.protobuf.Message;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class DeadReceived extends AbstractMessageReceive<Dead>{
	@Override public void receive(Dead message, Socket origin) {
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

	@Override public Class<? extends Message> getSubscription() {
		return Dead.class;
	}
}
