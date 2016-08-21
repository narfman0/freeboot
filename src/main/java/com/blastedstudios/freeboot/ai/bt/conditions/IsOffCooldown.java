// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 10/26/2014 08:40:17
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.conditions;

/** ModelCondition class created from MMPM condition IsOffCooldown. */
public class IsOffCooldown extends jbt.model.task.leaf.condition.ModelCondition {
	/**
	 * Value of the parameter "identifier" in case its value is specified at
	 * construction time. null otherwise.
	 */
	private java.lang.String identifier;
	/**
	 * Location, in the context, of the parameter "identifier" in case its value
	 * is not specified at construction time. null otherwise.
	 */
	private java.lang.String identifierLoc;

	/**
	 * Constructor. Constructs an instance of IsOffCooldown.
	 * 
	 * @param identifier
	 *            value of the parameter "identifier", or null in case it should
	 *            be read from the context. If null, <code>identifierLoc</code>
	 *            cannot be null.
	 * @param identifierLoc
	 *            in case <code>identifier</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 */
	public IsOffCooldown(jbt.model.core.ModelTask guard,
			java.lang.String identifier, java.lang.String identifierLoc) {
		super(guard);
		this.identifier = identifier;
		this.identifierLoc = identifierLoc;
	}

	/**
	 * Returns a com.blastedstudios.freeboot.ai.bt.conditions.execution.IsOffCooldown
	 * task that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.blastedstudios.freeboot.ai.bt.conditions.execution.IsOffCooldown(
				this, executor, parent, this.identifier, this.identifierLoc);
	}
}