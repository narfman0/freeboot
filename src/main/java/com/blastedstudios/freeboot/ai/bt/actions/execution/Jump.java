// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 10/19/2014 09:44:25
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.actions.execution;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.NPC;
import com.blastedstudios.freeboot.world.being.NPC.AIFieldEnum;

/** ExecutionAction class created from MMPM action Jump. */
public class Jump extends jbt.execution.task.leaf.action.ExecutionAction {
	/**
	 * Value of the parameter "towardPlayer" in case its value is specified at
	 * construction time. null otherwise.
	 */
	private java.lang.Boolean towardPlayer;
	/**
	 * Location, in the context, of the parameter "towardPlayer" in case its
	 * value is not specified at construction time. null otherwise.
	 */
	private java.lang.String towardPlayerLoc;

	/**
	 * Constructor. Constructs an instance of Jump that is able to run a
	 * com.blastedstudios.freeboot.ai.bt.actions.Jump.
	 * 
	 * @param towardPlayer
	 *            value of the parameter "towardPlayer", or null in case it
	 *            should be read from the context. If null,
	 *            <code>towardPlayerLoc<code> cannot be null.
	 * @param towardPlayerLoc
	 *            in case <code>towardPlayer</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 */
	public Jump(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent,
			java.lang.Boolean towardPlayer, java.lang.String towardPlayerLoc) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.freeboot.ai.bt.actions.Jump)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.freeboot.ai.bt.actions.Jump");
		}

		this.towardPlayer = towardPlayer;
		this.towardPlayerLoc = towardPlayerLoc;
	}

	/**
	 * Returns the value of the parameter "towardPlayer", or null in case it has
	 * not been specified or it cannot be found in the context.
	 */
	public java.lang.Boolean getTowardPlayer() {
		if (this.towardPlayer != null) {
			return this.towardPlayer;
		} else {
			return (java.lang.Boolean) this.getContext().getVariable(
					this.towardPlayerLoc);
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Log.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		NPC self = (NPC) getContext().getVariable(AIFieldEnum.SELF.name());
		WorldManager world = (WorldManager) getContext().getVariable(AIFieldEnum.WORLD.name());
		Vector2 jumpVector = new Vector2(0, 90f);
		if(getTowardPlayer() != null){
			boolean playerLeft = world.getClosestBeing(self, false, false).getPosition().x < self.getPosition().x;
			jumpVector.x = 200f;
			boolean moveLeft = (getTowardPlayer() && playerLeft) || (!getTowardPlayer() && !playerLeft);
			if(moveLeft)
				jumpVector.x *= -1f;
			self.setMoveLeft(moveLeft);
			self.setMoveRight(!moveLeft);
		}
		self.jumpImmediate(jumpVector);
		return jbt.execution.core.ExecutionTask.Status.SUCCESS;
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