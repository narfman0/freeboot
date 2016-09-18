package com.blastedstudios.freeboot.world.being.component.renderweapon;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.world.being.Being.BodyPart;
import com.blastedstudios.freeboot.world.being.component.AbstractComponent;
import com.blastedstudios.freeboot.world.weapon.Melee;
import com.blastedstudios.freeboot.world.weapon.shot.GunShot;

@PluginImplementation
public class RenderWeaponComponent extends AbstractComponent {
	private transient Sprite weaponSprite;

	@Override public void render(float dt, Batch batch, AssetManager sharedAssets,
			GDXRenderer renderer, boolean facingLeft, boolean paused) {
		if(!being.isSpawned())
			return;
		if(being.getEquippedWeapon() != null){
			if(weaponSprite == null){
				String path = "data/textures/weapons/" + being.getEquippedWeapon().getResource() + ".png";
				if(!sharedAssets.isLoaded(path)){
					Log.log("RenderWeaponComponent.render", "Texture not loaded: " + path);
					return;
				}
				weaponSprite = new Sprite(sharedAssets.get(path, Texture.class));
				float scale = being.getEquippedWeapon() instanceof Melee ? 
						GunShot.getWeaponRenderScale() : Properties.getFloat("ragdoll.custom.scale");
				weaponSprite.setScale(scale);
			}
			Vector2 position = being.getRagdoll().getHandFacingPosition();
			float rotation = being.getRagdoll().getBodyPart(BodyPart.lArm).getAngle();
			if(being.getEquippedWeapon() instanceof Melee){
				Melee weapon = ((Melee)being.getEquippedWeapon());
				if(weapon.getBody() != null){
					position = weapon.getBody().getWorldCenter();
					rotation = weapon.getBody().getAngle();
					if(weapon.isStartedFacingLeft())
						rotation -= Math.PI;
				}
			}
			weaponSprite.setPosition(position.x - weaponSprite.getWidth()/2f, position.y - weaponSprite.getHeight()/2f);
			weaponSprite.setRotation((float)Math.toDegrees(rotation));
			if(facingLeft)
				weaponSprite.flip(false, true);
			weaponSprite.draw(batch);
			if(facingLeft)
				weaponSprite.flip(false, true);
		}
	}

	@Override public void setCurrentWeapon(int currentWeapon, World world){
		weaponSprite = null;
	}
}
