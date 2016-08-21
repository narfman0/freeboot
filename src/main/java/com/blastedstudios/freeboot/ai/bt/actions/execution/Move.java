// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 01/25/2013 21:35:07
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.actions.execution;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.freeboot.physics.VisibleQueryCallback;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.NPC;
import com.blastedstudios.freeboot.world.being.NPC.AIFieldEnum;
import com.blastedstudios.freeboot.world.being.component.IComponent;
import com.blastedstudios.freeboot.world.being.component.jetpack.JetpackComponent;

/** ExecutionAction class created from MMPM action Move. */
public class Move extends jbt.execution.task.leaf.action.ExecutionAction {
	/**
	 * Value of the parameter "target" in case its value is specified at
	 * construction time. null otherwise.
	 */
	private float[] target;
	/**
	 * Location, in the context, of the parameter "target" in case its value is
	 * not specified at construction time. null otherwise.
	 */
	private java.lang.String targetLoc;

	/**
	 * Constructor. Constructs an instance of Move that is able to run a
	 * com.blastedstudios.freeboot.ai.bt.actions.Move.
	 * 
	 * @param target
	 *            value of the parameter "target", or null in case it should be
	 *            read from the context. If null,
	 *            <code>targetLoc<code> cannot be null.
	 * @param targetLoc
	 *            in case <code>target</code> is null, this variable represents
	 *            the place in the context where the parameter's value will be
	 *            retrieved from.
	 */
	public Move(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent, float[] target,
			java.lang.String targetLoc) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.freeboot.ai.bt.actions.Move)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.freeboot.ai.bt.actions.Move");
		}

		this.target = target;
		this.targetLoc = targetLoc;
	}

	/**
	 * Returns the value of the parameter "target", or null in case it has not
	 * been specified or it cannot be found in the context.
	 */
	public float[] getTarget() {
		if (this.target != null) {
			return this.target;
		} else {
			return (float[]) this.getContext().getVariable(this.targetLoc);
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Log.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		Log.debug(this.getClass().getCanonicalName(), "ticked");
		WorldManager world = (WorldManager) getContext().getVariable(AIFieldEnum.WORLD.name());
		NPC self = (NPC) getContext().getVariable(AIFieldEnum.SELF.name());
		if(getTarget() == null){
			Log.debug(this.getClass().getCanonicalName() + ".internalTick", "Null target for " + self.toString());
			self.stopMovement();
			return Status.FAILURE;
		}
		Vector2 target = new Vector2(getTarget()[0], getTarget()[1]);
		
		//When close, return early. Especially for operating turrets, no more jumping and moving all the time.
		if(target.dst(self.getPosition()) < .5f){
			self.setMoveRight(false);
			self.setMoveLeft(false);
			return Status.SUCCESS;
		}
		
		if(self.getPosition().x < target.x){
			self.setMoveRight(true);
			self.setMoveLeft(false);
		}else if(self.getPosition().x > target.x){
			self.setMoveRight(false);
			self.setMoveLeft(true);
		}

		boolean shouldJump = shouldJump(self, world.getWorld(), target); 
		self.setJump(shouldJump);
		
		for(IComponent component : self.getListeners())
			if(component instanceof JetpackComponent){
				JetpackComponent jetpack = (JetpackComponent) component; 
				if(self.getRagdoll().getLinearVelocity().y < 4f || //up too fast 
						self.getRagdoll().getLinearVelocity().y < -4f){ //falling too fast
					jetpack.setJetpackActivated(shouldJump);
				}else
					jetpack.setJetpackActivated(false);
			}
		return Status.SUCCESS;
	}
	
	private boolean shouldJump(NPC self, World world, Vector2 target){
		boolean up = self.getPosition().y+1f < target.y;
		if(self.getStats().getJetpackImpulse() > 100f)
			return up;
		//diagonally ahead of npc
		Vector2 groundJumpTarget = self.getPosition().cpy().add(self.getRagdoll().isFacingLeft() ? -1f : 1f, -1f);
		VisibleQueryCallback callback = new VisibleQueryCallback(self, self);
		world.rayCast(callback, self.getPosition(), groundJumpTarget);
		up |= !callback.called;
		//directly ahead of npc
		callback = new VisibleQueryCallback(self, self);
		groundJumpTarget = self.getPosition().cpy().add(self.getRagdoll().isFacingLeft() ? -1f : 1f, 0f);
		world.rayCast(callback, self.getPosition(), groundJumpTarget);
		up |= callback.called;
		return up;
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