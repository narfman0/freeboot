package com.blastedstudios.freeboot.plugin.network;

import java.net.Socket;

import com.blastedstudios.freeboot.network.Messages.NPCState;
import com.blastedstudios.freeboot.network.Messages.NetBeing;
import com.blastedstudios.freeboot.world.being.NPC;
import com.google.protobuf.Message;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class NPCStateReceived extends AbstractMessageReceive<NPCState> {
	@Override public void receive(NPCState message, Socket origin) {
		for(NetBeing updateNPC : message.getNpcsList())
			for(NPC npc : worldManager.getNpcs())
				if(!npc.isDead() && npc.getName().equals(updateNPC.getName()))
					npc.updateFromMessage(updateNPC);
	}

	@Override public Class<? extends Message> getSubscription() {
		return NPCState.class;
	}
}
