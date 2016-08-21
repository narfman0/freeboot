package com.blastedstudios.freeboot.ui.gameplay.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.blastedstudios.freeboot.ui.gameplay.inventory.GunButton.IButtonClicked;
import com.blastedstudios.freeboot.util.ui.FreebootWindow;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.weapon.Weapon;

/**
 * The player has inventory as follows:
 * There shall be non weapon armor: chest, head, and accessory
 * There shall be 4 settable gun slots, otherwise guns go in main inventory
 * There shall be 4*4 inventory slots available
 */
public class InventoryWindow extends FreebootWindow implements IButtonClicked {
	private final Skin skin;
	private final Stage stage;
	private final InventoryTable inventoryTable;
	private GunInformationWindow gunInformationWindow;

	public InventoryWindow(final Skin skin, final Being being,
			final AssetManager sharedAssets, final Stage stage){
		super("", skin);
		this.skin = skin;
		this.stage = stage;
		inventoryTable = new InventoryTable(skin, being, sharedAssets, this);
		add(inventoryTable);
		pack();
		setX(Gdx.graphics.getWidth() - getWidth());
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
		setMovable(false);
	}
	
	@Override public boolean remove(){
		if(gunInformationWindow != null)
			gunInformationWindow.remove();
		return super.remove();
	}

	@Override public void gunButtonClicked(Weapon weapon) {
		if(gunInformationWindow != null)
			gunInformationWindow.remove();
		stage.addActor(gunInformationWindow = new GunInformationWindow(skin, weapon));
	}
	
	@Override public boolean contains(float x, float y){
		return (gunInformationWindow != null && gunInformationWindow.contains(x, y)) || super.contains(x, y);
	}
}
