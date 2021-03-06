package com.blastedstudios.freeboot.util;

import java.util.UUID;

import com.blastedstudios.freeboot.network.Messages;

public class UUIDConvert {
	public static UUID convert(Messages.UUID uuid){
		if(uuid == null)
			return null;
		return new UUID(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
	}

	public static Messages.UUID convert(UUID uuid){
		if(uuid == null)
			return null;
		Messages.UUID.Builder builder = Messages.UUID.newBuilder();
		builder.setLeastSignificantBits(uuid.getLeastSignificantBits());
		builder.setMostSignificantBits(uuid.getMostSignificantBits());
		return builder.build();
	}
}
