package com.blastedstudios.freeboot.util;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.files.FileHandle;
import com.blastedstudios.gdxworld.util.FileUtil;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.freeboot.world.being.Player;

public class SaveHelper {
	private static FileHandle SAVE_DIRECTORY;
	
	public static List<Player> load() {
		Log.log("SaveHelper.load","Loading characters...");
		List<Player> characters = new LinkedList<Player>();
		for(FileHandle file : getSaveDirectory().child("save").list()){
			try {
				Player being = (Player) FileUtil.getSerializer(file).load(file);
				characters.add(being);
				Log.log("SaveHelper.load","Loaded: " + being.getName());
			} catch (Exception e) {
				Log.error("SaveHelper.load","Failed to load " + file.path());
				e.printStackTrace();
			} 
		}
		Log.log("SaveHelper.load","Done loading characters");
		return characters;
	}

	public static void save(Player character){
		getSaveDirectory().child("save").mkdirs();
		FileHandle file = getSaveDirectory().child("save").child(character.getName() + "." + Properties.get("save.extenstion", "xml"));
		try{
			FileUtil.getSerializer(file).save(file, character);
			Log.log("SaveHelper.save","Saved " + character.getName() + " successfully");
		}catch(Exception e){
			Log.error("SaveHelper.save","Failed to write " + character.getName() + " to " + file.path());
			e.printStackTrace();
		}
	}
	
	public static void saveProperties(){
		Gdx.files.external(".freeboot").mkdirs();
		Properties.store(Gdx.files.external(".freeboot/freeboot.properties").write(false), "");
	}
	
	public static void loadProperties(){
		try{
			Properties.load(new FileHandle("data/freeboot.properties").read());
			//this is pretty hacky, not a great way to get at this before Gdx is initialized with files
			FileHandle savedHandle = new FileHandle(new File(LwjglFiles.externalPath + "/.freeboot/freeboot.properties"));
			if(savedHandle.exists())
				Properties.load(savedHandle.read());
		}catch(Exception e){
			System.err.println("SaveHelper.loadProperties: Error loading properties: " + e.getMessage());
		}
	}

	public static GDXWorld loadWorld(String name) {
		Log.log("SaveHelper.loadWorld","Loading world");
		getSaveDirectory().child("worlds").mkdirs();
		for(FileHandle file : getSaveDirectory().child("worlds").list())
			if(file.nameWithoutExtension().equalsIgnoreCase(name))
				return GDXWorld.load(file);
		return null;
	}

	public static void saveWorld(GDXWorld world, String name){
		getSaveDirectory().child("worlds").mkdirs();
		world.save(getSaveDirectory().child("worlds").child(name + "." + Properties.get("save.extenstion", "xml")));
	}
	
	public static FileHandle getSaveDirectory(){
		if(SAVE_DIRECTORY == null)
			SAVE_DIRECTORY = Gdx.files.external(".freeboot");
		return SAVE_DIRECTORY;
	}
}
