package com.aftershock.mantis.scene;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import static com.aftershock.mantis.scene.util.MUtil.*;
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

public class MapLoader {
	static TmxMapLoader tMapLoader = new TmxMapLoader();

	private static void _processMap(MScene2D s, TiledMapTile[][][] mapData, float smallestW, float smallestH,
			float scale, float padding, String[] prefixes, boolean[] sensors, int[] cats, int[] groups, int[] masks,
			boolean isometric) {

		int w = mapData[0].length, h = mapData[0][0].length;
		int i = 0;
		if (isometric) {
			for (int z = 0; z < mapData.length; z++) { // LAYER/DEPTH
				for (int x = 0; x < w; x++) { // X POSITION (BL -> TR)
					for (int y = 0; y < h; y++) { // Y POSITION (TR -> BR)

						TiledMapTile tile = mapData[z][x][(h - 1) - y];
						if (tile == null)
							continue;

						String name = prefixes[z] + i++;

						// Tile Sizes
						float tileW = tile.getTextureRegion().getRegionWidth();
						float tileH = tile.getTextureRegion().getRegionHeight();

						// Tile Offsets
						float SOX = (tileW * 2.0f * (smallestW / tileW));
						float SOY = (tileH * 2.0f * (smallestH / tileW));

						// Tile Positions
						float xPos = x - y;
						float yPos = h - (x + y);

						boolean physical = (cats[z] + groups[z] + masks[z]) != 0;
						// Populate Scene
						s.createGObject(name, BodyType.StaticBody, vec((xPos * SOX) / 2, (yPos * SOY) / 2).scl(scale),
								vec(tileW + padding, tileH + padding).scl(scale), tile.getTextureRegion(), false, false,
								cats[z], groups[z], masks[z], physical);
						if (physical) {
							s.setGObjectRot(name, -30);
							s.setGObjectRotOffset(name, 30);
						}
					}
				}

			}

		} else {

			for (int z = 0; z < mapData.length; z++) { // LAYER/DEPTH

				for (int x = 0; x < w; x++) { // X POSITION (BL -> TR)
					for (int y = 0; y < h; y++) { // Y POSITION (TR -> BR)

						TiledMapTile tile = mapData[z][x][y];
						if (tile == null)
							continue;

						String name = prefixes[z] + i++;

						// Tile Sizes
						float tileW = tile.getTextureRegion().getRegionWidth() / 2.0f;
						float tileH = tile.getTextureRegion().getRegionHeight() / 2.0f;

						// Tile Offsets
						float SOX = ((x * tileW * 2.0f * (smallestW / tileW)) + tileW);
						float SOY = ((y * tileH * 2.0f * (smallestH / tileH)) + tileH);

						// Populate Scene
						s.createGObject(name, BodyType.StaticBody, new Vector2(SOX, SOY).scl(scale),
								new Vector2(tileW * scale, tileH * scale).add(padding, padding),
								tile.getTextureRegion(), false, false, cats[z], groups[z], masks[z],
								(cats[z] + groups[z] + masks[z]) != 0);
					}
				}
			}
		}
	}

	/**
	 * Loads map onto scene.
	 * 
	 * @param s
	 *            Scene on which to load the map.
	 * @param map
	 *            Map file to load (.tmx).
	 * @param scale
	 *            Map scale.
	 * @param padding
	 *            Size Padding
	 */
	public static Vector2 loadMap(MScene2D s, String map, float scale, float padding, boolean isometric) {

		TiledMap tMap = tMapLoader.load("assets/maps/" + map);
		TiledMapTileLayer firstLayer = (TiledMapTileLayer) tMap.getLayers().get(0);
		int w = firstLayer.getWidth();
		int h = firstLayer.getHeight();
		int lCount = tMap.getLayers().getCount();

		TiledMapTile[][][] layers = new TiledMapTile[lCount][w][h];
		String[] prefixes = new String[lCount];
		float smallestW = 0.0f, smallestH = 0.0f;

		boolean[] sensors = new boolean[lCount];
		int[] cats = new int[lCount];
		int[] groups = new int[lCount];
		int[] masks = new int[lCount];

		for (int lNum = 0; lNum < lCount; lNum++) {
			TiledMapTileLayer layer = (TiledMapTileLayer) tMap.getLayers().get(lNum);
			smallestW = layer.getTileWidth();
			smallestH = layer.getTileHeight();
			prefixes[lNum] = layer.getName();
			if (layer.getProperties().containsKey("cat"))
				cats[lNum] = Integer.parseInt((String) layer.getProperties().get("cat"));
			if (layer.getProperties().containsKey("group"))
				groups[lNum] = Integer.parseInt((String) layer.getProperties().get("group"));
			if (layer.getProperties().containsKey("mask"))
				masks[lNum] = Integer.parseInt((String) layer.getProperties().get("mask"));
			if (layer.getProperties().containsKey("sensor"))
				sensors[lNum] = Boolean.parseBoolean((String) layer.getProperties().get("sensor"));

			for (int x = 0; x < w; x++)
				for (int y = 0; y < h; y++)
					if (layer.getCell(x, y) != null)
						layers[lNum][x][y] = layer.getCell(x, y).getTile();
		}

		_processMap(s, layers, smallestW, smallestH, scale, padding, prefixes, sensors, cats, groups, masks, isometric);
		return new Vector2(w, h);
	}

	/**
	 * Loads map onto scene.
	 * 
	 * @param s
	 *            Scene on which to load the map.
	 * @param map
	 *            Map file to load (.tmx).
	 * @param scale
	 *            Map scale.
	 */
	public static Vector2 loadMap(MScene2D s, String map, float scale, boolean isometric) {
		return loadMap(s, map, scale, 0.0f, isometric);
	}

}
