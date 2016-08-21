// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 10/19/2014 08:49:28
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.actions;

/** ModelAction class created from MMPM action Action. */
public class Action extends jbt.model.task.leaf.action.ModelAction {
	/**
	 * Value of the parameter "type" in case its value is specified at
	 * construction time. null otherwise.
	 */
	private java.lang.String type;
	/**
	 * Location, in the context, of the parameter "type" in case its value is
	 * not specified at construction time. null otherwise.
	 */
	private java.lang.String typeLoc;

	/**
	 * Constructor. Constructs an instance of Action.
	 * 
	 * @param type
	 *            value of the parameter "type", or null in case it should be
	 *            read from the context. If null, <code>typeLoc</code> cannot be
	 *            null.
	 * @param typeLoc
	 *            in case <code>type</code> is null, this variable represents
	 *            the place in the context where the parameter's value will be
	 *            retrieved from.
	 */
	public Action(jbt.model.core.ModelTask guard, java.lang.String type,
			java.lang.String typeLoc) {
		super(guard);
		this.type = type;
		this.typeLoc = typeLoc;
	}

	/**
	 * Returns a com.blastedstudios.freeboot.ai.bt.actions.execution.Action task
	 * that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.blastedstudios.freeboot.ai.bt.actions.execution.Action(
				this, executor, parent, this.type, this.typeLoc);
	}
}