package com.blastedstudios.freeboot.world.being;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.gdxworld.world.quest.GDXQuestManager;
import com.blastedstudios.freeboot.network.Messages.NetBeing;
import com.blastedstudios.freeboot.network.Messages.NetBeing.ClassEnum;
import com.blastedstudios.freeboot.network.Messages.NetBeing.FactionEnum;
import com.blastedstudios.freeboot.ui.gameplay.GameplayNetReceiver;
import com.blastedstudios.freeboot.world.Stats;
import com.blastedstudios.freeboot.world.weapon.Gun;
import com.blastedstudios.freeboot.world.weapon.Weapon;

public class Player extends Being {
	private static final long serialVersionUID = 1L;
	private GDXQuestManager questManager = new GDXQuestManager();
	private final Map<String,Boolean> levelCompleted = new HashMap<>();
	protected final ClassEnum playerClass;

	public Player(String name, List<Weapon> guns, List<Weapon> inventory, Stats stats,
			int currentGun,	int cash, int level, int xp, FactionEnum faction,
			EnumSet<FactionEnum> factions, String resource, ClassEnum playerClass) {
		super(name, guns, inventory, stats, currentGun, cash, level, xp, faction, factions, resource, null);
		this.playerClass = playerClass;
	}
	
	public Player(NetBeing netBeing){
		super(netBeing);
		playerClass = netBeing.getPlayerClass();
	}
	
	@Override public void update(float dt, World world, IDeathCallback deathCallback, boolean paused,
			boolean inputEnabled, GameplayNetReceiver receiver){
		super.update(dt, world, deathCallback, paused, inputEnabled, receiver);
		if(!paused)
			questManager.tick(dt);
	}

	public GDXQuestManager getQuestManager(){
		return questManager;
	}
	
	public boolean isLevelCompleted(String level){
		return levelCompleted.containsKey(level) && levelCompleted.get(level);
	}
	
	public void setLevelCompleted(String level, boolean completed){
		levelCompleted.put(level, completed);
	}
	
	/**
	 * @return true if the player may start this level
	 */
	public boolean isLevelAvailable(GDXWorld world, GDXLevel level){
		//if player beat level, immediately return true. Player may replay levels, 
		//so this catches the case if he goes back and replays stuff
		if(isLevelCompleted(level.getName()))
			return true;
		String active = level.getProperties().get("Active");
		if(active != null && !Boolean.parseBoolean(active))
			return false;
		String prerequisies = level.getProperties().get("Prerequisites");
		if(prerequisies != null)
			for(String prereq : prerequisies.split(","))
				for(GDXLevel gdxLevel : world.getLevels())
					if(gdxLevel.getName().equals(prereq.trim()) && !isLevelCompleted(prereq))
						return false;
		return true;
	}
	
	public void touchUp(int x, int y, int x1, int y1){
		if(getEquippedWeapon() != null && getEquippedWeapon() instanceof Gun){
			Gun gun = (Gun) getEquippedWeapon();
			if(gun.isSemiAutomatic())
				gun.setAttackReleased(true);
		}
	}

	public ClassEnum getPlayerClass() {
		return playerClass;
	}
}
