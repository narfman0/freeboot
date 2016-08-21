// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 06/01/2013 13:14:15
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.actions;

/** ModelAction class created from MMPM action Search. */
public class Search extends jbt.model.task.leaf.action.ModelAction {
	/**
	 * Value of the parameter "lastTarget" in case its value is specified at
	 * construction time. null otherwise.
	 */
	private float[] lastTarget;
	/**
	 * Location, in the context, of the parameter "lastTarget" in case its value
	 * is not specified at construction time. null otherwise.
	 */
	private java.lang.String lastTargetLoc;
	/**
	 * Value of the parameter "lastTime" in case its value is specified at
	 * construction time. null otherwise.
	 */
	private java.lang.Integer lastTime;
	/**
	 * Location, in the context, of the parameter "lastTime" in case its value
	 * is not specified at construction time. null otherwise.
	 */
	private java.lang.String lastTimeLoc;

	/**
	 * Constructor. Constructs an instance of Search.
	 * 
	 * @param lastTarget
	 *            value of the parameter "lastTarget", or null in case it should
	 *            be read from the context. If null, <code>lastTargetLoc</code>
	 *            cannot be null.
	 * @param lastTargetLoc
	 *            in case <code>lastTarget</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 * @param lastTime
	 *            value of the parameter "lastTime", or null in case it should
	 *            be read from the context. If null, <code>lastTimeLoc</code>
	 *            cannot be null.
	 * @param lastTimeLoc
	 *            in case <code>lastTime</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 */
	public Search(jbt.model.core.ModelTask guard, float[] lastTarget,
			java.lang.String lastTargetLoc, java.lang.Integer lastTime,
			java.lang.String lastTimeLoc) {
		super(guard);
		this.lastTarget = lastTarget;
		this.lastTargetLoc = lastTargetLoc;
		this.lastTime = lastTime;
		this.lastTimeLoc = lastTimeLoc;
	}

	/**
	 * Returns a com.blastedstudios.freeboot.ai.bt.actions.execution.Search task
	 * that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.blastedstudios.freeboot.ai.bt.actions.execution.Search(
				this, executor, parent, this.lastTarget, this.lastTargetLoc,
				this.lastTime, this.lastTimeLoc);
	}
}