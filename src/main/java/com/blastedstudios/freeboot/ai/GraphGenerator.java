package com.blastedstudios.freeboot.ai;

import java.util.concurrent.atomic.AtomicBoolean;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.physics.IntersectQueryCallback;

/**
 * Generates graph from world
 */
public class GraphGenerator {
	private static final float 
		PATHING_LENGTH = Properties.getFloat("world.pathing.length", 4f),
		WORLD_MAX_X = Properties.getFloat("world.dimensions.x.max"),
		WORLD_MIN_X = Properties.getFloat("world.dimensions.x.min"),
		WORLD_MIN_Y = Properties.getFloat("world.dimensions.y.min"),
		WORLD_MAX_Y = Properties.getFloat("world.dimensions.y.max");
	
	public static SimpleWeightedGraph<Vector2, DefaultWeightedEdge> generateGraph(World world){
		SimpleWeightedGraph<Vector2, DefaultWeightedEdge> graph = 
				new SimpleWeightedGraph<Vector2, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		for(float y=WORLD_MIN_Y; y<WORLD_MAX_Y; y++)
			for(float x=WORLD_MIN_X; x<WORLD_MAX_X; x++){
				if(isValidNode(x,y,world)){
					Vector2 current = new Vector2(x, y);
					graph.addVertex(current);
					if(x>WORLD_MIN_X && isValidNode(x-1,y,world) && !collides(world, current, x-1, y)){
						DefaultWeightedEdge edge = graph.addEdge(current, new Vector2(x-1,y));
						if(isAirNode(x,y,world))
							graph.setEdgeWeight(edge, 2);
					}
					if(y>WORLD_MIN_Y && isValidNode(x,y-1,world) && !collides(world, current, x, y-1)){
						DefaultWeightedEdge edge = graph.addEdge(current, new Vector2(x,y-1));
						if(isAirNode(x,y,world))
							graph.setEdgeWeight(edge, 2);
					}if(x > WORLD_MIN_X && y>WORLD_MIN_Y && isValidNode(x-1,y-1,world) && !collides(world, current, x-1, y-1))
						graph.setEdgeWeight(graph.addEdge(current, new Vector2(x-1,y-1)), 
								isAirNode(x,y,world) ? 2.5f : 1.414f);
					if(x+1 < WORLD_MAX_X && y-1>WORLD_MIN_Y && isValidNode(x+1,y-1,world) && !collides(world, current, x+1, y-1))
						graph.setEdgeWeight(graph.addEdge(current, new Vector2(x+1,y-1)), 
								isAirNode(x,y,world) ? 2.5f : 1.414f);
				}
			}
		return graph;
	}
	
	private static boolean collides(World world, Vector2 current, float x, float y){
		final AtomicBoolean called = new AtomicBoolean();
		world.rayCast(new RayCastCallback() {
			@Override public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				called.set(true);
				return fraction;
			}
		}, current, new Vector2(x, y));
		return called.get();
	}
	
	/**
	 * @return true if point inside a body
	 */
	private static boolean isWithinBody(float x, float y, World world){
		IntersectQueryCallback insideFixtureCallback = new IntersectQueryCallback();
		//performance wise, faster to aabb first
		world.QueryAABB(insideFixtureCallback, x-.01f, y-.01f, x+.01f, y+.01f);
		if(insideFixtureCallback.called){
			//if aabb failed and was called, meaning it might be in something
			Array<Body> bodies = new Array<Body>();
			world.getBodies(bodies);
			for(Body body : bodies)
				for(Fixture fixture : body.getFixtureList())
					if(fixture.testPoint(x, y))
						return true;
		}
		return false;
	}
	
	private static boolean isWithinDistance(float x, float y, World world, float distance){
		//ensure it is within a certain threshold distance-wise
		IntersectQueryCallback farCallback = new IntersectQueryCallback();
		world.QueryAABB(farCallback, x-PATHING_LENGTH, y-PATHING_LENGTH, x+PATHING_LENGTH, y);
		//if it is, continue with more expensive checks
		if(farCallback.called){
			//if aabb failed and was called, meaning it might be in something
			Array<Body> bodies = new Array<Body>();
			world.getBodies(bodies);
			for(double angle=7.0/6.0*Math.PI; angle<11.0/6.0*Math.PI; angle+=Math.PI/8){
				float xa = (float) Math.cos(angle)*distance, ya = (float) Math.sin(angle)*distance;
				if(collides(world, new Vector2(x,y), x+xa, y+ya))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * For world building only (when creating nodes)
	 * @return if that x/y intersects with physical objects loaded from json
	 */
	private static boolean isValidNode(float x, float y, World world){
		if(isWithinBody(x, y, world))
			return false;
		return isWithinDistance(x, y, world, PATHING_LENGTH);
	}

	private static boolean isAirNode(float x, float y, World world){
		IntersectQueryCallback closeCallback = new IntersectQueryCallback(),
				farCallback = new IntersectQueryCallback();
		world.QueryAABB(closeCallback, x-1, y-1, x+1, y);
		world.QueryAABB(farCallback, x-PATHING_LENGTH, y-PATHING_LENGTH, x+PATHING_LENGTH, y);
		return farCallback.called && !closeCallback.called;
	}
}