package com.blastedstudios.freeboot.plugin.network;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.Socket;
import com.blastedstudios.freeboot.network.Messages.Attack;
import com.blastedstudios.freeboot.util.UUIDConvert;
import com.blastedstudios.freeboot.world.being.Being;
import com.google.protobuf.Message;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class AttackReceived extends AbstractMessageReceive<Attack>{
	public void receive(Attack message, Socket origin){
		Being existing = null;
		if(message.hasUuid())
			existing = worldManager.getRemotePlayer(UUIDConvert.convert(message.getUuid()));
		else
			for(Being being : worldManager.getAllBeings())
				if(being.getName().equals(message.getName()))
					existing = being;
		if(existing != null)
			existing.attack(new Vector2(message.getPosX(), message.getPosY()), worldManager);
	}

	@Override public Class<? extends Message> getSubscription() {
		return Attack.class;
	}
}
