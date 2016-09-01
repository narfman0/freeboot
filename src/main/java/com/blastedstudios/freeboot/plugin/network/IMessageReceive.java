package com.blastedstudios.freeboot.plugin.network;

import com.badlogic.gdx.net.Socket;
import com.blastedstudios.freeboot.network.BaseNetwork;
import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.MultiplayerType;
import com.blastedstudios.freeboot.world.WorldManager;
import com.google.protobuf.Message;

import net.xeoh.plugins.base.Plugin;

public interface IMessageReceive<T extends Message> extends Plugin{
	void initialize(WorldManager worldManager, BaseNetwork network, MultiplayerType multiplayerType);
	void receive(MessageType type, T message, Socket origin);
	MessageType getSubscription();
	boolean applies(MultiplayerType multiplayerType);
}
