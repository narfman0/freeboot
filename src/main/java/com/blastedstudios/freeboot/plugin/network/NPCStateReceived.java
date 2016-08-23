package com.blastedstudios.freeboot.plugin.network;

import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.blastedstudios.freeboot.network.Messages.NPCState;
import com.blastedstudios.freeboot.network.Messages.NetBeing;
import com.blastedstudios.freeboot.world.being.NPC;

public class NPCStateReceived extends AbstractMessageReceive<NPCState> {
	@Override public void receive(MessageType type, NPCState message) {
		for(NetBeing updateNPC : message.getNpcsList())
			for(NPC npc : worldManager.getNpcs())
				if(!npc.isDead() && npc.getName().equals(updateNPC.getName()))
					npc.updateFromMessage(updateNPC);
	}

	@Override public MessageType getSubscription() {
		return MessageType.NPC_STATE;
	}
}
