package com.blastedstudios.freeboot.ui.main;

import java.util.ArrayList;
import java.util.EnumSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.blastedstudios.freeboot.network.Messages.NetBeing.ClassEnum;
import com.blastedstudios.freeboot.network.Messages.NetBeing.FactionEnum;
import com.blastedstudios.freeboot.util.SaveHelper;
import com.blastedstudios.freeboot.util.ui.FreebootTextButton;
import com.blastedstudios.freeboot.util.ui.FreebootWindow;
import com.blastedstudios.freeboot.util.ui.UIHelper;
import com.blastedstudios.freeboot.world.Stats;
import com.blastedstudios.freeboot.world.being.NPCData;
import com.blastedstudios.freeboot.world.being.Player;
import com.blastedstudios.freeboot.world.weapon.Weapon;
import com.blastedstudios.freeboot.world.weapon.WeaponFactory;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.panner.PannerManager;

class NewCharacterWindow extends FreebootWindow{
	public NewCharacterWindow(final Skin skin, final GDXGame game, 
			final INewCharacterWindowListener listener, 
			final GDXRenderer gdxRenderer,
			final AssetManager sharedAssets, final PannerManager panner) {
		super("", skin);
		final TextField nameField = new TextField("", skin);
		try{
			nameField.setColor(UIHelper.getColor(skin, "new-name-field", "textfield", "secondary"));
		}catch(Exception e){}
		nameField.setMessageText("<name>");
		nameField.setMaxLength(12);
		final List<ClassEnum> classList = new List<>(skin);
		Array<ClassEnum> selectableClasses = new Array<>(ClassEnum.values());
		selectableClasses.removeValue(ClassEnum.UNRECOGNIZED, true);
		classList.setItems(selectableClasses);
		final List<FactionEnum> factionList = new List<>(skin);
		Array<FactionEnum> selectableFactions = new Array<>();
		selectableFactions.addAll(FactionEnum.Briton, FactionEnum.Spanish, FactionEnum.Pirate);
		factionList.setItems(selectableFactions);
		final Button createButton = new FreebootTextButton("Create", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				if(nameField.getText().isEmpty())
					return;
				NPCData npcData = NPCData.parse(classList.getSelected().name().toLowerCase());
				Player player = new Player(nameField.getText(), 
						WeaponFactory.getGuns(npcData.get("Weapons")), new ArrayList<Weapon>(), 
						Stats.parseNPCData(npcData),
						0,0,1,0, factionList.getSelected(), EnumSet.of(factionList.getSelected()), 
						factionList.getSelected().name().toLowerCase(), classList.getSelected());
				SaveHelper.save(player);
				listener.backButtonClicked();
			}
		});
		final Button backButton = new FreebootTextButton("Back", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				listener.backButtonClicked();
			}
		});
		add("New Character");
		row();
		add(nameField).fillX();
		row();
		add("Choose Class");
		row();
		add(classList).fillX();
		row();
		add("Choose Faction");
		row();
		add(factionList).fillX();
		row();
		add(createButton).fillX();
		row();
		add(backButton).fillX();
		setX(Gdx.graphics.getWidth()/2 - getWidth()/2);
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
		setMovable(false);
		pack();
	}
	
	interface INewCharacterWindowListener{
		void backButtonClicked();
	}
}