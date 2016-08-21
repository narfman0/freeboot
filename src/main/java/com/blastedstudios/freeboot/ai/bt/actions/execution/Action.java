// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 10/19/2014 08:49:28
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.actions.execution;

import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.freeboot.world.being.INPCActionExecutor;

/** ExecutionAction class created from MMPM action Action. */
public class Action extends jbt.execution.task.leaf.action.ExecutionAction {
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
	 * Constructor. Constructs an instance of Attack that is able to run a
	 * com.blastedstudios.freeboot.ai.bt.actions.Attack.
	 * 
	 * @param type
	 *            value of the parameter "type", or null in case it should be
	 *            read from the context. If null,
	 *            <code>typeLoc<code> cannot be null.
	 * @param typeLoc
	 *            in case <code>type</code> is null, this variable represents
	 *            the place in the context where the parameter's value will be
	 *            retrieved from.
	 */
	public Action(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent, java.lang.String type,
			java.lang.String typeLoc) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.freeboot.ai.bt.actions.Action)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.freeboot.ai.bt.actions.Action");
		}

		this.type = type;
		this.typeLoc = typeLoc;
	}

	/**
	 * Returns the value of the parameter "type", or null in case it has not
	 * been specified or it cannot be found in the context.
	 */
	public java.lang.String getType() {
		if (this.type != null) {
			return this.type;
		} else {
			return (java.lang.String) this.getContext().getVariable(
					this.typeLoc);
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Log.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		INPCActionExecutor executor = (INPCActionExecutor) getContext().getVariable(INPCActionExecutor.EXECUTE_CONTEXT_NAME);
		return executor.execute(getType());
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