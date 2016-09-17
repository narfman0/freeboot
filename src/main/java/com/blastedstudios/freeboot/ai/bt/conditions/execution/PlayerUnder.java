// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 10/19/2014 08:49:29
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.conditions.execution;

import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.being.NPC;
import com.blastedstudios.freeboot.world.being.NPC.AIFieldEnum;

/** ExecutionCondition class created from MMPM condition PlayerUnder. */
public class PlayerUnder extends
		jbt.execution.task.leaf.condition.ExecutionCondition {

	/**
	 * Constructor. Constructs an instance of PlayerUnder that is able to run a
	 * com.blastedstudios.freeboot.ai.bt.conditions.PlayerUnder.
	 */
	public PlayerUnder(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.freeboot.ai.bt.conditions.PlayerUnder)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.freeboot.ai.bt.conditions.PlayerUnder");
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
		Being target = world.getClosestBeing(self, false, false);
		return Math.abs(self.getPosition().x - target.getPosition().x) < Properties.getFloat("ai.player.under.distance", 2f) && 
				target.getPosition().y < self.getPosition().y ?
						Status.SUCCESS : Status.FAILURE;
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