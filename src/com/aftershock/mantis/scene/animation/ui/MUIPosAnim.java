package com.aftershock.mantis.scene.animation.ui;

import com.aftershock.mantis.MCallback;
import com.aftershock.mantis.scene.MAnimation;
import com.aftershock.mantis.scene.MUIPane;
import com.aftershock.mantis.scene.MUIPane.ElementType;
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

public class MUIPosAnim implements MAnimation {

	Vector2 pos0, pos1;
	public float length, elapsed = 0.0f;
	String object;
	boolean reverse = false;
	boolean loop = false;
	public MCallback callback;
	ElementType type;
	MUIPane owningPane;

	public MUIPosAnim(String obj, ElementType elemType, Vector2 begin, Vector2 end, float len, boolean loopAnim,
			MCallback cb, MUIPane pane) {
		object = obj;
		type = elemType;
		pos0 = begin;
		pos1 = end;
		length = len;
		loop = loopAnim;
		callback = cb;
		owningPane = pane;
	}

	@Override
	public boolean tick(float delta) {
		Vector2 pos0Copy = new Vector2(pos0.x, pos0.y);
		Vector2 pos1Copy = new Vector2(pos1.x, pos1.y);
		pos0Copy.lerp(pos1Copy, elapsed / length);
		if (!owningPane.doesUIElementExist(object))
			return true;
		switch (type) {
		case BUTTON:
			owningPane.setButtonPos(object, pos0Copy);
			break;
		case LABEL:
			owningPane.setLabelPos(object, pos0Copy);
			break;
		case PBAR:
			owningPane.setProgressBarPos(object, pos0Copy);
			break;
		case SLIDER:
			owningPane.setSliderPos(object, pos0Copy);
			break;
		case CHECKBOX:
			owningPane.setCheckboxPos(object, pos0Copy);
			break;
		case GROUP:
			owningPane.setGroupPos(object, pos0Copy);
			break;
		case IMAGE:
			owningPane.setImagePos(object, pos0Copy);
			break;
		default:
			break;
		}
		if (reverse)
			elapsed -= delta;
		else
			elapsed += delta;
		if (reverse) {
			if (elapsed < 0) {
				switch (type) {
				case BUTTON:
					owningPane.setButtonPos(object, pos0);
					break;
				case LABEL:
					owningPane.setLabelPos(object, pos0);
					break;
				case PBAR:
					owningPane.setProgressBarPos(object, pos0);
					break;
				case SLIDER:
					owningPane.setSliderPos(object, pos0);
					break;
				case CHECKBOX:
					owningPane.setCheckboxPos(object, pos0);
					break;
				case GROUP:
					owningPane.setGroupPos(object, pos0);
					break;
				case IMAGE:
					owningPane.setImagePos(object, pos0);
					break;
				default:
					break;
				}
				return true;
			}
		} else {
			if (elapsed > length) {
				switch (type) {
				case BUTTON:
					owningPane.setButtonPos(object, pos1);
					break;
				case LABEL:
					owningPane.setLabelPos(object, pos1);
					break;
				case PBAR:
					owningPane.setProgressBarPos(object, pos1);
					break;
				case SLIDER:
					owningPane.setSliderPos(object, pos1);
					break;
				case CHECKBOX:
					owningPane.setCheckboxPos(object, pos1);
					break;
				case GROUP:
					owningPane.setGroupPos(object, pos1);
					break;
				case IMAGE:
					owningPane.setImagePos(object, pos1);
					break;
				default:
					break;
				}
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
