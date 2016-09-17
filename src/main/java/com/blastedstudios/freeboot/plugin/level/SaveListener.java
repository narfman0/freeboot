package com.blastedstudios.freeboot.plugin.level;

import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.freeboot.util.SaveHelper;
import com.blastedstudios.freeboot.world.WorldManager;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class SaveListener implements ILevelCompletedListener {
	@Override public void levelComplete(boolean success, WorldManager world, GDXLevel level) {
		if(world.getPlayer() == null)
			return;
		//cant always setLevelCompleted(...,success), because if player 
		//previously beats a level then replays and loses, we don't want to 
		//make it so he can't play any later levels again
		if(success) 
			world.getPlayer().setLevelCompleted(level.getName(), true);
		SaveHelper.save(world.getPlayer());
	}
}
