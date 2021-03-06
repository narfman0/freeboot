package com.blastedstudios.freeboot.world.activity;

import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.network.Messages.Respawn;
import com.blastedstudios.freeboot.ui.gameplay.GameplayNetReceiver;
import com.blastedstudios.freeboot.util.UUIDConvert;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;

public class ReviveActivity extends BaseActivity {
	private final Being target, self;
	private final WorldManager world;
	private final GameplayNetReceiver receiver;
	private float duration = Properties.getFloat("activity.revive.duration", 3f);
	
	public ReviveActivity(Being self, Being target, WorldManager world, GameplayNetReceiver receiver){
		this.self = self;
		this.target = target;
		this.world = world;
		this.receiver = receiver;
	}

	@Override public boolean render(float dt) {
		float reviveDistance = Properties.getFloat("activity.revive.distance");
		if(self.isDead() || self.getPosition().dst(target.getPosition()) > reviveDistance)
			// hey that last 16.6ms he coulda been knocked away, who can know
			return false;
		duration -= dt;
		if(duration <= 0f){
			target.respawn(world, target.getPosition().x, target.getPosition().y);
			Respawn.Builder builder = Respawn.newBuilder();
			builder.setUuid(UUIDConvert.convert(target.getUuid()));
			if(receiver != null)
				receiver.send(builder.build());
			return false;
		}
		return true;
	}

}
