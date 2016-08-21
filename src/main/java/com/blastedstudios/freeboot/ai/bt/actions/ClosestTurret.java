// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 05/08/2014 20:29:40
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.actions;

/** ModelAction class created from MMPM action ClosestTurret. */
public class ClosestTurret extends jbt.model.task.leaf.action.ModelAction {

	/** Constructor. Constructs an instance of ClosestTurret. */
	public ClosestTurret(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a com.blastedstudios.freeboot.ai.bt.actions.execution.ClosestTurret
	 * task that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.blastedstudios.freeboot.ai.bt.actions.execution.ClosestTurret(
				this, executor, parent);
	}
}