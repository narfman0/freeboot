// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 10/19/2014 09:10:19
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.actions;

/** ModelAction class created from MMPM action CooldownStart. */
public class CooldownStart extends jbt.model.task.leaf.action.ModelAction {
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
	 * Value of the parameter "duration" in case its value is specified at
	 * construction time. null otherwise.
	 */
	private java.lang.Integer duration;
	/**
	 * Location, in the context, of the parameter "duration" in case its value
	 * is not specified at construction time. null otherwise.
	 */
	private java.lang.String durationLoc;

	/**
	 * Constructor. Constructs an instance of CooldownStart.
	 * 
	 * @param identifier
	 *            value of the parameter "identifier", or null in case it should
	 *            be read from the context. If null, <code>identifierLoc</code>
	 *            cannot be null.
	 * @param identifierLoc
	 *            in case <code>identifier</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 * @param duration
	 *            value of the parameter "duration", or null in case it should
	 *            be read from the context. If null, <code>durationLoc</code>
	 *            cannot be null.
	 * @param durationLoc
	 *            in case <code>duration</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 */
	public CooldownStart(jbt.model.core.ModelTask guard,
			java.lang.String identifier, java.lang.String identifierLoc,
			java.lang.Integer duration, java.lang.String durationLoc) {
		super(guard);
		this.identifier = identifier;
		this.identifierLoc = identifierLoc;
		this.duration = duration;
		this.durationLoc = durationLoc;
	}

	/**
	 * Returns a com.blastedstudios.freeboot.ai.bt.actions.execution.CooldownStart
	 * task that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.blastedstudios.freeboot.ai.bt.actions.execution.CooldownStart(
				this, executor, parent, this.identifier, this.identifierLoc,
				this.duration, this.durationLoc);
	}
}