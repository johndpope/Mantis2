package com.mantis.scene.animation.ui;

import com.mantis.MCallback;
import com.mantis.scene.MAnimation;
import com.mantis.scene.MUIPane;

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

public class MUIButtonTypeAnim implements MAnimation {

	String text;
	public float elapsed = 0.0f, length;
	String object;
	boolean reverse = false;
	boolean loop = false;
	public MCallback callback;
	MUIPane owningPane;

	public MUIButtonTypeAnim(String name, String typeText, float len, boolean loopAnim, MCallback cb, MUIPane pane) {
		object = name;
		length = len;
		loop = loopAnim;
		text = typeText;
		callback = cb;
		owningPane = pane;
	}

	@Override
	public boolean tick(float delta) {
		float alpha = elapsed / length;
		int charI = (int) Math.ceil((1.0f - alpha) * 0.0f + alpha * text.length());
		if (owningPane.doesUIElementExist(object))
			owningPane.setButtonText(object, text.substring(0, charI));
		else
			return true;
		if (reverse)
			elapsed -= delta;
		else
			elapsed += delta;

		if (reverse) {
			if (elapsed < 0) {
				owningPane.setLabelText(object, "");
				return true;
			}
		} else {
			if (elapsed > length) {
				owningPane.setLabelText(object, text);
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
