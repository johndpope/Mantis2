package com.aftershock.mantis.scene;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

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

public class MInputSystem extends InputListener {

	private MInputDispatcher dispatcher;

	public MInputSystem(MInputDispatcher inputDispatcher) {
		dispatcher = inputDispatcher;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		dispatcher.touch(event, x, y, pointer, button, false);
		return true;
	}

	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		dispatcher.touchUp(event, x, y, pointer, button);
	}

	@Override
	public void touchDragged(InputEvent event, float x, float y, int pointer) {
		dispatcher.touch(event, x, y, pointer, 0, true);
	}

	@Override
	public boolean keyDown(InputEvent event, int keycode) {
		dispatcher.key(event, keycode, ' ', true, false);
		return true;

	}

	@Override
	public boolean keyUp(InputEvent event, int keycode) {
		dispatcher.key(event, keycode, ' ', false, false);
		return true;

	}

	@Override
	public boolean keyTyped(InputEvent event, char key) {
		dispatcher.key(event, 0, key, false, true);
		return false;

	}

	@Override
	public boolean scrolled(InputEvent event, float x, float y, int amt) {
		dispatcher.scrolled(event, x, y, amt);
		return false;
	}

}
