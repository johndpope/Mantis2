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

public class MRotationAnim implements MAnimation {

	float rotation0, rotation1;
	public float elapsed = 0.0f, length;
	String object;
	boolean reverse = false;
	boolean loop = false;
	public MCallback callback;
	MScene2D owningScene;

	public MRotationAnim(String obj, float start, float end, float len, boolean loopAnim, MCallback cback,
			MScene2D scene) {
		rotation0 = start;
		rotation1 = end;
		length = len;
		loop = loopAnim;
		object = obj;
		callback = cback;
		owningScene = scene;
	}

	@Override
	public boolean tick(float delta) {
		delta /= owningScene.getTimeScale();

		float alpha = elapsed / length;
		if (owningScene.doesGObjectExist(object))
			owningScene.setGObjectRot(object, (1.0f - alpha) * rotation0 + alpha * rotation1);
		else
			return true;
		if (reverse)
			elapsed -= delta;
		else
			elapsed += delta;

		if (reverse) {
			if (elapsed < 0) {
				owningScene.setGObjectRot(object, rotation0);
				return true;
			}
		} else {
			if (elapsed > length) {
				owningScene.setGObjectRot(object, rotation1);
				return true;
			}
		}

		return false;
	}

	@Override
	public void reverse() {
		reverse = !reverse;
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
