package com.aftershock.mantis.scene;

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

import com.badlogic.gdx.math.Vector2;

public class MJointDat {
	public Vector2 anchor = new Vector2(0, 0);
	public float limitL = 0.0f;
	public float limitU = 360.0f;
	public boolean limit = false;
	public float dist = 1.0f;

}
