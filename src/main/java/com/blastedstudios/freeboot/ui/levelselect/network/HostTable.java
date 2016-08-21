package com.blastedstudios.freeboot.ui.levelselect.network;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.freeboot.network.HostStruct;
import com.blastedstudios.freeboot.network.Host;
import com.blastedstudios.freeboot.network.IMessageListener;
import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.blastedstudios.freeboot.world.being.Being;

public class HostTable extends Table {
	private final Host host;
	
	public HostTable(Skin skin, Being player){
		super(skin);
		List<String> clients = new List<String>(skin);
		host = new Host(player);
		host.addListener(MessageType.CONNECTED, new IMessageListener() {
			@Override public void receive(MessageType messageType, Object object) {
				if(object != null){
					HostStruct struct = (HostStruct) object;
					clients.getItems().add(struct.socket.getRemoteAddress());
				}
			}
		});
		host.addListener(MessageType.DISCONNECTED, new IMessageListener() {
			@Override public void receive(MessageType messageType, Object object) {
				HostStruct struct = (HostStruct) object;
				if(struct.player != null)
					clients.getItems().removeValue(struct.player.getName(), false);
				if(struct != null && struct.isConnected())
					clients.getItems().removeValue(struct.socket.getRemoteAddress(), false);
			}
		});
		host.addListener(MessageType.NAME_UPDATE, new IMessageListener() {
			@Override public void receive(MessageType messageType, Object object) {
				HostStruct struct = (HostStruct) object;
				// got name, append to ip
				for(int i=0; i<clients.getItems().size; i++)
					if(clients.getItems().get(i).equals(struct.socket.getRemoteAddress()))
						clients.getItems().set(i, clients.getItems().get(i) + " " + struct.player.getName());
			}
		});
		add(clients);
	}
	
	public void render(){
		if(host != null)
			host.render();
	}
	
	@Override public boolean remove(){
		if(host != null)
			host.dispose();
		return super.remove();
	}

	public Host getHost() {
		return host;
	}
}
