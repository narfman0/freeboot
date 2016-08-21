package com.blastedstudios.freeboot;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.blastedstudios.gdxworld.GDXWorldEditor;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.freeboot.ui.loading.MainLoadingScreen;
import com.blastedstudios.freeboot.util.SaveHelper;

public class Freeboot extends GDXGame {
	@Override public void create () {
		pushScreen(new MainLoadingScreen(this));
	}
	
	public static void main (String[] argv) {
		SaveHelper.loadProperties();
		new LwjglApplication(new Freeboot(), GDXWorldEditor.generateConfiguration("Freeboot"));
	}
}
