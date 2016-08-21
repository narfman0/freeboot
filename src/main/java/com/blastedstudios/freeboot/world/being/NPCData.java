package com.blastedstudios.freeboot.world.being;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.blastedstudios.gdxworld.plugin.serializer.xml.XMLSerializer;

/**
 * Manage defaults for beings and npcs
 */
public class NPCData {
	private static final XMLSerializer serializer = new XMLSerializer();
	private Map<String, String> properties = new HashMap<>();
	
	public void apply(Map<String, String> map){
		for(Entry<String,String> entry : map.entrySet())
			if(!entry.getValue().equals(""))
				properties.put(entry.getKey(), entry.getValue());
	}
	
	public String get(String key){
		if(!properties.containsKey(key))
			properties.put(key, "");
		return properties.get(key);
	}
	
	public float getFloat(String key){
		if(get(key).isEmpty())
			properties.put(key, "0");
		return Float.parseFloat(properties.get(key));
	}
	
	public int getInteger(String key){
		if(get(key).isEmpty())
			properties.put(key, "0");
		return Integer.parseInt(properties.get(key));
	}

	public boolean getBool(String key) {
		if(get(key).isEmpty())
			properties.put(key, "false");
		return Boolean.parseBoolean(properties.get(key));
	}
	
	public String set(String key, String value){
		return properties.put(key, value);
	}
	
	public static NPCData parse(String name){
		try {
			return (NPCData) serializer.load(Gdx.files.internal("data/world/npc/" + name + ".xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override public String toString(){
		StringBuilder builder = new StringBuilder("[NPCData properties:");
		builder.append(properties.toString());
		builder.append("]");
		return builder.toString();
	}
}
