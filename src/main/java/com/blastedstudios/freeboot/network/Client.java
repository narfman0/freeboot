package com.blastedstudios.freeboot.network;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.network.Messages.MessageType;

public class Client extends BaseNetwork {
	private HostStruct hostStruct;
	
	public void connect(String host){
		int port = Properties.getInt("network.port");
		// if host is something like 1.2.3.4:8888 try to split up host,port to component parts and connect that way
		if(host.contains(":")){
			port = Integer.parseInt(host.split(":")[1]);
			host = host.split(":")[0];
		}
		Socket socket = Gdx.net.newClientSocket(Protocol.TCP, host, port, null);
		hostStruct = new HostStruct(socket);
		Log.debug("Client.<init>", "Connected to server: " + socket.getRemoteAddress());
		receiveMessage(MessageType.CONNECTED, hostStruct);
	}
	
	@Override public void render(){
		if(!isConnected())
			return;
		List<MessageStruct> messages = receiveMessages(hostStruct.inStream, hostStruct.socket);
		for(MessageStruct message : messages){
			receiveMessage(message.messageType, message.message);
			Log.debug("Client.render", "Message received: " + message.messageType + " contents: " + message.message);
		}
		sendMessages(sendQueue, hostStruct.outStream);
		sendQueue.clear();
	}
	
	@Override public void dispose(){
		if(hostStruct != null && hostStruct.socket != null)
			hostStruct.socket.dispose();
		hostStruct = null;
	}

	@Override public boolean isConnected() {
		return hostStruct != null && hostStruct.socket != null && hostStruct.socket.isConnected();
	}
}
