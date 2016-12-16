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

varying vec2 v_texCoord0;
varying vec4 v_color;

uniform sampler2D u_texture;

void main() {
	vec3 col = v_color.rgb * texture2D(u_texture, v_texCoord0).rgb;
	float a = v_color.a * texture2D(u_texture, v_texCoord0).a;


	gl_FragColor = vec4(col, a);
}