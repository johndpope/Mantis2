package com.mantis.scene;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MUIInputListener extends ClickListener {

	private MUIInputHandler _handler;

	public MUIInputListener(MUIInputHandler handler) {
		_handler = handler;
	}

	@Override
	public void clicked(InputEvent event, float x, float y) {
		_handler.input(event, x, y);
	}

}
