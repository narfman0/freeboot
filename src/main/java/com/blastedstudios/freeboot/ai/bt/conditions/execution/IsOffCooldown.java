// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 10/26/2014 08:40:17
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.conditions.execution;

import com.blastedstudios.gdxworld.util.Log;

/** ExecutionCondition class created from MMPM condition IsOffCooldown. */
public class IsOffCooldown extends
		jbt.execution.task.leaf.condition.ExecutionCondition {
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
	 * Constructor. Constructs an instance of IsOffCooldown that is able to run a
	 * com.blastedstudios.freeboot.ai.bt.conditions.IsOffCooldown.
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
	public IsOffCooldown(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent,
			java.lang.String identifier, java.lang.String identifierLoc) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.freeboot.ai.bt.conditions.IsOffCooldown)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.freeboot.ai.bt.conditions.IsOffCooldown");
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
		Object variable = getContext().getVariable(getIdentifier()); 
		// if cooldown isn't set yet, it must not be on cooldown!
		if(variable != null){
			// check the time
			Long endTime = (Long) variable;
			if(System.currentTimeMillis() - endTime < 0)
				return Status.FAILURE;
		}
		return Status.SUCCESS;
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