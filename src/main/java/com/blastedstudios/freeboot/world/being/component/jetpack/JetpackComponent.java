package com.blastedstudios.freeboot.world.being.component.jetpack;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.freeboot.input.ActionEnum;
import com.blastedstudios.freeboot.world.WorldManager;
import com.blastedstudios.freeboot.world.being.Being;
import com.blastedstudios.freeboot.world.being.Being.BodyPart;
import com.blastedstudios.freeboot.world.being.component.AbstractComponent;
import com.blastedstudios.freeboot.world.being.component.IComponent;

@PluginImplementation
public class JetpackComponent extends AbstractComponent {
	private static final float DIFFERENCE = 30f,
			STRAIGHT_LOW = 270f, STRAIGHT_HIGH_MIN = STRAIGHT_LOW - DIFFERENCE, STRAIGHT_HIGH_MAX = STRAIGHT_LOW + DIFFERENCE, 
			LEFT_LOW = 300f, LEFT_HIGH_MIN = LEFT_LOW - DIFFERENCE, LEFT_HIGH_MAX = LEFT_LOW + DIFFERENCE,
			RIGHT_LOW = 240f, RIGHT_HIGH_MIN = RIGHT_LOW - DIFFERENCE, RIGHT_HIGH_MAX = RIGHT_LOW + DIFFERENCE,
			DASH_LEFT_LOW = 180f, DASH_LEFT_HIGH_MIN = DASH_LEFT_LOW - DIFFERENCE, DASH_LEFT_HIGH_MAX = DASH_LEFT_LOW + DIFFERENCE,
			DASH_RIGHT_LOW = 0f, DASH_RIGHT_HIGH_MIN = DASH_RIGHT_LOW - DIFFERENCE, DASH_RIGHT_HIGH_MAX = DASH_RIGHT_LOW + DIFFERENCE,
			DASH_BURN_RATE = Properties.getFloat("character.jetpack.dash.burnrate", 30f),
			DASH_DURATION = Properties.getFloat("character.dash.duration", .5f),
			DASH_FORCE = Properties.getFloat("character.jetpack.dash.force", 500);
	private ParticleEffect jetpackEffect;
	private boolean lastJetpackActivated, jetpackActivated, dashRight;
	private long lastTimeLeftPressed, lastTimeRightPressed;
	private float jetpackPower;
	private float timeUntilDashAvailable;

	@Override public void render(float dt, Batch batch, AssetManager sharedAssets,
			GDXRenderer gdxRenderer, boolean facingLeft, boolean paused) {
		if(!being.getStats().hasJetpack())
			return;
		if(!paused)
			updateJetpackAngles();
		jetpackEffect.setPosition(being.getPosition().x, being.getPosition().y);
		jetpackEffect.draw(batch, paused ? 0f : dt);
		if(paused)
			return;
		timeUntilDashAvailable -= dt;
		if(!being.isDead() && (jetpackActivated || isDashing()) && !lastJetpackActivated){
			lastJetpackActivated = true;
			jetpackEffect.setDuration(10000);
			jetpackEffect.start();
		}
		if(being.isDead() || ((!jetpackActivated || !isDashing()) && lastJetpackActivated)){
			lastJetpackActivated = false;
			jetpackEffect.setDuration(0);
		}
	}
	
	@Override public void update(float dt, boolean facingLeft, boolean paused) {
		if(!being.getStats().hasJetpack() || paused)
			return;
		jetpackRecharge();
		if(isDashing() && !being.isDead())
			applyDashForce();
	}

	private void updateJetpackAngles() {
		if(isDashing() && dashRight)
			setAngle(jetpackEffect, DASH_LEFT_HIGH_MIN, DASH_LEFT_HIGH_MAX, DASH_LEFT_LOW);
		else if(isDashing() && !dashRight)
			setAngle(jetpackEffect, DASH_RIGHT_HIGH_MIN, DASH_RIGHT_HIGH_MAX, DASH_RIGHT_LOW);
		else if((being.isMoveLeft() && being.isMoveRight()) || 
				(!being.isMoveLeft() && !being.isMoveRight()))
			setAngle(jetpackEffect, STRAIGHT_HIGH_MIN, STRAIGHT_HIGH_MAX, STRAIGHT_LOW);
		else if(being.isMoveLeft())
			setAngle(jetpackEffect, LEFT_HIGH_MIN, LEFT_HIGH_MAX, LEFT_LOW);
		else if(being.isMoveRight())
			setAngle(jetpackEffect, RIGHT_HIGH_MIN, RIGHT_HIGH_MAX, RIGHT_LOW);
	}

	private static void setAngle(ParticleEffect effect, float highMin, float highMax, float low){
		for(ParticleEmitter emitter : effect.getEmitters()){
			emitter.getAngle().setHigh(highMin, highMax);
			emitter.getAngle().setLow(low);
		}
	}

	private void jetpackRecharge(){
		if(!being.isDead() && jetpackActivated && jetpackPower > 0){
			if(being.getRagdoll().getBodyPart(BodyPart.torso).getLinearVelocity().y < 2f)
				being.getRagdoll().applyForceAtCenter(0, being.getStats().getJetpackImpulse());
			jetpackPower += -Properties.getFloat("character.jetpack.burnrate", 1f);
		}
		jetpackPower = Math.min(being.getStats().getJetpackMax(), jetpackPower + being.getStats().getJetpackRecharge());
	}

	public void dash(boolean right){
		if(!being.isDead() && jetpackPower > DASH_BURN_RATE && timeUntilDashAvailable <= 0f){
			timeUntilDashAvailable = DASH_DURATION;
			dashRight = right;
			applyDashForce();
			jetpackPower -= Properties.getFloat("character.jetpack.dash.burnrate");
			Log.log("Player.dash", "Player dashing " + (right ? "right" : "left"));
		}
	}

	public boolean isDashing(){
		return timeUntilDashAvailable > 0;
	}

	private void applyDashForce(){
		being.getRagdoll().applyForceAtCenter((dashRight ? 1 : -1) * DASH_FORCE, 0);
	}
	
	public float getJetpackPower(){
		return jetpackPower;
	}

	public void setJetpackActivated(boolean jetpackActivated) {
		this.jetpackActivated = jetpackActivated;
	}

	@Override public IComponent initialize(Being being){
		jetpackEffect = new ParticleEffect();
		jetpackEffect.load(Gdx.files.internal("data/particles/jetpack.p"), Gdx.files.internal("data/particles"));
		jetpackEffect.setDuration(0);
		return super.initialize(being);
	}

	@Override public boolean keyDown(int key, WorldManager worldManager) {
		if(ActionEnum.UP.matches(key)){
			if(!worldManager.isPause() && worldManager.isInputEnable())
				jetpackActivated = true;
		}else if(ActionEnum.LEFT.matches(key)){
			if(!worldManager.isPause() && worldManager.isInputEnable()){
				if(System.currentTimeMillis() - lastTimeLeftPressed < Properties.getFloat("input.doublepress.time", 250))
					dash(false);
				lastTimeLeftPressed = System.currentTimeMillis();
			}
		}else if(ActionEnum.RIGHT.matches(key)){
			if(!worldManager.isPause() && worldManager.isInputEnable()){
				if(System.currentTimeMillis() - lastTimeRightPressed < Properties.getFloat("input.doublepress.time", 250))
					dash(true);
				lastTimeRightPressed = System.currentTimeMillis();
			}
		}
		return false;
	}

	@Override public boolean keyUp(int key, WorldManager worldManager){
		if(ActionEnum.UP.matches(key))
			jetpackActivated = false;
		return false;
	}
}
