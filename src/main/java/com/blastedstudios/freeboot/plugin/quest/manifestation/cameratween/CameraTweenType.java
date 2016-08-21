package com.blastedstudios.freeboot.plugin.quest.manifestation.cameratween;

import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Circ;
import aurelienribon.tweenengine.equations.Cubic;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Expo;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;
import aurelienribon.tweenengine.equations.Quart;
import aurelienribon.tweenengine.equations.Quint;
import aurelienribon.tweenengine.equations.Sine;

public enum CameraTweenType {
	BounceInOut(Bounce.INOUT),
	BounceIn(Bounce.IN),
	BounceOut(Bounce.OUT),
	BackInOut(Back.INOUT),
	BackIn(Back.IN),
	BackOut(Back.OUT),
	CircInOut(Circ.INOUT),
	CircIn(Circ.IN),
	CircOut(Circ.OUT),
	CubicInOut(Cubic.INOUT),
	CubicIn(Cubic.IN),
	CubicOut(Cubic.OUT),
	ElasticInOut(Elastic.INOUT),
	ElasticIn(Elastic.IN),
	ElasticOut(Elastic.OUT),
	ExpoInOut(Expo.INOUT),
	ExpoIn(Expo.IN),
	ExpoOut(Expo.OUT),
	LinearInOut(Linear.INOUT),
	QuadInOut(Quad.INOUT),
	QuadIn(Quad.IN),
	QuadOut(Quad.OUT),
	QuartInOut(Quart.INOUT),
	QuartIn(Quart.IN),
	QuartOut(Quart.OUT),
	QuintInOut(Quint.INOUT),
	QuintIn(Quint.IN),
	QuintOut(Quint.OUT),
	SineInOut(Sine.INOUT),
	SineIn(Sine.IN),
	SineOut(Sine.OUT);
	
	public final TweenEquation equation;
	
	private CameraTweenType(TweenEquation equation) {
		this.equation = equation;
	}
}
