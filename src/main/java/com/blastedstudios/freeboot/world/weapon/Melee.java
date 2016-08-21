package com.blastedstudios.freeboot.world.weapon;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.blastedstudios.gdxworld.physics.PhysicsHelper;
import com.blastedstudios.gdxworld.util.FileUtil;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.group.GDXGroupExportStruct;
import com.blastedstudios.freeboot.physics.ragdoll.IRagdoll;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;

public class Melee extends Weapon {
	private static final float MIN_DAMAGE = Properties.getFloat("melee.damage.min", .005f);
	private static final long serialVersionUID = 1L;
	private float width, height, offsetX, offsetY, density;
	private String bodyPath = "";
	private transient Body body;
	private transient Being owner;
	private transient long lastAttack;
	private transient Joint joint;
	private transient boolean startedFacingLeft;
	
	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
	
	public float getDensity() {
		return density;
	}
	
	public Being getOwner() {
		return owner;
	}

	public Body getBody() {
		return body;
	}

	public String getBodyPath() {
		return bodyPath;
	}
	
	@Override public void activate(World world, IRagdoll ragdoll, Being owner) {
		this.owner = owner;
		this.startedFacingLeft = ragdoll.isFacingLeft();
		Vector2 position = ragdoll.getHandFacingPosition().cpy().add(offsetX, offsetY);
		if(bodyPath != null && !bodyPath.equals(""))
			body = loadBody(owner, bodyPath, world, position);
		else
			body = PhysicsHelper.createRectangle(world, width, height, position, BodyType.DynamicBody, 
					.2f, .5f, density, owner.getMask(), owner.getCat(), (short)0);
		body.setUserData(this);
		if(Properties.getBool("melee.useweld", false)){
			WeldJointDef def = new WeldJointDef();
			def.initialize(body, ragdoll.getHandFacing() ,ragdoll.getHandFacingPosition());
			joint = world.createJoint(def);
		}else{
			RevoluteJointDef def = new RevoluteJointDef();
			def.enableLimit = true;
			def.lowerAngle = -.5f;
			def.upperAngle = .01f;
			def.initialize(body, ragdoll.getHandFacing(), ragdoll.getHandFacingPosition());
			joint = world.createJoint(def);
		}
	}
	
	private static Body loadBody(Being owner, String bodyPath, World world, Vector2 position){
		FileHandle handle = Gdx.files.internal(bodyPath);
		try {
			GDXGroupExportStruct struct = (GDXGroupExportStruct) FileUtil.getSerializer(handle).load(handle);
			Map<String,Body> returnStruct = struct.instantiate(world, position);
			Body body = returnStruct.values().iterator().next();
			Filter filter = new Filter();
			filter.categoryBits = owner.getCat();
			filter.maskBits = owner.getMask();
			for(Fixture fixture : body.getFixtureList())
				fixture.setFilterData(filter);
			return body;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override public void dispose(World world) {
		death(world);
		if(body != null){
			world.destroyBody(body);
			body = null;
		}else
			Log.error("Melee.deactivate", "Want to destroy weapon body, but null! " + this);
	}

	@Override public void death(World world){
		if(body != null)
			body.setUserData(null);
		if(joint != null){
			world.destroyJoint(joint);
			joint = null;
		}else
			Log.error("Melee.deactivate", "Want to destroy weapon joint, but null! " + this);
	}
	
	public static double impulseToDamageScalar(float impulse, float dt){
		//TODO: use dt here, as it matters greatly. Probably divide impulse 
		//by it as long dts produce huge impulse, potentially
		return -.1 + .4 * Math.log(600 * impulse);
	}

	@Override public String toString(){
		return "[Melee name:" + name + " dmg: " + getDamage() + "]";
	}

	@Override public void beginContact(WorldManager worldManager, Being target, 
			Fixture hit, Contact contact, float i) {
		if(!owner.isFriendly(target.getFaction())){
			lastAttack = System.currentTimeMillis() - 1000l;//Smaller to make attack frequency better
			float meleeDmg = MIN_DAMAGE;
			if(i > Properties.getFloat("melee.contact.impulse.threshold", 2f))
				meleeDmg = (float)Melee.impulseToDamageScalar(i/10f, Gdx.graphics.getRawDeltaTime());
			worldManager.processHit(getDamage() * meleeDmg, target, owner, hit, contact.getWorldManifold().getNormal(),
					contact.getWorldManifold().getPoints()[0]);
		}
	}

	@Override public long getMSSinceAttack() {
		return System.currentTimeMillis() - lastAttack;
	}

	@Override public boolean canAttack() {
		return true;
	}

	public boolean isStartedFacingLeft() {
		return startedFacingLeft;
	}
}
