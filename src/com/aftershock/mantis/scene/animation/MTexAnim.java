package com.aftershock.mantis.scene.animation;

import com.aftershock.mantis.MCallback;
import com.aftershock.mantis.scene.MAnimation;
import com.aftershock.mantis.scene.MScene2D;

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

public class MTexAnim implements MAnimation {

	public float elapsed = 0.0f, length;
	boolean loop = false;
	boolean reverse = false;
	String[] textures;
	String object;
	public MCallback callback;
	MScene2D owningScene;

	public MTexAnim(String obj, String[] tex, float len, boolean doesLoop, MCallback cback, MScene2D scene) {
		object = obj;
		textures = tex;
		length = len;
		loop = doesLoop;
		callback = cback;
		owningScene = scene;
	}

	@Override
	public boolean tick(float delta) {
		delta /= owningScene.getTimeScale();

		int index = (int) (textures.length * (elapsed / length));
		if (index >= textures.length)
			index = textures.length - 1;
		if (owningScene.doesGObjectExist(object))
			owningScene.setTexture(object, textures[index]);
		else
			return true;
		if (reverse)
			elapsed -= delta;
		else
			elapsed += delta;
		if (reverse) {
			if (elapsed < 0) {
				owningScene.setTexture(object, textures[0]);
				return true;
			}
		} else {
			if (elapsed > length) {
				owningScene.setTexture(object, textures[textures.length - 1]);
				return true;
			}
		}
		return false;
	}

	@Override
	public void reverse() {
		// reverse = !reverse;
		elapsed = 0;
	}

	@Override
	public boolean doesLoop() {
		return loop;
	}

	@Override
	public void callCallback() {
		if (callback != null)
			callback.call();
	}

}
