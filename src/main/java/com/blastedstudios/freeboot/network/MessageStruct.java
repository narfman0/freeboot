package com.blastedstudios.freeboot.network;

import java.util.List;

import com.badlogic.gdx.net.Socket;
import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.google.protobuf.Message;

public class MessageStruct{
	public final MessageType messageType;
	public final Message message;
	public final List<Socket> destinations;
	
	public MessageStruct(MessageType messageType, Message message, List<Socket> destinations){
		this.messageType = messageType;
		this.message = message;
		this.destinations = destinations;
	}
	
	@Override public String toString(){
		return "[MessageStruct type: " + messageType.name() + " message: " + message.toString() + "]";
	}
}