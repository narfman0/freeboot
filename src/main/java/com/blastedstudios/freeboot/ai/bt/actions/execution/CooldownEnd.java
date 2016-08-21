// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 10/19/2014 09:06:38
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.actions.execution;

import com.blastedstudios.gdxworld.util.Log;

/** ExecutionAction class created from MMPM action CooldownEnd. */
public class CooldownEnd extends jbt.execution.task.leaf.action.ExecutionAction {
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
	 * Constructor. Constructs an instance of CooldownEnd that is able to run a
	 * com.blastedstudios.freeboot.ai.bt.actions.CooldownEnd.
	 * 
	 * @param identifier
	 *            value of the parameter "identifier", or null in case it should
	 *            be read from the context. If null,
	 *            <code>identifierLoc<code> cannot be null.
	 * @param identifierLoc
	 *            in case <code>identifier</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 */
	public CooldownEnd(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent,
			java.lang.String identifier, java.lang.String identifierLoc) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.freeboot.ai.bt.actions.CooldownEnd)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.freeboot.ai.bt.actions.CooldownEnd");
		}

		this.identifier = identifier;
		this.identifierLoc = identifierLoc;
	}

	/**
	 * Returns the value of the parameter "identifier", or null in case it has
	 * not been specified or it cannot be found in the context.
	 */
	public java.lang.String getIdentifier() {
		if (this.identifier != null) {
			return this.identifier;
		} else {
			return (java.lang.String) this.getContext().getVariable(
					this.identifierLoc);
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Log.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected Status internalTick() {
		Long endTime = (Long) getContext().getVariable(getIdentifier());
		if(System.currentTimeMillis() - endTime > 0)
			return Status.SUCCESS;
		return Status.RUNNING;
	}

	protected void internalTerminate() {}
	protected void restoreState(jbt.execution.core.ITaskState state) {}

	protected jbt.execution.core.ITaskState storeState() {
		return null;
	}

	protected jbt.execution.core.ITaskState storeTerminationState() {
		return null;
	}
}