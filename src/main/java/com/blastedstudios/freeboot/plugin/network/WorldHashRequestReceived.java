package com.blastedstudios.freeboot.plugin.network;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;

import com.badlogic.gdx.net.Socket;
import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.blastedstudios.freeboot.network.Messages.WorldHashRequest;
import com.blastedstudios.freeboot.network.Messages.WorldHashResponse;
import com.blastedstudios.freeboot.ui.main.MainScreen;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.MultiplayerType;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class WorldHashRequestReceived extends AbstractMessageReceive<WorldHashRequest> {
	@Override public void receive(MessageType type, WorldHashRequest message, Socket origin) {
		try {
			String md5 = DigestUtils.md5Hex(MainScreen.WORLD_FILE.read());
			WorldHashResponse.Builder builder = WorldHashResponse.newBuilder();
			builder.setMd5(md5);
			network.send(MessageType.WORLD_HASH_RESPONSE, builder.build(), Arrays.asList(origin));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override public boolean applies(MultiplayerType multiplayerType) {
		return multiplayerType == MultiplayerType.Host || multiplayerType == MultiplayerType.DedicatedServer;
	}

	@Override public MessageType getSubscription() {
		return MessageType.WORLD_HASH_REQUEST;
	}
}
