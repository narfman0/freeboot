package com.blastedstudios.freeboot.plugin.quest.handler.trigger;

import java.util.LinkedList;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.blastedstudios.gdxworld.plugin.quest.trigger.objectdistance.IObjectDistanceTriggerHandler;
import com.blastedstudios.gdxworld.plugin.quest.trigger.objectdistance.ObjectDistanceTrigger;
import com.blastedstudios.freeboot.input.ActionEnum;
import com.blastedstudios.freeboot.plugin.quest.handler.IWorldManagerInitializer;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;

@PluginImplementation
public class ObjectDistanceTriggerHandler implements IObjectDistanceTriggerHandler, IWorldManagerInitializer{
	private WorldManager world;
	
	@Override public boolean activate(ObjectDistanceTrigger trigger) {
		for(Vector2 origin : getPosition(trigger.getOrigin()))
			for(Vector2 target : getPosition(trigger.getTarget()))
				if(origin != null && target != null && origin.dst(target) <= trigger.getDistance() && 
						(!trigger.isActionRequired() || ActionEnum.ACTION.isPressed()))
					return true;
		return false;
	}
	
	private LinkedList<Vector2> getPosition(String name){
		LinkedList<Vector2> positions = new LinkedList<>();
		for(Being being : world.matchBeings(name))
			positions.add(being.getPosition());
		for(Body body : world.matchPhysicsObject(name))
			positions.add(body.getPosition());
		return positions;
	}
	
	@Override public void setWorldManager(WorldManager world){
		this.world = world;
	}
}
