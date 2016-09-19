package com.blastedstudios.freeboot.plugin.network;

import java.net.Socket;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.freeboot.network.Messages.Attack;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.MultiplayerType;
import com.blastedstudios.freeboot.world.being.Being;
import com.google.protobuf.Message;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class AttackReceived extends AbstractMessageReceive<Attack>{
	public void receive(Attack message, Socket origin){
		Being existing = worldManager.getAllBeings().get(message.getUuid());
		if(existing != null) {
			existing.attack(new Vector2(message.getPosX(), message.getPosY()), worldManager);
			if(multiplayerType == MultiplayerType.Host || multiplayerType == MultiplayerType.DedicatedServer)
				network.send(message);
		}
	}

	@Override public Class<? extends Message> getSubscription() {
		return Attack.class;
	}
}
