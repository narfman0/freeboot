package com.blastedstudios.freeboot.plugin.network;

import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.blastedstudios.freeboot.world.WorldManager;
import com.google.protobuf.Message;

import net.xeoh.plugins.base.Plugin;

public interface IMessageReceive<T extends Message> extends Plugin{
	void initialize(WorldManager worldManager);
	void receive(MessageType type, T message);
}
