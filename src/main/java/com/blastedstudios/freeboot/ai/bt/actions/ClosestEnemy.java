// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 10/12/2015 22:55:10
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.actions;

/** ModelAction class created from MMPM action ClosestEnemy. */
public class ClosestEnemy extends jbt.model.task.leaf.action.ModelAction {
	/**
	 * Value of the parameter "contextName" in case its value is specified at
	 * construction time. null otherwise.
	 */
	private java.lang.String contextName;
	/**
	 * Location, in the context, of the parameter "contextName" in case its
	 * value is not specified at construction time. null otherwise.
	 */
	private java.lang.String contextNameLoc;

	/**
	 * Constructor. Constructs an instance of ClosestEnemy.
	 * 
	 * @param contextName
	 *            value of the parameter "contextName", or null in case it
	 *            should be read from the context. If null,
	 *            <code>contextNameLoc</code> cannot be null.
	 * @param contextNameLoc
	 *            in case <code>contextName</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 */
	public ClosestEnemy(jbt.model.core.ModelTask guard,
			java.lang.String contextName, java.lang.String contextNameLoc) {
		super(guard);
		this.contextName = contextName;
		this.contextNameLoc = contextNameLoc;
	}

	/**
	 * Returns a com.blastedstudios.freeboot.ai.bt.actions.execution.ClosestEnemy
	 * task that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.blastedstudios.freeboot.ai.bt.actions.execution.ClosestEnemy(
				this, executor, parent, this.contextName, this.contextNameLoc);
	}
}