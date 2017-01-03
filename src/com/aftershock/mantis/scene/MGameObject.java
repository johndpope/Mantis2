package com.aftershock.mantis.scene;

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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MGameObject extends Actor {

	private Body _body;
	private Fixture _fixture;
	private TextureRegion _tex;
	private Sprite _drawSprite;
	private boolean _flipX = false, _flipY = false;
	private float _opacity = 1.0f;
	private float _rotOffset = 0.0f;
	private boolean _physical;

	/**
	 * Creates a game object.
	 * 
	 * @param world
	 *            World to use for physics.
	 * @param name
	 *            Name of the object.
	 * @param type
	 *            Physical type of the object.
	 * @param initialPos
	 *            Initial position of the object.
	 * @param initialSize
	 *            Initial size of the object.
	 * @param texture
	 *            The object's texture.
	 * @param circular
	 *            Whether or not to treat this object as a circle.
	 * @param rotate
	 *            Whether or not this object can rotate.
	 * @param cat
	 *            Category bit.
	 * @param group
	 *            Group bit.
	 * @param mask
	 *            Mask bit.
	 */
	public MGameObject(World world, String name, BodyType type, Vector2 initialPos, Vector2 initialSize,
			TextureRegion texture, boolean circular, boolean rotate, int cat, int group, int mask, boolean physical) {
		_physical = physical;
		if (_physical) {
			FixtureDef fDef = new FixtureDef();
			fDef.density = 1f;
			fDef.friction = 0.5f;
			fDef.restitution = 0.25f;

			fDef.filter.categoryBits = (short) cat;
			fDef.filter.groupIndex = (short) group;
			fDef.filter.maskBits = (short) mask;

			Shape shape;

			if (circular) {
				shape = new CircleShape();
				((CircleShape) shape).setRadius(initialSize.x);
			} else {
				shape = new PolygonShape();
				((PolygonShape) shape).setAsBox(initialSize.x, initialSize.y);
			}
			fDef.shape = shape;

			BodyDef bDef = new BodyDef();
			bDef.gravityScale = 5f;
			bDef.bullet = true;
			bDef.fixedRotation = !rotate;
			bDef.type = type;
			bDef.allowSleep = true;

			_body = world.createBody(bDef);
			_body.setUserData(name);
			_body.setTransform(initialPos.x, initialPos.y, (float) Math.toRadians(-5));
			_body.setSleepingAllowed(true);
			_fixture = _body.createFixture(fDef);

			setPosition(_body.getPosition().x, _body.getPosition().y);
			setRotation((float) Math.toDegrees(_body.getAngle()));
			setSize(initialSize.x, initialSize.y);
		} else {
			setPosition(initialPos.x, initialPos.y);
			setSize(initialSize.x, initialSize.y);
		}

		_tex = new TextureRegion(texture);
		_drawSprite = new Sprite(_tex);

		_drawSprite.setSize(getWidth(), getHeight());
	}

	/**
	 * Creates a game object.
	 * 
	 * @param world
	 *            World to use for physics.
	 * @param name
	 *            Name of the object.
	 * @param type
	 *            Physical type of the object.
	 * @param initialPos
	 *            Initial position of the object.
	 * @param initialSize
	 *            Initial size of the object.
	 * @param texture
	 *            The name of the object's texture.
	 * @param circular
	 *            Whether or not to treat this object as a circle.
	 * @param rotate
	 *            Whether or not this object can rotate.
	 * @param cat
	 *            Category bit.
	 * @param group
	 *            Group bit.
	 * @param mask
	 *            Mask bit.
	 */
	public MGameObject(World world, String name, BodyType type, Vector2 initialPos, Vector2 initialSize, String texture,
			boolean circular, boolean rotate, int cat, int group, int mask, boolean physical) {
		this(world, name, type, initialPos, initialSize, new TextureRegion(new Texture(texture)), circular, rotate, cat,
				group, mask, physical);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		_drawSprite.setPosition(getX(), getY());
		if (_physical)
			_drawSprite.setOrigin(_body.getLocalCenter().x, _body.getLocalCenter().y);
		else
			_drawSprite.setOrigin(_drawSprite.getWidth() / 2.0f, _drawSprite.getHeight() / 2.0f);
		_drawSprite.setFlip(_flipX, _flipY);
		_drawSprite.setScale(2.0f);
		_drawSprite.setRotation(getRotation() + _rotOffset + ((_physical) ? 5 : 0));
		_drawSprite.setAlpha(_opacity);
		if (_physical)
			_drawSprite.translate(_drawSprite.getOriginX(), _drawSprite.getOriginY());
		_drawSprite.draw(batch);
	}

	/**
	 * Sets the origin of the render sprite.
	 * 
	 * @param x
	 *            The X origin of the render sprite.
	 * @param y
	 *            The Y origin of the render sprite.
	 */
	public void setSpriteOrigin(float x, float y) {
		_drawSprite.setOrigin(x, y);
	}

	/**
	 * Sets the origin of the render sprite.
	 * 
	 * @param origin
	 *            The origin vector of the render sprite.
	 */
	public void setSpriteOrigin(Vector2 origin) {
		setSpriteOrigin(origin.x, origin.y);
	}

	/**
	 * Gets the origin of the render sprite.
	 * 
	 * @return The render sprite's origin.
	 */
	public Vector2 getSpriteOrigin() {
		return new Vector2(_drawSprite.getOriginX(), _drawSprite.getOriginY());
	}

	/**
	 * Sets the position of this object.
	 * 
	 * @param x
	 *            The new X position.
	 * @param y
	 *            The new Y position.
	 */
	public void setPos(float x, float y) {
		if (_physical) {
			_body.setTransform(x, y, _body.getAngle());
		} else {
			setPosition(x, y);
		}
	}

	/**
	 * Sets the position of this object.
	 * 
	 * @param pos
	 *            The new position.
	 */
	public void setPosition(Vector2 pos) {
		setPos(pos.x, pos.y);
	}

	/**
	 * Rotates this object by an angle.
	 * 
	 * @param angle
	 *            The angle to rotate by.
	 */
	public void rotate(float angle) {
		if (_physical) {
			_body.setTransform(_body.getWorldCenter(), angle - 90);
		} else {
			rotateBy(angle);
		}
	}

	/**
	 * Sets the rotation of this object in degrees.
	 * 
	 * @param angle
	 *            The new rotation.
	 */
	public void setRot(float angle) {
		if (_physical) {
			_body.setTransform(_body.getWorldCenter(), angle - 90);
		} else {
			setRotation(angle);
		}
	}

	/**
	 * Set the texture of this object.
	 * 
	 * @param reg
	 *            New TextureRegion to use.
	 */
	public void setTex(TextureRegion reg) {
		_tex = reg;
		Vector2 oldSize = new Vector2(_drawSprite.getWidth(), _drawSprite.getHeight());
		_drawSprite = new Sprite(_tex);
		_drawSprite.setSize(oldSize.x, oldSize.y);
	}

	/**
	 * Sets the rotation offset of this object.
	 * 
	 * @param rot
	 *            The new sprite rotation offset.
	 */
	public void setSpriteRotOffset(float rot) {
		_rotOffset = rot;
	}

	/**
	 * Gets the rotation offset of this object.
	 * 
	 * @return The sprite rotation offset.
	 */
	public float getSpriteRotOffset() {
		return _rotOffset;
	}

	/**
	 * Checks whether or not this object is flipped on the X axis.
	 * 
	 * @return Whether or not this object is flipped on the X axis.
	 */
	public boolean isFlippedX() {
		return _flipX;
	}

	/**
	 * Checks whether or not this object is flipped on the Y axis.
	 * 
	 * @return Whether or not this object is flipped on the Y axis.
	 */
	public boolean isFlippedY() {
		return _flipY;
	}

	/**
	 * Gets the opacity of this object.
	 * 
	 * @return This object's opacity.
	 */
	public float getOpacity() {
		return _opacity;
	}

	/**
	 * Sets the XY flip of this object.
	 * 
	 * @param x
	 *            The new X flip of this object.
	 * @param y
	 *            The new Y flip of this object.
	 */
	public void setFlip(boolean x, boolean y) {
		_flipX = x;
		_flipY = y;
	}

	/**
	 * Sets the X flip of this object.
	 * 
	 * @param flip
	 *            The new X flip.
	 */
	public void setFlipX(boolean flip) {
		_flipX = flip;
	}

	/**
	 * Sets the Y flip of this object.
	 * 
	 * @param flip
	 *            The new Y flip.
	 */
	public void setFlipY(boolean flip) {
		_flipY = flip;
	}

	/**
	 * Sets the opacity of this object.
	 * 
	 * @param opacity
	 *            The new opacity of this object.
	 */
	public void setOpacity(float opacity) {
		_opacity = opacity;
	}

	/**
	 * Gets the body of this object.
	 * 
	 * @return This object's physics body (if applicable).
	 */
	public Body getBody() {
		return _body;
	}

	/**
	 * Gets the fixture of this object.
	 * 
	 * @return This object's physics fixture (if applicable).
	 */
	public Fixture getFixture() {
		return _fixture;
	}

	/**
	 * Gets the render sprite of this object.
	 * 
	 * @return This object's render sprite.
	 */
	public Sprite getDrawSprite() {
		return _drawSprite;
	}

	/**
	 * Update this object.
	 */
	public void update() {
		if (_physical) {
			setPosition(_body.getPosition().x - (_drawSprite.getWidth() / 2.0f),
					_body.getPosition().y - (_drawSprite.getHeight() / 2.0f));
			setRotation((float) Math.toDegrees(_body.getAngle()));
		}
	}

}
