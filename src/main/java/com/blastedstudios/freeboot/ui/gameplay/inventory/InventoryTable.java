package com.blastedstudios.freeboot.ui.gameplay.inventory;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.blastedstudios.freeboot.ui.gameplay.inventory.GunButton.IButtonClicked;
import com.blastedstudios.freeboot.util.ui.UIHelper;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.weapon.Weapon;

public class InventoryTable extends Table implements IButtonClicked {
	private static final int GUNS_PER_ROW = 4;
	private final Sprite noGun;
	private final Table gunsTable;
	final List<Weapon> guns;
	private final AssetManager sharedAssets;
	private final Skin skin;
	private final IButtonClicked informationWindowListener;
	
	public InventoryTable(final Skin skin, final Being being,
			final AssetManager sharedAssets, IButtonClicked informationWindowListener){
		try{
			setColor(UIHelper.getColor(skin, "table-background", "background", "primary"));
		}catch(Exception e){}
		this.sharedAssets = sharedAssets;
		this.skin = skin;
		this.informationWindowListener = informationWindowListener;
		noGun = GunButton.createGunSprite(sharedAssets, "nogun");
		gunsTable = new Table();
		guns = new ArrayList<>(being.getGuns());
		rebuildGunTables();
		add(new Label("Inventory", skin)).colspan(2);
		row();
		add(gunsTable);
		pack();
	}

	private void rebuildGunTables(){
		addGunTables(skin);
	}

	private void addGunTables(final Skin skin){
		gunsTable.clear();
		for(int i=0; i<GUNS_PER_ROW; i++){
			if(guns.size() > i)
				gunsTable.add(new GunButton(skin, sharedAssets, guns.get(i), this));
			else
				gunsTable.add(new ImageButton(new SpriteDrawable(noGun)));
		}
	}

	@Override public void gunButtonClicked(Weapon weapon) {
		informationWindowListener.gunButtonClicked(weapon);
	}
}
