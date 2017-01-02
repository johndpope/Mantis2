package com.aftershock.mantis.scene;

import java.util.HashMap;

import com.aftershock.mantis.MCallback;
import com.aftershock.mantis.scene.MUIPane.ElementType;
import com.aftershock.mantis.scene.animation.MAmbientLightColorAnim;
import com.aftershock.mantis.scene.animation.MCallbackAnim;
import com.aftershock.mantis.scene.animation.MCamPosAnim;
import com.aftershock.mantis.scene.animation.MLightColorAnim;
import com.aftershock.mantis.scene.animation.MOpacityAnim;
import com.aftershock.mantis.scene.animation.MPosAnim;
import com.aftershock.mantis.scene.animation.MRotationAnim;
import com.aftershock.mantis.scene.animation.MTexAnim;
import com.aftershock.mantis.scene.animation.ui.MUIButtonTypeAnim;
import com.aftershock.mantis.scene.animation.ui.MUILabelTypeAnim;
import com.aftershock.mantis.scene.animation.ui.MUIOpacityAnim;
import com.aftershock.mantis.scene.animation.ui.MUIPosAnim;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

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

public class MAnimationManager {

	private static volatile HashMap<String, MAnimation> _animations = new HashMap<String, MAnimation>();
	private static HashMap<String, MCallback> _tickHooks = new HashMap<String, MCallback>();
	private static MScene2D _currentScene;
	private static MUIPane _currentPane;

	/**
	 * Tick all animations.
	 */
	public static void tick(float delta) {
		// Anim Tick Iterator
		for (final String key : _animations.keySet()) {
			// If animation finishes this tick
			if (_animations.get(key).tick(delta)) {
				// Reverse if loops
				if (_animations.get(key).doesLoop())
					_animations.get(key).reverse();
				else {
					// Remove next frame
					_currentScene.doLater(() -> {
						_animations.get(key).callCallback();
						_animations.remove(key);
					});
				}
			} else {
				if (_tickHooks.containsKey(key)) {
					_tickHooks.get(key).call();
				}
			}
		}
	}

	/**
	 * Creates a position animation.
	 * 
	 * @param animName
	 *            Name of the animation.
	 * @param name
	 *            Name of the object.
	 * @param pos0
	 *            First position.
	 * @param pos1
	 *            Second position.
	 * @param length
	 *            Animation length.
	 * @param loop
	 *            Whether or not to loop the animation.
	 * @param callback
	 *            Completion callback.
	 */
	public static void createPosAnim(final String animName, final String name, final Vector2 pos0, final Vector2 pos1,
			final float length, final boolean loop, final MCallback callback) {
		_currentScene.doLater(() -> {
			MPosAnim anim = new MPosAnim(name, pos0, pos1, length, loop, callback, _currentScene);
			_animations.put(animName, anim);
		});
	}

	/**
	 * Creates an opacity animation.
	 * 
	 * @param animName
	 *            Name of the animation.
	 * @param name
	 *            Name of the object.
	 * @param opacity0
	 *            First opacity.
	 * @param opacity1
	 *            Second opacity.
	 * @param length
	 *            Length of the animation.
	 * @param loop
	 *            Whether or not to loop the animation.
	 * @param callback
	 *            Completion callback.
	 */
	public static void createOpacityAnim(final String animName, final String name, final float opacity0,
			final float opacity1, final float length, final boolean loop, final MCallback callback) {
		_currentScene.doLater(() -> {
			MOpacityAnim anim = new MOpacityAnim(name, opacity0, opacity1, length, loop, callback, _currentScene);
			_animations.put(animName, anim);
		});
	}

	/**
	 * Creates a texture swap animation.
	 * 
	 * @param animName
	 *            Name of the animation.
	 * @param name
	 *            Name of the object.
	 * @param textures
	 *            List of textures to swap.
	 * @param length
	 *            Length of the animation.
	 * @param loop
	 *            Whether to loop the animation.
	 * @param callback
	 *            Completion callback.
	 */
	public static void createTexAnim(final String animName, final String name, final String[] textures,
			final float length, final boolean loop, final MCallback callback) {
		_currentScene.doLater(() -> {
			MTexAnim anim = new MTexAnim(name, textures, length, loop, callback, _currentScene);
			_animations.put(animName, anim);
		});
	}

	/**
	 * Creates a rotation animation.
	 * 
	 * @param animName
	 *            Name of the animation.
	 * @param name
	 *            Name of the object.
	 * @param rot0
	 *            First rotation.
	 * @param rot1
	 *            Second rotation.
	 * @param length
	 *            Length of the animation.
	 * @param loop
	 *            Whether or not to loop the animation.
	 * @param callback
	 *            Completion callback.
	 */
	public static void createRotationAnim(final String animName, final String name, final float rot0, final float rot1,
			final float length, final boolean loop, final MCallback callback) {
		_currentScene.doLater(() -> {
			MRotationAnim anim = new MRotationAnim(name, rot0, rot1, length, loop, callback, _currentScene);
			_animations.put(animName, anim);
		});
	}

	/**
	 * Creates a camera position animation.
	 * 
	 * @param animName
	 *            The name of the animation.
	 * @param pos0
	 *            The starting position.
	 * @param pos1
	 *            The ending position.
	 * @param length
	 *            The length of the animation.
	 * @param loop
	 *            Whether or not to loop the animation.
	 * @param callback
	 *            Completion callback.
	 */
	public static void createCamPosAnim(final String animName, final Vector2 pos0, final Vector2 pos1,
			final float length, final boolean loop, final MCallback callback) {
		_currentScene.doLater(() -> {
			MCamPosAnim anim = new MCamPosAnim(pos0, pos1, length, loop, callback, _currentScene);
			_animations.put(animName, anim);
		});
	}

	/**
	 * Creates a UI position animation.
	 * 
	 * @param animName
	 *            Name of the animation.
	 * @param type
	 *            Type of UI element.
	 * @param name
	 *            Name of the UI element.
	 * @param pos0
	 *            First position.
	 * @param pos1
	 *            Second position.
	 * @param length
	 *            Length of the animation.
	 * @param loop
	 *            Whether or not to loop the animation.
	 * @param callback
	 *            Completion callback.
	 */
	public static void createUIPosAnim(final String animName, final ElementType type, final String name,
			final Vector2 pos0, final Vector2 pos1, final float length, final boolean loop, final MCallback callback) {
		_currentScene.doLater(() -> {
			MUIPosAnim anim = new MUIPosAnim(name, type, pos0, pos1, length, loop, callback, _currentPane);
			_animations.put(animName, anim);
		});
	}

	/**
	 * Creates a UI opacity animation.
	 * 
	 * @param animName
	 *            Name of the animation.
	 * @param type
	 *            Type of UI element.
	 * @param name
	 *            Name of the UI element.
	 * @param opacity0
	 *            First opacity.
	 * @param opacity1
	 *            Second opacity.
	 * @param length
	 *            Length of the animation.
	 * @param loop
	 *            Whether or not to loop the animation.
	 * @param callback
	 *            Completion callback.
	 */
	public static void createUIOpacityAnim(final String animName, final ElementType type, final String name,
			final float opacity0, final float opacity1, final float length, final boolean loop,
			final MCallback callback) {
		_currentScene.doLater(() -> {
			MUIOpacityAnim anim = new MUIOpacityAnim(name, type, opacity0, opacity1, length, loop, callback,
					_currentPane);
			_animations.put(animName, anim);
		});
	}

	/**
	 * Creates a UI label type animation.
	 * 
	 * @param animName
	 *            Name of the animation.
	 * @param name
	 *            Name of the label.
	 * @param text
	 *            Completed string.
	 * @param length
	 *            Length of the animation.
	 * @param loop
	 *            Whether or not to loop the animation.
	 * @param callback
	 *            Completion callback.
	 */
	public static void createUILabelTypeAnim(final String animName, final String name, final String text,
			final float length, final boolean loop, final MCallback callback) {
		_currentScene.doLater(() -> {
			MUILabelTypeAnim anim = new MUILabelTypeAnim(name, text, length, loop, callback, _currentPane);
			_animations.put(animName, anim);
		});
	}

	/**
	 * Creates a UI button type animation.
	 * 
	 * @param animName
	 *            Name of the animation.
	 * @param name
	 *            Name of the button.
	 * @param text
	 *            Completed string.
	 * @param length
	 *            Length of the animation.
	 * @param loop
	 *            Whether or not to loop the animation.
	 * @param callback
	 *            Completion callback.
	 */
	public static void createUIButtonTypeAnim(final String animName, final String name, final String text,
			final float length, final boolean loop, final MCallback callback) {
		_currentScene.doLater(() -> {
			MUIButtonTypeAnim anim = new MUIButtonTypeAnim(name, text, length, loop, callback, _currentPane);
			_animations.put(animName, anim);
		});
	}

	// Current number of callbacks.
	static int callbackNum = 0;

	/**
	 * Creates a callback delay animation.
	 * 
	 * @param length
	 *            Length of the delay.
	 * @param callback
	 *            Callback to call.
	 */
	public static void createCallbackAnim(final float length, final MCallback callback) {
		_currentScene.doLater(() -> {
			MCallbackAnim anim = new MCallbackAnim(length, callback);
			_animations.put(Integer.toString(callbackNum), anim);
			callbackNum++;
		});
	}

	/**
	 * Adds a light color animation.
	 * 
	 * @param animName
	 *            Name of the animation to create.
	 * @param light
	 *            Light to animate.
	 * @param start
	 *            Start color of the light.
	 * @param end
	 *            End color of the light.
	 * @param length
	 *            Animation length.
	 * @param loop
	 *            Whether or not to loop the animation.
	 * @param callback
	 *            Completion callback.
	 */
	public static void createLightColorAnim(final String animName, final String light, final Color start,
			final Color end, final float length, final boolean loop, final MCallback callback) {
		_currentScene.doLater(() -> {
			MLightColorAnim anim = new MLightColorAnim(light, start, end, length, loop, callback, _currentScene);
			_animations.put(animName, anim);
		});
	}

	/**
	 * Adds an ambient light color animation.
	 * 
	 * @param animName
	 *            Name of the animation to create.
	 * @param start
	 *            Start color.
	 * @param end
	 *            End color.
	 * @param length
	 *            Animation length.
	 * @param loop
	 *            Whether or not to loop the animation.
	 * @param callback
	 *            Completion callback.
	 */
	public static void createAmbientLightColorAnim(final String animName, final Color start, final Color end,
			final float length, final boolean loop, final MCallback callback) {
		_currentScene.doLater(() -> {
			MAmbientLightColorAnim anim = new MAmbientLightColorAnim(start, end, length, loop, callback, _currentScene);
			_animations.put(animName, anim);
		});
	}

	/**
	 * Add animation tick hook.
	 * 
	 * @param animName
	 *            Name of the hook.
	 * @param callback
	 *            Hook callback.
	 */
	public static void addAnimTickHook(String animName, MCallback callback) {
		_tickHooks.put(animName, callback);
	}

	/**
	 * Removes an animation tick hook.
	 * 
	 * @param hookName
	 *            Animation tick hook name to remove.
	 */
	public static void removeAnimTickHook(final String hookName) {
		_currentScene.doLater(() -> {
			if (_tickHooks.containsKey(hookName))
				_tickHooks.remove(hookName);
		});
	}

	/**
	 * Sets a scene as the current animation scene.
	 * 
	 * @param scene
	 *            Scene to be set.
	 */
	public static void makeSceneCurrent(MScene2D scene) {
		_currentScene = scene;
	}

	/**
	 * Sets a UI pane as the current animation pane.
	 * 
	 * @param pane
	 *            Pane to be set.
	 */
	public static void makeUIPaneCurrent(MUIPane pane) {
		_currentPane = pane;
	}

	/**
	 * Checks for the existence of a given animation name.
	 * 
	 * @param name
	 *            The name to poll.
	 * @return Whether or not the given animation currently exists.
	 */
	public static boolean doesAnimExist(String name) {
		return _animations.containsKey(name);
	}

	/**
	 * Set the length of an animation.
	 * 
	 * @param name
	 *            The name of the animation to modify.
	 * @param length
	 *            The new length of the given animation.
	 */
	public static void setAnimLength(String name, float length) {
		if (_animations.get(name) instanceof MOpacityAnim)
			((MOpacityAnim) _animations.get(name)).length = length;
		if (_animations.get(name) instanceof MPosAnim)
			((MPosAnim) _animations.get(name)).length = length;
		if (_animations.get(name) instanceof MRotationAnim)
			((MRotationAnim) _animations.get(name)).length = length;
		if (_animations.get(name) instanceof MTexAnim)
			((MTexAnim) _animations.get(name)).length = length;
		if (_animations.get(name) instanceof MUIPosAnim)
			((MUIPosAnim) _animations.get(name)).length = length;
		if (_animations.get(name) instanceof MUIOpacityAnim)
			((MUIOpacityAnim) _animations.get(name)).length = length;
		if (_animations.get(name) instanceof MUILabelTypeAnim)
			((MUILabelTypeAnim) _animations.get(name)).length = length;
		if (_animations.get(name) instanceof MUIButtonTypeAnim)
			((MUIButtonTypeAnim) _animations.get(name)).length = length;
		if (_animations.get(name) instanceof MCallbackAnim)
			((MCallbackAnim) _animations.get(name)).length = length;
		if (_animations.get(name) instanceof MLightColorAnim)
			((MLightColorAnim) _animations.get(name)).length = length;
		if (_animations.get(name) instanceof MAmbientLightColorAnim)
			((MAmbientLightColorAnim) _animations.get(name)).length = length;
		if (_animations.get(name) instanceof MCamPosAnim)
			((MCamPosAnim) _animations.get(name)).length = length;
	}

	/**
	 * Deletes an animation.
	 * 
	 * @param animName
	 *            Animation name to delete.
	 */
	public static void deleteAnim(final String animName) {
		_currentScene.doLater(() -> {
			if (_animations.containsKey(animName))
				_animations.remove(animName);
		});
	}

	/**
	 * Gets the current scene for animations.
	 * 
	 * @return The current scene.
	 */
	public static MScene2D getCurrentScene() {
		return _currentScene;
	}

	/**
	 * Gets the current UI pane for UI animations.
	 * 
	 * @return The current MUIPane.
	 */
	public static MUIPane getCurrentUIPane() {
		return _currentPane;
	}

}
