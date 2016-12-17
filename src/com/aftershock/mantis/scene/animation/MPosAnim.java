package com.aftershock.mantis.scene.animation;

import com.aftershock.mantis.MCallback;
import com.aftershock.mantis.scene.MAnimation;
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

public class MPosAnim implements MAnimation {

	Vector2 pos0, pos1;
	public float length, elapsed = 0.0f;
	String object;
	boolean reverse = false;
	boolean loop = false;
	public MCallback callback;
	MScene2D owningScene;

	public MPosAnim(String obj, Vector2 begin, Vector2 end, float len, boolean loopAnim, MCallback cback,
			MScene2D scene) {
		pos0 = begin;
		pos1 = end;
		length = len;
		object = obj;
		loop = loopAnim;
		callback = cback;
		owningScene = scene;
	}

	@Override
	public boolean tick(float delta) {
		delta /= owningScene.getTimeScale();

		Vector2 pos0Copy = new Vector2(pos0.x, pos0.y);
		Vector2 pos1Copy = new Vector2(pos1.x, pos1.y);
		pos0Copy.lerp(pos1Copy, elapsed / length);
		if (owningScene.doesGObjectExist(object))
			owningScene.setGObjectPos(object, pos0Copy.x, pos0Copy.y);
		else
			return true;
		if (reverse)
			elapsed -= delta;
		else
			elapsed += delta;
		if (reverse) {
			if (elapsed < 0) {
				owningScene.setGObjectPos(object, pos0.x, pos0.y);
				return true;
			}
		} else {
			if (elapsed > length) {
				owningScene.setGObjectPos(object, pos1.x, pos1.y);
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
