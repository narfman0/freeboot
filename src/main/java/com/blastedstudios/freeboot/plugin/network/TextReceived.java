package com.blastedstudios.freeboot.plugin.network;

import com.badlogic.gdx.graphics.Color;
import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.blastedstudios.freeboot.network.Messages.Text;
import com.blastedstudios.freeboot.ui.gameplay.console.History;

public class TextReceived extends AbstractMessageReceive<Text> {
	@Override public void receive(MessageType type, Text message) {
		if(!message.getOrigin().equals(worldManager.getPlayer().getName()))
			History.add(message.getContent(), Color.BLACK);
	}

	@Override public MessageType getSubscription() {
		return MessageType.TEXT;
	}
}
