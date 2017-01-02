package com.aftershock.mantis.scene.animation;

import com.aftershock.mantis.MCallback;
import com.aftershock.mantis.scene.MAnimation;

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

public class MCallbackAnim implements MAnimation {

	public float elapsed = 0.0f, length;
	public MCallback callback;

	public MCallbackAnim(float len, MCallback cb) {
		length = len;
		callback = cb;
	}

	@Override
	public boolean tick(float delta) {
		elapsed += delta;
		return elapsed > length;
	}

	@Override
	public void reverse() {

	}

	@Override
	public boolean doesLoop() {
		return false;
	}

	@Override
	public void callCallback() {
		if (callback != null)
			callback.call();
	}

}
