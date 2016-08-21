package com.blastedstudios.freeboot.ui.gameplay;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.freeboot.world.weapon.Weapon;

/**
 * Gun widget showing all the desired visible attributes of a gun to client
 */
public class GunTable extends Table {
	public final Weapon gun;
	
	public GunTable(Skin skin, Weapon gun){
		this.gun = gun;
		add(new Label("Name: " + gun.getName(), skin));
		add(new Label(" Accuracy: " + gun.getAccuracy(), skin));
		row();
		add(new Label("Cost: " + gun.getCost(), skin));
		add(new Label(" Muzzle Velocity (m/s): " + gun.getMuzzleVelocity(), skin));
		row();
		add(new Label("Dmg: " + gun.getDamage(), skin));
		add(new Label(" Rate of Fire (r/s): " + gun.getRateOfFire(), skin));
		row();
		add(new Label("Rounds: " + gun.getRoundsPerClip(), skin));
		add(new Label(" Recoil (j): " + gun.getRecoil(), skin));
		row();
		add(new Label("Reload speed (s): " + gun.getReloadSpeed(), skin));
	}
}
