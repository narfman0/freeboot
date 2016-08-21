package com.blastedstudios.freeboot.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.blastedstudios.freeboot.network.Messages.NameUpdate;
import com.blastedstudios.freeboot.network.Messages.NetBeing;
import com.blastedstudios.freeboot.network.Messages.Text;
import com.blastedstudios.freeboot.network.Messages.TextRequest;
import com.blastedstudios.freeboot.world.being.Being;
import com.google.protobuf.Message;

public class Host extends BaseNetwork{
	private final List<HostStruct> clients = Collections.synchronizedList(new LinkedList<HostStruct>());
	private ServerSocket serverSocket;
	private Timer timer;
	private Being player;
	
	public Host(Being player){
		this.player = player;
		int port = Properties.getInt("network.port");
		serverSocket = Gdx.net.newServerSocket(Protocol.TCP, port, null);
		timer = new Timer("Server accept thread");
		timer.schedule(new TimerTask() {
			@Override public void run() {
				try{
					Socket socket = serverSocket.accept(null);
					HostStruct client = new HostStruct(socket); 
					clients.add(client);
					Log.debug("Host.<init>", "Added client: " + socket.getRemoteAddress());
					receiveMessage(MessageType.CONNECTED, client);
				}catch(Exception e){
					Log.error("Host.<init> timer.tick", "Exception received, aborting host thread. Message: " + e.getMessage());
					this.cancel();
				}
			}
		}, 0, 100);
		Log.debug("Host.<init>", "Network created, listening for conenctions on port: " + port);
		receiveMessage(MessageType.CONNECTED, null);
	}
	
	@Override public void render(){
		// Build new list of messages to send this frame. Grab messages initially, don't check queue again!
		ArrayList<MessageStruct> currentQueue = new ArrayList<>(sendQueue);
		// "but jrob, thats a queue that could be modified between copying and clearing, you should iterate..."
		// GTFO /uninstall /uninstall /uninstall
		// no but you're right... *shrugs*
		sendQueue.clear();
		
		for(Iterator<HostStruct> iter = clients.iterator(); iter.hasNext();){
			HostStruct client = iter.next();
			if(!client.socket.isConnected()){
				receiveMessage(MessageType.DISCONNECTED, client);
				Log.debug("Host.render", "Disconnecting client: " + client.socket.getRemoteAddress());
				iter.remove();
			}else{
				List<MessageStruct> messages = receiveMessages(client.inStream, client.socket);
				for(MessageStruct struct : messages){
					switch(struct.messageType){
					case ATTACK:
					case DEAD:
					case EXIT_GAMEPLAY:
					case PAUSE:
					case RELOAD:
					case RESPAWN:
						send(struct.messageType, struct.message);
						receiveMessage(struct.messageType, struct.message);
						break;
					case NAME_UPDATE:
						NameUpdate message = (NameUpdate) struct.message;
						if(client.player == null)
							client.player = NetBeing.newBuilder();
						client.player.setName(message.getName());
						receiveMessage(MessageType.NAME_UPDATE, client);
						break;
					case TEXT_REQUEST:
						TextRequest request = (TextRequest) struct.message;
						Text.Builder builder = Text.newBuilder();
						builder.setContent(request.getContent());
						builder.setOrigin(client.toString());
						Text text = builder.build();
						send(MessageType.TEXT, text);
						break;
					default:
						receiveMessage(struct.messageType, struct.message);
						break;
					}
					Log.debug("Host.render", "Message received: " + struct.messageType + " contents: " +
							struct.message + " from " + client.socket.getRemoteAddress());
				}
				sendMessages(currentQueue, client.outStream);
			}
		}
	}

	/**
	 * Intercept send messages and translate as need be, e.g. textrequests can just be texts immediately
	 */
	@Override public void send(MessageType messageType, Message message) {
		switch(messageType){
		case TEXT_REQUEST:
			TextRequest request = (TextRequest) message;
			Text.Builder builder = Text.newBuilder();
			builder.setContent(request.getContent());
			// sending text message as host. this receives the message for render
			builder.setOrigin(player == null ? "null host player" : player.getName());
			message = builder.build();
			messageType = MessageType.TEXT;
			receiveMessage(messageType, message);
			break;
		case TEXT:
			receiveMessage(messageType, message);
			break;
		default:
			break;
		}
		super.send(messageType, message);
	}
	
	@Override public void dispose(){
		if(serverSocket != null)
			serverSocket.dispose();
		serverSocket = null;
		for(HostStruct client : clients)
			client.socket.dispose();
		if(timer != null)
			timer.cancel();
		timer = null;
	}

	@Override public boolean isConnected() {
		return serverSocket != null;
	}
}
