// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 10/19/2014 08:49:28
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.actions;

/** ModelAction class created from MMPM action Drop. */
public class Drop extends jbt.model.task.leaf.action.ModelAction {

	/** Constructor. Constructs an instance of Drop. */
	public Drop(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a com.blastedstudios.freeboot.ai.bt.actions.execution.Drop task that
	 * is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.blastedstudios.freeboot.ai.bt.actions.execution.Drop(this,
				executor, parent);
	}
}