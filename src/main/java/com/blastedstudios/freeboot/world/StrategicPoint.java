package com.blastedstudios.freeboot.world;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.freeboot.network.Messages.NetBeing.FactionEnum;
import com.blastedstudios.gdxworld.util.Properties;

public class StrategicPoint {
	private static final float CAPTURE_AMOUNT = Properties.getFloat("strategicpoint.capture.max", 100f);
	public final Vector2[] aabb;
	private CapturedStruct captured;
	private final CaptureListener listener;
	
	public StrategicPoint(Vector2[] aabb, CaptureListener listener){
		this.aabb = aabb;
		this.listener = listener;
	}
	
	public Vector2 getCenter(){
		return aabb[0].cpy().add(aabb[1]).scl(.5f);
	}
	
	/**
	 * @return bottommost centered location in strategic point
	 */
	public Vector2 getBase(){
		return new Vector2((aabb[0].x + aabb[1].x)/2f, Math.min(aabb[0].y, aabb[1].y));
	}
	
	public CapturedStruct getPercentCaptured() {
		return captured;
	}

	public void setCaptured(CapturedStruct captured) {
		this.captured = captured;
	}
	
	public boolean contains(Vector2 position){
		return Math.min(aabb[0].x, aabb[1].x) <= position.x && Math.max(aabb[0].x, aabb[1].x) >= position.x &&
				Math.min(aabb[0].y, aabb[1].y) <= position.y && Math.max(aabb[0].y, aabb[1].y) >= position.y;
	}

	public void capture(float dt, FactionEnum faction, float amount){
		if(captured == null)
			captured = new CapturedStruct(faction, amount*dt);
		else if(captured.getPercentCaptured() < CAPTURE_AMOUNT &&
				captured.faction.equals(faction)){
			captured.addPercentCaptured(amount*dt);
			if(captured.getPercentCaptured() >= CAPTURE_AMOUNT)
				listener.strategicPointCaptured(this);
		}else if(!captured.faction.equals(faction)){
			captured.addPercentCaptured(-amount*dt);
			if(captured.getPercentCaptured() <= 0){
				captured = null;
				listener.strategicPointLost(this);
			}
		}
	}
	
	public boolean isCaptured(){
		return captured != null && captured.percentCaptured >= CAPTURE_AMOUNT;
	}
	
	public boolean isCaptured(FactionEnum faction){
		return isCaptured() && captured.faction.equals(faction);
	}
	
	public FactionEnum getFaction(){
		return captured != null ? captured.faction : null;
	}
	
	@Override public String toString(){
		return "StrategicPoint coords="+ getCenter() + (captured == null ? "" : captured.toString());
	}
	
	public interface CaptureListener {
		void strategicPointCaptured(StrategicPoint point);
		void strategicPointLost(StrategicPoint point);
	}

	class CapturedStruct{
		public final FactionEnum faction;
		private float percentCaptured;
		
		public CapturedStruct(FactionEnum faction, float percentCaptured){
			this.faction = faction;
			this.percentCaptured = percentCaptured;
		}

		public float getPercentCaptured() {
			return percentCaptured;
		}

		public void setPercentCaptured(float percentCaptured) {
			this.percentCaptured = Math.max(0,Math.min(percentCaptured, CAPTURE_AMOUNT));
		}

		public void addPercentCaptured(float add) {
			this.percentCaptured = Math.max(0,Math.min(percentCaptured+add, CAPTURE_AMOUNT));
		}
		
		@Override public String toString(){
			return "faction:" + faction.name() + " percent:" + percentCaptured;
		}
	}
}