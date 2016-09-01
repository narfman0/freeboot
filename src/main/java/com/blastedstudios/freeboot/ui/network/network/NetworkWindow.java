package com.blastedstudios.freeboot.ui.network.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.freeboot.network.BaseNetwork;
import com.blastedstudios.freeboot.util.ui.FreebootTextButton;
import com.blastedstudios.freeboot.util.ui.FreebootWindow;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.gdxworld.world.GDXWorld;

public class NetworkWindow extends FreebootWindow{
	public enum MultiplayerType{
		Local, Host, Client, DedicatedServer
	}
	
	private final Table multiplayerTypeParentTable;
	private final SelectBox<MultiplayerType> multiplayerTypeSelect;
	private HostTable hostTable = null;
	private ClientTable clientTable = null;

	public NetworkWindow(Skin skin, Being player, INetworkWindowListener listener) {
		super("", skin);
		multiplayerTypeParentTable = new Table(skin);
		multiplayerTypeSelect = new SelectBox<>(skin);
		multiplayerTypeSelect.setItems(MultiplayerType.Local, MultiplayerType.Host, MultiplayerType.Client);
		multiplayerTypeSelect.addListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {
				multiplayerTypeParentTable.clear();
				if(hostTable != null)
					hostTable.remove();
				if(clientTable != null)
					clientTable.remove();
				switch(multiplayerTypeSelect.getSelected()){
				case Host:
					hostTable = new HostTable(skin, player);
					multiplayerTypeParentTable.add(hostTable);
					break;
				case Client:
					clientTable = new ClientTable(skin, player, listener);
					multiplayerTypeParentTable.add(clientTable);
					break;
				case Local:
					break;
				default:
					break;
				}
				listener.networkSelected(multiplayerTypeSelect.getSelected());
			}
		});
		final Button startButton = new FreebootTextButton("Start", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				listener.start();
			}
		});
		
		add("Network");
		add(multiplayerTypeSelect);
		row();
		add(multiplayerTypeParentTable).colspan(2).fillX().expandX();
		row();
		add(startButton).colspan(2).fillX().expandX();
		setSize(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/3f);
		setX(Gdx.graphics.getWidth()/2 - getWidth()/2);
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
		setMovable(false);
	}
	
	public void render(){
		if(hostTable != null)
			hostTable.render();
		if(clientTable != null)
			clientTable.render();
	}
	
	public BaseNetwork getSource(){
		if(hostTable != null)
			return hostTable.getHost();
		if(clientTable != null)
			return clientTable.getClient();
		return null;
	}
	
	public MultiplayerType getMultiplayerType(){
		return multiplayerTypeSelect.getSelected();
	}
	
	public interface INetworkWindowListener{
		void networkSelected(MultiplayerType type);
		void start();
		void worldSelected(GDXWorld world);
	}
}
