package com.blastedstudios.freeboot.plugin.network;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;

import com.blastedstudios.freeboot.network.Messages.WorldFileRequest;
import com.blastedstudios.freeboot.network.Messages.WorldFileResponse;
import com.blastedstudios.freeboot.ui.main.MainScreen;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.MultiplayerType;
import com.google.protobuf.ByteString;
import com.google.protobuf.Message;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class WorldFileRequestReceived extends AbstractMessageReceive<WorldFileRequest> {
	@Override public void receive(WorldFileRequest message, Socket origin) {
		try {
			String md5 = DigestUtils.md5Hex(MainScreen.WORLD_FILE.read());
			WorldFileResponse.Builder builder = WorldFileResponse.newBuilder();
			builder.setMd5(md5);
			builder.setFile(ByteString.readFrom(MainScreen.WORLD_FILE.read()));
			network.send(builder.build(), Arrays.asList(origin));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override public boolean applies(MultiplayerType multiplayerType) {
		return multiplayerType == MultiplayerType.Host || multiplayerType == MultiplayerType.DedicatedServer;
	}

	@Override public Class<? extends Message> getSubscription() {
		return WorldFileRequest.class;
	}
}
