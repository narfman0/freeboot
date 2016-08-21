// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 07/23/2014 22:10:47
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.actions.execution;

import jbt.execution.core.IContext;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.util.FileUtil;
import com.blastedstudios.gdxworld.util.ISerializer;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.animation.GDXAnimationHandler;
import com.blastedstudios.gdxworld.world.animation.GDXAnimations;
import com.blastedstudios.gdxworld.world.quest.manifestation.IQuestManifestationExecutor;
import com.blastedstudios.freeboot.physics.ragdoll.AbstractRagdoll;
import com.blastedstudios.freeboot.physics.ragdoll.IRagdoll;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being.BodyPart;
import com.blastedstudios.freeboot.world.being.INPCActionExecutor;
import com.blastedstudios.freeboot.world.being.NPC;
import com.blastedstudios.freeboot.world.being.NPC.AIFieldEnum;
import com.blastedstudios.freeboot.world.weapon.Melee;
import com.blastedstudios.freeboot.world.weapon.RocketLauncher;

/** ExecutionAction class created from MMPM action GMInit. */
public class GMInit extends
jbt.execution.task.leaf.action.ExecutionAction {
	private static String HANDLER_NAME = "AnimationHandler",
			HANDLER_PATH = "data/world/npc/animation/garbageman.xml",
			TIME_MOVED_DIRECTION = "TimeMoved",
			TIME_LAST = "TimeLast",
			CURRENT = "CurrentAnimation",
			RECOVER_TIME = "Recover";
	private static final float STOMP_DISTANCE = Properties.getFloat("garbageman.stomp.distance", 6f);
	private static final float TOTAL_TIME_DIRECTION = Properties.getFloat("garbageman.move.time", 7f);//s

	/**
	 * Constructor. Constructs an instance of GMInit that is able to run a
	 * com.blastedstudios.freeboot.ai.bt.actions.GMInit.
	 */
	public GMInit(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.freeboot.ai.bt.actions.GMInit)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.freeboot.ai.bt.actions.GMInit");
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Log.debug(this.getClass().getCanonicalName(), "spawned");
		getContext().setVariable(TIME_LAST, System.currentTimeMillis());
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		final NPC self = (NPC) getContext().getVariable(AIFieldEnum.SELF.name());
		final WorldManager world = (WorldManager) getContext().getVariable(AIFieldEnum.WORLD.name());
		self.aim(4f);
		getContext().setVariable(TIME_MOVED_DIRECTION, 0f);
		FileHandle handle = FileUtil.find(HANDLER_PATH);
		ISerializer serializer = FileUtil.getSerializer(handle);
		try {
			final GDXAnimationHandler handler = new GDXAnimationHandler((GDXAnimations) serializer.load(handle),
					createQuestExecutor(self, world.getWorld()));
			handler.setActive(true);//defer until we see player?
			getContext().setVariable(HANDLER_NAME, handler);
			getContext().setVariable(INPCActionExecutor.EXECUTE_CONTEXT_NAME, new NPCExecutor(self, world, handler, getContext()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Status.SUCCESS;
	}
	
	private static IQuestManifestationExecutor createQuestExecutor(final NPC self, final World world){
		return new IQuestManifestationExecutor() {
			@Override public World getWorld() {
				return world;
			}
			@Override public Body getPhysicsObject(String name) {
				return self.getRagdoll().getBodyPart(BodyPart.valueOf(name));
			}
			@Override public Joint getPhysicsJoint(String name) {
				return null;
			}
		};
	}
	
	private static void recover(IContext context, IRagdoll ragdoll){
		Object var = context.getVariable(RECOVER_TIME);
		if(var == null)
			return;
		float recoverTime = (float) var;
		recoverTime -= Gdx.graphics.getDeltaTime();
		Body torso = ragdoll.getBodyPart(BodyPart.torso);
		if(recoverTime <= 0f){
			if(!ragdoll.isFixedRotation())
				ragdoll.setFixedRotation(true);
			if(Math.abs(torso.getAngle()) > .05f)
				AbstractRagdoll.recoverFromFreeRotation(torso);
			else{
				context.clearVariable(RECOVER_TIME);
				return;
			}
		}
		context.setVariable(RECOVER_TIME, Math.max(0f, recoverTime));
	}
	
	public class NPCExecutor implements INPCActionExecutor{
		private final NPC self;
		private final WorldManager world; 
		private final GDXAnimationHandler handler;
		private final IContext context;
		
		public NPCExecutor(final NPC self, final WorldManager world, 
			final GDXAnimationHandler handler, final IContext context){
			this.self = self;
			this.world = world;
			this.handler = handler;
			this.context = context;
		}
		
		private void setWeaponClass(boolean melee){
			for(int i=0; i<self.getGuns().size(); i++)
				if(melee ? self.getGuns().get(i) instanceof Melee : self.getGuns().get(i) instanceof RocketLauncher)
					self.setCurrentWeapon(i, world.getWorld(), false);
		}
		
		@Override public Status execute(String identifier) {
			//track time
			Float time = (Float) context.getVariable(TIME_MOVED_DIRECTION);
			float dt = (System.currentTimeMillis() - (long)context.getVariable(TIME_LAST)) / 1000f;
			context.setVariable(TIME_LAST, System.currentTimeMillis());
			time += dt;
			
			float playerDistance = world.getPlayer().getPosition().dst(self.getPosition());
			if(identifier.equals("sword")){
				// check if we should start if, if not already started
				if(!handler.getCurrentAnimation().getName().equals("sword")){
					if(playerDistance < Properties.getFloat("garbageman.sword.distance.max", 10f) && playerDistance > STOMP_DISTANCE){
						setWeaponClass(true);
						handler.applyCurrentAnimation(handler.getAnimations().getAnimation("sword"), 0);
						context.setVariable(RECOVER_TIME, 1.3f);
						self.setFixedRotation(false);
						return Status.RUNNING;
					}else
						return Status.FAILURE;
				}
				recover(context, self.getRagdoll());
				handler.render(dt);
				return handler.getCurrentAnimation().getName().equals("sword") ? Status.RUNNING : Status.SUCCESS;
			}else if(identifier.equals("stomp")){
				// check if we should start if, if not already started
				if(!handler.getCurrentAnimation().getName().equals("stomp")){
					if(playerDistance < STOMP_DISTANCE){
						setWeaponClass(true);
						handler.applyCurrentAnimation(handler.getAnimations().getAnimation("stomp"), 0);
						return Status.RUNNING;
					}else
						return Status.FAILURE;
				}
				handler.render(dt);
				return handler.getCurrentAnimation().getName().equals("stomp") ? Status.RUNNING : Status.SUCCESS;
			}else if(identifier.equals("shoot")){
				if(playerDistance > Properties.getFloat("garbageman.shoot.distance", 10f) && !world.getPlayer().isDead()){
					setWeaponClass(false);
					self.attack(world.getPlayer().getPosition().cpy().add(0, 1f).sub(self.getPosition()).nor(), world);
				}else
					return Status.FAILURE;
				handler.render(dt);
				return Status.SUCCESS;
			}else{ //GMMove
				if(time > TOTAL_TIME_DIRECTION){
					String currentAnimation = (String) context.getVariable(CURRENT);
					boolean isRight = currentAnimation == null || currentAnimation.equals("walkLeft");
					String newAnimation = isRight ? "walkRight" : "walkLeft";
					context.setVariable(CURRENT, newAnimation);
					handler.applyCurrentAnimation(handler.getAnimations().getAnimation(newAnimation), 0f);
					time = 0f;
				}
				context.setVariable(TIME_MOVED_DIRECTION, time);
				//if we got here, the last node in the tree, we want to update handler and return success
				handler.render(dt);
				return Status.SUCCESS;
			}
		}
	}

	protected void internalTerminate() {}
	protected void restoreState(jbt.execution.core.ITaskState state) {}

	protected jbt.execution.core.ITaskState storeState() {
		return null;
	}

	protected jbt.execution.core.ITaskState storeTerminationState() {
		return null;
	}
}