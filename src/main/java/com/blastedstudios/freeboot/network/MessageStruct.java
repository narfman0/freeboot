package com.blastedstudios.freeboot.network;

import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.google.protobuf.Message;

public class MessageStruct{
	public final MessageType messageType;
	public final Message message;
	
	public MessageStruct(MessageType messageType, Message message){
		this.messageType = messageType;
		this.message = message;
	}
	
	@Override public String toString(){
		return "[MessageStruct type: " + messageType.name() + " message: " + message.toString() + "]";
	}
}