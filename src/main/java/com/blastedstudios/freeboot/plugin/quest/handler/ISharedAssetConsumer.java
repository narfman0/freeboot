package com.blastedstudios.freeboot.plugin.quest.handler;

import com.badlogic.gdx.assets.AssetManager;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public interface ISharedAssetConsumer extends Plugin{
	void setAssets(AssetManager assets);
}
