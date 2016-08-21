package com.blastedstudios.freeboot.world.being.component.damagesound;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.plugin.quest.handler.ISharedAssetConsumer;
import com.blastedstudios.freeboot.world.being.component.AbstractComponent;
import com.blastedstudios.freeboot.world.weapon.DamageStruct;

@PluginImplementation
public class DamageSoundComponent extends AbstractComponent implements ISharedAssetConsumer{
	private AssetManager assets;
	
	@Override public void receivedDamage(DamageStruct damage){
		if(damage == null || damage.getOrigin() == null)
			return;
		assets.get("data/sounds/thud004.mp3", Sound.class).play(Properties.getFloat("sound.volume", 1f));
	}

	@Override public void setAssets(AssetManager assets) {
		this.assets = assets;
	}
}
