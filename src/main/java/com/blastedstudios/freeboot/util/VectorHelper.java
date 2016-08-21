package com.blastedstudios.freeboot.util;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class VectorHelper {
	public static Vector2 calculateAverage(List<Vector2> vectors){
		Vector2 average = new Vector2();
		for(Vector2 vector : vectors)
			average.add(vector);
		return average.scl(1f/(float)vectors.size());
	}
	
	public static Vector2 calculateAverage(Vector2[] vectors){
		return calculateAverage(Arrays.asList(vectors));
	}
	
	public static Vector2 parse(String vectorString){
		String[] components = vectorString.split(",");
		return new Vector2(Float.parseFloat(components[0]), Float.parseFloat(components[1]));
	}
}
