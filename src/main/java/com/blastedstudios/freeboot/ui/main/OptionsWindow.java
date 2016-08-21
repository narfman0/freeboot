package com.blastedstudios.freeboot.ui.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.util.SaveHelper;
import com.blastedstudios.freeboot.util.ui.FreebootTextButton;
import com.blastedstudios.freeboot.util.ui.FreebootWindow;
import com.blastedstudios.freeboot.util.ui.UIHelper;

class OptionsWindow extends FreebootWindow{
	public OptionsWindow(final Skin skin, final GDXGame game, 
			final IOptionsWindowListener listener) {
		super("", skin);
		final Slider soundVolumeSlider = new Slider(0f, 1f, .05f, false, skin);
		soundVolumeSlider.setValue(Properties.getFloat("sound.volume", 1f));
		soundVolumeSlider.setColor(UIHelper.getColor(skin, "sound-volume", "volume", "secondary"));
		final Slider musicVolumeSlider = new Slider(0f, 1f, .05f, false, skin);
		musicVolumeSlider.setValue(Properties.getFloat("music.volume", 1f));
		musicVolumeSlider.setColor(UIHelper.getColor(skin, "music-volume", "volume", "secondary"));
		final CheckBox fullscreenBox = new CheckBox("Fullscreen", skin);
		fullscreenBox.setChecked(Properties.getBool("graphics.fullscreen"));
		final TextField widthField = new TextField(Properties.getInt("graphics.width")+"", skin);
		widthField.setMessageText("<width>");
		final TextField heightField = new TextField(Properties.getInt("graphics.height")+"", skin);
		heightField.setMessageText("<height>");
		final CheckBox vsyncBox = new CheckBox("Vsync", skin);
		vsyncBox.setChecked(Properties.getBool("graphics.vsync"));
		fullscreenBox.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				widthField.setDisabled(!fullscreenBox.isChecked());
				heightField.setDisabled(!fullscreenBox.isChecked());
			}
		});
		final Button acceptButton = new FreebootTextButton("Accept", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				Properties.set("sound.volume", soundVolumeSlider.getValue()+"");
				Properties.set("music.volume", musicVolumeSlider.getValue()+"");
				Properties.set("graphics.fullscreen", fullscreenBox.isChecked()+"");
				Properties.set("graphics.width", widthField.getText());
				Properties.set("graphics.height", heightField.getText());
				Properties.set("graphics.vsync", vsyncBox.isChecked()+"");
				SaveHelper.saveProperties();
				listener.optionsClosed();
			}
		});
		final Button cancelButton = new FreebootTextButton("Cancel", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				listener.optionsClosed();
			}
		});
		add("Sound volume: ");
		add(soundVolumeSlider);
		row();
		add("Music volume: ");
		add(musicVolumeSlider);
		row();
		add(fullscreenBox).colspan(2);
		row();
		add("Resolution width:");
		add(widthField);
		row();
		add("Resolution height:");
		add(heightField);
		row();
		add(vsyncBox).colspan(2);
		row();
		add(acceptButton);
		add(cancelButton);
		setX(Gdx.graphics.getWidth()/2 - getWidth()/2);
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
		setMovable(false);
		pack();
	}
	
	interface IOptionsWindowListener{
		void optionsClosed();
	}
}