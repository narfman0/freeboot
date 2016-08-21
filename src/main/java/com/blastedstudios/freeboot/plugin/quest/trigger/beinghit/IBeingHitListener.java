package com.blastedstudios.freeboot.plugin.quest.trigger.beinghit;

import com.blastedstudios.freeboot.world.weapon.DamageStruct;

public interface IBeingHitListener {
	void beingHit(DamageStruct damageStruct);
}
