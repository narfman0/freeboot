// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 10/19/2014 09:44:25
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.actions;

/** ModelAction class created from MMPM action Jump. */
public class Jump extends jbt.model.task.leaf.action.ModelAction {
	/**
	 * Value of the parameter "towardPlayer" in case its value is specified at
	 * construction time. null otherwise.
	 */
	private java.lang.Boolean towardPlayer;
	/**
	 * Location, in the context, of the parameter "towardPlayer" in case its
	 * value is not specified at construction time. null otherwise.
	 */
	private java.lang.String towardPlayerLoc;

	/**
	 * Constructor. Constructs an instance of Jump.
	 * 
	 * @param towardPlayer
	 *            value of the parameter "towardPlayer", or null in case it
	 *            should be read from the context. If null,
	 *            <code>towardPlayerLoc</code> cannot be null.
	 * @param towardPlayerLoc
	 *            in case <code>towardPlayer</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 */
	public Jump(jbt.model.core.ModelTask guard, java.lang.Boolean towardPlayer,
			java.lang.String towardPlayerLoc) {
		super(guard);
		this.towardPlayer = towardPlayer;
		this.towardPlayerLoc = towardPlayerLoc;
	}

	/**
	 * Returns a com.blastedstudios.freeboot.ai.bt.actions.execution.Jump task that
	 * is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.blastedstudios.freeboot.ai.bt.actions.execution.Jump(this,
				executor, parent, this.towardPlayer, this.towardPlayerLoc);
	}
}