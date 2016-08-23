package com.blastedstudios.freeboot.ui.network.network;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.google.protobuf.Message;
import com.blastedstudios.freeboot.network.Client;
import com.blastedstudios.freeboot.network.IMessageListener;
import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.blastedstudios.freeboot.network.Messages.NameUpdate;
import com.blastedstudios.freeboot.util.SaveHelper;
import com.blastedstudios.freeboot.util.ui.FreebootTextButton;
import com.blastedstudios.freeboot.world.being.Being;

public class ClientTable extends Table {
	private final Client client = new Client();
	
	public ClientTable(Skin skin, Being player){
		super(skin);
		final Table clientTable = new Table(skin);
		final TextField hostnameText = new TextField(Properties.get("host.default", "127.0.0.1"), skin);
		TextButton connectButton = new FreebootTextButton("Connect", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				Properties.set("host.default", hostnameText.getText());
				SaveHelper.saveProperties();
				if(client.isConnected()){
					Log.error("ClientTable.<init>", "Already connected to a host, aborting");
					return;
				}
				client.addListener(MessageType.CONNECTED, new IMessageListener() {
					@Override public void receive(MessageType messageType, Message object) {
						// send minimal information - name!
						NameUpdate.Builder builder = NameUpdate.newBuilder();
						builder.setName(player.getName());
						client.send(MessageType.NAME_UPDATE, builder.build());
					}
				});
				client.addListener(MessageType.DISCONNECTED, new IMessageListener() {
					@Override public void receive(MessageType messageType, Message object) {
						client.dispose();
					}
				});
				client.connect(hostnameText.getText());
			}
		});
		clientTable.add("Hostname: ");
		clientTable.add(hostnameText);
		clientTable.row();
		clientTable.add(connectButton).colspan(2);
		add(clientTable);
	}
	
	public void render(){
		client.render();
	}
	
	@Override public boolean remove(){
		client.dispose();
		return super.remove();
	}

	public Client getClient() {
		return client;
	}
}
