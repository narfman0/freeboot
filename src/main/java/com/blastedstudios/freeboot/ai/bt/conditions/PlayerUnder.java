// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 10/19/2014 08:49:29
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.conditions;

/** ModelCondition class created from MMPM condition PlayerUnder. */
public class PlayerUnder extends jbt.model.task.leaf.condition.ModelCondition {

	/** Constructor. Constructs an instance of PlayerUnder. */
	public PlayerUnder(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a com.blastedstudios.freeboot.ai.bt.conditions.execution.PlayerUnder
	 * task that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.blastedstudios.freeboot.ai.bt.conditions.execution.PlayerUnder(
				this, executor, parent);
	}
}