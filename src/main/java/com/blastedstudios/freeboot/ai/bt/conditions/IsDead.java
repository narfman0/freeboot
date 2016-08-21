// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 01/25/2013 21:35:07
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.conditions;

/** ModelCondition class created from MMPM condition IsDead. */
public class IsDead extends jbt.model.task.leaf.condition.ModelCondition {

	/** Constructor. Constructs an instance of IsDead. */
	public IsDead(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a com.blastedstudios.freeboot.ai.bt.conditions.execution.IsDead task
	 * that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.blastedstudios.freeboot.ai.bt.conditions.execution.IsDead(
				this, executor, parent);
	}
}