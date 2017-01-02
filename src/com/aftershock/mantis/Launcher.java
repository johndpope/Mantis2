package com.aftershock.mantis;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

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

public class Launcher {

	public static Mantis launch(Game game, String title, int width, int height, boolean resizable, boolean vsync,
			int samples, String iconPrefix) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = width;
		config.height = height;
		config.resizable = resizable;
		config.samples = samples;
		config.title = title;
		config.vSyncEnabled = vsync;

		if (iconPrefix != null) {
			config.addIcon(iconPrefix + "16.png", FileType.Internal);
			config.addIcon(iconPrefix + "32.png", FileType.Internal);
			config.addIcon(iconPrefix + "128.png", FileType.Internal);
		} else {
			config.addIcon("mantis16.png", FileType.Internal);
			config.addIcon("mantis32.png", FileType.Internal);
			config.addIcon("mantis128.png", FileType.Internal);
		}
		Mantis m = new Mantis(game);
		new LwjglApplication(m, config);
		return m;
	}

}
