package com.aftershock.mantis.scene.util;

import com.aftershock.mantis.scene.MUIInputHandler;
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

public class MUIRef {

	private MUIPane _owningPane;
	private String _name;
	private ElementType _type;

	public MUIRef(String name, MUIPane owningPane, ElementType etype) {
		_name = name;
		_owningPane = owningPane;
		_type = etype;
	}

	/**
	 * Sets the position of the referenced element.
	 * 
	 * @param x
	 *            The new X position.
	 * @param y
	 *            The new Y position.
	 */
	public void setPos(float x, float y) {
		switch (_type) {
		case BUTTON:
			_owningPane.setButtonPos(_name, x, y);
			break;
		case CHECKBOX:
			_owningPane.setCheckboxPos(_name, x, y);
			break;
		case GROUP:
			_owningPane.setGroupPos(_name, x, y);
			break;
		case IMAGE:
			_owningPane.setImagePos(_name, x, y);
			break;
		case LABEL:
			_owningPane.setLabelPos(_name, x, y);
			break;
		case PBAR:
			_owningPane.setProgressBarPos(_name, x, y);
			break;
		case SLIDER:
			_owningPane.setSliderPos(_name, x, y);
			break;
		default:
			break;
		}
	}

	/**
	 * Sets the position of the referenced element.
	 * 
	 * @param pos
	 *            The new position.
	 */
	public void setPos(Vector2 pos) {
		switch (_type) {
		case BUTTON:
			_owningPane.setButtonPos(_name, pos);
			break;
		case CHECKBOX:
			_owningPane.setCheckboxPos(_name, pos);
			break;
		case GROUP:
			_owningPane.setGroupPos(_name, pos);
			break;
		case IMAGE:
			_owningPane.setImagePos(_name, pos);
			break;
		case LABEL:
			_owningPane.setLabelPos(_name, pos);
			break;
		case PBAR:
			_owningPane.setProgressBarPos(_name, pos);
			break;
		case SLIDER:
			_owningPane.setSliderPos(_name, pos);
			break;
		default:
			break;
		}
	}

	/**
	 * Sets the size of the referenced element.
	 * 
	 * @param w
	 *            The new width.
	 * @param h
	 *            The new height.
	 */
	public void setSize(float w, float h) {
		switch (_type) {
		case BUTTON:
			_owningPane.setButtonSize(_name, w, h);
			break;
		case CHECKBOX:
			_owningPane.setCheckboxSize(_name, w, h);
			break;
		case GROUP:
			_owningPane.setGroupSize(_name, w, h);
			break;
		case IMAGE:
			_owningPane.setImageSize(_name, w, h);
			break;
		case LABEL:
			_owningPane.setLabelSize(_name, w, h);
			break;
		case PBAR:
			_owningPane.setProgressBarSize(_name, w, h);
			break;
		case SLIDER:
			_owningPane.setSliderSize(_name, w, h);
			break;
		default:
			break;
		}
	}

	/**
	 * Sets the size of the referenced element.
	 * 
	 * @param size
	 *            The new size.
	 */
	public void setSize(Vector2 size) {
		switch (_type) {
		case BUTTON:
			_owningPane.setButtonSize(_name, size);
			break;
		case CHECKBOX:
			_owningPane.setCheckboxSize(_name, size);
			break;
		case GROUP:
			_owningPane.setGroupSize(_name, size);
			break;
		case IMAGE:
			_owningPane.setImageSize(_name, size);
			break;
		case LABEL:
			_owningPane.setLabelSize(_name, size);
			break;
		case PBAR:
			_owningPane.setProgressBarSize(_name, size);
			break;
		case SLIDER:
			_owningPane.setSliderSize(_name, size);
			break;
		default:
			break;
		}
	}

	/**
	 * Sets the text content of the referenced button or label.
	 * 
	 * @param text
	 *            The new text of the referenced button or label.
	 */
	public void setText(String text) {
		switch (_type) {
		case BUTTON:
			_owningPane.setButtonText(_name, text);
			break;
		case LABEL:
			_owningPane.setLabelText(_name, text);
			break;
		default:
			return;
		}
	}

	/**
	 * Sets the visibility of the referenced element.
	 * 
	 * @param visable
	 *            Whether or not the referenced element is visible.
	 */
	public void setVisable(boolean visable) {
		switch (_type) {
		case BUTTON:
			_owningPane.setButtonVisible(_name, visable);
			break;
		case CHECKBOX:
			_owningPane.setCheckboxVisible(_name, visable);
			break;
		case GROUP:
			_owningPane.setGroupVisible(_name, visable);
			break;
		case IMAGE:
			_owningPane.setImageVisible(_name, visable);
			break;
		case LABEL:
			_owningPane.setLabelVisible(_name, visable);
			break;
		case PBAR:
			_owningPane.setProgressBarVisible(_name, visable);
			break;
		case SLIDER:
			_owningPane.setSliderVisible(_name, visable);
			break;
		default:
			break;
		}
	}

	/**
	 * Sets the opacity of the referenced element.
	 * 
	 * @param opacity
	 *            The opacity of the referenced element.
	 */
	public void setOpacity(float opacity) {
		switch (_type) {
		case BUTTON:
			_owningPane.setButtonOpacity(_name, opacity);
			break;
		case CHECKBOX:
			_owningPane.setCheckboxOpacity(_name, opacity);
			break;
		case GROUP:
			_owningPane.setGroupOpacity(_name, opacity);
			break;
		case IMAGE:
			_owningPane.setImageOpacity(_name, opacity);
			break;
		case LABEL:
			_owningPane.setLabelOpacity(_name, opacity);
			break;
		case PBAR:
			_owningPane.setProgressBarOpacity(_name, opacity);
			break;
		case SLIDER:
			_owningPane.setSliderOpacity(_name, opacity);
			break;
		default:
			break;
		}
	}

	/**
	 * Sets the padding of the referenced element (if the element is a button).
	 * 
	 * @param l
	 *            The left padding of the referenced element.
	 * @param r
	 *            The right padding of the referenced element.
	 * @param t
	 *            The top padding of the referenced element.
	 * @param b
	 *            The bottom padding of the referenced element.
	 */
	public void setPad(float l, float r, float t, float b) {
		switch (_type) {
		case BUTTON:
			_owningPane.setButtonPad(_name, l, r, t, b);
		default:
			return;
		}
	}

	/**
	 * Sets the checked value of the referenced element (if the element is a
	 * button or checkbox).
	 * 
	 * @param value
	 *            The checked value.
	 */
	public void setValB(boolean value) {
		switch (_type) {
		case BUTTON:
			_owningPane.setButtonValue(_name, value);
			break;
		case CHECKBOX:
			_owningPane.setCheckboxValue(_name, value);
			break;
		default:
			return;
		}
	}

	/**
	 * Sets the float value of the referenced element (if the element is a
	 * progress bar or slider.
	 * 
	 * @param value
	 *            The float value of the referenced element.
	 */
	public void setVal(float value) {
		switch (_type) {
		case PBAR:
			_owningPane.setProgressBarValue(_name, value);
			break;
		case SLIDER:
			_owningPane.setSliderValue(_name, value);
			break;
		default:
			return;
		}
	}

	/**
	 * Adds a listener to the referenced element (if the element is a button,
	 * checkbox, or slider).
	 * 
	 * @param listener
	 *            The referenced element's listener.
	 */
	public void addListener(MUIInputHandler listener) {
		switch (_type) {
		case BUTTON:
			_owningPane.addButtonListener(_name, listener);
			break;
		case CHECKBOX:
			_owningPane.addCheckboxListener(_name, listener);
			break;
		case SLIDER:
			_owningPane.addSliderListener(_name, listener);
			break;
		default:
			return;
		}
	}

	/**
	 * Adds the referenced element to a group.
	 * 
	 * @param group
	 *            The name of the group to add to.
	 */
	public void addToGroup(String group) {
		_owningPane.addElementToGroup(group, _type, _name);
	}

	/**
	 * Removes the referenced element from the pane.
	 */
	public void remove() {
		switch (_type) {
		case BUTTON:
			_owningPane.removeButton(_name);
			break;
		case CHECKBOX:
			_owningPane.removeCheckbox(_name);
			break;
		case GROUP:
			_owningPane.removeGroup(_name);
			break;
		case IMAGE:
			_owningPane.removeImage(_name);
			break;
		case LABEL:
			_owningPane.removeLabel(_name);
			break;
		case PBAR:
			_owningPane.removeProgressBar(_name);
			break;
		case SLIDER:
			_owningPane.removeSlider(_name);
			break;
		default:
			break;
		}
	}

	/**
	 * Checks whether or not the referenced object exists.
	 * 
	 * @return Whether or not the referenced element exists.
	 */
	public boolean doesExist() {
		return _owningPane.doesUIElementExist(_name);
	}

	/**
	 * Gets the position of the referenced element.
	 * 
	 * @return The position of the referenced element.
	 */
	public Vector2 getPos() {
		switch (_type) {
		case BUTTON:
			return _owningPane.getButtonPos(_name);
		case CHECKBOX:
			return _owningPane.getCheckboxPos(_name);
		case GROUP:
			return _owningPane.getGroupPos(_name);
		case IMAGE:
			return _owningPane.getImagePos(_name);
		case LABEL:
			return _owningPane.getLabelPos(_name);
		case PBAR:
			return _owningPane.getProgressBarPos(_name);
		case SLIDER:
			return _owningPane.getSliderPos(_name);
		default:
			return new Vector2(0.0f, 0.0f);
		}
	}

	/**
	 * Gets the opacity of the referenced element.
	 * 
	 * @return The referenced element's opacity.
	 */
	public float getOpacity() {
		switch (_type) {
		case BUTTON:
			return _owningPane.getButtonOpacity(_name);
		case CHECKBOX:
			return _owningPane.getCheckboxOpacity(_name);
		case GROUP:
			return _owningPane.getGroupOpacity(_name);
		case IMAGE:
			return _owningPane.getImageOpacity(_name);
		case LABEL:
			return _owningPane.getLabelOpacity(_name);
		case PBAR:
			return _owningPane.getProgressBarOpacity(_name);
		case SLIDER:
			return _owningPane.getSliderOpacity(_name);
		default:
			return 0.0f;
		}
	}

	/**
	 * Gets the size of the referenced element.
	 * 
	 * @return The referenced element's size.
	 */
	public Vector2 getSize() {
		switch (_type) {
		case BUTTON:
			return _owningPane.getButtonSize(_name);
		case CHECKBOX:
			return _owningPane.getCheckboxSize(_name);
		case GROUP:
			return _owningPane.getGroupSize(_name);
		case IMAGE:
			return _owningPane.getImageSize(_name);
		case LABEL:
			return _owningPane.getLabelSize(_name);
		case PBAR:
			return _owningPane.getProgressBarSize(_name);
		case SLIDER:
			return _owningPane.getSliderSize(_name);
		default:
			return new Vector2(0.0f, 0.0f);
		}
	}

	/**
	 * Checks the visibility of the referenced element.
	 * 
	 * @return Whether or not the referenced element is visible.
	 */
	public boolean isVisible() {
		switch (_type) {
		case BUTTON:
			return _owningPane.isButtonVisible(_name);
		case CHECKBOX:
			return _owningPane.isCheckboxVisible(_name);
		case GROUP:
			return _owningPane.isGroupVisible(_name);
		case IMAGE:
			return _owningPane.isImageVisible(_name);
		case LABEL:
			return _owningPane.isLabelVisible(_name);
		case PBAR:
			return _owningPane.isProgressBarVisible(_name);
		case SLIDER:
			return _owningPane.isSliderVisible(_name);
		default:
			return false;
		}
	}

	/**
	 * Gets the text of the referenced element.
	 * 
	 * @return The referenced object's text.
	 */
	public String getText() {
		switch (_type) {
		case BUTTON:
			return _owningPane.getButtonText(_name);
		case LABEL:
			return _owningPane.getLabelText(_name);
		default:
			return "";
		}
	}

	/**
	 * Gets the float value of the referenced element (if the element is a
	 * progress bar or a slider).
	 * 
	 * @return The referenced element's float value.
	 */
	public float getVal() {
		switch (_type) {
		case PBAR:
			return _owningPane.getProgressBarValue(_name);
		case SLIDER:
			return _owningPane.getSliderValue(_name);
		default:
			return 0.0f;
		}
	}

	/**
	 * Checks whether or not the referenced element is checked (if the element is a
	 * button or checkbox).
	 * 
	 * @return The boolean value of the referenced element.
	 */
	public boolean getValB() {
		switch (_type) {
		case BUTTON:
			return _owningPane.getButtonValue(_name);
		case CHECKBOX:
			return _owningPane.getCheckboxValue(_name);
		default:
			return false;
		}
	}

}
