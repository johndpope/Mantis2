package com.mantis;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Launcher {

	public static Mantis launch(Game game, String title, int width, int height, boolean resizable, int samples,
			String iconPrefix) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = width;
		config.height = height;
		config.resizable = resizable;
		config.samples = samples;
		config.title = title;

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
