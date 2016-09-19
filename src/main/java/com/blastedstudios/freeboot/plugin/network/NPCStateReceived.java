package com.blastedstudios.freeboot.plugin.network;

import java.net.Socket;
import java.util.UUID;

import com.blastedstudios.freeboot.network.Messages.NPCState;
import com.blastedstudios.freeboot.network.Messages.NetBeing;
import com.blastedstudios.freeboot.util.UUIDConvert;
import com.blastedstudios.freeboot.world.being.NPC;
import com.google.protobuf.Message;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class NPCStateReceived extends AbstractMessageReceive<NPCState> {
	@Override public void receive(NPCState message, Socket origin) {
		for(NetBeing updateNPC : message.getNpcsList()){
			UUID uuid = UUIDConvert.convert(updateNPC.getUuid());
			NPC npc = worldManager.getNpcs().get(uuid);
			if(npc == null){
				npc = new NPC(worldManager, updateNPC);
				worldManager.getNpcs().put(uuid, npc);
				if(!updateNPC.getDead())
					npc.respawn(worldManager, updateNPC.getPosX(), updateNPC.getPosY());
					
			}else if(!npc.isDead())
				npc.updateFromMessage(worldManager, updateNPC);
		}
	}

	@Override public Class<? extends Message> getSubscription() {
		return NPCState.class;
	}
}
