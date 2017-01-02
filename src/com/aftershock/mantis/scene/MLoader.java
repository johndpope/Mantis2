package com.aftershock.mantis.scene;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.aftershock.mantis.MSharedObjectHandler;
import com.aftershock.mantis.scene.MScene2D.LightType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

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

public class MLoader {
	private static ClassLoader _loader = MLoader.class.getClassLoader();
	private static URLClassLoader _urlClassLoader;

	/**
	 * Loads a MGameState object from a given class.
	 * 
	 * @param gameStateClass
	 *            Class to load.
	 * @return MGameState object. Null if load failed.
	 */
	public static MGameState loadGameState(String gameStateClass) {
		try {
			return (MGameState) _loader.loadClass(gameStateClass).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Loads a MGameState object from a given class.
	 * 
	 * @param url
	 *            Jar URL to load from.
	 * @param gameStateClass
	 *            Class to load.
	 * @return MGameState object. Null if load failed.
	 */
	public static MGameState loadGameState(String url, String gameStateClass) {
		try {
			_urlClassLoader = URLClassLoader.newInstance(new URL[] { new URL("jar", "", url) });

			return (MGameState) _urlClassLoader.loadClass(gameStateClass).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Loads a game from class.
	 * 
	 * @param gameClass
	 *            Class to load from.
	 * @return Loaded game.
	 */
	public static Game loadGame(String gameClass) {
		try {
			return (Game) _loader.loadClass(gameClass).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * Loads a game from class.
	 * 
	 * @param url
	 *            Jar to find class in.
	 * @param gameClass
	 *            Game class to load from.
	 * @return Loaded game.
	 */
	public static Game loadGame(String url, String gameClass) {
		try {
			_urlClassLoader = URLClassLoader.newInstance(new URL[] { new URL("jar", "", url) });

			return (Game) _urlClassLoader.loadClass(gameClass).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Loads scene data from an XML document.
	 * 
	 * @param scene
	 *            Scene to populate.
	 * @param sceneFile
	 *            XML file to read.
	 */
	public static void loadScene(MScene2D scene, String sceneFile) {
		FileHandle file = Gdx.files.internal("assets/scenes/" + sceneFile);

		// Load map file into DOM
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file.file());
			if (doc == null)
				throw new NullPointerException("Failed to load XML scene!");
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}

		// Set Gravity
		if (doc.getElementsByTagName("gravity").getLength() > 0) {
			float gravX = Float.parseFloat(((Element) doc.getElementsByTagName("gravity").item(0))
					.getElementsByTagName("x").item(0).getAttributes().getNamedItem("value").getNodeValue());
			float gravY = Float.parseFloat(((Element) doc.getElementsByTagName("gravity").item(0))
					.getElementsByTagName("y").item(0).getAttributes().getNamedItem("value").getNodeValue());
			scene.setGrav(gravX, gravY);
		}

		// Parse map(s)
		NodeList maps = doc.getElementsByTagName("map");
		for (int i = 0; i < maps.getLength(); i++) {
			String mapSrc = ((Element) maps.item(i)).getElementsByTagName("src").item(0).getAttributes()
					.getNamedItem("value").getNodeValue();
			float mapScale = Float.parseFloat(((Element) maps.item(i)).getElementsByTagName("scale").item(0)
					.getAttributes().getNamedItem("value").getNodeValue());
			boolean isometric = false;
			if (((Element) maps.item(i)).getElementsByTagName("scale").item(0).getAttributes().getLength() > 0) {
				isometric = Boolean.parseBoolean(((Element) maps.item(i)).getElementsByTagName("scale").item(0)
						.getAttributes().getNamedItem("isometric").getNodeValue());
			}
			float padding = 0.0f;
			if (((Element) maps.item(i)).getElementsByTagName("padding").getLength() > 0) {
				padding = Float.parseFloat(((Element) maps.item(i)).getElementsByTagName("padding").item(0)
						.getAttributes().getNamedItem("value").getNodeValue());
			}
			MapLoader.loadMap(scene, mapSrc, mapScale, padding, isometric);
		}

		// Parse entities
		NodeList ents = doc.getElementsByTagName("entity");

		for (int i = 0; i < ents.getLength(); i++) {
			MGameEntity entity = null;

			String entName = ((Element) ents.item(i)).getElementsByTagName("name").item(0).getAttributes()
					.getNamedItem("value").getNodeValue();
			String packageName = ((Element) ents.item(i)).getElementsByTagName("package").item(0).getAttributes()
					.getNamedItem("value").getNodeValue();
			String className = ((Element) ents.item(i)).getElementsByTagName("class").item(0).getAttributes()
					.getNamedItem("value").getNodeValue();
			boolean isShared = false;
			int shareLevel = -1;
			float initialX = Float
					.parseFloat(((Element) (((Element) ents.item(i)).getElementsByTagName("position")).item(0))
							.getElementsByTagName("x").item(0).getAttributes().getNamedItem("value").getNodeValue());
			float initialY = Float
					.parseFloat(((Element) (((Element) ents.item(i)).getElementsByTagName("position")).item(0))
							.getElementsByTagName("y").item(0).getAttributes().getNamedItem("value").getNodeValue());
			if (((Element) ents.item(i)).getElementsByTagName("shared").getLength() > 0) {
				isShared = Boolean.parseBoolean((((Element) ents.item(i)).getElementsByTagName("shared").item(0))
						.getAttributes().getNamedItem("value").getNodeValue());
				shareLevel = Integer.parseInt((((Element) ents.item(i)).getElementsByTagName("shared").item(0))
						.getAttributes().getNamedItem("level").getNodeValue());
			}
			if (isShared) {
				if (MSharedObjectHandler.doesExist(entName, shareLevel)) {
					entity = MSharedObjectHandler.gso(entName, shareLevel);
				} else {
					try {
						entity = (MGameEntity) MLoader.class.getClassLoader().loadClass(packageName + "." + className)
								.newInstance();
					} catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
						e.printStackTrace();
					}
					MSharedObjectHandler.rso(entName, shareLevel, entity);
				}
			} else {
				try {
					entity = (MGameEntity) MLoader.class.getClassLoader().loadClass(packageName + "." + className)
							.newInstance();
				} catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			entity.init(scene, new Vector2(initialX, initialY));
			scene.addEnt(entName, entity);
		}

		// Parse Game Objects
		NodeList objects = doc.getElementsByTagName("object");

		for (int i = 0; i < objects.getLength(); i++) {
			float initialX = Float
					.parseFloat(((Element) (((Element) objects.item(i)).getElementsByTagName("position")).item(0))
							.getElementsByTagName("x").item(0).getAttributes().getNamedItem("value").getNodeValue());
			float initialY = Float
					.parseFloat(((Element) (((Element) objects.item(i)).getElementsByTagName("position")).item(0))
							.getElementsByTagName("y").item(0).getAttributes().getNamedItem("value").getNodeValue());
			float width = Float
					.parseFloat(((Element) (((Element) objects.item(i)).getElementsByTagName("size")).item(0))
							.getElementsByTagName("w").item(0).getAttributes().getNamedItem("value").getNodeValue());
			float height = Float
					.parseFloat(((Element) (((Element) objects.item(i)).getElementsByTagName("size")).item(0))
							.getElementsByTagName("h").item(0).getAttributes().getNamedItem("value").getNodeValue());
			String texture = ((Element) objects.item(i)).getElementsByTagName("texture").item(0).getAttributes()
					.getNamedItem("value").getNodeValue();
			String name = ((Element) objects.item(i)).getElementsByTagName("name").item(0).getAttributes()
					.getNamedItem("value").getNodeValue();
			BodyType type = null;
			short category;
			short group;
			short mask;
			boolean circle = (((Element) objects.item(i)).getElementsByTagName("circle").getLength() > 0)
					? Boolean.parseBoolean(((Element) objects.item(i)).getElementsByTagName("circle").item(0)
							.getAttributes().getNamedItem("value").getNodeValue())
					: false;
			boolean rotate = (((Element) objects.item(i)).getElementsByTagName("rotate").getLength() > 0)
					? Boolean.parseBoolean(((Element) objects.item(i)).getElementsByTagName("rotate").item(0)
							.getAttributes().getNamedItem("value").getNodeValue())
					: false;
			boolean physical = false;
			if (((Element) objects.item(i)).getElementsByTagName("physical").getLength() > 0) {
				physical = Boolean.parseBoolean(((Element) objects.item(i)).getElementsByTagName("physical").item(0)
						.getAttributes().getNamedItem("value").getNodeValue());
			}
			if (((Element) objects.item(i)).getElementsByTagName("phystype").getLength() > 0)
				switch (((Element) objects.item(i)).getElementsByTagName("phystype").item(0).getAttributes()
						.getNamedItem("value").getNodeValue()) {
				case "static":
					type = BodyType.StaticBody;
					break;
				case "dynamic":
					type = BodyType.DynamicBody;
					break;
				case "kinematic":
					type = BodyType.KinematicBody;
					break;
				}
			else
				type = BodyType.StaticBody;
			category = Short.parseShort(((Element) objects.item(i)).getElementsByTagName("category").item(0)
					.getAttributes().getNamedItem("value").getNodeValue());
			group = Short.parseShort(((Element) objects.item(i)).getElementsByTagName("group").item(0).getAttributes()
					.getNamedItem("value").getNodeValue());
			mask = Short.parseShort(((Element) objects.item(i)).getElementsByTagName("mask").item(0).getAttributes()
					.getNamedItem("value").getNodeValue());
			scene.createGObject(name, type, new Vector2(initialX, initialY), new Vector2(width, height), texture,
					circle, rotate, category, group, mask, physical);
		}

		// Parse lights
		NodeList lights = doc.getElementsByTagName("light");

		for (int i = 0; i < lights.getLength(); i++) {
			String name = ((Element) lights.item(i)).getElementsByTagName("name").item(0).getAttributes()
					.getNamedItem("value").getNodeValue();
			float lightR = Float.parseFloat(
					((Element) ((Element) lights.item(i)).getElementsByTagName("color").item(0).getChildNodes())
							.getElementsByTagName("r").item(0).getAttributes().getNamedItem("value").getNodeValue());
			float lightG = Float.parseFloat(
					((Element) ((Element) lights.item(i)).getElementsByTagName("color").item(0).getChildNodes())
							.getElementsByTagName("g").item(0).getAttributes().getNamedItem("value").getNodeValue());
			float lightB = Float.parseFloat(
					((Element) ((Element) lights.item(i)).getElementsByTagName("color").item(0).getChildNodes())
							.getElementsByTagName("b").item(0).getAttributes().getNamedItem("value").getNodeValue());
			float lightA = Float.parseFloat(
					((Element) ((Element) lights.item(i)).getElementsByTagName("color").item(0).getChildNodes())
							.getElementsByTagName("a").item(0).getAttributes().getNamedItem("value").getNodeValue());
			float radius = 0.0f;
			if (((Element) lights.item(i)).getElementsByTagName("radius").getLength() > 0)
				radius = Float.parseFloat(((Element) lights.item(i)).getElementsByTagName("radius").item(0)
						.getAttributes().getNamedItem("value").getNodeValue());
			float softness = 0.0f;
			if (((Element) lights.item(i)).getElementsByTagName("softness").getLength() > 0)
				softness = Float.parseFloat(((Element) lights.item(i)).getElementsByTagName("softness").item(0)
						.getAttributes().getNamedItem("value").getNodeValue());
			float posX = Float
					.parseFloat(((Element) (((Element) lights.item(i)).getElementsByTagName("position")).item(0))
							.getElementsByTagName("x").item(0).getAttributes().getNamedItem("value").getNodeValue());
			float posY = Float
					.parseFloat(((Element) (((Element) lights.item(i)).getElementsByTagName("position")).item(0))
							.getElementsByTagName("y").item(0).getAttributes().getNamedItem("value").getNodeValue());
			float angle = 0.0f;
			float conedeg = 0.0f;
			if (((Element) lights.item(i)).getElementsByTagName("angle").getLength() > 0) {
				angle = Float.parseFloat(((Element) lights.item(i)).getElementsByTagName("angle").item(0)
						.getAttributes().getNamedItem("value").getNodeValue());
			}
			if (((Element) lights.item(i)).getElementsByTagName("conedeg").getLength() > 0) {
				conedeg = Float.parseFloat(((Element) lights.item(i)).getElementsByTagName("conedeg").item(0)
						.getAttributes().getNamedItem("value").getNodeValue());
			}
			short category;
			short group;
			short mask;
			category = Short.parseShort(((Element) lights.item(i)).getElementsByTagName("category").item(0)
					.getAttributes().getNamedItem("value").getNodeValue());
			group = Short.parseShort(((Element) lights.item(i)).getElementsByTagName("group").item(0).getAttributes()
					.getNamedItem("value").getNodeValue());
			mask = Short.parseShort(((Element) lights.item(i)).getElementsByTagName("mask").item(0).getAttributes()
					.getNamedItem("value").getNodeValue());
			switch (lights.item(i).getAttributes().getNamedItem("type").getNodeValue()) {
			case "point":
				scene.createLight(name, posX, posY, LightType.POINT, radius, angle, conedeg, softness,
						new Color(lightR, lightG, lightB, lightA), category, group, mask);
				break;
			case "spot":
				scene.createLight(name, posX, posY, LightType.SPOT, radius, angle, conedeg, softness,
						new Color(lightR, lightG, lightB, lightA), category, group, mask);
				break;
			case "directional":
				scene.createLight(name, posX, posY, LightType.DIRECTIONAL, radius, angle, conedeg, softness,
						new Color(lightR, lightG, lightB, lightA), category, group, mask);
				break;
			}
			if (((Element) lights.item(i)).getElementsByTagName("attachedbody").getLength() > 0) {
				scene.addLightToBody(name, ((Element) lights.item(i)).getElementsByTagName("attachedbody").item(0)
						.getAttributes().getNamedItem("value").getNodeValue());
			}
		}
	}

}
