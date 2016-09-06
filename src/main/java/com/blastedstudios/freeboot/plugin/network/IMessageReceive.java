package com.blastedstudios.freeboot.plugin.network;

import java.net.Socket;

import com.blastedstudios.entente.BaseNetwork;
import com.blastedstudios.entente.IMessageListener;
import com.blastedstudios.freeboot.ui.network.network.NetworkWindow.MultiplayerType;
import com.blastedstudios.freeboot.world.WorldManager;
import com.google.protobuf.Message;

import net.xeoh.plugins.base.Plugin;

public interface IMessageReceive<T extends Message> extends Plugin, IMessageListener<T>{
	void initialize(WorldManager worldManager, BaseNetwork network, MultiplayerType multiplayerType);
	void receive(T message, Socket origin);
	Class<? extends Message> getSubscription();
	boolean applies(MultiplayerType multiplayerType);
}
