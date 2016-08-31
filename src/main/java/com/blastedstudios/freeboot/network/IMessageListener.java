package com.blastedstudios.freeboot.network;

import com.badlogic.gdx.net.Socket;
import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.google.protobuf.Message;

public interface IMessageListener {
	void receive(MessageType messageType, Message object, Socket origin);
}
