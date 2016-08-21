// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 06/01/2013 13:14:16
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.actions.execution;

import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.util.VisibilityReturnStruct;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.NPC;
import com.blastedstudios.freeboot.world.being.NPC.AIFieldEnum;

/** ExecutionAction class created from MMPM action Search. */
public class Search extends jbt.execution.task.leaf.action.ExecutionAction {
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
	 * Constructor. Constructs an instance of Search that is able to run a
	 * com.blastedstudios.freeboot.ai.bt.actions.Search.
	 * 
	 * @param lastTarget
	 *            value of the parameter "lastTarget", or null in case it should
	 *            be read from the context. If null,
	 *            <code>lastTargetLoc<code> cannot be null.
	 * @param lastTargetLoc
	 *            in case <code>lastTarget</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 * @param lastTime
	 *            value of the parameter "lastTime", or null in case it should
	 *            be read from the context. If null,
	 *            <code>lastTimeLoc<code> cannot be null.
	 * @param lastTimeLoc
	 *            in case <code>lastTime</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 */
	public Search(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent, float[] lastTarget,
			java.lang.String lastTargetLoc, java.lang.Integer lastTime,
			java.lang.String lastTimeLoc) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.freeboot.ai.bt.actions.Search)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.freeboot.ai.bt.actions.Search");
		}

		this.lastTarget = lastTarget;
		this.lastTargetLoc = lastTargetLoc;
		this.lastTime = lastTime;
		this.lastTimeLoc = lastTimeLoc;
	}

	/**
	 * Returns the value of the parameter "lastTarget", or null in case it has
	 * not been specified or it cannot be found in the context.
	 */
	public float[] getLastTarget() {
		if (this.lastTarget != null) {
			return this.lastTarget;
		} else {
			return (float[]) this.getContext().getVariable(this.lastTargetLoc);
		}
	}

	/**
	 * Returns the value of the parameter "lastTime", or null in case it has not
	 * been specified or it cannot be found in the context.
	 */
	public java.lang.Integer getLastTime() {
		if (this.lastTime != null) {
			return this.lastTime;
		} else {
			return (java.lang.Integer) this.getContext().getVariable(
					this.lastTimeLoc);
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Log.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		Log.debug(this.getClass().getCanonicalName(), "ticked");
		int searchTime = Properties.getInt("npc.search.time", 5000);
		if(getLastTime() == null || (int)System.currentTimeMillis() - getLastTime() > searchTime)
			return Status.FAILURE;
		WorldManager world = (WorldManager) getContext().getVariable(AIFieldEnum.WORLD.name());
		VisibilityReturnStruct struct = world.isVisible((NPC) getContext().getVariable(AIFieldEnum.SELF.name()));
		if(struct.isVisible())
			return Status.SUCCESS;
		getContext().setVariable("SearchLocation", getLastTarget());
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