package com.aftershock.mantis.scene;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.xml.sax.SAXException;

import com.aftershock.mantis.MCallback;
import com.aftershock.mantis.scene.util.MUIRef;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.steadystate.css.parser.CSSOMParser;

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

public class MUIPane extends Stage {

	private LinkedHashMap<String, BitmapFont> _fonts = new LinkedHashMap<String, BitmapFont>();
	private LinkedHashMap<String, Label> _labels = new LinkedHashMap<String, Label>();
	private LinkedHashMap<String, LabelStyle> _labelStyles = new LinkedHashMap<String, LabelStyle>();
	private LinkedHashMap<String, TextButtonStyle> _buttonStyles = new LinkedHashMap<String, TextButtonStyle>();
	private LinkedHashMap<String, TextButton> _buttons = new LinkedHashMap<String, TextButton>();
	private LinkedHashMap<String, ProgressBarStyle> _pbarStyles = new LinkedHashMap<String, ProgressBarStyle>();
	private LinkedHashMap<String, ProgressBar> _pbars = new LinkedHashMap<String, ProgressBar>();
	private LinkedHashMap<String, Boolean> _pbarorients = new LinkedHashMap<String, Boolean>();
	private LinkedHashMap<String, SliderStyle> _sliderStyles = new LinkedHashMap<String, SliderStyle>();
	private LinkedHashMap<String, Slider> _sliders = new LinkedHashMap<String, Slider>();
	private LinkedHashMap<String, CheckBoxStyle> _checkboxStyles = new LinkedHashMap<String, CheckBoxStyle>();
	private LinkedHashMap<String, CheckBox> _checkboxes = new LinkedHashMap<String, CheckBox>();
	private LinkedHashMap<String, Group> _groups = new LinkedHashMap<String, Group>();
	private LinkedHashMap<String, Image> _images = new LinkedHashMap<String, Image>();

	private LinkedList<MCallback> dlq = new LinkedList<MCallback>();

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
		doLater(() -> {
			FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(
					Gdx.files.internal("assets/ui/font/" + font));
			FreeTypeFontParameter fontParam = new FreeTypeFontParameter();

			fontParam.color = fontCol;
			fontParam.borderColor = borderCol;
			fontParam.size = size;
			fontParam.borderWidth = borderWidth;
			BitmapFont bFont = fontGenerator.generateFont(fontParam);
			_fonts.put(name, bFont);
			fontGenerator.dispose();
		});
	}

	/*
	 * LABELS
	 */

	/**
	 * Creates a new label style.
	 * 
	 * @param name
	 *            The name of the style.
	 * @param font
	 *            The style's font.
	 */
	public void createLabelStyle(String name, String font) {
		doLater(() -> {
			LabelStyle style = new LabelStyle();
			style.font = _fonts.get(font);
			_labelStyles.put(name, style);
		});
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
		doLater(() -> {
			Label label = new Label("", _labelStyles.get(style));
			label.setAlignment(align);
			label.setWrap(true);
			_labels.put(name, label);
			addActor(label);
		});
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
		doLater(() -> {
			_labels.get(name).setPosition(position.x, position.y);
		});
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
		doLater(() -> {
			_labels.get(name).setPosition(x, y);
		});
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
		doLater(() -> {
			_labels.get(name).setSize(size.x, size.y);
		});
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
		doLater(() -> {
			_labels.get(name).setSize(w, h);
		});
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
		doLater(() -> {
			_labels.get(name).setText(text);
		});
	}

	/**
	 * Sets the visibility of a label.
	 * 
	 * @param name
	 *            The name of the label to modify.
	 * @param vis
	 *            Whether or not to show the label.
	 */
	public void setLabelVisible(String name, boolean vis) {
		doLater(() -> {
			_labels.get(name).setVisible(vis);
		});
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
		doLater(() -> {
			Color col = _labels.get(name).getColor();
			_labels.get(name).setColor(col.r, col.g, col.b, opacity);
		});
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
	public boolean isLabelVisible(String name) {
		return _labels.get(name).isVisible();
	}

	/**
	 * Removes a label from the pane.
	 * 
	 * @param name
	 *            The name of the label to remove.
	 */
	public void removeLabel(String name) {
		doLater(() -> {
			_labels.get(name).remove();
		});
	}

	/*
	 * BUTTONS
	 */

	/**
	 * Creates a button style.
	 * 
	 * @param name
	 *            The name of the button style.
	 * @param drawableUp
	 *            The unpressed drawable of the button.
	 * @param drawableDown
	 *            The pressed drawable of the button.
	 * @param font
	 *            The font of the button style.
	 */
	public void createButtonStyle(String name, String drawableUp, String drawableDown, String font) {
		doLater(() -> {
			TextureRegion drawableUpTexReg = new TextureRegion(
					new Texture(Gdx.files.internal("assets/ui/button/" + drawableUp)));
			TextureRegion drawableDownTexReg = new TextureRegion(
					new Texture(Gdx.files.internal("assets/ui/button/" + drawableDown)));

			TextButtonStyle bStyle = new TextButtonStyle(new TextureRegionDrawable(drawableUpTexReg),
					new TextureRegionDrawable(drawableDownTexReg), new TextureRegionDrawable(drawableUpTexReg),
					_fonts.get(font));
			_buttonStyles.put(name, bStyle);
		});
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
		doLater(() -> {
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
		});
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
		doLater(() -> {
			TextButton button = new TextButton(text, _buttonStyles.get(style));
			button.setName(name);
			_buttons.put(name, button);
			addActor(button);
		});
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
		doLater(() -> {
			_buttons.get(name).getLabel().setAlignment(align);
		});
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
		doLater(() -> {
			_buttons.get(name).setTouchable((active) ? Touchable.enabled : Touchable.disabled);
		});
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
		doLater(() -> {
			_buttons.get(name).pad(padTop, padLeft, padBottom, padRight);
		});
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
		doLater(() -> {
			_buttons.get(name).setColor(1.0f, 1.0f, 1.0f, opacity);
		});
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
		doLater(() -> {
			_buttons.get(name).addListener(new MUIInputListener(listener));
		});
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
		doLater(() -> {
			_buttons.get(name).setPosition(pos.x, pos.y);
		});
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
		doLater(() -> {
			_buttons.get(name).setPosition(x, y);
		});
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
		doLater(() -> {
			_buttons.get(name).setSize(size.x, size.y);
		});
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
		doLater(() -> {
			_buttons.get(name).setChecked(value);
		});
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
		doLater(() -> {
			_buttons.get(name).setSize(w, h);
		});
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
		doLater(() -> {
			_buttons.get(name).setText(text);
		});
	}

	/**
	 * Sets the visibility of a button.
	 * 
	 * @param name
	 *            The name of the button to modify.
	 * @param vis
	 *            The new visibility of the button.
	 */
	public void setButtonVisible(String name, boolean vis) {
		doLater(() -> {
			_buttons.get(name).setVisible(vis);
		});
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
	 * Gets the visibility of the button.
	 * 
	 * @param name
	 *            The name of the button to poll.
	 * @return The visibility of the given button.
	 */
	public boolean isButtonVisible(String name) {
		return _buttons.get(name).isVisible();
	}

	/**
	 * Removes a button from the pane.
	 * 
	 * @param name
	 *            The button to remove.
	 */
	public void removeButton(String name) {
		doLater(() -> {
			_buttons.get(name).remove();
			_buttons.remove(name);
		});
	}

	/*
	 * PROGRESS BARS
	 */

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
		doLater(() -> {
			TextureRegion pbarbg = new TextureRegion(
					new Texture(Gdx.files.internal("assets/ui/progressbar/" + drawable)));
			TextureRegion pbarknob = new TextureRegion(
					new Texture(Gdx.files.internal("assets/ui/progressbar/" + knob)));
			ProgressBarStyle pbStyle = new ProgressBarStyle();
			pbStyle.knob = new TextureRegionDrawable(pbarknob);
			pbStyle.knobBefore = new TextureRegionDrawable(pbarknob);
			pbStyle.background = new TextureRegionDrawable(pbarbg);

			_pbarStyles.put(name, pbStyle);
		});
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
		doLater(() -> {
			ProgressBar pbar = new ProgressBar(min, max, step, vertical, _pbarStyles.get(style));
			pbar.setName(name);
			pbar.setValue(defaultVal);
			_pbars.put(name, pbar);
			_pbarorients.put(name, vertical);
			addActor(pbar);
		});
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
		doLater(() -> {
			_pbars.get(name).setColor(1.0f, 1.0f, 1.0f, opacity);
		});
	}

	/**
	 * Sets the value of a progress bar.
	 * 
	 * @param name
	 *            The name of the progress bar to modify.
	 * @param val
	 *            The value of the given progress bar.
	 */
	public void setProgressBarValue(String name, float val) {
		doLater(() -> {
			_pbars.get(name).setValue(val);
		});
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
		doLater(() -> {
			_pbars.get(name).setPosition(pos.x, pos.y);
		});
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
		doLater(() -> {
			_pbars.get(name).setPosition(x, y);
		});
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
		doLater(() -> {
			setProgressBarSize(name, size.x, size.y);
		});
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
		doLater(() -> {
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
		});
	}

	/**
	 * Sets the visibility of a progress bar.
	 * 
	 * @param name
	 *            The name of the progress bar to set.
	 * @param vis
	 *            The visibility to set.
	 */
	public void setProgressBarVisible(String name, boolean vis) {
		doLater(() -> {
			_pbars.get(name).setVisible(vis);
		});
	}

	/**
	 * Gets the value of a progress bar.
	 * 
	 * @param name
	 *            The name of the progress bar to poll.
	 * @return The value of the given progress bar.
	 */
	public float getProgressBarValue(String name) {
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
	 * Gets the visibility of a progress bar.
	 * 
	 * @param name
	 *            The name of the progress bar to poll.
	 * @return Whether or not the progress bar is visible.
	 */
	public boolean isProgressBarVisible(String name) {
		return _pbars.get(name).isVisible();
	}

	/**
	 * Removes a progress bar from the pane.
	 * 
	 * @param name
	 *            The name of the progress bar to remove.
	 */
	public void removeProgressBar(String name) {
		doLater(() -> {
			_pbars.get(name).remove();
			_pbars.remove(name);
			_pbarorients.remove(name);
		});
	}

	/*
	 * SLIDERS
	 */

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
		doLater(() -> {
			SliderStyle sliderStyle = new SliderStyle(
					new TextureRegionDrawable(
							new TextureRegion(new Texture(Gdx.files.internal("assets/ui/slider/" + drawable)))),
					new TextureRegionDrawable(
							new TextureRegion(new Texture(Gdx.files.internal("assets/ui/slider/" + knob)))));
			_sliderStyles.put(name, sliderStyle);
		});
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
		doLater(() -> {
			Slider slider = new Slider(min, max, stepSize, vertical, _sliderStyles.get(style));
			slider.setName(name);
			slider.setValue(defaultVal);
			_sliders.put(name, slider);
			addActor(slider);
		});
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
		doLater(() -> {
			_sliders.get(name).setColor(1.0f, 1.0f, 1.0f, opacity);
		});
	}

	/**
	 * Sets the value of a slider.
	 * 
	 * @param name
	 *            The name of the slider to modify.
	 * @param val
	 *            The value to set.
	 */
	public void setSliderValue(String name, float val) {
		doLater(() -> {
			_sliders.get(name).setValue(val);
		});
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
		doLater(() -> {
			_sliders.get(name).setTouchable((active) ? Touchable.enabled : Touchable.disabled);
		});
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
		doLater(() -> {
			_sliders.get(name).setPosition(pos.x, pos.y);
		});
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
		doLater(() -> {
			_sliders.get(name).setPosition(x, y);
		});
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
		doLater(() -> {
			_sliders.get(name).setSize(size.x, size.y);
		});
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
		doLater(() -> {
			_sliders.get(name).setSize(w, h);
		});
	}

	/**
	 * Sets the visibility of a slider.
	 * 
	 * @param name
	 *            The name of the slider to modify.
	 * @param vis
	 *            The visibility to set.
	 */
	public void setSliderVisible(String name, boolean vis) {
		doLater(() -> {
			_sliders.get(name).setVisible(vis);
		});
	}

	/**
	 * Gets the value of a slider.
	 * 
	 * @param name
	 *            Name of the slider to poll.
	 * @return The value of the given slider.
	 */
	public float getSliderValue(String name) {
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
	 * Gets the visibility of a slider.
	 * 
	 * @param name
	 *            The name of the slider to poll.
	 * @return The visibility of the given slider.
	 */
	public boolean isSliderVisible(String name) {
		return _sliders.get(name).isVisible();
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
	 * Removes a slider from the pane.
	 * 
	 * @param name
	 *            The name of the slider to remove.
	 */
	public void removeSlider(String name) {
		doLater(() -> {
			_sliders.get(name).remove();
			_sliders.remove(name);
		});
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
		doLater(() -> {
			_sliders.get(name).addListener(new MUIInputListener(listener));
		});
	}

	/*
	 * CHECKBOXES
	 */

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
		doLater(() -> {
			CheckBoxStyle cbStyle = new CheckBoxStyle(
					new TextureRegionDrawable(
							new TextureRegion(new Texture(Gdx.files.internal("assets/ui/checkbox/" + checkOff)))),
					new TextureRegionDrawable(
							new TextureRegion(new Texture(Gdx.files.internal("assets/ui/checkbox/" + check)))),
					new BitmapFont(), Color.BLACK);
			_checkboxStyles.put(name, cbStyle);
		});
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
		doLater(() -> {
			CheckBox cb = new CheckBox("", _checkboxStyles.get(style));
			_checkboxes.put(name, cb);
			addActor(cb);
		});
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
		doLater(() -> {
			_checkboxes.get(name).setColor(1.0f, 1.0f, 1.0f, opacity);
		});
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
		doLater(() -> {
			_checkboxes.get(name).setPosition(pos.x, pos.y);
		});
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
		doLater(() -> {
			_checkboxes.get(name).setPosition(x, y);
		});
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
		doLater(() -> {
			_checkboxes.get(name).setChecked(value);
		});
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
		doLater(() -> {
			_checkboxes.get(name).setSize(size.x, size.y);
		});
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
		doLater(() -> {
			_checkboxes.get(name).setSize(w, h);
		});
	}

	/**
	 * Sets the visibility of a checkbox.
	 * 
	 * @param name
	 *            The name of the checkbox to modify.
	 * @param vis
	 *            The visibility to set.
	 */
	public void setCheckboxVisible(String name, boolean vis) {
		doLater(() -> {
			_checkboxes.get(name).setVisible(vis);
		});
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
		doLater(() -> {
			_checkboxes.get(name).setTouchable((active) ? Touchable.enabled : Touchable.disabled);
		});
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
	 * Checks whether or not a checkbox processes input.
	 * 
	 * @param name
	 *            The name of the checkbox to poll.
	 * @return Whether or not the given checkbox processes input.
	 */
	public boolean getCheckboxActive(String name) {
		return _checkboxes.get(name).isTouchable();
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
	 * Gets the visibility of a checkbox.
	 * 
	 * @param name
	 *            The name of the checkbox to poll.
	 * @return Whether or not the checkbox is visible.
	 */
	public boolean isCheckboxVisible(String name) {
		return _checkboxes.get(name).isVisible();
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
	 * Removes a checkbox from the pane.
	 * 
	 * @param name
	 *            The name of the checkbox to remove.
	 */
	public void removeCheckbox(String name) {
		doLater(() -> {
			_checkboxes.get(name).remove();
			_checkboxes.remove(name);
		});
	}

	/*
	 * GROUPS
	 */

	/**
	 * Creates an element group.
	 * 
	 * @param name
	 *            The name of the group to create.
	 */
	public void createGroup(String name) {
		doLater(() -> {
			Group group = new Group();
			_groups.put(name, group);
			addActor(group);
		});
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
		doLater(() -> {
			_groups.get(name).setPosition(pos.x, pos.y);
		});
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
		doLater(() -> {
			_groups.get(name).setPosition(x, y);
		});
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
		doLater(() -> {
			_groups.get(name).setSize(size.x, size.y);
		});
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
		doLater(() -> {
			_groups.get(name).setSize(w, h);
		});
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
		doLater(() -> {
			_groups.get(name).setTouchable((active) ? Touchable.enabled : Touchable.disabled);
		});
	}

	/**
	 * Sets the visibility of a given group.
	 * 
	 * @param name
	 *            The name of the group to modify.
	 * @param visible
	 *            Whether or not the given group is visible.
	 */
	public void setGroupVisible(String name, boolean visible) {
		doLater(() -> {
			_groups.get(name).setVisible(visible);
		});
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
		doLater(() -> {
			Color col = _groups.get(group).getColor();
			_groups.get(group).setColor(new Color(col.r, col.g, col.b, opacity));
		});
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
	 * Checks whether or not a group is visible.
	 * 
	 * @param name
	 *            The name of the group to poll.
	 * @return Whether or not the given group is visible.
	 */
	public boolean isGroupVisible(String name) {
		return _groups.get(name).isVisible();
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
	 * Adds a custom actor to an element group.
	 * 
	 * @param group
	 *            The name of the group to add the actor to.
	 * @param element
	 *            The element to be added.
	 */
	public void addElementToGroup(String group, Actor element) {
		doLater(() -> {
			_groups.get(group).addActor(element);
		});
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
		doLater(() -> {
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
		});
	}

	/**
	 * Removes a group from the pane.
	 * 
	 * @param group
	 *            The name of the group to remove.
	 */
	public void removeGroup(String group) {
		doLater(() -> {
			_groups.get(group).remove();
			_groups.remove(group);
		});
	}

	/*
	 * IMAGES
	 */

	/**
	 * Creates an image object.
	 * 
	 * @param name
	 *            The name of the image to create.
	 * @param image
	 *            The image file to load.
	 */
	public void createImage(String name, String image) {
		doLater(() -> {
			Image imageObj = new Image(new Texture(Gdx.files.internal("assets/ui/images/" + image)));
			_images.put(name, imageObj);
			this.addActor(imageObj);
		});
	}

	/**
	 * Sets an image's visibility.
	 * 
	 * @param name
	 *            The name of the image to modify.
	 * @param visible
	 *            Whether or not the image is visible.
	 */
	public void setImageVisible(String name, boolean visible) {
		doLater(() -> {
			_images.get(name).setVisible(visible);
		});
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
		doLater(() -> {
			_images.get(name).setPosition(pos.x, pos.y);
		});
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
		doLater(() -> {
			_images.get(name).setPosition(x, y);
		});
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
		doLater(() -> {
			_images.get(name).setSize(size.x, size.y);
		});
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
		doLater(() -> {
			_images.get(name).setSize(w, h);
		});
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
		doLater(() -> {
			_images.get(name).setColor(new Color(1.0f, 1.0f, 1.0f, alpha));
		});
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
	 * Checks whether or not an image is visible.
	 * 
	 * @param name
	 *            The name of the image to poll.
	 * @return Whether or not the given image is visible.
	 */
	public boolean isImageVisible(String name) {
		return _images.get(name).isVisible();
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
		doLater(() -> {
			_images.get(name).remove();
			_images.remove(name);
		});
	}

	/**
	 * Checks whether an element exists.
	 * 
	 * @param element
	 *            The name of the element to query.
	 * @return Whether or not the element exists in the pane.
	 */
	public boolean doesUIElementExist(String element) {
		if (_labels.containsKey(element) || _buttons.containsKey(element) || _pbars.containsKey(element)
				|| _sliders.containsKey(element) || _checkboxes.containsKey(element) || _images.containsKey(element)
				|| _groups.containsKey(element))
			return true;
		return false;
	}

	/*
	 * REFERENCES
	 */

	/**
	 * Gets a referenced to a given button.
	 * 
	 * @param name
	 *            The name of the button to reference.
	 * @return A new reference to the given button.
	 */
	public MUIRef getButtonRef(String name) {
		return new MUIRef(name, this, ElementType.BUTTON);
	}

	/**
	 * Gets a referenced to a given label.
	 * 
	 * @param name
	 *            The name of the label to reference.
	 * @return A new reference to the given label.
	 */
	public MUIRef getLabelRef(String name) {
		return new MUIRef(name, this, ElementType.LABEL);
	}

	/**
	 * Gets a referenced to a given progress bar.
	 * 
	 * @param name
	 *            The name of the progress bar to reference.
	 * @return A new reference to the given progress bar.
	 */
	public MUIRef getProgressBarRef(String name) {
		return new MUIRef(name, this, ElementType.PBAR);
	}

	/**
	 * Gets a referenced to a given slider.
	 * 
	 * @param name
	 *            The name of the slider to reference.
	 * @return A new reference to the given slider.
	 */
	public MUIRef getSliderRef(String name) {
		return new MUIRef(name, this, ElementType.SLIDER);
	}

	/**
	 * Gets a referenced to a given checkbox.
	 * 
	 * @param name
	 *            The name of the checkbox to reference.
	 * @return A new reference to the given checkbox.
	 */
	public MUIRef getCheckboxRef(String name) {
		return new MUIRef(name, this, ElementType.CHECKBOX);
	}

	/**
	 * Gets a referenced to a given group.
	 * 
	 * @param name
	 *            The name of the group to reference.
	 * @return A new reference to the given group.
	 */
	public MUIRef getGroupRef(String name) {
		return new MUIRef(name, this, ElementType.GROUP);
	}

	/**
	 * Gets a referenced to a given image.
	 * 
	 * @param name
	 *            The name of the image to reference.
	 * @return A new reference to the given image.
	 */
	public MUIRef getImageRef(String name) {
		return new MUIRef(name, this, ElementType.IMAGE);
	}

	/*
	 * PANE METHODS
	 */

	/**
	 * Pop and call a callback next update.
	 * 
	 * @param callback
	 *            The callback to queue.
	 */
	public void doLater(MCallback callback) {
		dlq.add(callback);
	}

	/*
	 * CSS PARSING
	 */

	/**
	 * Parses a CSS style, applying its contents to the scene.
	 * 
	 * @param file
	 *            The name of the CSS file to parse.
	 */
	public void useCSS(String file) {
		CSSOMParser parser = new CSSOMParser();
		CSSStyleSheet sheet;
		try {
			sheet = parser.parseStyleSheet(new InputSource(Gdx.files.internal("assets/ui/styles/" + file).reader()),
					null, null);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		for (int i = 0; i < sheet.getCssRules().getLength(); i++) {
			CSSStyleRule rule = (CSSStyleRule) sheet.getCssRules().item(i);
			String rrule = rule.getSelectorText();
			for (int n = 0; n < rule.getStyle().getLength(); n++) {
				String rkey = rule.getStyle().item(n);
				String rval = rule.getStyle().getPropertyValue(rkey);
				_applyStyle(rrule, rkey, rval);
			}
		}
	}

	/**
	 * Evaluates a position or size string to a numerical value.
	 * 
	 * @param num
	 *            The formatted number string.
	 * @param vh
	 *            Whether the input number is relative to the scene's height or
	 *            width.
	 * @return The parsed number.
	 */
	public float _evaluateNum(String num, boolean vh) {
		if (num.endsWith("%")) {
			if (vh) {
				return (Float.parseFloat(num.replaceAll("%", "")) / 100.0f) * this.getHeight();
			} else {
				return (Float.parseFloat(num.replaceAll("%", "")) / 100.0f) * this.getWidth();
			}
		} else if (num.endsWith("px")) {
			return Float.parseFloat(num.substring(0, num.indexOf("px")));
		} else {
			return Float.parseFloat(num);
		}
	}

	/**
	 * Sets the width of a UI element.
	 * 
	 * @param element
	 *            The name of the UI element to modify.
	 * @param type
	 *            The type of the UI element to modify.
	 * @param w
	 *            The new width.
	 */
	public void _setWidth(String element, String type, String w) {
		switch (type.toLowerCase()) {
		case "button":
			setButtonSize(element, new Vector2(_evaluateNum(w, false), getButtonSize(element).y));
			break;
		case "slider":
			setSliderSize(element, new Vector2(_evaluateNum(w, false), getSliderSize(element).y));
			break;
		case "progressbar":
			setProgressBarSize(element, new Vector2(_evaluateNum(w, false), getProgressBarSize(element).y));
			break;
		case "label":
			setLabelSize(element, new Vector2(_evaluateNum(w, false), getLabelSize(element).y));
			break;
		case "checkbox":
			setCheckboxSize(element, new Vector2(_evaluateNum(w, false), getCheckboxSize(element).y));
			break;
		case "group":
			setGroupSize(element, new Vector2(_evaluateNum(w, false), getGroupSize(element).y));
			break;
		case "image":
			setImageSize(element, new Vector2(_evaluateNum(w, false), getImageSize(element).y));
			break;
		}
		update(0.0f);
	}

	/**
	 * Sets the height of a UI element.
	 * 
	 * @param element
	 *            The name of the UI element to modify.
	 * @param type
	 *            The type of the UI element to modify.
	 * @param h
	 *            The new height.
	 */
	public void _setHeight(String element, String type, String h) {
		switch (type.toLowerCase()) {
		case "button":
			setButtonSize(element, new Vector2(getButtonSize(element).x, _evaluateNum(h, true)));
			break;
		case "slider":
			setSliderSize(element, new Vector2(getSliderSize(element).x, _evaluateNum(h, true)));
			break;
		case "progressbar":
			setProgressBarSize(element, new Vector2(getProgressBarSize(element).x, _evaluateNum(h, true)));
			break;
		case "label":
			setLabelSize(element, new Vector2(getLabelSize(element).x, _evaluateNum(h, true)));
			break;
		case "checkbox":
			setCheckboxSize(element, new Vector2(getCheckboxSize(element).x, _evaluateNum(h, true)));
			break;
		case "group":
			setGroupSize(element, new Vector2(getGroupSize(element).x, _evaluateNum(h, true)));
			break;
		case "image":
			setImageSize(element, new Vector2(getImageSize(element).x, _evaluateNum(h, true)));
			break;
		}
		update(0.0f);
	}

	/**
	 * Sets the opacity of a UI element.
	 * 
	 * @param element
	 *            The name of the UI element to modify.
	 * @param type
	 *            The type of the UI element to modify.
	 * @param op
	 *            The new opacity.
	 */
	public void _setOpacity(String element, String type, String op) {
		switch (type.toLowerCase()) {
		case "button":
			setButtonOpacity(element, Float.parseFloat(op));
			break;
		case "slider":
			setSliderOpacity(element, Float.parseFloat(op));
			break;
		case "progressbar":
			setProgressBarOpacity(element, Float.parseFloat(op));
			break;
		case "label":
			setLabelOpacity(element, Float.parseFloat(op));
			break;
		case "checkbox":
			setCheckboxOpacity(element, Float.parseFloat(op));
			break;
		case "group":
			setGroupOpacity(element, Float.parseFloat(op));
			break;
		case "image":
			setImageOpacity(element, Float.parseFloat(op));
			break;
		}
		update(0.0f);
	}

	/**
	 * Sets the left-oriented position of a UI element.
	 * 
	 * @param element
	 *            The name of the UI element to modify.
	 * @param type
	 *            The type of the UI element to modify.
	 * @param left
	 *            The new left-oriented position.
	 */
	public void _setLeft(String element, String type, String left) {
		float leftPos = _evaluateNum(left, false);

		switch (type.toLowerCase()) {
		case "button":
			setButtonPos(element, leftPos, getButtonPos(element).y);
			break;
		case "slider":
			setSliderPos(element, leftPos, getSliderPos(element).y);
			break;
		case "progressbar":
			setProgressBarPos(element, leftPos, getProgressBarPos(element).y);
			break;
		case "label":
			setLabelPos(element, leftPos, getLabelPos(element).y);
			break;
		case "checkbox":
			setCheckboxPos(element, leftPos, getCheckboxPos(element).y);
			break;
		case "group":
			setGroupPos(element, leftPos, getGroupPos(element).y);
			break;
		case "image":
			setImagePos(element, leftPos, getImagePos(element).y);
			break;
		}
		update(0.0f);
	}

	/**
	 * Sets the right-oriented position of a UI element.
	 * 
	 * @param element
	 *            The name of the UI element to modify.
	 * @param type
	 *            The type of the UI element to modify.
	 * @param right
	 *            The new right-oriented position.
	 */
	public void _setRight(String element, String type, String right) {
		float rightPos = this.getWidth() - _evaluateNum(right, false);

		switch (type.toLowerCase()) {
		case "button":
			setButtonPos(element, rightPos, getButtonPos(element).y);
			break;
		case "slider":
			setSliderPos(element, rightPos, getSliderPos(element).y);
			break;
		case "progressbar":
			setProgressBarPos(element, rightPos, getProgressBarPos(element).y);
			break;
		case "label":
			setLabelPos(element, rightPos, getLabelPos(element).y);
			break;
		case "checkbox":
			setCheckboxPos(element, rightPos, getCheckboxPos(element).y);
			break;
		case "group":
			setGroupPos(element, rightPos, getGroupPos(element).y);
			break;
		case "image":
			setImagePos(element, rightPos, getImagePos(element).y);
			break;
		}
		update(0.0f);
	}

	/**
	 * Sets the top-oriented position of a UI element.
	 * 
	 * @param element
	 *            The name of the UI element to modify.
	 * @param type
	 *            The type of the UI element to modify.
	 * @param top
	 *            The new top-oriented position.
	 */
	public void _setTop(String element, String type, String top) {
		float topPos = this.getHeight() - _evaluateNum(top, true);

		switch (type.toLowerCase()) {
		case "button":
			setButtonPos(element, getButtonPos(element).x, topPos);
			break;
		case "slider":
			setSliderPos(element, getSliderPos(element).x, topPos);
			break;
		case "progressbar":
			setProgressBarPos(element, getProgressBarPos(element).x, topPos);
			break;
		case "label":
			setLabelPos(element, getLabelPos(element).x, topPos);
			break;
		case "checkbox":
			setCheckboxPos(element, getCheckboxPos(element).x, topPos);
			break;
		case "group":
			setGroupPos(element, getGroupPos(element).x, topPos);
			break;
		case "image":
			setImagePos(element, getImagePos(element).x, topPos);
			break;
		}
		update(0.0f);
	}

	/**
	 * Sets the bottom-oriented position of a UI element.
	 * 
	 * @param element
	 *            The name of the UI element to modify.
	 * @param type
	 *            The type of the UI element to modify.
	 * @param bottom
	 *            The new bottom-oriented position.
	 */
	public void _setBottom(String element, String type, String bottom) {
		float bottomPos = _evaluateNum(bottom, true);

		switch (type.toLowerCase()) {
		case "button":
			setButtonPos(element, getButtonPos(element).x, bottomPos);
			break;
		case "slider":
			setSliderPos(element, getSliderPos(element).x, bottomPos);
			break;
		case "progressbar":
			setProgressBarPos(element, getProgressBarPos(element).x, bottomPos);
			break;
		case "label":
			setLabelPos(element, getLabelPos(element).x, bottomPos);
			break;
		case "checkbox":
			setCheckboxPos(element, getCheckboxPos(element).x, bottomPos);
			break;
		case "group":
			setGroupPos(element, getGroupPos(element).x, bottomPos);
			break;
		case "image":
			setImagePos(element, getImagePos(element).x, bottomPos);
			break;
		}
		update(0.0f);
	}

	/**
	 * Applies a style pair to a given element name.
	 * 
	 * @param name
	 *            The name of the element to apply the style to.
	 * @param key
	 *            The style key.
	 * @param value
	 *            The style value.
	 */
	private void _applyStyle(String name, String key, String value) {
		String element = name.replaceAll("^#", "").replaceAll("\\[\\w+\\]", "");
		String type = name.replaceAll("^#\\w+\\[", "").replaceAll("[\\[\\]]", "");

		switch (key.toLowerCase()) {
		case "left":
			_setLeft(element, type, value);
			break;
		case "right":
			_setRight(element, type, value);
			break;
		case "top":
			_setTop(element, type, value);
			break;
		case "bottom":
			_setBottom(element, type, value);
			break;
		case "width":
			_setWidth(element, type, value);
			break;
		case "height":
			_setHeight(element, type, value);
			break;
		case "opacity":
			_setOpacity(element, type, value);
			break;
		}
	}

	/*
	 * XML PARSING
	 */

	/**
	 * Parse an XML file, loading it's contents to the pane.
	 * 
	 * @param file
	 *            The XML file to parse.
	 */
	public void useXML(String file) {
		FileHandle handle = Gdx.files.internal("assets/ui/structure/" + file);
		Document doc;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(handle.file());
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
			return;
		}
		_parseLayer(null, null, doc.getChildNodes());
		update(0.0f);
	}

	/**
	 * Adds a button to the pane from an XML stage.
	 * 
	 * @param name
	 *            The name of the button.
	 * @param group
	 *            The parent group, if any.
	 * @param style
	 *            The style of the button.
	 * @param text
	 *            The button's label text.
	 */
	private void _addButton(String name, String group, String style, String text) {
		if (text == null) {
			text = "";
		}
		createButton(name, style, text);
		if (group != null) {
			addElementToGroup(group, ElementType.BUTTON, name);
		}
	}

	/**
	 * Add a group to the pane from an XML stage.
	 * 
	 * @param name
	 *            The name of the group.
	 */
	private void _addGroup(String name) {
		createGroup(name);
	}

	/**
	 * Parses an alignment string.
	 * 
	 * @param align
	 *            The input alignment string.
	 * @return The integer ID of the input string.
	 */
	private int _getAlign(String align) {
		switch (align.toLowerCase()) {
		case "left":
			return Align.left;
		case "right":
			return Align.right;
		case "top":
			return Align.top;
		case "bottom":
			return Align.bottom;
		case "center":
			return Align.center;
		case "topleft":
			return Align.topLeft;
		case "topright":
			return Align.topRight;
		case "bottomleft":
			return Align.bottomLeft;
		case "bottomright":
			return Align.bottomRight;
		default:
			return -1;
		}
	}

	/**
	 * Adds a label to the pane from an XML stage.
	 * 
	 * @param name
	 *            The name of the label.
	 * @param group
	 *            The parent group, if any.
	 * @param style
	 *            The style of the label.
	 * @param align
	 *            The alignment of the label text.
	 * @param text
	 *            The text of the label.
	 */
	private void _addLabel(String name, String group, String style, String align, String text) {
		if (align == null) {
			align = "left";
		}
		createLabel(name, style, _getAlign(align));
		setLabelText(name, text);
		if (group != null) {
			addElementToGroup(group, ElementType.LABEL, name);
		}
	}

	/**
	 * Adds a progress bar to the pane from an XML stage.
	 * 
	 * @param name
	 *            The name of the progress bar.
	 * @param group
	 *            The parent group, if any.
	 * @param style
	 *            The style of the progress bar.
	 * @param min
	 *            The minimum progress bar value.
	 * @param max
	 *            The maximum progress bar value.
	 * @param step
	 *            The progress bar increment.
	 * @param defval
	 *            The default progress value.
	 * @param vertical
	 *            Whether or not the progress bar is vertical.
	 */
	private void _addProgressBar(String name, String group, String style, String min, String max, String step,
			String defval, boolean vertical) {
		if (min == null) {
			min = "0.0";
		}
		if (max == null) {
			max = "1.0";
		}
		if (step == null) {
			step = "0.01";
		}
		if (defval == null) {
			defval = "0.0";
		}
		createProgressBar(name, style, Float.parseFloat(min), Float.parseFloat(max), Float.parseFloat(step),
				Float.parseFloat(defval), vertical);
		if (group != null) {
			addElementToGroup(group, ElementType.PBAR, name);
		}
	}

	/**
	 * Adds a checkbox to the pane from an XML stage.
	 * 
	 * @param name
	 *            The name of the checkbox.
	 * @param group
	 *            The parent group, if any.
	 * @param style
	 *            The style of the checkbox.
	 * @param defval
	 *            The default value of the checkbox.
	 */
	private void _addCheckbox(String name, String group, String style, boolean defval) {
		createCheckbox(name, style, defval);
		if (group != null) {
			addElementToGroup(group, ElementType.CHECKBOX, name);
		}
	}

	/**
	 * Adds a slider to the pane from an XML stage.
	 * 
	 * @param name
	 *            The name of the slider.
	 * @param group
	 *            The parent group, if any.
	 * @param style
	 *            The style of the slider.
	 * @param min
	 *            The minimum slider value.
	 * @param max
	 *            The maximum slider value.
	 * @param step
	 *            The slider increment.
	 * @param defval
	 *            The default value of the slider.
	 * @param vertical
	 *            Whether or not the slider is vertical.
	 */
	private void _addSlider(String name, String group, String style, String min, String max, String step, String defval,
			boolean vertical) {
		if (min == null) {
			min = "0.0";
		}
		if (max == null) {
			max = "1.0";
		}
		if (step == null) {
			step = "0.01";
		}
		if (defval == null) {
			defval = "0.0";
		}
		createSlider(name, style, Float.parseFloat(min), Float.parseFloat(max), Float.parseFloat(step),
				Float.parseFloat(defval), vertical);
		if (group != null) {
			addElementToGroup(group, ElementType.SLIDER, name);
		}
	}

	/**
	 * Adds an image to the pane from an XML stage.
	 * 
	 * @param name
	 *            The name of the image.
	 * @param group
	 *            The parent group, if any.
	 * @param src
	 *            The image file path.
	 */
	private void _addImage(String name, String group, String src) {
		createImage(name, src);
		if (group != null) {
			addElementToGroup(group, ElementType.IMAGE, name);
		}
	}

	/**
	 * Parses an XML layer, and it's children recursively.
	 * 
	 * @param parentType
	 *            The type of the parent node, if such a node exists.
	 * @param parent
	 *            The name of the parent node, if such a node exists.
	 * @param layer
	 *            The layer to parse.
	 */
	private void _parseLayer(String parentType, String parent, NodeList layer) {
		for (int i = 0; i < layer.getLength(); i++) {
			if (layer.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element elem = _toElem(layer.item(i));

				// Generic
				String type = elem.getTagName().trim();
				String val = elem.getTextContent().trim();
				String name = elem.getAttribute("name");
				String style = elem.getAttribute("style");

				// Label
				String align = elem.getAttribute("align");

				// Progress Bar, Slider
				String min = elem.getAttribute("min");
				String max = elem.getAttribute("max");
				String step = elem.getAttribute("step");
				String defval = elem.getAttribute("default");
				String vertical = elem.getAttribute("vertical");

				// Checkbox
				String checked = elem.getAttribute("checked");

				String src = elem.getAttribute("src");

				String group = null;
				if (parent != null && parentType != null) {
					if (parentType.equals("group")) {
						group = parent;
					}
				}
				switch (type.toLowerCase()) {
				case "button":
					_addButton(name, group, style, val);
					break;
				case "group":
					_addGroup(name);
					break;
				case "label":
					_addLabel(name, group, style, align, val);
					break;
				case "progressbar":
					_addProgressBar(name, group, style, min, max, step, defval, !(vertical == null));
					break;
				case "checkbox":
					_addCheckbox(name, group, style, !(checked == null));
					break;
				case "slider":
					_addSlider(name, group, style, min, max, step, defval, !(vertical == null));
					break;
				case "image":
					_addImage(name, group, src);
					break;
				}
				_parseLayer(type, name, layer.item(i).getChildNodes());
			}
		}
	}

	/**
	 * Casts a Node to an Element.
	 * 
	 * @param n
	 *            The node to convert.
	 * @return The Element-casted Node.
	 */
	private Element _toElem(Node n) {
		return (Element) n;
	}

	/**
	 * Removes all elements from the scene.
	 */
	public void clearPane() {
		for (String key : _buttons.keySet()) {
			removeButton(key);
		}
		for (String key : _pbars.keySet()) {
			removeProgressBar(key);
		}
		for (String key : _groups.keySet()) {
			removeGroup(key);
		}
		for (String key : _sliders.keySet()) {
			removeSlider(key);
		}
		for (String key : _images.keySet()) {
			removeImage(key);
		}
		for (String key : _labels.keySet()) {
			removeLabel(key);
		}
		for (String key : _checkboxes.keySet()) {
			removeCheckbox(key);
		}
	}

	/**
	 * Updates the pane.
	 * 
	 * @param delta
	 *            The update's delta time.
	 */
	public void update(float delta) {
		while (!dlq.isEmpty()) {
			if (dlq.peek() != null)
				dlq.pop().call();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		for (BitmapFont font : _fonts.values())
			font.dispose();
		System.gc();
	}

}
