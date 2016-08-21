// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 10/19/2014 08:52:01
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.conditions;

/** ModelCondition class created from MMPM condition PlayerLeft. */
public class PlayerLeft extends jbt.model.task.leaf.condition.ModelCondition {

	/** Constructor. Constructs an instance of PlayerLeft. */
	public PlayerLeft(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a com.blastedstudios.freeboot.ai.bt.conditions.execution.PlayerLeft
	 * task that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.blastedstudios.freeboot.ai.bt.conditions.execution.PlayerLeft(
				this, executor, parent);
	}
}