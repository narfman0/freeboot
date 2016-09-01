package com.blastedstudios.freeboot.plugin.network;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;

import com.badlogic.gdx.net.Socket;
import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.blastedstudios.freeboot.network.Messages.WorldFileRequest;
import com.blastedstudios.freeboot.network.Messages.WorldFileResponse;
import com.blastedstudios.freeboot.ui.main.MainScreen;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.MultiplayerType;
import com.google.protobuf.ByteString;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class WorldFileRequestReceived extends AbstractMessageReceive<WorldFileRequest> {
	@Override public void receive(MessageType type, WorldFileRequest message, Socket origin) {
		try {
			String md5 = DigestUtils.md5Hex(MainScreen.WORLD_FILE.read());
			WorldFileResponse.Builder builder = WorldFileResponse.newBuilder();
			builder.setMd5(md5);
			builder.setFile(ByteString.readFrom(MainScreen.WORLD_FILE.read()));
			network.send(MessageType.WORLD_FILE_RESPONSE, builder.build(), Arrays.asList(origin));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override public boolean applies(MultiplayerType multiplayerType) {
		return multiplayerType == MultiplayerType.Host || multiplayerType == MultiplayerType.DedicatedServer;
	}

	@Override public MessageType getSubscription() {
		return MessageType.WORLD_FILE_REQUEST;
	}
}
