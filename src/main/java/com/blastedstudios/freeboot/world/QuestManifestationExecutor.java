package com.blastedstudios.freeboot.world;

import java.util.Random;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.dialog.IDialogHandler;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.IQuestManifestationExecutor;
import com.blastedstudios.freeboot.network.Messages.NetBeing.FactionEnum;
import com.blastedstudios.freeboot.plugin.quest.handler.IGameplayScreenInitializer;
import com.blastedstudios.freeboot.plugin.quest.handler.IWorldManagerInitializer;
import com.blastedstudios.freeboot.plugin.quest.handler.manifestation.DialogHandlerPlugin;
import com.blastedstudios.freeboot.plugin.quest.manifestation.cameratween.CameraAccessor;
import com.blastedstudios.freeboot.ui.gameplay.GameplayScreen;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.weapon.Gun;
import com.blastedstudios.freeboot.world.weapon.Turret;
import com.blastedstudios.freeboot.world.weapon.WeaponFactory;

public class QuestManifestationExecutor implements IQuestManifestationExecutor{
	private final GameplayScreen screen;
	private final WorldManager worldManager;
	private final Random random;
	
	public QuestManifestationExecutor(GameplayScreen screen, WorldManager worldManager){
		this.screen = screen;
		this.worldManager = worldManager;
		random = new Random();
		for(IDialogHandler handler : PluginUtil.getPlugins(IDialogHandler.class))
			((DialogHandlerPlugin)handler).setDialogManager(screen.getDialogManager());
		for(IWorldManagerInitializer init : PluginUtil.getPlugins(IWorldManagerInitializer.class))
			init.setWorldManager(worldManager);
		for(IGameplayScreenInitializer init : PluginUtil.getPlugins(IGameplayScreenInitializer.class))
			init.setGameplayScreen(screen);
	}

	@Override public Joint getPhysicsJoint(String name) {
		Array<Joint> joints = new Array<>(worldManager.getWorld().getJointCount());
		worldManager.getWorld().getJoints(joints);
		for(Joint joint : joints)
			if(joint.getUserData() != null && joint.getUserData().equals(name))
				return joint;
		return null;
	}

	@Override public Body getPhysicsObject(String name) {
		Array<Body> bodyArray = new Array<>(worldManager.getWorld().getBodyCount());
		worldManager.getWorld().getBodies(bodyArray);
		for(Body body : bodyArray)
			if(name != null && name.equals(body.getUserData()))
				return body;
		return null;
	}

	public CompletionEnum factionChange(String beingName, FactionEnum faction) {
		for(Being being : worldManager.getAllBeings())
			if(being.getName().matches(beingName) || 
					"player".matches(beingName) && being == worldManager.getPlayer()){
				being.setFaction(faction);
				being.respawn(worldManager, being.getPosition().x, being.getPosition().y);
			}
		return CompletionEnum.COMPLETED;
	}

	public CompletionEnum beingRotation(String beingString, boolean fixedRotation, float torque) {
		for(Being being : worldManager.getAllBeings())
			if(being.getName().matches(beingString) || 
					"player".matches(beingString) && being == worldManager.getPlayer()){
				being.setFixedRotation(fixedRotation);
				being.getRagdoll().applyTorque(torque);
			}
		return CompletionEnum.COMPLETED;
	}

	public CompletionEnum weaponAdd(String weapon, String target) {
		for(Being being : worldManager.getAllBeings())
			if(being.getName().matches(target) ||
					"player".matches(target) && being == worldManager.getPlayer()){
				if(!being.getGuns().isEmpty())
					being.getGuns().get(0).dispose(worldManager.getWorld());
				being.getGuns().add(0, WeaponFactory.getWeapon(weapon));
				being.setCurrentWeapon(0, worldManager.getWorld(), true);
				if(being.getGuns().size() > 3)
					being.getInventory().add(being.getGuns().remove(3));
			}
		return CompletionEnum.COMPLETED;
	}

	public CompletionEnum addXP(String beingName, long xp) {
		for(Being being : worldManager.getAllBeings())
			if(being.getName().matches(beingName) || 
					"player".matches(beingName) && being == worldManager.getPlayer())
				being.addXp(xp);
		return CompletionEnum.COMPLETED;
	}

	public CompletionEnum turretAdd(Vector2 location, Vector2 mountLocation, String weapon, 
			String baseResource, float direction, float directionLow, float directionHigh) {
		worldManager.turretAdd(new Turret((Gun)WeaponFactory.getWeapon(weapon), baseResource, 
				location, mountLocation, direction, directionLow, directionHigh, random));
		return CompletionEnum.COMPLETED;
	}

	@Override public World getWorld() {
		return worldManager.getWorld();
	}

	public CompletionEnum beingInvuln(String beingName, boolean invuln) {
		if("player".matches(beingName))
			if(worldManager.getPlayer() != null)
				worldManager.getPlayer().setInvulnerable(invuln);
		else
			for(Being being : worldManager.getAllBeings())
				if(being.getName().matches(beingName))
					being.setInvulnerable(invuln);
		return CompletionEnum.COMPLETED;
	}

	public CompletionEnum jetpackManifestation(String beingName,
			boolean changeMax, float max, boolean changeRecharge,
			float recharge, boolean changeImpulse, float impulse) {
		Being target = null;
		if("player".matches(beingName))
			target = worldManager.getPlayer();
		else
			for(Being being : worldManager.getAllBeings())
				if(being.getName().matches(beingName))
					target = being;
		if(target == null)
			Log.error("QuestManifestationExecutor.jetpackManifestation", "Being not found with name: " + beingName);
		else{
			if(changeImpulse && target.getStats().getJetpackImpulse() < impulse)
				target.getStats().setJetpackImpulse(impulse);
			if(changeMax && target.getStats().getJetpackMax() < max)
				target.getStats().setJetpackMax(max);
			if(changeRecharge && target.getStats().getJetpackRecharge() < recharge)
				target.getStats().setJetpackRecharge(recharge);
		}
		return CompletionEnum.COMPLETED;
	}
	
	public CompletionEnum cameraTween(int tweenType, float duration, Vector2 target, TweenEquation ease){
		if(screen.getCamera() == null)
			return CompletionEnum.EXECUTING;
		Tween.registerAccessor(screen.getCamera().getClass(), new CameraAccessor());
		Tween.to(screen.getCamera(), tweenType, duration).target(target.x, target.y).
			ease(ease).start(worldManager.getTweenManager());
		return CompletionEnum.COMPLETED;
	}
}
