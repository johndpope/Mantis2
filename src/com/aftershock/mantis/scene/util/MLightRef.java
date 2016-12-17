package com.aftershock.mantis.scene.util;

import com.aftershock.mantis.scene.MScene2D;
import com.badlogic.gdx.graphics.Color;
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

public class MLightRef {
	private String _name;
	private MScene2D _owningScene;

	public MLightRef(String name, MScene2D owningScene) {
		_name = name;
		_owningScene = owningScene;
	}

	/**
	 * Sets the alpha (transparency) of the referenced light.
	 * 
	 * @param alpha
	 *            The new alpha.
	 */
	public void setAlpha(float alpha) {
		_owningScene.setLightAlpha(_name, alpha);
	}

	/**
	 * Sets the color of the referenced light.
	 * 
	 * @param col
	 *            The color.
	 */
	public void setCol(Color col) {
		_owningScene.setLightColor(_name, col);
	}

	/**
	 * Sets the position of the referenced light.
	 * 
	 * @param x
	 *            The X position.
	 * @param y
	 *            The Y position.
	 */
	public void setPos(float x, float y) {
		_owningScene.setLightPos(_name, x, y);
	}

	/**
	 * Sets the position of the referenced light.
	 * 
	 * @param pos
	 *            The new position.
	 */
	public void setPos(Vector2 pos) {
		_owningScene.setLightPos(_name, pos);
	}

	/**
	 * Sets the angle of the referenced light (for spot/directional lights).
	 * 
	 * @param angle
	 *            The new facing angle.
	 */
	public void setAngle(float angle) {
		_owningScene.setLightAngle(_name, angle);
	}

	/**
	 * Sets the cone field of the referenced light (for spot lights).
	 * 
	 * @param angle
	 *            The new cone angle.
	 */
	public void setConeAngle(float angle) {
		_owningScene.setLightConeAngle(_name, angle);
	}

	/**
	 * Gets the alpha (transparency) of the referenced light.
	 * 
	 * @return The referenced light's alpha.
	 */
	public float getAlpha() {
		return _owningScene.getLightAlpha(_name);
	}

	/**
	 * Gets the color of the referenced light.
	 * 
	 * @return The referenced light's color.
	 */
	public Color getCol() {
		return _owningScene.getLightColor(_name);
	}

	/**
	 * Get the position of the referenced light.
	 * 
	 * @return The referenced light's position.
	 */
	public Vector2 getPos() {
		return _owningScene.getLightPos(_name);
	}

	/**
	 * Gets the facing angle of the referenced (spot or directional) light.
	 * 
	 * @return The referenced light's facing angle.
	 */
	public float getAngle() {
		return _owningScene.getLightAngle(_name);
	}

	/**
	 * Gets the cone angle of the referenced (spot) light.
	 * 
	 * @return The referenced light's cone angle.
	 */
	public float getConeAngle() {
		return _owningScene.getLightConeAngle(_name);
	}

}
