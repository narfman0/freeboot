package com.blastedstudios.freeboot.ai;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.physics.PhysicsHelper;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;

public class AIWorld {
	private final SimpleWeightedGraph<Vector2, DefaultWeightedEdge> movementGraph;
	
	public AIWorld(World gameWorld){
		movementGraph = GraphGenerator.generateGraph(gameWorld);
		Log.log("AIWorld.<init>","Initialized graph with " +movementGraph.edgeSet().size() + 
				" edges and " +	movementGraph.vertexSet().size() + " vertices");
	}
	
	/**
	 * @return closest valid runtime node (not to be used while building world)
	 */
	private Vector2 getClosestNode(float x, float y){
		if(movementGraph.containsVertex(new Vector2(x,y)))
			return new Vector2(x,y);
		for(float dst=1; dst<100; dst++)
			for(float angle=0; angle<Math.PI*2; angle+=Math.PI/12){
				Vector2 tmp = new Vector2(
						(int) (x + Math.cos(angle)*dst), 
						(int) (y + Math.sin(angle)*dst));
				if(movementGraph.containsVertex(tmp))
					return tmp;
			}
		return new Vector2(x,y);
	}
	
	public Queue<Vector2> getPathToPoint(Vector2 origin, Vector2 destination){
		LinkedList<Vector2> steps = new LinkedList<Vector2>();
		Vector2 originRounded = getClosestNode(Math.round(origin.x), Math.round(origin.y));
		Vector2 destinationRounded = getClosestNode(Math.round(destination.x),Math.round(destination.y));
		try{
			List<DefaultWeightedEdge> list = DijkstraShortestPath.findPathBetween(
					movementGraph, originRounded, destinationRounded);
			for(DefaultWeightedEdge edge : list)
				steps.add(movementGraph.getEdgeSource(edge));
			steps.add(movementGraph.getEdgeTarget(list.get(list.size()-1)));
		}catch(Exception e){
			Log.error("AIWorld.<init>","Error pathfinding for origin="+originRounded+
					" destination="+destinationRounded+" with message: "+e.getMessage());
		}
		return steps;
	}

	public World createGraphVisible(){
		boolean nodesVisible = Properties.getBool("world.pathing.nodes.visible", true),
				edgesVisible = Properties.getBool("world.pathing.edges.visible", true);
		World world = new World(new Vector2(), true);
		if(nodesVisible)
			for(Vector2 node : movementGraph.vertexSet())
				PhysicsHelper.createCircle(world, .1f, node, BodyType.StaticBody, .2f, .5f, 1f);
		if(edgesVisible)
			for(DefaultWeightedEdge edge : movementGraph.edgeSet()){
				Vector2 source = movementGraph.getEdgeSource(edge), target = movementGraph.getEdgeTarget(edge);
				PhysicsHelper.createEdge(world, BodyType.StaticBody, source.x, source.y, target.x, target.y, 1);
			}
		return world;
	}
}
