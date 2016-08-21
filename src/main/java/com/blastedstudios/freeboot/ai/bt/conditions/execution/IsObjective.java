// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 10/12/2015 22:50:06
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.conditions.execution;

import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.world.GDXPath;
import com.blastedstudios.freeboot.world.being.NPC.AIFieldEnum;

/** ExecutionCondition class created from MMPM condition IsObjective. */
public class IsObjective extends
		jbt.execution.task.leaf.condition.ExecutionCondition {

	/**
	 * Constructor. Constructs an instance of IsObjective that is able to run a
	 * com.blastedstudios.freeboot.ai.bt.conditions.IsObjective.
	 */
	public IsObjective(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.freeboot.ai.bt.conditions.IsObjective)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.freeboot.ai.bt.conditions.IsObjective");
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Log.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected Status internalTick() {
		Log.debug(this.getClass().getCanonicalName(), "ticked");
		GDXPath objective = (GDXPath) getContext().getVariable(AIFieldEnum.OBJECTIVE.name());
		if(objective != null && !objective.getNodes().isEmpty())
			return Status.SUCCESS;
		return Status.FAILURE;
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