package com.blastedstudios.freeboot.ui.network.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.freeboot.network.BaseNetwork;
import com.blastedstudios.freeboot.network.IMessageListener;
import com.blastedstudios.freeboot.network.Messages.MessageType;
import com.blastedstudios.freeboot.network.Messages.Text;
import com.blastedstudios.freeboot.network.Messages.TextRequest;
import com.blastedstudios.freeboot.util.ui.FreebootTextButton;
import com.blastedstudios.freeboot.util.ui.FreebootWindow;
import com.google.protobuf.Message;

public class ChatWindow extends FreebootWindow implements IMessageListener {
	private final TextArea chatText;
	private final TextField sendText;
	private final BaseNetwork network;
	
	public ChatWindow(Skin skin, BaseNetwork network){
		super("", skin);
		this.network = network;
		chatText = new TextArea("", skin);
		chatText.setPrefRows(5);
		chatText.setTouchable(Touchable.disabled);
		sendText = new TextField("", skin);
		if(network.isConnected()){
			network.addListener(MessageType.TEXT, this);
		}else
			network.addListener(MessageType.CONNECTED, new IMessageListener() {
				@Override public void receive(MessageType messageType, Message object) {
					network.removeListener(MessageType.CONNECTED, this);
					network.addListener(MessageType.TEXT, ChatWindow.this);
				}
			});
		final Button sendButton = new FreebootTextButton("Send", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				if(network != null){
					TextRequest.Builder textRequest = TextRequest.newBuilder();
					textRequest.setContent(sendText.getText());
					network.send(MessageType.TEXT_REQUEST, textRequest.build());
				}
				sendText.setText("");
			}
		});
		add("Chat").colspan(2).expandX();
		row();
		add(chatText).colspan(2).fillX().expandX();
		row();
		add(sendText).fillX().expandX();
		add(sendButton);

		pack();
		setWidth(Gdx.graphics.getWidth()/2);
		setX(Gdx.graphics.getWidth()/2 - getWidth()/2);
		setMovable(false);
	}
	
	@Override public boolean remove(){
		if(network != null)
			network.removeListener(MessageType.TEXT, this);
		return super.remove();
	}

	@Override public void receive(MessageType messageType, Message object) {
		Text text = (Text) object;
		String appended = chatText.getText().isEmpty() ? "" : "\n";
		appended += text.getOrigin() + ": " + text.getContent();
		chatText.setText(chatText.getText() + appended);
		chatText.setCursorPosition(chatText.getText().length()-1);
	}
}
