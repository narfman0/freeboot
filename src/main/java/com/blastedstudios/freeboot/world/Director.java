package com.blastedstudios.freeboot.world;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.freeboot.network.Messages.NetBeing.FactionEnum;
import com.blastedstudios.freeboot.world.StrategicPoint.CaptureListener;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.being.NPC;
import com.blastedstudios.gdxworld.math.PolygonUtils;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXNPC;
import com.blastedstudios.gdxworld.world.shape.GDXPolygon;

public class Director implements CaptureListener {
	private final LinkedList<StrategicPoint> strategicPoints = new LinkedList<>();
	private final WorldManager worldManager;
	private int lastCreatedIndex = 0;
	
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
		for(int i=0; i<FactionEnum.values().length-1 && i<strategicPoints.size(); i++)
			for(int j=0; j<rounds; j++)
				createNPC(FactionEnum.values()[i]);
	}	

	public void update(float dt){
		for(Being being : worldManager.getAllBeings().values())
			if(!being.isDead() && being.isSpawned()){
				for(StrategicPoint strategicPoint : strategicPoints)
					if(strategicPoint.contains(being.getPosition()))
						strategicPoint.capture(dt, being.getFaction(), Properties.getFloat("strategicpoint.capture.amount", 1f));
			}else{
				if(being.canRespawn()){
					Vector2 coordinates = createSpawnPoint(being.getFaction());
					if(coordinates == null)
						coordinates = being.getPosition() == null ? Vector2.Zero : being.getPosition();
					being.respawn(worldManager, coordinates.x, coordinates.y);
				}
			}
	}
	
	/**
	 * @return created NPC or null if failed
	 */
	private NPC createNPC(FactionEnum faction){
		GDXNPC npc = new GDXNPC();
		String npcDataName = null;
		switch(faction){
		case Briton:
			npcDataName = "briton";
			break;
		case Undead:
			npcDataName = "skelly";
			break;
		case Spanish:
			npcDataName = "spanish";
			break;
		default:
			return null;
		}
		npc.getProperties().put("NPCData", npcDataName);
		npc.setCoordinates(createSpawnPoint(faction));
		npc.setName("NPC" + lastCreatedIndex++);
		return worldManager.spawnNPC(npc);
	}
	
	/**
	 * @return coordinates of a @param faction-owned prospective spawn point
	 */
	public Vector2 createSpawnPoint(FactionEnum faction){
		List<StrategicPoint> points = getFactionStrategicPoints(faction);
		if(points.isEmpty())
			return null;
		StrategicPoint startingPoint = points.get(worldManager.getRandom().nextInt(points.size()));
		return startingPoint.getBase().add(worldManager.getRandom().nextFloat()*10f-5f, worldManager.getRandom().nextFloat()*5f);
	}
	
	/**
	 * @return list of spawn points owned by @param faction
	 */
	private List<StrategicPoint> getFactionStrategicPoints(FactionEnum faction){
		LinkedList<StrategicPoint> points = new LinkedList<>();
		for(StrategicPoint point : strategicPoints)
			if(point.isCaptured(faction))
				points.add(point);
		return points;
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