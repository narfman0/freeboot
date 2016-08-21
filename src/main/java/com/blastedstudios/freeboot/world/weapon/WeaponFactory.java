package com.blastedstudios.freeboot.world.weapon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.blastedstudios.gdxworld.plugin.serializer.xml.XMLSerializer;
import com.blastedstudios.gdxworld.util.Log;

public class WeaponFactory {
	private static final XMLSerializer serializer = new XMLSerializer();

	public static Collection<Weapon> getStockWeapons(){
		HashMap<String, Weapon> guns = new HashMap<String, Weapon>();
		for(FileHandle handle : Gdx.files.internal("data/world/weapons").list())
			try {
				Weapon gun = (Weapon) serializer.load(handle); 
				guns.put(gun.getName(), gun);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return guns.values();
	}

	public static Weapon getWeapon(String id){
		try {
			return (Weapon) serializer.load(Gdx.files.internal("data/world/weapons/" + id + ".xml"));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	public static List<Weapon> getGuns(String weaponsString){
		List<Weapon> guns = new ArrayList<>();
		if(!weaponsString.isEmpty())
			for(String gun : weaponsString.split(","))
				guns.add(WeaponFactory.getWeapon(gun));
		return guns;
	}
	
	/**
	 * @param mLevel monster level
	 * @return iLevel of item to be dropped. This is a function of the mLevel, 
	 * with low probability being within 2 levels to mLevel, higher probability
	 * to be 3-6 levels lower, and decreasingly lower 7+ levels below
	 */
	public static int generateILevel(int mLevel){
		Random random = new Random();
		float roll = random.nextFloat()*10;
		double distribution = -0.00479021*Math.pow(roll,3) + 0.0858566*Math.pow(roll,2) - 0.315717*roll + 0.357;
		return (int)(distribution * (double)mLevel);
	}

	/**
	 * @param iLevel monster level dropping weapon
	 * @param pLevel player level for determining weapon type
	 */
	public static Weapon generateGun(int iLevel, int pLevel){
		Random random = new Random();
		Weapon weapon = rollWeaponType(random.nextFloat(), pLevel, random);
		weapon.setDamage(weapon.getDamage()*( (100f+(float)(iLevel*2))/100f) );

		float rarity = random.nextFloat();
		int modCount = 0;
		if(rarity < .01f)		//legendary 4-7 mods
			modCount = random.nextInt(3) + 4;
		else if(rarity < .05f)	//rare 3-5 mods
			modCount = random.nextInt(2) + 3;
		else if(rarity < .15f)	//magic 2-3 mods
			modCount = random.nextInt(1) + 2;
		else if(rarity < .25f)	//superior 1 mod
			modCount = 1;

		for(; --modCount > 0;){
			float modType = random.nextFloat();
			if(modType < .01f)
				weapon.setDamage(weapon.getDamage() * getPercent(random, .5f, iLevel) );
			else if(modType < .1f)
				weapon.getStats().setAttack(random.nextFloat() * (float)iLevel / 5f);
			else if(modType < .2f)
				weapon.getStats().setDefense(random.nextFloat() * (float)iLevel / 5f);
			else if(modType < .25f)
				weapon.getStats().setDefensePerLevel(random.nextFloat() * (float)iLevel / 10f);
			else if(modType < .3f)
				weapon.getStats().setAttackPerLevel(random.nextFloat() * (float)iLevel / 10f);
			else if(modType < .4f)
				weapon.getStats().setAccuracy(Math.min(.999f, weapon.getAccuracy() * getPercent(random, 10f, iLevel)));
			else if(modType < .55f)
				weapon.getStats().setHp(random.nextFloat()*((float)iLevel)/2f);
			else if(modType < .6f)
				weapon.getStats().setHpPerLevel(random.nextFloat()*((float)iLevel)/6f);
			else if(modType < .7f)
				weapon.getStats().setMuzzleVelocity(weapon.getMuzzleVelocity()+random.nextFloat()*((float)iLevel));
			else if(modType < .8f)
				weapon.getStats().setRateOfFire(weapon.getRateOfFire() * getPercent(random, 5f, iLevel));
			else if(modType < .9f)
				weapon.getStats().setReloadSpeed(weapon.getReloadSpeed() * getPercent(random, 5f, iLevel));
			else if(modType < 1f)
				weapon.getStats().setRoundsPerClip((int)(weapon.getRoundsPerClip() * getPercent(random, 5f, iLevel)));
		}
		return weapon;
	}
	
	/**
	 * @param pLevel player level for determining weapon type
	 */
	private static Weapon rollWeaponType(float roll, int pLevel, Random random){
		if(pLevel < 1){
			Log.error("WeaponFactory.rollWeaponType", "Error: mLevel of " + pLevel + ", using 1");
			pLevel = 1;
		}
		Collection<Weapon> weapons = getStockWeapons();
		int total = 0;
		for(Weapon weapon : weapons)
			if(weapon.getMinLevel() <= pLevel)
				total += weapon.getRolls();
		int current = 0, target = random.nextInt(total);
		for(Weapon weapon : weapons){
			if(weapon.getMinLevel() <= pLevel){
				current += weapon.getRolls();
				if(current >= target)
					return weapon;
			}
		}
		return null;
	}
	
	private static float getPercent(Random random, float divisor, float mLevel){
		return (100f + random.nextFloat() / divisor * mLevel) / 100f;
	}
}