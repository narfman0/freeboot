package com.blastedstudios.freeboot.util;

import java.util.LinkedList;

import com.blastedstudios.freeboot.world.being.Being;

public class VisibilityReturnStruct {
	public final LinkedList<Being> enemies;
	public final Being closestEnemy;
	public final float[] target; 
	
	public VisibilityReturnStruct(LinkedList<Being> enemies, Being closestEnemy){
		this.enemies = enemies;
		this.closestEnemy = closestEnemy;
		if(closestEnemy != null)
			target = new float[]{closestEnemy.getPosition().x, closestEnemy.getPosition().y};
		else
			target = null;
	}
	
	public boolean isVisible(){
		return closestEnemy != null;
	}
}
