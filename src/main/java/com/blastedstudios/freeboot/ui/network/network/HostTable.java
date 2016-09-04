package com.blastedstudios.freeboot.ui.network.network;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.entente.Host;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.gdxworld.util.Properties;

public class HostTable extends Table {
	private final Host host;
	
	public HostTable(Skin skin, Being player){
		super(skin);
		List<String> clients = new List<String>(skin);
		host = new Host(Properties.getInt("network.port", 23452));
		add(clients);
	}
	
	public void render(){
		if(host != null)
			host.update();
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
