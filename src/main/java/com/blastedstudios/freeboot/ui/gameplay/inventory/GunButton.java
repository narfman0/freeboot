package com.blastedstudios.freeboot.ui.gameplay.inventory;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.world.weapon.Weapon;

public class GunButton extends ImageButton {
	private static final float INVENTORY_GUN_DIMENSION_X = Properties.getFloat("inventory.gun.dimension.x", 64f),
			INVENTORY_GUN_DIMENSION_Y = Properties.getFloat("inventory.gun.dimension.y", 64f);
	public final Weapon gun;
	
	public GunButton(final Skin skin, final AssetManager sharedAssets, 
			final Weapon gun, final IButtonClicked clickedListener) {
		super(new SpriteDrawable(createGunSprite(sharedAssets, gun.getResource())));
		this.gun = gun;
		addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				clickedListener.gunButtonClicked(gun);
			}
		});
		setWidth(INVENTORY_GUN_DIMENSION_X);
		setHeight(INVENTORY_GUN_DIMENSION_Y);
	}

	static Sprite createGunSprite(AssetManager sharedAssets, String resource){
		String assetPath = "data/textures/weapons/" + resource + ".png";
		Sprite sprite = null;
		try{
			sprite = new Sprite(sharedAssets.get(assetPath, Texture.class));
		}catch(Exception e){
			final Texture EMPTY = new Texture(1, 1, Format.RGBA4444);
			sprite = new Sprite(EMPTY);
			Log.error("GunButton.createGunSprite", "Failed to create sprite with resource: " + resource + " " + e.getMessage());
		}
		float scale = Math.min(1f, Math.min(INVENTORY_GUN_DIMENSION_Y / sprite.getHeight(), 
											INVENTORY_GUN_DIMENSION_X / sprite.getWidth()));
		sprite.setSize(sprite.getWidth() * scale, sprite.getHeight() * scale);
		return sprite;
	}
	
	public interface IButtonClicked{
		void gunButtonClicked(Weapon gun);
	}
}
