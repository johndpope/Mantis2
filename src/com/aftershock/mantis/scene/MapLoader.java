package com.aftershock.mantis.scene;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

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

	/**
	 * Loads map onto scene.
	 * 
	 * @param s
	 *            Scene on which to load the map.
	 * @param map
	 *            Map file to load (.tmx).
	 * @param prefixes
	 *            Layer prefixes.
	 * @param sensor
	 *            Layer sensor states.
	 * @param cats
	 *            Category bits.
	 * @param groups
	 *            Group bits.
	 * @param masks
	 *            Mask bits.
	 * @param smallestW
	 *            Base width.
	 * @param smallestH
	 *            Base height.
	 * @param scale
	 *            Map scale.
	 * @param padding
	 *            Size Padding
	 */
	public static Vector2 loadMap(MScene2D s, String map, String[] prefixes, boolean[] sensor, short[] cats,
			short[] groups, short[] masks, int smallestW, int smallestH, float scale, float padding) {
		TiledMap tMap = tMapLoader.load("assets/maps/" + map);
		int i = 0;
		float w = 0.0f, h = 0.0f;

		for (int layerNum = 0; layerNum < tMap.getLayers().getCount(); layerNum++) {
			TiledMapTileLayer layer = (TiledMapTileLayer) tMap.getLayers().get(layerNum);
			w = layer.getWidth();
			h = layer.getWidth();
			for (int x = 0; x < layer.getWidth(); x++)
				for (int y = 0; y < layer.getHeight(); y++) {
					if (layer.getCell(x, y) != null) {

						Cell c = layer.getCell(x, y);

						TiledMapTile tile = c.getTile();

						float tileW = tile.getTextureRegion().getRegionWidth();
						float tileH = tile.getTextureRegion().getRegionHeight();
						s.createGObject(prefixes[layerNum] + i, BodyType.StaticBody,

								new Vector2(((x * tileW * 2.0f * (smallestW / tileW)) + tileW) * scale,
										((y * tileH * 2.0f * (smallestH / tileH)) + tileH) * scale),

								new Vector2(tileW * scale, tileH * scale).add(padding, padding),

								tile.getTextureRegion(), false, false, cats[layerNum], groups[layerNum],
								masks[layerNum]);
						s.setSensor(prefixes[layerNum] + i, sensor[layerNum]);
						i++;
					}

				}
			i = 0;
		}
		return new Vector2(w, h);
	}

	/**
	 * Loads map onto scene.
	 * 
	 * @param s
	 *            Scene on which to load the map.
	 * @param map
	 *            Map file to load (.tmx).
	 * @param prefixes
	 *            Layer prefixes.
	 * @param sensor
	 *            Layer sensor states.
	 * @param cats
	 *            Category bits.
	 * @param groups
	 *            Group bits.
	 * @param masks
	 *            Mask bits.
	 * @param smallestW
	 *            Base width.
	 * @param smallestH
	 *            Base height.
	 * @param scale
	 *            Map scale.
	 */
	public static Vector2 loadMap(MScene2D s, String map, String[] prefixes, boolean[] sensor, short[] cats,
			short[] groups, short[] masks, int smallestW, int smallestH, float scale) {
		return loadMap(s, map, prefixes, sensor, cats, groups, masks, smallestW, smallestH, scale, 0.0f);
	}

}
