package com.mantis.scene;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

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

public class MUIPane extends Stage {

	private HashMap<String, BitmapFont> _fonts = new HashMap<String, BitmapFont>();
	private HashMap<String, Label> _labels = new HashMap<String, Label>();
	private HashMap<String, LabelStyle> _labelStyles = new HashMap<String, LabelStyle>();
	private HashMap<String, TextButtonStyle> _buttonStyles = new HashMap<String, TextButtonStyle>();
	private HashMap<String, TextButton> _buttons = new HashMap<String, TextButton>();
	private HashMap<String, ProgressBarStyle> _pbarStyles = new HashMap<String, ProgressBarStyle>();
	private HashMap<String, ProgressBar> _pbars = new HashMap<String, ProgressBar>();
	private HashMap<String, Boolean> _pbarorients = new HashMap<String, Boolean>();
	private HashMap<String, SliderStyle> _sliderStyles = new HashMap<String, SliderStyle>();
	private HashMap<String, Slider> _sliders = new HashMap<String, Slider>();
	private HashMap<String, CheckBoxStyle> _checkboxStyles = new HashMap<String, CheckBoxStyle>();
	private HashMap<String, CheckBox> _checkboxes = new HashMap<String, CheckBox>();
	private HashMap<String, Group> _groups = new HashMap<String, Group>();
	private HashMap<String, Image> _images = new HashMap<String, Image>();

	public enum ElementType {
		BUTTON, SLIDER, PBAR, LABEL, CHECKBOX, GROUP, IMAGE
	}

	/**
	 * Loads a font from .otf or .ttf file.
	 * 
	 * @param name
	 *            The name of the font.
	 * @param font
	 *            The filename of the font.
	 * @param fontCol
	 *            The base color of the font.
	 * @param borderCol
	 *            The border color of the font.
	 * @param size
	 *            Size of the font.
	 * @param borderWidth
	 *            The width of the font's border.
	 */
	public void loadFont(String name, String font, Color fontCol, Color borderCol, int size, int borderWidth) {
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("assets/ui/font/" + font));
		FreeTypeFontParameter fontParam = new FreeTypeFontParameter();

		fontParam.color = fontCol;
		fontParam.borderColor = borderCol;
		fontParam.size = size;
		fontParam.borderWidth = borderWidth;
		BitmapFont bFont = fontGenerator.generateFont(fontParam);
		_fonts.put(name, bFont);
		fontGenerator.dispose();
	}

	/**
	 * Creates a new label style.
	 * 
	 * @param name
	 *            The name of the style.
	 * @param font
	 *            The style's font.
	 */
	public void createLabelStyle(String name, String font) {
		LabelStyle style = new LabelStyle();
		style.font = _fonts.get(font);
		_labelStyles.put(name, style);
	}

	/**
	 * Creates a label.
	 * 
	 * @param name
	 *            The label name.
	 * @param style
	 *            The style of the label.
	 * @param align
	 *            The alignment of the label's text.
	 */
	public void createLabel(String name, String style, int align) {
		Label label = new Label("", _labelStyles.get(style));
		label.setAlignment(align);
		label.setWrap(true);
		_labels.put(name, label);
		addActor(label);
	}

	/**
	 * Sets the screen position of the label.
	 * 
	 * @param name
	 *            The name of the label to modify.
	 * @param position
	 *            The new position of the label.
	 */
	public void setLabelPos(String name, Vector2 position) {
		_labels.get(name).setPosition(position.x, position.y);
	}

	/**
	 * Sets the screen position of the label.
	 * 
	 * @param name
	 *            Name of the label to modify.
	 * @param x
	 *            The new X position of the label.
	 * @param y
	 *            The new Y position of the label.
	 */
	public void setLabelPos(String name, float x, float y) {
		_labels.get(name).setPosition(x, y);
	}

	/**
	 * Sets the size of a label.
	 * 
	 * @param name
	 *            The name of the label to modify.
	 * @param size
	 *            The size of the label.
	 */
	public void setLabelSize(String name, Vector2 size) {
		_labels.get(name).setSize(size.x, size.y);
	}

	/**
	 * Sets the size of a label.
	 * 
	 * @param name
	 *            The Name of the label to modify.
	 * @param w
	 *            The new width of the label.
	 * @param h
	 *            The new height of the label.
	 */
	public void setLabelSize(String name, float w, float h) {
		_labels.get(name).setSize(w, h);
	}

	/**
	 * Sets the text of a label.
	 * 
	 * @param name
	 *            Name of the label to modify.
	 * @param text
	 *            The new text of the label.
	 */
	public void setLabelText(String name, String text) {
		_labels.get(name).setText(text);
	}

	/**
	 * Gets the size of a label.
	 * 
	 * @param name
	 *            The name of the label to poll.
	 * @return The size of the given label.
	 */
	public Vector2 getLabelSize(String name) {
		return new Vector2(_labels.get(name).getWidth(), _labels.get(name).getHeight());
	}

	/**
	 * Gets the text of a label.
	 * 
	 * @param name
	 *            The name of the label to poll.
	 * @return The text of the given label.
	 */
	public String getLabelText(String name) {
		return _labels.get(name).getText().toString();
	}

	/**
	 * Gets the position of a label.
	 * 
	 * @param name
	 *            The name of the label to poll.
	 * @return The position of the given label.
	 */
	public Vector2 getLabelPos(String name) {
		return new Vector2(_labels.get(name).getX(), _labels.get(name).getY());
	}

	/**
	 * Sets the visibility of a label.
	 * 
	 * @param name
	 *            The name of the label to modify.
	 * @param vis
	 *            Whether or not to show the label.
	 */
	public void setLabelVisable(String name, boolean vis) {
		_labels.get(name).setVisible(vis);
	}

	/**
	 * Sets the opacity of a label.
	 * 
	 * @param name
	 *            The name of the label to modify.
	 * @param opacity
	 *            The new opacity of the label.
	 */
	public void setLabelOpacity(String name, float opacity) {
		Color col = _labels.get(name).getColor();
		_labels.get(name).setColor(col.r, col.g, col.b, opacity);
	}

	/**
	 * Gets the opacity of a label.
	 * 
	 * @param name
	 *            The name of the label to poll.
	 * @return The opacity of the given label.
	 */
	public float getLabelOpacity(String name) {
		return _labels.get(name).getColor().a;
	}

	/**
	 * Gets whether or not a label is visible.
	 * 
	 * @param name
	 *            The name of the label to poll.
	 * @return Whether or not the label is visible.
	 */
	public boolean isLabelVisable(String name) {
		return _labels.get(name).isVisible();
	}

	/**
	 * Creates a label style.
	 * 
	 * @param name
	 *            The name of the label style.
	 * @param drawableUp
	 *            The unpressed drawable of the button.
	 * @param drawableDown
	 *            The pressed drawable of the button.
	 * @param font
	 *            The font of the button style.
	 */
	public void createButtonStyle(String name, String drawableUp, String drawableDown, String font) {
		TextureRegion drawableUpTexReg = new TextureRegion(
				new Texture(Gdx.files.internal("assets/ui/button/" + drawableUp)));
		TextureRegion drawableDownTexReg = new TextureRegion(
				new Texture(Gdx.files.internal("assets/ui/button/" + drawableDown)));

		TextButtonStyle bStyle = new TextButtonStyle(new TextureRegionDrawable(drawableUpTexReg),
				new TextureRegionDrawable(drawableDownTexReg), new TextureRegionDrawable(drawableUpTexReg),
				_fonts.get(font));
		_buttonStyles.put(name, bStyle);
	}

	/**
	 * Creates a label style.
	 * 
	 * @param name
	 *            The name of the label style.
	 * @param drawableUp
	 *            The unpressed drawable of the button.
	 * @param drawableDown
	 *            The pressed drawable of the button.
	 * @param font
	 *            The font of the button style.
	 * @param toggle
	 *            Whether or not to create a toggle button style.
	 */
	public void createButtonStyle(String name, String drawableUp, String drawableDown, String font, boolean toggle) {
		if (toggle) {
			TextureRegion drawableUpTexReg = new TextureRegion(
					new Texture(Gdx.files.internal("assets/ui/button/" + drawableUp)));
			TextureRegion drawableDownTexReg = new TextureRegion(
					new Texture(Gdx.files.internal("assets/ui/button/" + drawableDown)));

			TextButtonStyle bStyle = new TextButtonStyle(new TextureRegionDrawable(drawableUpTexReg),
					new TextureRegionDrawable(drawableDownTexReg), new TextureRegionDrawable(drawableDownTexReg),
					_fonts.get(font));
			_buttonStyles.put(name, bStyle);
		} else {
			createButtonStyle(name, drawableUp, drawableDown, font);
		}
	}

	/**
	 * Creates a button.
	 * 
	 * @param name
	 *            The bame of the button.
	 * @param style
	 *            The style of the button.
	 * @param text
	 *            The text of the button.
	 */
	public void createButton(String name, String style, String text) {
		TextButton button = new TextButton(text, _buttonStyles.get(style));
		button.setName(name);
		_buttons.put(name, button);
		addActor(button);
	}

	/**
	 * Sets the text alignment of a button.
	 * 
	 * @param name
	 *            The button to be modified.
	 * @param align
	 *            The new alignment to be set.
	 */
	public void setButtonLabelAlign(String name, int align) {
		_buttons.get(name).getLabel().setAlignment(align);
	}

	/**
	 * Sets the padding of a button.
	 * 
	 * @param name
	 *            The name of the button to be modified.
	 * @param padLeft
	 *            Left padding.
	 * @param padRight
	 *            Right padding.
	 * @param padTop
	 *            Top padding.
	 * @param padBottom
	 *            Bottom padding.
	 */
	public void setButtonPad(String name, float padLeft, float padRight, float padTop, float padBottom) {
		_buttons.get(name).pad(padTop, padLeft, padBottom, padRight);
	}

	/**
	 * Sets the opacity of a button.
	 * 
	 * @param name
	 *            The name of the button to modify.
	 * @param opacity
	 *            The opacity to set.
	 */
	public void setButtonOpacity(String name, float opacity) {
		_buttons.get(name).setColor(1.0f, 1.0f, 1.0f, opacity);
	}

	/**
	 * Adds a button listener.
	 * 
	 * @param name
	 *            The name of the button to modify.
	 * @param listener
	 *            The listener to set.
	 */
	public void addButtonListener(String name, MUIInputHandler listener) {
		_buttons.get(name).addListener(new MUIInputListener(listener));
	}

	/**
	 * Sets the position of a button.
	 * 
	 * @param name
	 *            The name of the button to modify.
	 * @param pos
	 *            The position to set.
	 */
	public void setButtonPos(String name, Vector2 pos) {
		_buttons.get(name).setPosition(pos.x, pos.y);
	}

	/**
	 * Sets the position of a button.
	 * 
	 * @param name
	 *            The name of the button to modify.
	 * @param x
	 *            The new X position of the button.
	 * @param y
	 *            The new Y position of the button.
	 */
	public void setButtonPos(String name, float x, float y) {
		_buttons.get(name).setPosition(x, y);
	}

	/**
	 * Sets the size of a button.
	 * 
	 * @param name
	 *            The name of the button to modify.
	 * @param size
	 *            The size to set.
	 */
	public void setButtonSize(String name, Vector2 size) {
		_buttons.get(name).setSize(size.x, size.y);
	}

	/**
	 * Sets whether or not a button is toggled.
	 * 
	 * @param name
	 *            The name of the button to modify.
	 * @param value
	 *            Whether or not the given button is checked.
	 */
	public void setButtonValue(String name, boolean value) {
		_buttons.get(name).setChecked(value);
	}

	/**
	 * Checks whether or not a button is toggled.
	 * 
	 * @param name
	 *            The name of the button to poll.
	 * @return Whether or not the given button is toggled.
	 */
	public boolean getButtonValue(String name) {
		return _buttons.get(name).isChecked();
	}

	/**
	 * Sets the size of a button.
	 * 
	 * @param name
	 *            The name of the button to modify.
	 * @param w
	 *            The new width of the button.
	 * @param h
	 *            The new height of the button.
	 */
	public void setButtonSize(String name, float w, float h) {
		_buttons.get(name).setSize(w, h);
	}

	/**
	 * Sets the text of a button.
	 * 
	 * @param name
	 *            The name of the button to modify.
	 * @param text
	 *            The text to set.
	 */
	public void setButtonText(String name, String text) {
		_buttons.get(name).setText(text);
	}

	/**
	 * Gets the text of a button.
	 * 
	 * @param name
	 *            The name of the button to poll.
	 * @return The text of the given button.
	 */
	public String getButtonText(String name) {
		return (String) _buttons.get(name).getText();
	}

	/**
	 * Gets the opacity of a button.
	 * 
	 * @param name
	 *            The name of the button to poll.
	 * @return The opacity of the given button.
	 */
	public float getButtonOpacity(String name) {
		return _buttons.get(name).getColor().a;
	}

	/**
	 * Gets the size of a button.
	 * 
	 * @param name
	 *            The name of the button to poll.
	 * @return The size of the given button.
	 */
	public Vector2 getButtonSize(String name) {
		return new Vector2(_buttons.get(name).getWidth(), _buttons.get(name).getHeight());
	}

	/**
	 * Gets the position of a button.
	 * 
	 * @param name
	 *            The name of the button to poll.
	 * @return The position of the given button.
	 */
	public Vector2 getButtonPos(String name) {
		return new Vector2(_buttons.get(name).getX(), _buttons.get(name).getY());
	}

	/**
	 * Sets the visibility of a button.
	 * 
	 * @param name
	 *            The name of the button to modify.
	 * @param vis
	 *            The new visibility of the button.
	 */
	public void setButtonVisable(String name, boolean vis) {
		_buttons.get(name).setVisible(vis);
	}

	/**
	 * Gets the visibility of the button.
	 * 
	 * @param name
	 *            The name of the button to poll.
	 * @return The visibility of the given button.
	 */
	public boolean isButtonVisable(String name) {
		return _buttons.get(name).isVisible();
	}

	/**
	 * Removes a button from the pane.
	 * 
	 * @param name
	 *            The button to remove.
	 */
	public void removeButton(String name) {
		_buttons.get(name).remove();
		_buttons.remove(name);
	}

	/**
	 * Creates a progress bar style.
	 * 
	 * @param name
	 *            Name of the style.
	 * @param drawable
	 *            The background drawable of the progress bar.
	 * @param knob
	 *            The knob drawable for the progress bar.
	 */
	public void createProgressBarStyle(String name, String drawable, String knob) {
		TextureRegion pbarbg = new TextureRegion(new Texture(Gdx.files.internal("assets/ui/progressbar/" + drawable)));
		TextureRegion pbarknob = new TextureRegion(new Texture(Gdx.files.internal("assets/ui/progressbar/" + knob)));
		ProgressBarStyle pbStyle = new ProgressBarStyle();
		pbStyle.knob = new TextureRegionDrawable(pbarknob);
		pbStyle.knobBefore = new TextureRegionDrawable(pbarknob);
		pbStyle.background = new TextureRegionDrawable(pbarbg);

		_pbarStyles.put(name, pbStyle);
	}

	/**
	 * Creates a progress bar.
	 * 
	 * @param name
	 *            Name of the progress bar.
	 * @param style
	 *            The progress bar style.
	 * @param min
	 *            Minimum value for the progress bar.
	 * @param max
	 *            Maximum value for the progress bar.
	 * @param step
	 *            The progress step.
	 * @param defaultVal
	 *            The initial value of the progress bar.
	 * @param vertical
	 *            Whether or not to draw the progress bar vertically.
	 */
	public void createProgressBar(String name, String style, float min, float max, float step, float defaultVal,
			boolean vertical) {
		ProgressBar pbar = new ProgressBar(min, max, step, vertical, _pbarStyles.get(style));
		pbar.setName(name);
		pbar.setValue(defaultVal);
		_pbars.put(name, pbar);
		_pbarorients.put(name, vertical);
		addActor(pbar);
	}

	/**
	 * Sets the opacity of a progress bar.
	 * 
	 * @param name
	 *            The progress bar to modify.
	 * @param opacity
	 *            The opacity to set.s
	 */
	public void setProgressBarOpacity(String name, float opacity) {
		_pbars.get(name).setColor(1.0f, 1.0f, 1.0f, opacity);
	}

	/**
	 * Sets the value of a progress bar.
	 * 
	 * @param name
	 *            The name of the progress bar to modify.
	 * @param val
	 *            The value of the given progress bar.
	 */
	public void setProgressBarVal(String name, float val) {
		_pbars.get(name).setValue(val);
	}

	/**
	 * Sets the position of a progress bar.
	 * 
	 * @param name
	 *            The name of the progress bar to modify.
	 * @param pos
	 *            The position to set.
	 */
	public void setProgressBarPos(String name, Vector2 pos) {
		_pbars.get(name).setPosition(pos.x, pos.y);
	}

	/**
	 * Sets the position of a progress bar.
	 * 
	 * @param name
	 *            The name of the progress bar to modify.
	 * @param x
	 *            The new X position of the progress bar.
	 * @param y
	 *            The new Y position of the progress bar.
	 */
	public void setProgressBarPos(String name, float x, float y) {
		_pbars.get(name).setPosition(x, y);
	}

	/**
	 * Sets the size of a progress bar.
	 * 
	 * @param name
	 *            The name of the progress bar to modify.
	 * @param size
	 *            The size to set.
	 */
	public void setProgressBarSize(String name, Vector2 size) {
		setProgressBarSize(name, size.x, size.y);
	}

	/**
	 * Sets the size of a progress bar.
	 * 
	 * @param name
	 *            The name of the progress bar to modify.
	 * @param w
	 *            The new width of the progress bar.
	 * @param h
	 *            The new height of the progress bar.
	 */
	public void setProgressBarSize(String name, float w, float h) {
		_pbars.get(name).getStyle().background.setMinWidth(w);
		_pbars.get(name).getStyle().background.setMinHeight(h);
		if (_pbarorients.get(name))
			_pbars.get(name).getStyle().knob.setMinWidth(w);
		else
			_pbars.get(name).getStyle().knob.setMinHeight(h);
		_pbars.get(name).getStyle().knobBefore.setMinWidth(w);
		_pbars.get(name).getStyle().knobBefore.setMinHeight(h);
		_pbars.get(name).setWidth(w);
		_pbars.get(name).setHeight(h);
	}

	/**
	 * Gets the value of a progress bar.
	 * 
	 * @param name
	 *            The name of the progress bar to poll.
	 * @return The value of the given progress bar.
	 */
	public float getProgressBarVal(String name) {
		return _pbars.get(name).getValue();
	}

	/**
	 * Gets the opacity of a progress bar.
	 * 
	 * @param name
	 *            The name of the progress bar to poll.
	 * @return The opacity of the given progress bar.
	 */
	public float getProgressBarOpacity(String name) {
		return _pbars.get(name).getColor().a;
	}

	/**
	 * Gets the position of a progress bar.
	 * 
	 * @param name
	 *            The name of the progress bar to poll.
	 * @return The position of the given progress bar.
	 */
	public Vector2 getProgressBarPos(String name) {
		return new Vector2(_pbars.get(name).getX(), _pbars.get(name).getY());
	}

	/**
	 * Gets the size of a progress bar.
	 * 
	 * @param name
	 *            The name of the progress bar to poll.
	 * @return The size of the given progress bar.
	 */
	public Vector2 getProgressBarSize(String name) {
		return new Vector2(_pbars.get(name).getWidth(), _pbars.get(name).getHeight());
	}

	/**
	 * Sets the visibility of a progress bar.
	 * 
	 * @param name
	 *            The name of the progress bar to set.
	 * @param vis
	 *            The visibility to set.
	 */
	public void setProgressBarVisable(String name, boolean vis) {
		_pbars.get(name).setVisible(vis);
	}

	/**
	 * Gets the visibility of a progress bar.
	 * 
	 * @param name
	 *            The name of the progress bar to poll.
	 * @return Whether or not the progress bar is visible.
	 */
	public boolean isProgressBarVisable(String name) {
		return _pbars.get(name).isVisible();
	}

	/**
	 * Removes a progress bar from the pane.
	 * 
	 * @param name
	 *            The name of the progress bar to remove.
	 */
	public void removeProgressBar(String name) {
		_pbars.get(name).remove();
		_pbars.remove(name);
		_pbarorients.remove(name);
	}

	/**
	 * Creates a slider style.
	 * 
	 * @param name
	 *            The name of the slider style to create.
	 * @param drawable
	 *            The background drawable of the slider.
	 * @param knob
	 *            The knob drawable of the slider.
	 */
	public void createSliderStyle(String name, String drawable, String knob) {
		SliderStyle sliderStyle = new SliderStyle(
				new TextureRegionDrawable(
						new TextureRegion(new Texture(Gdx.files.internal("assets/ui/slider/" + drawable)))),
				new TextureRegionDrawable(
						new TextureRegion(new Texture(Gdx.files.internal("assets/ui/slider/" + knob)))));
		_sliderStyles.put(name, sliderStyle);
	}

	/**
	 * Creates a slider.
	 * 
	 * @param name
	 *            The name of the slider to create.
	 * @param style
	 *            The style of the slider.
	 * @param defaultVal
	 *            The default value of the slider.
	 * @param min
	 *            The minimum value of the slider.
	 * @param max
	 *            The maximum value of the slider.
	 * @param stepSize
	 *            The step size of the slider.
	 * @param vertical
	 *            Whether or not the slider is vertical.
	 */
	public void createSlider(String name, String style, float defaultVal, float min, float max, float stepSize,
			boolean vertical) {
		Slider slider = new Slider(min, max, stepSize, vertical, _sliderStyles.get(style));
		slider.setName(name);
		slider.setValue(defaultVal);
		_sliders.put(name, slider);
		addActor(slider);
	}

	/**
	 * Sets the opacity of a slider.
	 * 
	 * @param name
	 *            The name of the slider to modify.
	 * @param opacity
	 *            The opacity to set.
	 */
	public void setSliderOpacity(String name, float opacity) {
		_sliders.get(name).setColor(1.0f, 1.0f, 1.0f, opacity);
	}

	/**
	 * Sets the value of a slider.
	 * 
	 * @param name
	 *            The name of the slider to modify.
	 * @param val
	 *            The value to set.
	 */
	public void setSliderVal(String name, float val) {
		_sliders.get(name).setValue(val);
	}

	/**
	 * Set the position of a slider.
	 * 
	 * @param name
	 *            The name of the slider to modify.
	 * @param pos
	 *            The position to set.
	 */
	public void setSliderPos(String name, Vector2 pos) {
		_sliders.get(name).setPosition(pos.x, pos.y);
	}

	/**
	 * Sets the position of a slider.
	 * 
	 * @param name
	 *            The name of the slider to modify.
	 * @param x
	 *            The new X position.
	 * @param y
	 *            The new Y position.
	 */
	public void setSliderPos(String name, float x, float y) {
		_sliders.get(name).setPosition(x, y);
	}

	/**
	 * Sets the size of a slider.
	 * 
	 * @param name
	 *            The name of the slider to modify.
	 * @param size
	 *            The size to set.
	 */
	public void setSliderSize(String name, Vector2 size) {
		_sliders.get(name).setSize(size.x, size.y);
	}

	/**
	 * Sets the size of a slider.
	 * 
	 * @param name
	 *            The name of the slider to modify.
	 * @param w
	 *            The new width of the slider.
	 * @param h
	 *            The new height of the slider.
	 */
	public void setSliderSize(String name, float w, float h) {
		_sliders.get(name).setSize(w, h);
	}

	/**
	 * Gets the value of a slider.
	 * 
	 * @param name
	 *            Name of the slider to poll.
	 * @return The value of the given slider.
	 */
	public float getSliderVal(String name) {
		return _sliders.get(name).getValue();
	}

	/**
	 * Gets the opacity of a slider.
	 * 
	 * @param name
	 *            The name of the slider to poll.
	 * @return The opacity of the given slider.
	 */
	public float getSliderOpacity(String name) {
		return _sliders.get(name).getColor().a;
	}

	/**
	 * Gets the position of a slider.
	 * 
	 * @param name
	 *            The name of the slider to poll.
	 * @return The position of the given slider.
	 */
	public Vector2 getSliderPos(String name) {
		return new Vector2(_sliders.get(name).getX(), _sliders.get(name).getY());
	}

	/**
	 * Gets the size of a slider.
	 * 
	 * @param name
	 *            The name of the slider to poll.
	 * @return The size of the given slider.
	 */
	public Vector2 getSliderSize(String name) {
		return new Vector2(_sliders.get(name).getWidth(), _sliders.get(name).getHeight());
	}

	/**
	 * Sets the visibility of a slider.
	 * 
	 * @param name
	 *            The name of the slider to modify.
	 * @param vis
	 *            The visibility to set.
	 */
	public void setSliderVisable(String name, boolean vis) {
		_sliders.get(name).setVisible(vis);
	}

	/**
	 * Gets the visibility of a slider.
	 * 
	 * @param name
	 *            The name of the slider to poll.
	 * @return The visibility of the given slider.
	 */
	public boolean isSliderVisable(String name) {
		return _sliders.get(name).isVisible();
	}

	/**
	 * Removes a slider from the pane.
	 * 
	 * @param name
	 *            The name of the slider to remove.
	 */
	public void removeSlider(String name) {
		_sliders.get(name).remove();
		_sliders.remove(name);
	}

	/**
	 * Adds a listener to a slider.
	 * 
	 * @param name
	 *            Name of the slider to add the listener to.
	 * @param listener
	 *            The listener to add.
	 */
	public void addSliderListener(String name, MUIInputHandler listener) {
		_sliders.get(name).addListener(new MUIInputListener(listener));
	}

	/**
	 * Creates a checkbox style.
	 * 
	 * @param name
	 *            Name of the checkbox style.
	 * @param checkbox
	 *            The checkbox background drawable.
	 * @param check
	 *            The checked drawable.
	 * @param checkOff
	 *            The unchecked drawable.
	 */
	public void createCheckboxStyle(String name, String checkbox, String check, String checkOff) {
		CheckBoxStyle cbStyle = new CheckBoxStyle(
				new TextureRegionDrawable(
						new TextureRegion(new Texture(Gdx.files.internal("assets/ui/checkbox/" + checkOff)))),
				new TextureRegionDrawable(
						new TextureRegion(new Texture(Gdx.files.internal("assets/ui/checkbox/" + check)))),
				new BitmapFont(), Color.BLACK);
		_checkboxStyles.put(name, cbStyle);
	}

	/**
	 * Creates a checkbox.
	 * 
	 * @param name
	 *            The name of the checkbox.
	 * @param style
	 *            The style of the checkbox.
	 * @param defaultVal
	 *            The default value of the checkbox.
	 */
	public void createCheckbox(String name, String style, boolean defaultVal) {
		CheckBox cb = new CheckBox("", _checkboxStyles.get(style));
		_checkboxes.put(name, cb);
		addActor(cb);
	}

	/**
	 * Sets the opacity of a checkbox.
	 * 
	 * @param name
	 *            The name of the checkbox to modify.
	 * @param opacity
	 *            The new opacity of the checkbox.
	 */
	public void setCheckboxOpacity(String name, float opacity) {
		_checkboxes.get(name).setColor(1.0f, 1.0f, 1.0f, opacity);
	}

	/**
	 * Sets the position of a checkbox.
	 * 
	 * @param name
	 *            The name of the checkbox to modify.
	 * @param pos
	 *            The new position of the checkbox.
	 */
	public void setCheckboxPos(String name, Vector2 pos) {
		_checkboxes.get(name).setPosition(pos.x, pos.y);
	}

	/**
	 * Sets the position of a checkbox.
	 * 
	 * @param name
	 *            The name of the checkbox to modify.
	 * @param x
	 *            The new X position.
	 * @param y
	 *            The new Y position.
	 */
	public void setCheckboxPos(String name, float x, float y) {
		_checkboxes.get(name).setPosition(x, y);
	}

	/**
	 * Sets the value of a checkbox.
	 * 
	 * @param name
	 *            The name of the checkbox to modidfy.
	 * @param value
	 *            The new value of the given checkbox.
	 */
	public void setCheckboxValue(String name, boolean value) {
		_checkboxes.get(name).setChecked(value);
	}

	/**
	 * Checks whether or not a checkbox is toggled.
	 * 
	 * @param name
	 *            The name of the checkbox to poll.
	 * @return Whether or not the given checkbox is toggled.
	 */
	public boolean getCheckboxValue(String name) {
		return _checkboxes.get(name).isChecked();
	}

	/**
	 * Sets the size of a checkbox.
	 * 
	 * @param name
	 *            The name of the checkbox to modify.
	 * @param size
	 *            The new size of the checkbox.
	 */
	public void setCheckboxSize(String name, Vector2 size) {
		_checkboxes.get(name).setSize(size.x, size.y);
	}

	/**
	 * Sets the size of a checkbox.
	 * 
	 * @param name
	 *            The name of the checkbox to modify.
	 * @param w
	 *            The new width of the checkbox.
	 * @param h
	 *            The new height of the checkbox.
	 */
	public void setCheckboxSize(String name, float w, float h) {
		_checkboxes.get(name).setSize(w, h);
	}

	/**
	 * Set whether or not a checkbox is checked.
	 * 
	 * @param name
	 *            The name of the checkbox to set.
	 * @param checked
	 *            Whether or not the checkbox is checked.
	 */
	public void setChecked(String name, boolean checked) {
		_checkboxes.get(name).setChecked(checked);
	}

	/**
	 * Gets the position of a checkbox.
	 * 
	 * @param name
	 *            Name of the checkbox to poll.
	 * @return The position of the given checkbox.
	 */
	public Vector2 getCheckboxPos(String name) {
		return new Vector2(_checkboxes.get(name).getX(), _checkboxes.get(name).getY());
	}

	/**
	 * Gets the size of a given checkbox.
	 * 
	 * @param name
	 *            The name of the checkbox to poll.
	 * @return The size of the given checkbox.
	 */
	public Vector2 getCheckboxSize(String name) {
		return new Vector2(_checkboxes.get(name).getWidth(), _checkboxes.get(name).getHeight());
	}

	/**
	 * Gets the opacity of a checkbox.
	 * 
	 * @param name
	 *            The name of the checkbox to poll.
	 * @return The opacity of the given checkbox.
	 */
	public float getCheckboxOpacity(String name) {
		return _checkboxes.get(name).getColor().a;
	}

	/**
	 * Sets the visibility of a checkbox.
	 * 
	 * @param name
	 *            The name of the checkbox to modify.
	 * @param vis
	 *            The visibility to set.
	 */
	public void setCheckboxVisable(String name, boolean vis) {
		_checkboxes.get(name).setVisible(vis);
	}

	/**
	 * Gets the visibility of a checkbox.
	 * 
	 * @param name
	 *            The name of the checkbox to poll.
	 * @return Whether or not the checkbox is visible.
	 */
	public boolean isCheckboxVisable(String name) {
		return _checkboxes.get(name).isVisible();
	}

	/**
	 * Removes a checkbox from the pane.
	 * 
	 * @param name
	 *            The name of the checkbox to remove.
	 */
	public void removeCheckbox(String name) {
		_checkboxes.get(name).remove();
		_checkboxes.remove(name);
	}

	/**
	 * Creates an element group.
	 * 
	 * @param name
	 *            The name of the group to create.
	 */
	public void createGroup(String name) {
		Group group = new Group();
		_groups.put(name, group);
		addActor(group);
	}

	/**
	 * Sets the position of an element group.
	 * 
	 * @param name
	 *            The name of the group to modify.
	 * @param pos
	 *            The new position to set.
	 */
	public void setGroupPos(String name, Vector2 pos) {
		_groups.get(name).setPosition(pos.x, pos.y);
	}

	/**
	 * Sets the position of an element group.
	 * 
	 * @param name
	 *            The name of the group to modify.
	 * @param x
	 *            The new X position.
	 * @param y
	 *            The new Y position.
	 */
	public void setGroupPos(String name, float x, float y) {
		_groups.get(name).setPosition(x, y);
	}

	/**
	 * Sets the size of an element group.
	 * 
	 * @param name
	 *            The name of the group to modify.
	 * @param size
	 *            The new size of the group.
	 */
	public void setGroupSize(String name, Vector2 size) {
		_groups.get(name).setSize(size.x, size.y);
	}

	/**
	 * 
	 * @param name
	 *            The name of the group to modify.
	 * @param w
	 *            The new width of the group.
	 * @param h
	 *            The new height of the group.
	 */
	public void setGroupSize(String name, float w, float h) {
		_groups.get(name).setSize(w, h);
	}

	/**
	 * Gets the position of a group.
	 * 
	 * @param name
	 *            The name of the group to poll.
	 * @return The position of the given group.
	 */
	public Vector2 getGroupPos(String name) {
		return new Vector2(_groups.get(name).getX(), _groups.get(name).getY());
	}

	/**
	 * Gets the size of a group.
	 * 
	 * @param name
	 *            The name of the group to poll.
	 * @return The size of the given group.
	 */
	public Vector2 getGroupSize(String name) {
		return new Vector2(_groups.get(name).getWidth(), _groups.get(name).getHeight());
	}

	/**
	 * Creates an image object.
	 * 
	 * @param name
	 *            The name of the image to create.
	 * @param image
	 *            The image file to load.
	 */
	public void createImage(String name, String image) {
		Image imageObj = new Image(new Texture(Gdx.files.internal("assets/ui/images/" + image)));
		_images.put(name, imageObj);
		this.addActor(imageObj);
	}

	/**
	 * Sets the position of an image.
	 * 
	 * @param name
	 *            The name of the image to modify.
	 * @param pos
	 *            The new position of the image.
	 */
	public void setImagePos(String name, Vector2 pos) {
		_images.get(name).setPosition(pos.x, pos.y);
	}

	/**
	 * Sets the position of an image.
	 * 
	 * @param name
	 *            The name of the image to modify.
	 * @param x
	 *            The new X position of the image.
	 * @param y
	 *            The new Y position of the image.
	 */
	public void setImagePos(String name, float x, float y) {
		_images.get(name).setPosition(x, y);
	}

	/**
	 * Sets the size of an image.
	 * 
	 * @param name
	 *            The name of the image to modify.
	 * @param size
	 *            The new size of the image.
	 */
	public void setImageSize(String name, Vector2 size) {
		_images.get(name).setSize(size.x, size.y);
	}

	/**
	 * Sets the size of an image.
	 * 
	 * @param name
	 *            The name of the image to modify.
	 * @param w
	 *            The new width of the image.
	 * @param h
	 *            The new height of the image.
	 */
	public void setImageSize(String name, float w, float h) {
		_images.get(name).setSize(w, h);
	}

	/**
	 * Sets the alpha of an image.
	 * 
	 * @param name
	 *            Sets the alpha of an image.
	 * @param alpha
	 *            The new alpha to set.
	 */
	public void setImageOpacity(String name, float alpha) {
		_images.get(name).setColor(new Color(1.0f, 1.0f, 1.0f, alpha));
	}

	/**
	 * Gets the alpha of an image.
	 * 
	 * @param name
	 *            The name of the image to poll.
	 * @return The alpha of the given image.
	 */
	public float getImageOpacity(String name) {
		return _images.get(name).getColor().a;
	}

	/**
	 * Gets the position of an image.
	 * 
	 * @param name
	 *            The name of the image to poll.
	 * @return The position of the given image.
	 */
	public Vector2 getImagePos(String name) {
		return new Vector2(_images.get(name).getX(), _images.get(name).getY());
	}

	/**
	 * Gets the size of an image.
	 * 
	 * @param name
	 *            The name of the image to poll.
	 * @return The size of the given image.
	 */
	public Vector2 getImageSize(String name) {
		return new Vector2(_images.get(name).getWidth(), _images.get(name).getHeight());
	}

	/**
	 * Removes an image from the pane.
	 * 
	 * @param name
	 *            The name of the image to remove.
	 */
	public void removeImage(String name) {
		_images.get(name).remove();
		_images.remove(name);
	}

	/**
	 * Adds a supported element to an element group.
	 * 
	 * @param group
	 *            The name of the group to add the object to.
	 * @param type
	 *            The type of the element to be added.
	 * @param elementName
	 *            The name of the element to be added.
	 */
	public void addElementToGroup(String group, ElementType type, String elementName) {
		switch (type) {
		case BUTTON:
			_buttons.get(elementName).remove();
			_groups.get(group).addActor(_buttons.get(elementName));
			break;
		case CHECKBOX:
			_checkboxes.get(elementName).remove();
			_groups.get(group).addActor(_checkboxes.get(elementName));
			break;
		case LABEL:
			_labels.get(elementName).remove();
			_groups.get(group).addActor(_labels.get(elementName));
			break;
		case PBAR:
			_pbars.get(elementName).remove();
			_groups.get(group).addActor(_pbars.get(elementName));
			break;
		case SLIDER:
			_sliders.get(elementName).remove();
			_groups.get(group).addActor(_sliders.get(elementName));
			break;
		case IMAGE:
			_images.get(elementName).remove();
			_groups.get(group).addActor(_images.get(elementName));
			break;
		case GROUP:
			break;
		default:
			break;
		}
	}

	/**
	 * Adds a custom actor to an element group.
	 * 
	 * @param group
	 *            The name of the group to add the actor to.
	 * @param element
	 *            The element to be added.
	 */
	public void addElementToGroup(String group, Actor element) {
		_groups.get(group).addActor(element);
	}

	/**
	 * Sets the opacity of a group.
	 * 
	 * @param group
	 *            The name of the group to modify.
	 * @param opacity
	 *            The new opacity of the group.
	 */
	public void setGroupOpacity(String group, float opacity) {
		Color col = _groups.get(group).getColor();
		_groups.get(group).setColor(new Color(col.r, col.g, col.b, opacity));
	}

	/**
	 * Gets the opacity of a group.
	 * 
	 * @param group
	 *            The name of the group to poll.
	 * @return The opacity of the given group.
	 */
	public float getGroupOpacity(String group) {
		return _groups.get(group).getColor().a;
	}

	/**
	 * Adds a listener to checkbox.
	 * 
	 * @param name
	 *            The name of the checkbox to add the listener to.
	 * @param listener
	 *            The listener to set.
	 */
	public void addCheckboxListener(String name, MUIInputHandler listener) {
		_checkboxes.get(name).addListener(new MUIInputListener(listener));
	}

	/**
	 * Checks whether an element exists.
	 * 
	 * @param object
	 *            The name of the element to query.
	 * @return Whether or not the element exists in the pane.
	 */
	public boolean doesUIElementExist(String object) {
		if (_labels.containsKey(object) || _buttons.containsKey(object) || _pbars.containsKey(object)
				|| _sliders.containsKey(object) || _checkboxes.containsKey(object) || _images.containsKey(object)
				|| _groups.containsKey(object))
			return true;
		return false;
	}

	/**
	 * Sets whether a group processes input.
	 * 
	 * @param name
	 *            The name of the group to modify.
	 * @param active
	 *            Whether or not the group should process input.
	 */
	public void setGroupActive(String name, boolean active) {
		_groups.get(name).setTouchable((active) ? Touchable.enabled : Touchable.disabled);
	}

	/**
	 * Sets whether a button processes input.
	 * 
	 * @param name
	 *            The name of the button to modify.
	 * @param active
	 *            Whether or not the button should process input.
	 */
	public void setButtonActive(String name, boolean active) {
		_buttons.get(name).setTouchable((active) ? Touchable.enabled : Touchable.disabled);
	}

	/**
	 * Sets whether a slider processes input.
	 * 
	 * @param name
	 *            The name of the slider to modify.
	 * @param active
	 *            Whether or not the slider should process input.
	 */
	public void setSliderActive(String name, boolean active) {
		_sliders.get(name).setTouchable((active) ? Touchable.enabled : Touchable.disabled);
	}

	/**
	 * Sets whether a checkbox processes input.
	 * 
	 * @param name
	 *            The name of the checkbox to modify.
	 * @param active
	 *            Whether or not the checkbox should process input.
	 */
	public void setCheckboxActive(String name, boolean active) {
		_checkboxes.get(name).setTouchable((active) ? Touchable.enabled : Touchable.disabled);
	}

	/**
	 * Checks whether or not a group processes input.
	 * 
	 * @param name
	 *            The name of the group to poll.
	 * @return Whether or not the given group processes input.
	 */
	public boolean getGroupActive(String name) {
		return _groups.get(name).isTouchable();
	}

	/**
	 * Checks whether or not a button processes input.
	 * 
	 * @param name
	 *            The name of the button to poll.
	 * @return Whether or not the given button processes input.
	 */
	public boolean getButtonActive(String name) {
		return _buttons.get(name).isTouchable();
	}

	/**
	 * Checks whether or not a slider processes input.
	 * 
	 * @param name
	 *            The name of the slider to poll.
	 * @return Whether or not the given slider processes input.
	 */
	public boolean getSliderActive(String name) {
		return _sliders.get(name).isTouchable();
	}

	/**
	 * Checks whether or not a checkbox processes input.
	 * 
	 * @param name
	 *            The name of the checkbox to poll.
	 * @return Whether or not the given checkbox processes input.
	 */
	public boolean getCheckboxActive(String name) {
		return _checkboxes.get(name).isTouchable();
	}

	@Override
	public void dispose() {
		super.dispose();
		for (BitmapFont font : _fonts.values())
			font.dispose();
		System.gc();
	}

}
