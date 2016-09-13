package com.blastedstudios.freeboot.world;

import java.util.LinkedList;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.freeboot.network.Messages.NetBeing.FactionEnum;
import com.blastedstudios.freeboot.world.StrategicPoint.CaptureListener;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.gdxworld.math.PolygonUtils;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXNPC;
import com.blastedstudios.gdxworld.world.shape.GDXPolygon;

public class Director implements CaptureListener {
	private final LinkedList<StrategicPoint> strategicPoints = new LinkedList<>();
	private final WorldManager worldManager;
	
	public Director(GDXLevel level, WorldManager worldManager){
		this.worldManager = worldManager;
		for(GDXPolygon polygon : level.getPolygons())
			if(polygon.getTag() != null && polygon.getTag().contains("strategicPoint")){
				Vector2[] aabb = PolygonUtils.getAABB(polygon.getVerticesAbsolute().toArray(new Vector2[2]));
				strategicPoints.add(new StrategicPoint(aabb, this));
			}
		LinkedList<StrategicPoint> points = new LinkedList<>(strategicPoints);
		for(FactionEnum faction : FactionEnum.values())
			if(!points.isEmpty()){
				StrategicPoint point = points.pop();
				point.setCaptured(faction, StrategicPoint.CAPTURE_AMOUNT);
			}
		
		int rounds = Properties.getInt("ai.npcs.generated.count", 32) / FactionEnum.values().length;
		for(int i=0; i<FactionEnum.values().length && i<strategicPoints.size(); i++)
			for(int j=0; j<rounds; j++){
				GDXNPC npc = new GDXNPC();
				npc.getProperties().put("NPCData", "skelly");
				npc.setCoordinates(strategicPoints.get(i).getBase());
				npc.setName("NPC" + (i*rounds + j));
				worldManager.spawnNPC(npc);
			}
	}
	
	public void update(float dt){
		for(Being being : worldManager.getAllBeings())
			if(!being.isDead())
				for(StrategicPoint strategicPoint : strategicPoints)
					if(strategicPoint.contains(being.getPosition()))
						strategicPoint.capture(dt, being.getFaction(), Properties.getFloat("strategicpoint.capture.amount", 1f));
	}

	@Override
	public void strategicPointCaptured(StrategicPoint point) {
		Log.log("Director.strategicPointCaptured", "Point captured: " + point);
	}

	@Override
	public void strategicPointLost(StrategicPoint point) {
		Log.log("Director.strategicPointLost", "Point lost: " + point);
	}
}
