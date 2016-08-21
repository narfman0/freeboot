package com.blastedstudios.freeboot.plugin.ragdoll.loader;

import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.util.FileUtil;
import com.blastedstudios.gdxworld.util.ISerializer;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.world.group.GDXGroupExportStruct;
import com.blastedstudios.freeboot.physics.ragdoll.AbstractRagdoll;
import com.blastedstudios.freeboot.world.being.Being;

public class RagdollLoader extends AbstractRagdoll {
	public RagdollLoader(World world, float x, float y, Being being, TextureAtlas atlas, Map<String,Body> bodyMap){
		super(being, atlas, x, y, bodyMap.get("torso"), bodyMap.get("head"), bodyMap.get("rLeg"), 
				bodyMap.get("lLeg"), bodyMap.get("rArm"), bodyMap.get("lArm"), bodyMap.get("lHand"), bodyMap.get("rHand"));
		initializeJoints(world);
		setTransform(x,y,0);
	}
	
	public RagdollLoader(World world, float x, float y, Being being, TextureAtlas atlas, FileHandle file){
		this(world, x, y, being, atlas, createBodyMap(world, file, x, y));
		setFilters(being.getMask(), being.getCat());
	}
	
	protected static Map<String,Body> createBodyMap(World world, FileHandle fileHandle, float x, float y){
		GDXGroupExportStruct struct = null;
		ISerializer serializer = FileUtil.getSerializer(fileHandle);
		try {
			struct = (GDXGroupExportStruct) serializer.load(fileHandle);
		} catch (Exception e) {
			Log.error("RagdollCustom.<init>", "Error deserializing ragdoll: " + 
					fileHandle.path() +" message: " + e.getMessage());
			e.printStackTrace();
		}
		if(struct == null)
			Log.error("RagdollCustom.<init>", "Error deserializing ragdoll: " + fileHandle.path());
		return struct.instantiate(world, new Vector2(x,y));
	}
}
