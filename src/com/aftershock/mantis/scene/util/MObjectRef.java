package com.aftershock.mantis.scene.util;

import com.aftershock.mantis.scene.MScene2D;
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

public class MObjectRef {
	private String _name;
	private MScene2D _owningScene;

	public MObjectRef(String name, MScene2D owningScene) {
		_name = name;
		_owningScene = owningScene;
	}

	/**
	 * Sets the position of the referred MGameObject.
	 * 
	 * @param x
	 *            The X position.
	 * @param y
	 *            The Y position.
	 */
	public void setPos(float x, float y) {
		_owningScene.setGObjectPos(_name, x, y);
	}

	/**
	 * Sets the position of the referred MGameObject.
	 * 
	 * @param pos
	 *            The position.
	 */
	public void setPos(Vector2 pos) {
		_owningScene.setGObjectPos(_name, pos);
	}

	/**
	 * Sets the rotation of the referred MGameObject.
	 * 
	 * @param deg
	 *            The angle in degrees.
	 */
	public void setRot(float deg) {
		_owningScene.setGObjectRot(_name, deg);
	}

	/**
	 * Sets the velocity of the referred MGameObject.
	 * 
	 * @param x
	 *            The X velocity.
	 * @param y
	 *            The Y velocity.
	 */
	public void setVel(float x, float y) {
		_owningScene.setGObjectVelocity(_name, x, y);
	}

	/**
	 * Sets the velocity of the referred MGameObject.
	 * 
	 * @param vel
	 *            The object velocity.
	 */
	public void setVel(Vector2 vel) {
		_owningScene.setGObjectVelocity(_name, vel);
	}

	/**
	 * Sets the restitution of the referenced MGameObject.
	 * 
	 * @param res
	 *            The new restitution.
	 */
	public void setRestitution(float res) {
		_owningScene.setGObjectRestitution(_name, res);
	}

	/**
	 * Sets the friction of the referenced MGameObject.
	 * 
	 * @param fric
	 *            The new friction.
	 */
	public void setFriction(float fric) {
		_owningScene.setGObjectFriction(_name, fric);
	}

	/**
	 * Sets the linear dampening of the referenced MGameObject.
	 * 
	 * @param damp
	 *            The new linear dampening.
	 */
	public void setLinDamp(float damp) {
		_owningScene.setGObjectLinearDampening(_name, damp);
	}

	/**
	 * Sets whether or not the referenced MGameObject is a sensor.
	 * 
	 * @param sensor
	 *            The new sensor state.
	 */
	public void setSensor(boolean sensor) {
		_owningScene.setSensor(_name, sensor);
	}

	/**
	 * Sets the texture of the MGameObject.
	 * 
	 * @param tex
	 *            The new texture.
	 */
	public void setTex(String tex) {
		_owningScene.setTexture(_name, tex);
	}

	/**
	 * Set the sprite width and height of the referenced MGameObject.
	 * 
	 * @param w
	 *            The new width.
	 * @param h
	 *            The new height.
	 */
	public void setSpriteSize(float w, float h) {
		_owningScene.setGObjectSpriteSize(_name, w, h);
	}

	/**
	 * Sets the sprite size of the referenced MGameObject.
	 * 
	 * @param size
	 *            The new size.
	 */
	public void setSpriteSize(Vector2 size) {
		_owningScene.setGObjectSpriteSize(_name, size);
	}

	/**
	 * Stop the referred MGameObject.
	 */
	public void stop() {
		_owningScene.stopGObject(_name);
	}

	/**
	 * Apply a force to the referred MGameObject.
	 * 
	 * @param x
	 *            The X force.
	 * @param y
	 *            The Y force.
	 */
	public void applyForce(float x, float y) {
		_owningScene.applyForce(_name, x, y);
	}

	/**
	 * Apply a force to the referred MGameObject.
	 * 
	 * @param force
	 *            The force.
	 */
	public void applyForce(Vector2 force) {
		_owningScene.applyForce(_name, force);
	}

	/**
	 * Apply an impulse to the referred MGameObject.
	 * 
	 * @param x
	 *            The X impulse.
	 * @param y
	 *            The Y impulse.
	 */
	public void applyImpulse(float x, float y) {
		_owningScene.applyImpulse(_name, x, y);
	}

	/**
	 * Apply an impulse to the referred MGameObject.
	 * 
	 * @param imp
	 *            The impulse.
	 */
	public void applyImpulse(Vector2 imp) {
		_owningScene.applyImpulse(_name, imp);
	}

	/**
	 * Translate the referred MGameObject.
	 * 
	 * @param x
	 *            The X translation.
	 * @param y
	 *            The Y translation.
	 */
	public void translate(float x, float y) {
		_owningScene.translateGObject(_name, x, y);
	}

	/**
	 * Translate the referred MGameObject.
	 * 
	 * @param trans
	 *            The translation.
	 */
	public void translate(Vector2 trans) {
		_owningScene.translateGObject(_name, trans);
	}

	/**
	 * Rotate the referred MGameObject.
	 * 
	 * @param deg
	 *            The rotation, in degrees.
	 */
	public void rotate(float deg) {
		_owningScene.rotateGObject(_name, deg);
	}

	/**
	 * Sets the flip of the referred MGameObject.
	 * 
	 * @param x
	 *            The X flip.
	 * @param y
	 *            The Y flip.
	 */
	public void setFlip(boolean x, boolean y) {
		_owningScene.setGObjectFlip(_name, x, y);
	}

	/**
	 * Returns if the referred MGameObject exists.
	 * 
	 * @return Whether or not the referred object is present in the scene.
	 */
	public boolean doesExist() {
		return _owningScene.doesGObjectExist(_name);
	}

	/**
	 * Destroys the referred MGameObject.
	 */
	public void destroy() {
		_owningScene.destroyGObject(_name);
	}

	/**
	 * Gets the position of the referred MGameObject.
	 * 
	 * @return The referred object's position.
	 */
	public Vector2 getPos() {
		return _owningScene.getGObjectPos(_name);
	}

	/**
	 * Gets the rotation of the referred MGameObject.
	 * 
	 * @return The referred object's rotation.
	 */
	public float getRot() {
		return _owningScene.getGObjectRot(_name);
	}

	/**
	 * Checks whether or not the referred MGameObject is flipped on the X axis.
	 * 
	 * @return Whether the referred object is X flipped.
	 */
	public boolean getFlipX() {
		return _owningScene.getGObjectFlippedX(_name);
	}

	/**
	 * Checks whether or not the referred MGameObject is flipped on the Y axis.
	 * 
	 * @return Whether the referred object is Y flipped.
	 */
	public boolean getFlipY() {
		return _owningScene.getGObjectFlippedY(_name);
	}

	/**
	 * Gets the velocity of the referred MGameObject.
	 * 
	 * @return The referred object's velocity.
	 */
	public Vector2 getVel() {
		return _owningScene.getGObjectVelocity(_name);
	}

	/**
	 * Gets the restitution of the referenced MGameObject.
	 * 
	 * @return The referenced object's restitution.
	 */
	public float getRestitution() {
		return _owningScene.getGObjectRestitution(_name);
	}

	/**
	 * Gets the friction of the referenced MGameObject.
	 * 
	 * @return The referenced object's friction.
	 */
	public float getFriction() {
		return _owningScene.getGObjectFriction(_name);
	}

	/**
	 * Gets the linear dampening of the referenced MGameObject.
	 * 
	 * @return The referenced object's linear dampening.
	 */
	public float getLinDamp() {
		return _owningScene.getGObjectLinearDampening(_name);
	}

	/**
	 * Gets whether or not the referenced object is a sensor.
	 * 
	 * @return Whether the referenced object is a sensor.
	 */
	public boolean isSensor() {
		return _owningScene.isSensor(_name);
	}

}
