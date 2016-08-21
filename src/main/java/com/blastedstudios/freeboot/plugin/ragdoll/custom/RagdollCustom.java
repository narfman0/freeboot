package com.blastedstudios.freeboot.plugin.ragdoll.custom;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.physics.PhysicsHelper;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.physics.PhysicsEnvironment;
import com.blastedstudios.freeboot.physics.ragdoll.AbstractRagdoll;
import com.blastedstudios.freeboot.world.being.Being;

public class RagdollCustom extends AbstractRagdoll {
	private static float DENSITY = Properties.getFloat("character.ragdoll.density", 5f),
			SCALE = Properties.getFloat("character.ragdoll.scale", 1f);
	private final static float torsoY = .27f, legX = .1f, legY = -.2f, armX = -.02f, armY = .3f, headY = .6f;

	public RagdollCustom(World world, float x, float y, Being being, TextureAtlas atlas){
		super(being, atlas, x, y, 
				createTorso(world, being.getMask(), being.getCat()), 
				createHead(world, being.getMask(), being.getCat()), 
				createRLeg(world, being.getMask(), being.getCat()), 
				createLLeg(world, being.getMask(), being.getCat()),
				createRArm(world, being.getMask(), being.getCat()), 
				createLArm(world, being.getMask(), being.getCat()), 
				createLHand(world, being.getMask(), being.getCat()), 
				createRHand(world, being.getMask(), being.getCat()));
		setFilters(being.getMask(), being.getCat());
		initializeJoints(world);
		setTransform(x,y,0);
	}
	
	@Override protected void initializeJoints(World world){
		lLegJoint = PhysicsEnvironment.addWeld(world, lLegBody, torsoBody, new Vector2(-.1f,-.05f).scl(SCALE));
		rLegJoint = PhysicsEnvironment.addWeld(world, rLegBody, torsoBody, new Vector2(.1f,-.05f).scl(SCALE));
		lArmJoint = PhysicsEnvironment.addRevolute(world, lArmBody, torsoBody, 0,0,new Vector2(-.17f,.3f).scl(SCALE));
		lHandJoint = PhysicsEnvironment.addWeld(world, lHandBody, lArmBody, new Vector2(-armX,armY-.11f).scl(SCALE));
		rHandJoint = PhysicsEnvironment.addWeld(world, rHandBody, rArmBody, new Vector2(armX,armY-.11f).scl(SCALE));
		rArmJoint = PhysicsEnvironment.addRevolute(world, rArmBody, torsoBody, 0,0,new Vector2(.17f,.3f).scl(SCALE));
		headJoint = PhysicsEnvironment.addWeld(world, headBody, torsoBody, new Vector2(0,.45f).scl(SCALE));
	}
	
	private static Body createTorso(World world, short mask, short cat){
		return PhysicsHelper.createRectangle(world, .15f*SCALE, .3f*SCALE, 
				new Vector2(0,torsoY).scl(SCALE), BodyType.DynamicBody, .2f, .5f, DENSITY, 
				mask, cat, (short)0);
	}
	
	private static Body createLLeg(World world, short mask, short cat){
		Body lLegBody = PhysicsHelper.createRectangle(world, .1f*SCALE, .25f*SCALE, 
				new Vector2(-legX, legY).scl(SCALE), BodyType.DynamicBody, .2f, .5f, DENSITY, 
				mask, cat, (short)0);
		lLegBody.setTransform(lLegBody.getPosition(), -.2f*SCALE);
		return lLegBody;
	}
	
	private static Body createRLeg(World world, short mask, short cat){
		Body rLegBody = PhysicsHelper.createRectangle(world, .1f*SCALE, .25f*SCALE, 
				new Vector2(legX, legY).scl(SCALE), BodyType.DynamicBody, .2f, .5f, DENSITY, 
				mask, cat, (short)0);
		rLegBody.setTransform(rLegBody.getPosition(), .2f*SCALE);
		return rLegBody;
	}
	
	private static Body createLArm(World world, short mask, short cat){
		PolygonShape lArm = new PolygonShape();
		lArm.setAsBox(.1f*SCALE, .22f*SCALE, new Vector2(-armX, armY), -1.57f*SCALE);
		Body lArmBody = world.createBody(getDynamicBody());
		lArmBody.createFixture(lArm, DENSITY);
		lArm.dispose();
		return lArmBody;
	}
	
	private static Body createRArm(World world, short mask, short cat){
		PolygonShape rArm = new PolygonShape();
		rArm.setAsBox(.1f*SCALE, .22f*SCALE, new Vector2(armX, armY).scl(SCALE), 1.57f*SCALE);
		Body rArmBody = world.createBody(getDynamicBody());
		rArmBody.createFixture(rArm, DENSITY);
		rArm.dispose();
		return rArmBody;
	}
	
	private static Body createLHand(World world, short mask, short cat){
		CircleShape lHand = new CircleShape();
		lHand.setPosition(new Vector2(-armX+.14f, armY));
		lHand.setRadius(.1f);
		Body lHandBody = world.createBody(getDynamicBody());
		lHandBody.createFixture(lHand, DENSITY/2f);
		lHand.dispose();
		return lHandBody;
	}
	
	private static Body createRHand(World world, short mask, short cat){
		CircleShape rHand = new CircleShape();
		rHand.setPosition(new Vector2(armX-.14f, armY));
		rHand.setRadius(.1f);
		Body rHandBody = world.createBody(getDynamicBody());
		rHandBody.createFixture(rHand, DENSITY/2f);
		rHand.dispose();
		return rHandBody;
	}
	
	private static Body createHead(World world, short mask, short cat){
		CircleShape head = new CircleShape();
		head.setRadius(.2f*SCALE);
		head.setPosition(new Vector2(0, headY).scl(SCALE));
		Body headBody = world.createBody(getDynamicBody());
		headBody.createFixture(head, DENSITY);
		head.dispose();
		return headBody;
	}

	private static BodyDef getDynamicBody(){
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		return def;
	}
}