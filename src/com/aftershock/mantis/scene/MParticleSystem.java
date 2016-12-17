package com.aftershock.mantis.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;

/*
 * Copyright 2016 Luke Diamond
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

public class MParticleSystem {

	private ParticleEffect _emitter;

	/**
	 * MParticleSystem constructor.
	 * 
	 * @param effect
	 *            Name of the effect file.
	 * @param imgDir
	 *            Name of the folder containing the particle image(s).
	 */
	public MParticleSystem(String effect, String imgDir) {
		_emitter = new ParticleEffect();
		_emitter.load(Gdx.files.internal("assets/particles/" + effect),
				Gdx.files.internal("assets/particles/sprites/" + imgDir));
	}

	/**
	 * Draw the particle emitter.
	 * 
	 * @param batch
	 *            Batch to draw to.
	 */
	public void draw(Batch batch) {
		_emitter.draw(batch);
	}

	/**
	 * Update the particle emitter.
	 * 
	 * @param delta
	 *            Delta time of the update.
	 */
	public void update(float delta) {
		_emitter.update(delta);
	}

	/**
	 * Starts the emitter.
	 * 
	 * @param loop
	 *            Whether or not to loop the emitter.
	 */
	public void start() {
		_emitter.start();
	}

	/**
	 * Sets the position of the emitter.
	 * 
	 * @param pos
	 *            The new position to be set.
	 */
	public void setPos(Vector2 pos) {
		_emitter.setPosition(pos.x, pos.y);
	}

	/**
	 * Sets whether or not to loop this effect.
	 * 
	 * @param loop
	 *            Whether or not to loop the emitter.
	 */
	public void setContinuous(boolean loop) {
		_emitter.getEmitters().get(0).setContinuous(loop);
	}

	/**
	 * Gets whether or not the emitter is continuous.
	 * 
	 * @return If the emitter is continuous.
	 */
	public boolean getContinuous() {
		return _emitter.getEmitters().get(0).isContinuous();
	}

	/**
	 * Gets the current position of the emitter.
	 * 
	 * @return The emitter's position.
	 */
	public Vector2 getPos() {
		return new Vector2(_emitter.getBoundingBox().getCenterX(), _emitter.getBoundingBox().getCenterY());
	}

	/**
	 * Dispose of the emitter.
	 */
	public void dispose() {
		_emitter.dispose();
	}

}
