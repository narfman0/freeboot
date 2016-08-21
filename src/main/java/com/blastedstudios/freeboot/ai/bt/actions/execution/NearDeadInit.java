// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 10/25/2014 15:29:55
// ******************************************************* 
package com.blastedstudios.freeboot.ai.bt.actions.execution;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.being.INPCActionExecutor;
import com.blastedstudios.freeboot.world.being.NPC;
import com.blastedstudios.freeboot.world.being.NPC.AIFieldEnum;

/** ExecutionAction class created from MMPM action NearDeadInit. */
public class NearDeadInit extends
		jbt.execution.task.leaf.action.ExecutionAction {

	/**
	 * Constructor. Constructs an instance of NearDeadInit that is able to run a
	 * com.blastedstudios.freeboot.ai.bt.actions.NearDeadInit.
	 */
	public NearDeadInit(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.freeboot.ai.bt.actions.NearDeadInit)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.freeboot.ai.bt.actions.NearDeadInit");
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Log.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected Status internalTick() {
		final NPC self = (NPC) getContext().getVariable(AIFieldEnum.SELF.name());
		final WorldManager world = (WorldManager) getContext().getVariable(AIFieldEnum.WORLD.name());
		INPCActionExecutor executor = new INPCActionExecutor() {
			@Override public Status execute(String identifier) {
				if(identifier.equals("aim")){
					float aimAngle = Being.getAimAngle(self, world.getPlayer());
					// If within 45 degrees below player, aim at him
					if(self.sees(world.getPlayer(), world.getWorld()) && aimAngle > -.75*Math.PI && aimAngle < -.25*Math.PI){
						float[] target = new float[]{world.getPlayer().getPosition().x, world.getPlayer().getPosition().y};
						getContext().setVariable("aim_target", target);
					}else
						return Status.FAILURE;
				}else if(identifier.equals("choose_weapon")){
					if(!self.sees(world.getPlayer(), world.getWorld()))
						return Status.FAILURE;
					float d = self.getPosition().dst(world.getPlayer().getPosition());
					if(d > Properties.getFloat("neardead.boss.weapon.far", 15f))
						self.setCurrentWeapon(2, world.getWorld(), false);
					else if(d > Properties.getFloat("neardead.boss.weapon.medium", 9f))
						self.setCurrentWeapon(1, world.getWorld(), false);
					else
						self.setCurrentWeapon(0, world.getWorld(), false);
				}else if(identifier.equals("path")){
					long duration = Properties.getLong("neardead.boss.direction.duration", 15000);
					float distance = Properties.getFloat("neardead.boss.player.distance", 10f);
					boolean moveLeft = System.currentTimeMillis() % duration < duration/2;
					Vector2 origin = world.getPlayer().getPosition().cpy(),
							target = origin.add(distance * (moveLeft ? -1f : 1f), distance);
					getContext().setVariable("move_target", new float[]{target.x, target.y});
				}
				return Status.SUCCESS;
			}
		};
		getContext().setVariable(INPCActionExecutor.EXECUTE_CONTEXT_NAME, executor);
		return jbt.execution.core.ExecutionTask.Status.SUCCESS;
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