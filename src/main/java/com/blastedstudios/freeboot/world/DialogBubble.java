package com.blastedstudios.freeboot.world;

import java.util.LinkedList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.world.DialogManager.DialogStruct;
import com.blastedstudios.freeboot.world.being.Being;

public class DialogBubble {
	private final Sprite dialogBubbleSprite;
	private final SpriteBatch spriteBatch = new SpriteBatch();

	public DialogBubble(AssetManager assetManager, Skin skin){
		dialogBubbleSprite = new Sprite(assetManager.get("data/textures/dialogBubble.png", Texture.class));
	}

	public void render(AssetManager assetManager, WorldManager world, DialogManager dialog, OrthographicCamera camera){
		if(!Properties.getBool("dialog.bubble.enable", true))
			return;
		DialogStruct current = dialog.peek();
		if(current == null)
			return;
		LinkedList<Vector2> renderLocations = new LinkedList<>();
		for(String nameRaw : current.portrait.split(",")){
			String name = extractName(nameRaw);
			if(name.equals("player"))
				if(world.getPlayer().getPosition() != null) //when player isn't spawned?
					renderLocations.add(world.getPlayer().getPosition());
			else
				for(Being npc : world.getAllBeings().values())
					if(extractName(npc.getName()).equals(name) || name.equals("player"))
						renderLocations.add(npc.getPosition());
		}

		dialogBubbleSprite.setScale(camera.zoom);
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		for(Vector2 position : renderLocations){
			Vector2 offset = new Vector2(Properties.getFloat("dialog.bubble.offset.x", 0f), 
										 Properties.getFloat("dialog.bubble.offset.y", .85f));
			dialogBubbleSprite.setPosition(offset.x + position.x - dialogBubbleSprite.getWidth()/2,
										   offset.y + position.y - dialogBubbleSprite.getHeight()/2);
			dialogBubbleSprite.draw(spriteBatch);
		}
		spriteBatch.end();
	}
	
	private static String extractName(String name){
		return name.toLowerCase().replaceAll(" ", "");
	}
}
