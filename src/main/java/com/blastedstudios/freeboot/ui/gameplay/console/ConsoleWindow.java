package com.blastedstudios.freeboot.ui.gameplay.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.network.Messages.TextRequest;
import com.blastedstudios.freeboot.ui.gameplay.GameplayScreen;
import com.blastedstudios.freeboot.util.IConsoleCommand;
import com.blastedstudios.freeboot.util.ui.FreebootTextButton;
import com.blastedstudios.freeboot.util.ui.FreebootWindow;
import com.blastedstudios.freeboot.world.WorldManager;

public class ConsoleWindow extends FreebootWindow implements IHistoryListener{
	private final TextField text;
	private final TextArea history;
	private final GameplayScreen screen;
	
	public ConsoleWindow(final Skin skin, final WorldManager world, 
			final GameplayScreen screen, final EventListener listener) {
		super("", skin);
		this.screen = screen;
		history = new TextArea("", skin);
		redrawHistory();
		for(IConsoleCommand command : PluginUtil.getPlugins(IConsoleCommand.class))
			command.initialize(world, screen);
		text = new TextField("", skin);
		text.setMessageText("<enter command>");
		TextButton executeButton = new FreebootTextButton("Send", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				execute();
			}
		});
		TextButton closeButton = new FreebootTextButton("Close", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				listener.handle(event);
			}
		});
		TextButton exitButton = new FreebootTextButton(Properties.get("ui.back.button.text", "Exit to Map"), skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				screen.levelComplete(false);
			}
		});
		add("Console").colspan(4);
		row();
		add(history).colspan(4).fill().expand();
		row();
		add(text).fillX().expandX();
		add(executeButton);
		add(closeButton);
		add(exitButton);
		setSize(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f);
		setX(Gdx.graphics.getWidth()/2 - getWidth()/2);
		History.addListener(this);
	}
	
	@Override public boolean remove(){
		History.removeListener(this);
		return super.remove();
	}
	
	private void redrawHistory(){
		StringBuilder builder = new StringBuilder();
		for(ConsoleOutputStruct struct : History.items){
			builder.append(builder.length() == 0 ? "" : "\n");
			builder.append(struct.output);
		}
		history.setText(builder.toString());
		history.setCursorPosition(builder.length());
	}

	/**
	 * Interpret text and execute command therein
	 */
	public void execute() {
		History.add(text.getText(), Color.BLACK);
		if(text.getText().startsWith("!") || text.getText().startsWith("/"))
			try{
				String[] tokens = text.getText().substring(1).split(" ");
				for(IConsoleCommand command : PluginUtil.getPlugins(IConsoleCommand.class))
					for(String match : command.getMatches())
						try{
							if(tokens[0].matches(match))
								command.execute(tokens);
						}catch(Exception e){
							Log.error("ConsoleWindow.execute", "Failed to execute command: " + text.getText());
							e.printStackTrace();
						}
			}catch(Exception e){
				e.printStackTrace();
			}
		else{
			TextRequest.Builder builder = TextRequest.newBuilder();
			builder.setContent(text.getText());
			if(screen.getReceiver() != null)
				screen.getReceiver().send(builder.build());
		}
		text.setText("");
		redrawHistory();
	}

	@Override public void added(String msg, Color color) {
		redrawHistory();
	}
}
