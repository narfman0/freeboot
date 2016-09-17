package com.blastedstudios.freeboot.plugin.network;

import java.net.Socket;

import com.badlogic.gdx.graphics.Color;
import com.blastedstudios.freeboot.network.Messages.Text;
import com.blastedstudios.freeboot.ui.gameplay.console.History;
import com.google.protobuf.Message;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class TextReceived extends AbstractMessageReceive<Text> {
	@Override public void receive(Text message, Socket origin) {
		if(!message.getOrigin().equals(worldManager.getPlayer() == null ? null : worldManager.getPlayer().getName()))
			History.add(message.getContent(), Color.BLACK);
	}

	@Override public Class<? extends Message> getSubscription() {
		return Text.class;
	}
}
