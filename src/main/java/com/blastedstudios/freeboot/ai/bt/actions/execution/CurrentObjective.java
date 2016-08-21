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
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.world.GDXPath;
import com.blastedstudios.freeboot.world.being.NPC;
import com.blastedstudios.freeboot.world.being.NPC.AIFieldEnum;

/** ExecutionAction class created from MMPM action CurrentObjective. */
public class CurrentObjective extends
		jbt.execution.task.leaf.action.ExecutionAction {

	/**
	 * Constructor. Constructs an instance of CurrentObjective that is able to
	 * run a com.blastedstudios.freeboot.ai.bt.actions.CurrentObjective.
	 */
	public CurrentObjective(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.freeboot.ai.bt.actions.CurrentObjective)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.freeboot.ai.bt.actions.CurrentObjective");
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Log.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		Log.debug(this.getClass().getCanonicalName(), "ticked");
		GDXPath objective = (GDXPath) getContext().getVariable(AIFieldEnum.OBJECTIVE.name());
		NPC self = (NPC) getContext().getVariable(AIFieldEnum.SELF.name());
		if(objective != null && !objective.getNodes().isEmpty()){
			Vector2 target = objective.getNodes().get(0);
			getContext().setVariable("CurrentObjectiveTarget", new float[]{target.x, target.y});
			if(objective.getNodes().get(0).dst(self.getPosition()) < 1)
				objective.getNodes().remove(0);
			if(objective.getNodes().isEmpty()){
				switch(objective.getCompletionCriteria()){
				case END:
					getContext().clearVariable(AIFieldEnum.OBJECTIVE.name());
					break;
				case REPEAT:
					getContext().setVariable(AIFieldEnum.OBJECTIVE.name(), self.getPath().clone());
					break;
				case REVERSE:
					boolean reversed = getContext().getVariable("PATH_REVERSED") == null || 
							(boolean)getContext().getVariable("PATH_REVERSED");
					GDXPath clone = (GDXPath) self.getPath().clone();
					if(!reversed)
						clone.reverse();
					getContext().setVariable("PATH_REVERSED", !reversed);
					getContext().setVariable(AIFieldEnum.OBJECTIVE.name(), clone);
					break;
				}
			}
			return Status.SUCCESS;
		}else{
			getContext().clearVariable("CurrentObjectiveTarget");
			self.stopMovement();
			return Status.FAILURE;
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