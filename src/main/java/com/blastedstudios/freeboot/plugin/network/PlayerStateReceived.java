package com.blastedstudios.freeboot.plugin.network;

import java.util.UUID;

import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.blastedstudios.freeboot.network.Messages.NetBeing;
import com.blastedstudios.freeboot.network.Messages.PlayerState;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.MultiplayerType;
import com.blastedstudios.freeboot.util.UUIDConvert;
import com.blastedstudios.freeboot.world.being.Player;
import com.blastedstudios.gdxworld.util.Log;

public class PlayerStateReceived extends AbstractMessageReceive<PlayerState> {
	@Override public void receive(MessageType type, PlayerState message) {
		for(NetBeing netBeing : message.getPlayersList()){
			if(worldManager.getPlayer() != null && netBeing.getName().equals(worldManager.getPlayer().getName()))
				// don't want to make a new player with my name! should refactor to use ids somehow in future
				break;
			UUID uuid = UUIDConvert.convert(netBeing.getUuid());
			Player remotePlayer = worldManager.getRemotePlayer(uuid);
			if(remotePlayer == null){
				remotePlayer = new Player(netBeing);
				worldManager.getRemotePlayers().add(remotePlayer);
				remotePlayer.respawn(worldManager.getWorld(), netBeing.getPosX(), netBeing.getPosY());
				Log.log("GameplayScreen.receive", "Received first player update: " + netBeing.getName());
			}else if(remotePlayer.getPosition() != null)
				remotePlayer.updateFromMessage(netBeing);
		}
		if(worldManager.getReceiver().type == MultiplayerType.Host || worldManager.getReceiver().type == MultiplayerType.DedicatedServer)
			network.send(MessageType.PLAYER_STATE, message);
	}

	@Override
	public MessageType getSubscription() {
		return MessageType.PLAYER_STATE;
	}

}
