package com.blastedstudios.freeboot.ui.gameplay.console;

import com.badlogic.gdx.graphics.Color;

class ConsoleOutputStruct {
	final String output;
	final Color color;
	
	ConsoleOutputStruct(String output, Color color){
		this.output = output;
		this.color = color;
	}
	
	@Override public String toString(){
		return "[ConsoleOutputStruct output: " + output + " color: " + color + "]";
	}
}
