package com.blastedstudios.freeboot.plugin.network;

import java.net.Socket;

import com.blastedstudios.freeboot.network.Messages.Logout;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.MultiplayerType;
import com.blastedstudios.freeboot.util.UUIDConvert;
import com.blastedstudios.freeboot.world.being.Player;
import com.google.protobuf.Message;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class LogoutReceived extends AbstractMessageReceive<Logout> {
	@Override
	public void receive(Logout message, Socket origin) {
		Player player = worldManager.getAllPlayers().get(UUIDConvert.convert(message.getUuid()));
		if(player != null)
			worldManager.dispose(player);
		if(multiplayerType == MultiplayerType.Host || multiplayerType == MultiplayerType.DedicatedServer)
			network.send(message);
	}

	@Override
	public Class<? extends Message> getSubscription() {
		return Logout.class;
	}
}
