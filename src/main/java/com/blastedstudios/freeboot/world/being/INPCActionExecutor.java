package com.blastedstudios.freeboot.world.being;

import jbt.execution.core.ExecutionTask.Status;

public interface INPCActionExecutor {
	public static final String EXECUTE_CONTEXT_NAME = INPCActionExecutor.class.getSimpleName() + ".context.name";
	Status execute(String identifier);
}
