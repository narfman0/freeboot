package com.blastedstudios.freeboot.network;

import com.blastedstudios.freeboot.network.Messages.MessageType;

public interface IMessageListener {
	void receive(MessageType messageType, Object object);
}
