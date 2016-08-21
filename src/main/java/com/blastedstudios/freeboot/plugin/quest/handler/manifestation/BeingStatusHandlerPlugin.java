package com.blastedstudios.freeboot.plugin.quest.handler.manifestation;

import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.freeboot.plugin.quest.handler.IWorldManagerInitializer;
import com.blastedstudios.freeboot.plugin.quest.manifestation.beingstatus.IBeingStatusHandler;
import com.blastedstudios.freeboot.util.VectorHelper;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;

@PluginImplementation
public class BeingStatusHandlerPlugin implements IBeingStatusHandler, IWorldManagerInitializer{
	private WorldManager world;
	
	@Override public void setWorldManager(WorldManager world){
		this.world = world;
	}

	@Override
	public CompletionEnum statusBeing(String beingName, float dmg, boolean kill,
			String textureAtlas, boolean remove, boolean doAim, float aim, String attackTarget) {
		List<Being> targets = world.matchBeings(beingName);
		for(Being target : targets){
			if (dmg > 0f)
				target.setHp(target.getHp() - dmg);
			if(kill)
				target.setHp(0f);
			if(textureAtlas != null && !textureAtlas.isEmpty())
				target.setResource(textureAtlas);
			if(remove)
				world.dispose(target);
			if(doAim)
				target.aim(aim);
			if(!attackTarget.equals("")){
				Vector2 direction;
				try{
					direction = VectorHelper.parse(attackTarget);
				}catch(Exception e){
					direction = world.matchBeings(attackTarget).get(0).getPosition();
				}
				if(direction == null)
					Gdx.app.error("BeingStatusHandlerPlugin.statusBeing", "Direction null for: " + attackTarget);
				else
					target.attack(direction, world);
			}
		}
		Log.log("BeingStatusHandlerPlugin.statusBeing", "Status change for " + beingName);
		return CompletionEnum.COMPLETED;
	}
}
