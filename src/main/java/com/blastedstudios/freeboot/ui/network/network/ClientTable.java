package com.blastedstudios.freeboot.ui.network.network;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import org.apache.commons.io.FileUtils;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.entente.Client;
import com.blastedstudios.entente.IMessageListener;
import com.blastedstudios.freeboot.network.Messages.NameUpdate;
import com.blastedstudios.freeboot.network.Messages.WorldFileRequest;
import com.blastedstudios.freeboot.network.Messages.WorldFileResponse;
import com.blastedstudios.freeboot.network.Messages.WorldHashRequest;
import com.blastedstudios.freeboot.network.Messages.WorldHashResponse;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.INetworkWindowListener;
import com.blastedstudios.freeboot.util.SaveHelper;
import com.blastedstudios.freeboot.util.ui.FreebootTextButton;
import com.blastedstudios.freeboot.world.being.Being;

public class ClientTable extends Table {
	private final TextButton connectButton;
	private Client client;
	
	public ClientTable(Skin skin, Being player, final INetworkWindowListener listener){
		super(skin);
		final Table clientTable = new Table(skin);
		final TextField hostnameText = new TextField(Properties.get("host.default", "127.0.0.1"), skin);
		connectButton = new FreebootTextButton("Connect", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				Properties.set("host.default", hostnameText.getText());
				SaveHelper.saveProperties();
				if(client != null && client.isConnected()){
					Log.error("ClientTable.<init>", "Already connected to a host, aborting");
					return;
				}
				client = new Client(hostnameText.getText(), Properties.getInt("network.port"));
				if(client.isConnected()){
					connectButton.remove();
					// send minimal information - name!
					NameUpdate.Builder builder = NameUpdate.newBuilder();
					builder.setName(player.getName());
					client.send(builder.build());
					client.send(WorldHashRequest.getDefaultInstance());
					client.subscribe(WorldFileResponse.class, new IMessageListener<WorldFileResponse>() {
						@Override public void receive(WorldFileResponse response, Socket origin) {
							client.unsubscribe(WorldFileResponse.class, this);
							Log.log("ClientTable.<init>", "World file response: " + response.getMd5());
							File file = SaveHelper.getSaveDirectory().child("worlds").child(response.getMd5() + "." + Properties.get("save.extenstion", "xml")).file();
							try {
								FileUtils.writeByteArrayToFile(file, response.getFile().toByteArray());
							} catch (IOException e) {
								e.printStackTrace();
							}
							GDXWorld world = SaveHelper.loadWorld(response.getMd5());
							listener.worldSelected(world);
						}
					});
					client.subscribe(WorldHashResponse.class, new IMessageListener<WorldHashResponse>() {
						@Override public void receive(WorldHashResponse response, Socket origin) {
							client.unsubscribe(WorldHashResponse.class, this);
							Log.log("ClientTable.<init>", "World hash response: " + response.getMd5());
							GDXWorld responseWorld = SaveHelper.loadWorld(response.getMd5());
							if(responseWorld == null)
								client.send(WorldFileRequest.getDefaultInstance());
							else
								listener.worldSelected(responseWorld);
						}
					});
				}
			}
		});
		clientTable.add("Hostname: ");
		clientTable.add(hostnameText);
		clientTable.row();
		clientTable.add(connectButton).colspan(2);
		add(clientTable);
	}
	
	public void render(){
		if(client != null)
			client.update();
	}
	
	@Override public boolean remove(){
		if(client != null)
			client.dispose();
		return super.remove();
	}

	public Client getClient() {
		return client;
	}
}
