package com.aftershock.mantis.scene.animation;

import com.aftershock.mantis.MCallback;
import com.aftershock.mantis.scene.MAnimation;
import com.aftershock.mantis.scene.MScene2D;

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

public class MOpacityAnim implements MAnimation {

	float opacity0, opacity1;
	public float elapsed = 0.0f, length;
	String object;
	boolean reverse = false;
	boolean loop = false;
	public MCallback callback;
	MScene2D owningScene;

	public MOpacityAnim(String obj, float start, float end, float len, boolean loopAnim, MCallback cback,
			MScene2D scene) {
		opacity0 = start;
		opacity1 = end;
		object = obj;
		length = len;
		loop = loopAnim;
		callback = cback;
		owningScene = scene;
	}

	@Override
	public boolean tick(float delta) {
		delta /= owningScene.getTimeScale();

		float alpha = elapsed / length;
		if (owningScene.doesGObjectExist(object))
			owningScene.setGObjectOpacity(object, (1.0f - alpha) * opacity0 + alpha * opacity1);
		else
			return true;
		if (reverse)
			elapsed -= delta;
		else
			elapsed += delta;

		if (reverse) {
			if (elapsed < 0) {
				owningScene.setGObjectOpacity(object, opacity0);
				return true;
			}
		} else {
			if (elapsed > length) {
				owningScene.setGObjectOpacity(object, opacity1);
				return true;
			}
		}
		return false;
	}

	@Override
	public void reverse() {
		reverse = !reverse;
	}

	public boolean doesLoop() {
		return loop;
	}

	@Override
	public void callCallback() {
		if (callback != null)
			callback.call();
	}
}
