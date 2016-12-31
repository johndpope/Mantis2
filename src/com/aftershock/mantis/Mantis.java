package com.aftershock.mantis;

import java.util.ArrayList;

import com.aftershock.mantis.scene.MAnimationManager;
import com.aftershock.mantis.scene.MScene2D;
import com.aftershock.mantis.scene.MUIPane;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

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

public class Mantis extends ApplicationAdapter {

	public static Game currentgame;
	private static ArrayList<MScene2D> _scenes = new ArrayList<MScene2D>();
	private static ArrayList<MUIPane> _panes = new ArrayList<MUIPane>();
	private static long _delta, _now = 0, _last = 0;

	public Mantis(Game game) {
		// Set the initial game instance
		currentgame = game;
	}

	@Override
	public void create() {
		currentgame.create();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		_now = System.nanoTime();
		currentgame.render();
		_delta = _now - _last;
		_last = _now;

		update(getDeltaTime());
	}

	/**
	 * Gets the current tick's delta time.
	 * 
	 * @return The engine's delta time.
	 */
	public static float getDeltaTime() {
		return ((float) _delta) / 1000000000.0f;
	}

	/**
	 * Adds a scene to the engine.
	 * 
	 * @param scene
	 *            The scene object to be added.
	 */
	public static void addScene(MScene2D scene) {
		_scenes.add(scene);
	}

	/**
	 * Adds a UI pane to the engine.
	 * 
	 * @param pane
	 *            The pane to be added.
	 */
	public static void addPane(MUIPane pane) {
		_panes.add(pane);
	}

	/**
	 * Sets the current game.
	 * 
	 * @param g
	 *            The new game.
	 */
	public static void setGame(Game g) {
		currentgame = g;
	}

	@Override
	public void resize(int width, int height) {
		currentgame.resize(width, height);
		for (MScene2D s : _scenes) {
			s.resize(width, height);
		}
	}

	public void update(float delta) {
		currentgame.update(delta);
		MAnimationManager.tick(delta);
	}

	@Override
	public void dispose() {
		for (MScene2D s : _scenes) {
			s.dispose();
		}
		for (MUIPane p : _panes) {
			p.dispose();
		}
	}

}
