package com.mantis.scene.animation;

import com.badlogic.gdx.graphics.Color;
import com.mantis.MCallback;
import com.mantis.scene.MAnimation;
import com.mantis.scene.MScene2D;

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

public class MLightColorAnim implements MAnimation {

	Color col0, col1;
	public float elapsed = 0.0f, length;
	boolean reverse = false;
	boolean loop = false;
	public MCallback callback;
	String light;
	MScene2D owningScene;

	public MLightColorAnim(String animLight, Color start, Color end, float len, boolean loopAnim, MCallback cback,
			MScene2D scene) {
		light = animLight;
		col0 = start;
		col1 = end;
		length = len;
		loop = loopAnim;
		callback = cback;
		owningScene = scene;
	}

	@Override
	public boolean tick(float delta) {
		delta /= owningScene.getTimeScale();

		float alpha = elapsed / length;
		if (owningScene.doesLightExist(light))
			owningScene.setLightColor(light, col0.lerp(col1, alpha));
		if (reverse)
			elapsed -= delta;
		else
			elapsed += delta;

		if (reverse) {
			if (elapsed < 0) {
				owningScene.setLightColor(light, col0);
				return true;
			}
		} else {
			if (elapsed > length) {
				owningScene.setLightColor(light, col1);
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
