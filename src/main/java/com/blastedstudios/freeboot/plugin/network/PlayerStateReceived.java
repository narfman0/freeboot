package com.blastedstudios.freeboot.plugin.network;

import java.net.Socket;
import java.util.UUID;

import com.blastedstudios.freeboot.network.Messages.NetBeing;
import com.blastedstudios.freeboot.network.Messages.PlayerState;
import com.blastedstudios.freeboot.util.UUIDConvert;
import com.blastedstudios.freeboot.world.being.Player;
import com.blastedstudios.gdxworld.util.Log;
import com.google.protobuf.Message;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class PlayerStateReceived extends AbstractMessageReceive<PlayerState> {
	@Override public void receive(PlayerState message, Socket origin) {
		for(NetBeing netBeing : message.getPlayersList()){
			UUID uuid = UUIDConvert.convert(netBeing.getUuid());
			if(worldManager.getPlayer() != null && uuid.equals(worldManager.getPlayer().getUuid()))
				// don't want to make a new player with my name!
				break;
			Player remotePlayer = worldManager.getRemotePlayer(uuid);
			if(remotePlayer == null){
				remotePlayer = new Player(netBeing);
				worldManager.getRemotePlayers().put(remotePlayer.getUuid(), remotePlayer);
				if(remotePlayer.getHp() > 0)
					remotePlayer.respawn(worldManager, netBeing.getPosX(), netBeing.getPosY());
				Log.log("GameplayScreen.receive", "Received first player update: " + netBeing.getName());
			}else if(remotePlayer.getPosition() != null)
				remotePlayer.updateFromMessage(worldManager, netBeing);
		}
	}

	@Override
	public Class<? extends Message> getSubscription() {
		return PlayerState.class;
	}

}
