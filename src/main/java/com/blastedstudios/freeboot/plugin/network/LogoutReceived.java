package com.blastedstudios.freeboot.plugin.network;

import java.net.Socket;
import java.util.ArrayList;

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
		for(Player player : new ArrayList<>(worldManager.getAllPlayers()))
			if(UUIDConvert.convert(player.getUuid()) == message.getUuid() || player.getName().equals(message.getName()))
				worldManager.dispose(player);
		if(multiplayerType == MultiplayerType.Host || multiplayerType == MultiplayerType.DedicatedServer)
			network.send(message);
	}

	@Override
	public Class<? extends Message> getSubscription() {
		return Logout.class;
	}
}
