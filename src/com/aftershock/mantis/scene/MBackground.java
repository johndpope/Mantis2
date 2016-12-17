package com.aftershock.mantis.scene;

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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class MBackground {

	private Sprite background;
	private float parallax = 1.0f;
	private float opacity = 1.0f;
	private float scale = 1.0f;
	private boolean staticBackground;
	private Vector2 offset;

	/**
	 * Default constructor. Creates a background.
	 * 
	 * @param bgImg
	 *            Image for the background.
	 */
	public MBackground(String bgImg) {
		background = new Sprite(new Texture(Gdx.files.internal("assets/backgrounds/" + bgImg)));
	}

	/**
	 * Sets the parallax of the background.
	 * 
	 * @param newParallax
	 *            New parallax to be set.
	 */
	public void setParallax(float newParallax) {
		parallax = (newParallax <= 0.0f) ? 1.0f : newParallax; // Prevent
																// division by 0
	}

	/**
	 * Sets the scale of a background.
	 * 
	 * @param newScale
	 *            New scale to be set.
	 */
	public void setScale(float newScale) {
		scale = newScale;
	}

	/**
	 * Sets whether or not the background is static.
	 * 
	 * @param isStatic
	 *            Whether or not the background stays fixed.
	 */
	public void setStatic(boolean isStatic) {
		staticBackground = isStatic;
	}

	/**
	 * Sets the opacity of the background.
	 * 
	 * @param newOpacity
	 *            New opacity to be used.
	 */
	public void setOpacity(float newOpacity) {
		opacity = newOpacity;
		background.setAlpha(opacity);
	}

	/**
	 * Sets the offset of the background.
	 * 
	 * @param newOffset
	 *            Pixel offset of the background.
	 */
	public void setOffset(Vector2 newOffset) {
		offset = newOffset;
	}

	/**
	 * Get the parallax of the background.
	 * 
	 * @return The background's parallax.
	 */
	public float getParallax() {
		return parallax;
	}

	/**
	 * Checks whether or not the background is static.
	 * 
	 * @return Whether or not the background is fixed.
	 */
	public boolean isStatic() {
		return staticBackground;
	}

	/**
	 * Get the opacity of the background.
	 * 
	 * @return The alpha/opacity of the background.
	 */
	public float getOpacity() {
		return opacity;
	}

	/**
	 * Get the scale of a background.
	 * 
	 * @return The background's scale.
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Get the pixel offset of the background.
	 * 
	 * @return The background's offset.
	 */
	public Vector2 getOffset() {
		return offset;
	}

	/**
	 * Get the background sprite.
	 * 
	 * @return The sprite drawn by this background.
	 */
	public Sprite getBackground() {
		return background;
	}
}
