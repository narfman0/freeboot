// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 01/25/2013 21:35:07
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.conditions.execution;

import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.freeboot.util.VisibilityReturnStruct;
import com.blastedstudios.freeboot.world.being.NPC;
import com.blastedstudios.freeboot.world.being.NPC.AIFieldEnum;
import com.blastedstudios.freeboot.world.weapon.DamageStruct;

/** ExecutionCondition class created from MMPM condition Danger. */
public class Danger extends
		jbt.execution.task.leaf.condition.ExecutionCondition {

	/**
	 * Constructor. Constructs an instance of Danger that is able to run a
	 * com.blastedstudios.freeboot.ai.bt.conditions.Danger.
	 */
	public Danger(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.freeboot.ai.bt.conditions.Danger)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.freeboot.ai.bt.conditions.Danger");
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Log.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		Log.debug(this.getClass().getCanonicalName(), "ticked");
		NPC self = (NPC) getContext().getVariable(AIFieldEnum.SELF.name());
		// figure out, upon receiving damage, where origin is
		DamageStruct shotDamage = (DamageStruct) getContext().getVariable(AIFieldEnum.ATTACK_TICK.name());
		if(shotDamage != null && shotDamage.getOrigin() != null){
			getContext().setVariable(NPC.AIFieldEnum.ALERT.name(), shotDamage.getOrigin());
			self.aim(shotDamage.getOrigin());
			getContext().clearVariable(AIFieldEnum.ATTACK_TICK.name());
			float[] target = new float[]{shotDamage.getOrigin().getPosition().x, shotDamage.getOrigin().getPosition().y};
			getContext().setVariable("DangerTarget", target);
			getContext().setVariable("DangerLastTime", (int)System.currentTimeMillis());
		}
		
		VisibilityReturnStruct struct = (VisibilityReturnStruct) getContext().getVariable(NPC.AIFieldEnum.VISIBLE.name());
		if(struct != null && struct.isVisible()){
			getContext().setVariable("DangerTarget", struct.target);
			getContext().setVariable("DangerLastTime", (int)System.currentTimeMillis());
			return Status.SUCCESS;
		}
		return Status.FAILURE;
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