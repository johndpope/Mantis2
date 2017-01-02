package com.aftershock.mantis.scene.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/*
 * Copyright 2016-2017 Luke Diamond
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

public class MUtil {

	/**
	 * Creates a Vector2.
	 * 
	 * @param x
	 *            The vector's X value.
	 * @param y
	 *            The vector's Y value.
	 * @return A new Vector2.
	 */
	public static Vector2 vec(float x, float y) {
		return new Vector2(x, y);
	}

	/**
	 * Creates a Vector3.
	 * 
	 * @param x
	 *            The vector's X value.
	 * @param y
	 *            The vector's Y value.
	 * @param z
	 *            The vector's Z value.
	 * @return A new Vector3.
	 */
	public static Vector3 vec(float x, float y, float z) {
		return new Vector3(x, y, z);
	}

	/**
	 * Creates a Color.
	 * 
	 * @param r
	 *            The color's red value.
	 * @param g
	 *            The color's green value.
	 * @param b
	 *            The color's blue value.
	 * @param a
	 *            The color's alpha (transparency) value.
	 * @return A new Color.
	 */
	public static Color col(float r, float g, float b, float a) {
		return new Color(r, g, b, a);
	}

	/**
	 * Creates a Color.
	 * 
	 * @param r
	 *            The color's red value.
	 * @param g
	 *            The color's green value.
	 * @param b
	 *            The color's blue value.
	 * @return A new color.
	 */
	public static Color col(float r, float g, float b) {
		return new Color(r, g, b, 1.0f);
	}

	/**
	 * Creates a new Color (shade of gray).
	 * 
	 * @param v
	 *            The color's red, green, and blue values.
	 * @return A new color.
	 */
	public static Color col(float v) {
		return new Color(v, v, v, 1.0f);
	}

	/**
	 * Creates a new color (shade of gray with alpha).
	 * 
	 * @param v
	 *            The color's red, green, and blue values.
	 * @param a
	 *            The color's alpha value.
	 * @return A new color.
	 */
	public static Color col(float v, float a) {
		return new Color(v, v, v, a);
	}

	/**
	 * Copies a Vector2.
	 * 
	 * @param v
	 *            The Vector2 to copy.
	 * @return A copy of the given Vector2.
	 */
	public static Vector2 vCopy(Vector2 v) {
		return new Vector2(v.x, v.y);
	}

	/**
	 * Copies a Vector3.
	 * 
	 * @param v
	 *            The Vector3 to copy.
	 * @return A copy of the given Vector3.
	 */
	public static Vector3 vCopy(Vector3 v) {
		return new Vector3(v.x, v.y, v.z);
	}

	/**
	 * Copies a Color.
	 * 
	 * @param c
	 *            The Color to copy.
	 * @return A copy of the given Color.
	 */
	public static Color cCopy(Color c) {
		return new Color(c.r, c.g, c.b, c.a);
	}

	/**
	 * Gets the angle between two Vector2 positions.
	 * 
	 * @param p0
	 *            The first Vector2.
	 * @param p1
	 *            The second Vector2.
	 * @return The angle (in degrees) between the two points.
	 */
	public static float angleBetween(Vector2 p0, Vector2 p1) {
		return (float) Math.toDegrees(Math.atan2(p1.x - p0.x, p1.y - p0.y));
	}

}
