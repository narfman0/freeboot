// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 10/12/2015 22:55:10
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.actions.execution;

import java.util.Queue;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.world.GDXPath;
import com.blastedstudios.gdxworld.world.GDXPath.CompletionEnum;
import com.blastedstudios.freeboot.ai.AIWorld;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.being.NPC;
import com.blastedstudios.freeboot.world.being.NPC.AIFieldEnum;

/** ExecutionAction class created from MMPM action ClosestEnemy. */
public class ClosestEnemy extends
		jbt.execution.task.leaf.action.ExecutionAction {
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
	 * Constructor. Constructs an instance of ClosestEnemy that is able to run a
	 * com.blastedstudios.freeboot.ai.bt.actions.ClosestEnemy.
	 * 
	 * @param contextName
	 *            value of the parameter "contextName", or null in case it
	 *            should be read from the context. If null,
	 *            <code>contextNameLoc<code> cannot be null.
	 * @param contextNameLoc
	 *            in case <code>contextName</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 */
	public ClosestEnemy(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent,
			java.lang.String contextName, java.lang.String contextNameLoc) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.freeboot.ai.bt.actions.ClosestEnemy)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.freeboot.ai.bt.actions.ClosestEnemy");
		}

		this.contextName = contextName;
		this.contextNameLoc = contextNameLoc;
	}

	/**
	 * Returns the value of the parameter "contextName", or null in case it has
	 * not been specified or it cannot be found in the context.
	 */
	public java.lang.String getContextName() {
		if (this.contextName != null) {
			return this.contextName;
		} else {
			return (java.lang.String) this.getContext().getVariable(
					this.contextNameLoc);
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Log.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected Status internalTick() {
		NPC self = (NPC) getContext().getVariable(AIFieldEnum.SELF.name());
		WorldManager world = (WorldManager) getContext().getVariable(AIFieldEnum.WORLD.name());
		AIWorld aiWorld = (AIWorld) getContext().getVariable(AIFieldEnum.AI_WORLD.name());
		Being closest = world.getClosestBeing(self, false, false);
		if(closest == null)
			return Status.FAILURE;
		Queue<Vector2> path = aiWorld.getPathToPoint(self.getPosition(), closest.getPosition());
		GDXPath gdxPath = new GDXPath();
		gdxPath.getNodes().addAll(path);
		gdxPath.setCompletionCriteria(CompletionEnum.END);
		getContext().setVariable(getContextName(), gdxPath);
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