package com.aftershock.mantis.scene.animation.ui;

import com.aftershock.mantis.MCallback;
import com.aftershock.mantis.scene.MAnimation;
import com.aftershock.mantis.scene.MUIPane;
import com.aftershock.mantis.scene.MUIPane.ElementType;

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

public class MUIOpacityAnim implements MAnimation {

	float opacity0, opacity1;
	public float elapsed = 0.0f, length;
	String object;
	boolean reverse = false;
	boolean loop = false;
	public MCallback callback;
	ElementType type;
	MUIPane owningPane;

	public MUIOpacityAnim(String obj, ElementType elemType, float start, float end, float len, boolean loopAnim,
			MCallback cback, MUIPane pane) {
		opacity0 = start;
		opacity1 = end;
		object = obj;
		type = elemType;
		length = len;
		loop = loopAnim;
		callback = cback;
		owningPane = pane;
	}

	@Override
	public boolean tick(float delta) {
		if (reverse)
			elapsed -= delta;
		else
			elapsed += delta;

		float alpha = elapsed / length;
		if (!owningPane.doesUIElementExist(object))
			return true;
		float lerpAlpha = (1.0f - alpha) * opacity0 + alpha * opacity1;
		switch (type) {
		case BUTTON:
			owningPane.setButtonOpacity(object, lerpAlpha);
			break;
		case LABEL:
			owningPane.setLabelOpacity(object, lerpAlpha);
			break;
		case PBAR:
			owningPane.setProgressBarOpacity(object, lerpAlpha);
			break;
		case SLIDER:
			owningPane.setSliderOpacity(object, lerpAlpha);
			break;
		case CHECKBOX:
			owningPane.setCheckboxOpacity(object, lerpAlpha);
			break;
		case GROUP:
			owningPane.setGroupOpacity(object, lerpAlpha);
			break;
		case IMAGE:
			owningPane.setImageOpacity(object, lerpAlpha);
			break;
		default:
			break;
		}
		if (reverse) {
			if (elapsed < 0) {
				switch (type) {
				case BUTTON:
					owningPane.setButtonOpacity(object, opacity0);
					break;
				case LABEL:
					owningPane.setLabelOpacity(object, opacity0);
					break;
				case PBAR:
					owningPane.setProgressBarOpacity(object, opacity0);
					break;
				case SLIDER:
					owningPane.setSliderOpacity(object, opacity0);
					break;
				case CHECKBOX:
					owningPane.setCheckboxOpacity(object, opacity0);
					break;
				case GROUP:
					owningPane.setGroupOpacity(object, opacity0);
					break;
				case IMAGE:
					owningPane.setImageOpacity(object, opacity0);
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
					owningPane.setButtonOpacity(object, opacity1);
					break;
				case LABEL:
					owningPane.setLabelOpacity(object, opacity1);
					break;
				case PBAR:
					owningPane.setProgressBarOpacity(object, opacity1);
					break;
				case SLIDER:
					owningPane.setSliderOpacity(object, opacity1);
					break;
				case CHECKBOX:
					owningPane.setCheckboxOpacity(object, opacity1);
					break;
				case GROUP:
					owningPane.setGroupOpacity(object, opacity1);
					break;
				case IMAGE:
					owningPane.setImageOpacity(object, opacity1);
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
