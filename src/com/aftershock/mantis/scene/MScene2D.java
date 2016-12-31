package com.aftershock.mantis.scene;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.aftershock.mantis.MCallback;
import com.aftershock.mantis.Mantis;
import com.aftershock.mantis.scene.util.MLightRef;
import com.aftershock.mantis.scene.util.MObjectRef;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;

import box2dLight.ConeLight;
import box2dLight.DirectionalLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

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

public class MScene2D extends Stage {

	public enum LightType {
		POINT, SPOT, DIRECTIONAL
	}

	public int lightRays = 1024;
	private World _world;
	private LinkedHashMap<String, MGameObject> _objects = new LinkedHashMap<String, MGameObject>();
	private LinkedHashMap<String, Texture> _texCache = new LinkedHashMap<String, Texture>();
	private LinkedHashMap<String, Light> _lights = new LinkedHashMap<String, Light>();
	private LinkedHashMap<String, Vector2> _lightangles = new LinkedHashMap<String, Vector2>();
	private LinkedHashMap<String, MBackground> _backgrounds = new LinkedHashMap<String, MBackground>();
	private ArrayList<MBackground> _backgroundList = new ArrayList<MBackground>();
	private LinkedHashMap<String, Sound> _sounds = new LinkedHashMap<String, Sound>();
	private LinkedHashMap<String, Music> _music = new LinkedHashMap<String, Music>();
	private LinkedHashMap<String, MParticleSystem> _particleSystems = new LinkedHashMap<String, MParticleSystem>();
	private LinkedHashMap<String, MGameEntity> _entities = new LinkedHashMap<String, MGameEntity>();
	private LinkedHashMap<String, MPhysHook> _physHooks = new LinkedHashMap<String, MPhysHook>();
	private float _timeScale = 1.0f;
	private float _volume = 1.0f, _pitch = 1.0f, _pan = 1.0f;
	private int _objectcnt = 0;
	public ShaderProgram shader;

	Box2DDebugRenderer b2dr = new Box2DDebugRenderer();

	// Do-Later Queue
	LinkedList<MCallback> dlq = new LinkedList<MCallback>();

	Vector2 parallaxPos;
	Vector2 grav = new Vector2();
	RayHandler handler;

	public MScene2D() {
		super();
		_world = new World(Vector2.Zero, true);
		handler = new RayHandler(_world);
		handler.setShadows(true);
		handler.resizeFBO(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		handler.setAmbientLight(new Color(0f, 0f, 0f, 0.5f));
		handler.setCulling(true);
		RayHandler.setGammaCorrection(true);
		_world.setContactListener(new ContactListener() {
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {

			}

			@Override
			public void endContact(Contact contact) {
				String nameA = (String) contact.getFixtureA().getBody().getUserData();
				String nameB = (String) contact.getFixtureB().getBody().getUserData();
				for (MPhysHook hook : _physHooks.values()) {
					if (hook == null) {
						continue;
					}
					hook.check(new String[] { nameA, nameB },
							new Fixture[] { contact.getFixtureA(), contact.getFixtureB() }, false);
					hook.check(new String[] { nameB, nameA },
							new Fixture[] { contact.getFixtureB(), contact.getFixtureA() }, false);
				}
			}

			@Override
			public void beginContact(Contact contact) {
				String nameA = (String) contact.getFixtureA().getBody().getUserData();
				String nameB = (String) contact.getFixtureB().getBody().getUserData();
				for (MPhysHook hook : _physHooks.values()) {
					if (hook == null) {
						continue;
					}
					hook.check(new String[] { nameA, nameB },
							new Fixture[] { contact.getFixtureA(), contact.getFixtureB() }, true);
					hook.check(new String[] { nameB, nameA },
							new Fixture[] { contact.getFixtureB(), contact.getFixtureA() }, true);
				}
			}
		});
		Mantis.addScene(this);
	}

	// TODO: Replace with per-object and per-framebuffer shaders.
	@Deprecated
	public void setShader(String vert, String frag) {
		if (shader != null)
			shader.dispose();
		shader = new ShaderProgram(Gdx.files.internal("assets/shaders/" + vert),
				Gdx.files.internal("assets/shaders/" + frag));
		System.out.println("Compiled Shaders: vert=" + vert + ", frag=" + frag);
		System.out.println(shader.getLog());
	}

	/**
	 * Sets the scene's zoom.
	 * 
	 * @param zoom
	 *            Zoom to be set.
	 */
	public void setZoom(float zoom) {
		((OrthographicCamera) this.getCamera()).zoom = zoom;
	}

	/**
	 * Sets the light handler's shadow blur.
	 * 
	 * @param blur
	 *            Whether or not to enable shadow blurring.
	 * @param num
	 *            Amount of blurring to apply.
	 */
	public void setLightBlur(boolean blur, int num) {
		handler.setBlur(blur);
		handler.setBlurNum(num);
	}

	/**
	 * Set ambient light color/level of the scene.
	 * 
	 * @param c
	 *            Ambient light color.
	 */
	public void setAmbientLight(Color c) {
		handler.setAmbientLight(c);
	}

	/**
	 * Add sound to sound library.
	 * 
	 * @param name
	 *            The name of the sound.
	 * @param soundFile
	 *            Filename of the sound.
	 */
	public void addSound(String name, String soundFile) {
		if (_sounds.containsKey(soundFile))
			return;
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/" + soundFile));
		_sounds.put(name, sound);
	}

	/**
	 * Play sound in library.
	 * 
	 * @param name
	 *            The name of the sound.
	 * @param vol
	 *            Volume to play the sound at.
	 * @param pitch
	 *            Pitch offset to apply to the sound.
	 * @param pan
	 *            Panning offset of the sound.
	 * @param loop
	 *            Whether or not to loop the sound.
	 */
	public long playSound(String name, float vol, float pitch, float pan, boolean loop) {
		if (_sounds.get(name) != null) {
			long id = _sounds.get(name).play(vol * _volume, pitch * _pitch, pan * _pan);
			_sounds.get(name).setLooping(id, loop);
			return id;
		}
		return 0;
	}

	/**
	 * Sets the volume of a playing sound.
	 * 
	 * @param name
	 *            The name of the sound to modify.
	 * @param id
	 *            The ID of the sound instance.
	 * @param vol
	 *            The new volume.
	 */
	public void setSoundVol(String name, long id, float vol) {
		_sounds.get(name).setVolume(id, vol);
	}

	/**
	 * Sets the pitch of a playing sound.
	 * 
	 * @param name
	 *            The name of the sound to modify.
	 * @param id
	 *            The ID of the sound instance.
	 * @param pitch
	 *            The new pitch.
	 */
	public void setSoundPitch(String name, long id, float pitch) {
		_sounds.get(name).setPitch(id, pitch);
	}

	/**
	 * Sets the pan of a playing sound/
	 * 
	 * @param name
	 *            The name of the sound to modify.
	 * @param id
	 *            The ID of the sound instance.
	 * @param pan
	 *            The new pan.
	 * @param volume
	 *            The new volume.
	 */
	public void setSoundPan(String name, long id, float pan, float volume) {
		_sounds.get(name).setPan(id, pan, volume);
	}

	/**
	 * Stop all instances of this sound.
	 * 
	 * @param name
	 *            The name of the sound to be stopped.
	 */
	public void stopSound(String name) {
		if (_sounds.get(name) != null)
			_sounds.get(name).stop();
	}

	/**
	 * Stops a specific sound instance.
	 * 
	 * @param name
	 *            The name of the sound to stop.
	 * @param id
	 *            The instance ID of the sound to stop.
	 */
	public void stopSound(String name, long id) {
		_sounds.get(name).stop(id);
	}

	/**
	 * Adds music the the library.
	 * 
	 * @param name
	 *            The name of the music.
	 * @param musicFile
	 *            Filename of the music.
	 */
	public void addMusic(String name, String musicFile) {
		_music.put(name, Gdx.audio.newMusic(Gdx.files.internal("assets/sounds/" + musicFile)));
	}

	/**
	 * Plays music in library.
	 * 
	 * @param name
	 *            The name of the music.
	 */
	public void playMusic(String name) {
		if (_music.get(name) != null)
			_music.get(name).play();
	}

	/**
	 * Stop started music before completion.
	 * 
	 * @param name
	 *            The name of the music to be stopped.
	 */
	public void stopMusic(String name) {
		if (_music.get(name) != null)
			_music.get(name).stop();
	}

	/**
	 * Adds an entity to the scene.
	 * 
	 * @param name
	 *            Name of the entity to be added.
	 * @param ent
	 *            Entity to add.
	 */
	public void addEnt(String name, MGameEntity ent) {
		doLater(() -> _entities.put(name, ent));

	}

	/**
	 * Removes an entity from the scene.
	 * 
	 * @param name
	 *            The name of the entity to remove.
	 */
	public void destroyEnt(String name) {
		doLater(() -> _entities.remove(name));
	}

	/**
	 * Creates a game object.
	 * 
	 * @param name
	 *            The name of the object.
	 * @param type
	 *            Object physics type.
	 * @param initialPos
	 *            Initial position of the object.
	 * @param initialSize
	 *            Initial size of the object.
	 * @param texture
	 *            Texture region of the object.
	 * @param circle
	 *            Whether or not to treat the object as a circle.
	 * @param rotate
	 *            Does physics object rotate.
	 * @param cat
	 *            Category bit.
	 * @param group
	 *            Group bit.
	 * @param mask
	 *            Mask bit.
	 */
	public void createGObject(String name, BodyType type, Vector2 initialPos, Vector2 initialSize,
			TextureRegion texture, boolean circle, boolean rotate, int cat, int group, int mask, boolean physical) {
		doLater(() -> {
			MGameObject newObject = null;

			newObject = new MGameObject(_world, name, type, initialPos, initialSize, texture, circle, rotate, (int) cat,
					(int) group, (int) mask, physical);

			_objects.put(name, newObject);
			this.addActor(newObject);
			_objectcnt++;
		});
	}

	/**
	 * Creates a game object.
	 * 
	 * @param name
	 *            The name of the object.
	 * @param type
	 *            Object physics type.
	 * @param initialPos
	 *            Initial position of the object.
	 * @param initialSize
	 *            Initial size of the object.
	 * @param texture
	 *            The texture name of the object.
	 * @param circle
	 *            Whether or not to treat the object as a circle.
	 * @param rotate
	 *            Does physics object rotate.
	 * @param cat
	 *            Category bit.
	 * @param group
	 *            Group bit.
	 * @param mask
	 *            Mask bit.
	 */
	public void createGObject(String name, BodyType type, Vector2 initialPos, Vector2 initialSize, String texture,
			boolean circle, boolean rotate, int cat, int group, int mask, boolean physical) {
		doLater(() -> {
			MGameObject newObject = null;

			newObject = new MGameObject(_world, name, type, initialPos, initialSize, texture, circle, rotate, cat,
					group, mask, physical);

			_objects.put(name, newObject);
			this.addActor(newObject);
			_objectcnt++;
		});
	}

	/**
	 * Sets the rotation offset of a given object.
	 * 
	 * @param name
	 *            The name of the object to modify.
	 * @param rotOffset
	 *            The new rotation offset.
	 */
	public void setGObjectRotOffset(String name, float rotOffset) {
		doLater(() -> {
			_objects.get(name).setSpriteRotOffset(rotOffset);
		});
	}

	/**
	 * Gets the rotation offset of a given object.
	 * 
	 * @param name
	 *            The name of the object to poll.
	 * @return The given object's rotation offset.
	 */
	public float getGObjectRotOffset(String name) {
		return _objects.get(name).getSpriteRotOffset();
	}

	/**
	 * Sets the linear velocity of an object.
	 * 
	 * @param name
	 *            The name of the object to modify.
	 * @param vel
	 *            New velocity.
	 */
	public void setGObjectVelocity(String name, Vector2 vel) {
		doLater(() -> {
			if (_objects.get(name) != null)
				_objects.get(name).getBody().setLinearVelocity(vel);
		});
	}

	/**
	 * Sets the linear velocity of an object.
	 * 
	 * @param name
	 *            The name of the object to modify.
	 * @param x
	 *            X velocity.
	 * @param y
	 *            Y velocity.
	 */
	public void setGObjectVelocity(String name, float x, float y) {
		doLater(() -> {
			if (_objects.get(name) != null)
				_objects.get(name).getBody().setLinearVelocity(x, y);
		});
	}

	/**
	 * Sets the linear dampening of an object.
	 * 
	 * @param name
	 *            The name of the object to modify.
	 * @param linDamp
	 *            New linear dampening.
	 */
	public void setGObjectLinearDampening(String name, float linDamp) {
		doLater(() -> {
			if (_objects.get(name) != null)
				_objects.get(name).getBody().setLinearDamping(linDamp);
		});
	}

	/**
	 * Sets the friction of an object.
	 * 
	 * @param name
	 *            The name of the object to modify.
	 * @param fric
	 *            New friction for the object.
	 */
	public void setGObjectFriction(String name, float fric) {
		doLater(() -> {
			if (_objects.get(name) != null)
				_objects.get(name).getFixture().setFriction(fric);
		});
	}

	/**
	 * Sets the restitution/bounciness of the object.
	 * 
	 * @param name
	 *            The name of the object to modify.
	 * @param res
	 *            The new restitution for the object.
	 */
	public void setGObjectRestitution(String name, float res) {
		doLater(() -> {
			if (_objects.get(name) != null)
				_objects.get(name).getFixture().setRestitution(res);
		});
	}

	/**
	 * Gets the restitution of a given object.
	 * 
	 * @param name
	 *            The name of the object to poll.
	 * @return The given object's restitution.
	 */
	public float getGObjectRestitution(String name) {
		return _objects.get(name).getFixture().getRestitution();
	}

	/**
	 * Gets the friction of a given object.
	 * 
	 * @param name
	 *            The name of the object to poll.
	 * @return The given object's friction.
	 */
	public float getGObjectFriction(String name) {
		return _objects.get(name).getFixture().getFriction();
	}

	/**
	 * Gets the linear dampening of a given object.
	 * 
	 * @param name
	 *            The name of the object to poll.
	 * @return The given object's linear dampening.
	 */
	public float getGObjectLinearDampening(String name) {
		return _objects.get(name).getBody().getLinearDamping();
	}

	/**
	 * Sets an object as a sensor.
	 * 
	 * @param name
	 *            The name of the object to modify.
	 * @param sensor
	 *            Whether or not to treat the given object as a sensor.
	 */
	public void setSensor(String name, boolean sensor) {
		doLater(() -> {
			if (_objects.get(name) != null)
				_objects.get(name).getFixture().setSensor(sensor);
		});
	}

	/**
	 * Checks whether or not an object is a sensor.
	 * 
	 * @param name
	 *            The name of the object to poll.
	 * @return Whether or not the given object is a sensor.
	 */
	public boolean isSensor(String name) {
		return _objects.get(name).getFixture().isSensor();
	}

	/**
	 * Sets an object's texture.
	 * 
	 * @param name
	 *            The name of the object to modify.
	 * @param tex
	 *            New texture of the object.
	 */
	public void setTexture(String name, String tex) {
		doLater(() -> {
			if (_objects.get(name) != null)
				if (_texCache.containsKey(tex)) {
					if (_objects.get(name) != null)
						_objects.get(name).setTex(new TextureRegion(_texCache.get(tex)));
				} else {
					_texCache.put(tex, new Texture(Gdx.files.internal("assets/textures/" + tex)));
					_objects.get(name).setTex(new TextureRegion(_texCache.get(tex)));
				}
		});
	}

	/**
	 * Sets the sprite size of an object.
	 * 
	 * @param name
	 *            The name of the object to modify.
	 * @param w
	 *            The new width of the given object.
	 * @param h
	 *            The new height of the given object.
	 */
	public void setGObjectSpriteSize(String name, float w, float h) {
		doLater(() -> {
			if (_objects.get(name) != null)
				_objects.get(name).getDrawSprite().setSize(w, h);
		});
	}

	/**
	 * Sets the size of an object.
	 * 
	 * @param name
	 *            The name of the object to modify.
	 * @param size
	 *            The new size of the given object.
	 */
	public void setGObjectSpriteSize(String name, Vector2 size) {
		doLater(() -> {
			if (_objects.get(name) != null)
				_objects.get(name).getDrawSprite().setSize(size.x, size.y);
		});
	}

	/**
	 * Create a light and add it to the scene.
	 * 
	 * @param name
	 *            The name of the light.
	 * @param x
	 *            X position of the light.
	 * @param y
	 *            Y position of the light.
	 * @param type
	 *            The light's type (POINT | SPOT | DIRECTIONAL).
	 * @param dist
	 *            Light radius/distance.
	 * @param ang
	 *            Facing angle of the light.
	 * @param coneAngle
	 *            Cone angle of the light.
	 * @param c
	 *            The light's color.
	 * @param cat
	 *            Category bit.
	 * @param group
	 *            Group bit.
	 * @param mask
	 *            Mask bit.
	 */
	public void createLight(String name, float x, float y, LightType type, float dist, float ang, float coneAngle,
			Color c, int cat, int group, int mask) {
		createLight(name, x, y, type, dist, ang, coneAngle, 0.0f, c, cat, group, mask);
	}

	/**
	 * Create a light and add it to the scene.
	 * 
	 * @param name
	 *            The name of the light.
	 * @param pos
	 *            The position of the light.
	 * @param type
	 *            The light's type (POINT | SPOT | DIRECTIONAL).
	 * @param dist
	 *            Light radius/distance.
	 * @param ang
	 *            Facing angle of the light.
	 * @param coneAngle
	 *            Cone angle of the light.
	 * @param c
	 *            The light's color.
	 * @param cat
	 *            Category bit.
	 * @param group
	 *            Group bit.
	 * @param mask
	 *            Mask bit.
	 */
	public void createLight(String name, Vector2 pos, LightType type, float dist, float ang, float coneAngle, Color c,
			int cat, int group, int mask) {
		createLight(name, pos.x, pos.y, type, dist, ang, coneAngle, 0.0f, c, cat, group, mask);
	}

	/**
	 * Create a light and add it to the scene.
	 * 
	 * @param name
	 *            The name of the light.
	 * @param x
	 *            X position of the light.
	 * @param y
	 *            Y position of the light.
	 * @param type
	 *            The light's type (POINT | SPOT | DIRECTIONAL).
	 * @param dist
	 *            Light radius/distance.
	 * @param ang
	 *            Facing angle of the light.
	 * @param coneAngle
	 *            Cone angle of the light.
	 * @param c
	 *            The light's color.
	 */
	public void createLight(String name, float x, float y, LightType type, float dist, float ang, float coneAngle,
			Color c) {
		createLight(name, x, y, type, dist, ang, coneAngle, c, 0, 0, 0);
	}

	/**
	 * Create a light and add it to the scene.
	 * 
	 * @param name
	 *            The name of the light.
	 * @param pos
	 *            The position of the light.
	 * @param type
	 *            The light's type (POINT | SPOT | DIRECTIONAL).
	 * @param dist
	 *            Light radius/distance.
	 * @param ang
	 *            Facing angle of the light.
	 * @param coneAngle
	 *            Cone angle of the light.
	 * @param c
	 *            The light's color.
	 */
	public void createLight(String name, Vector2 pos, LightType type, float dist, float ang, float coneAngle, Color c) {
		createLight(name, pos.x, pos.y, type, dist, ang, coneAngle, c, 0, 0, 0);
	}

	/**
	 * Create a light and add it to the scene.
	 * 
	 * @param name
	 *            The name of the light.
	 * @param x
	 *            X position of the light.
	 * @param y
	 *            Y position of the light.
	 * @param type
	 *            The light's type (POINT | SPOT | DIRECTIONAL).
	 * @param dist
	 *            Light radius/distance.
	 * @param ang
	 *            Facing angle of the light.
	 * @param coneAngle
	 *            Cone angle of the light.
	 * @param softness
	 *            Softness of the light.
	 * @param c
	 *            The light's color.
	 */
	public void createLight(String name, float x, float y, LightType type, float dist, float ang, float coneAngle,
			float softness, Color c) {
		createLight(name, x, y, type, dist, ang, coneAngle, softness, c, 0, 0, 0);
	}

	/**
	 * Create a light and add it to the scene.
	 * 
	 * @param name
	 *            The name of the light.
	 * @param pos
	 *            The position of the light.
	 * @param type
	 *            The light's type (POINT | SPOT | DIRECTIONAL).
	 * @param dist
	 *            Light radius/distance.
	 * @param ang
	 *            Facing angle of the light.
	 * @param coneAngle
	 *            Cone angle of the light.
	 * @param softness
	 *            Softness of the light.
	 * @param c
	 *            The light's color.
	 */
	public void createLight(String name, Vector2 pos, LightType type, float dist, float ang, float coneAngle,
			float softness, Color c) {
		createLight(name, pos.x, pos.y, type, dist, ang, coneAngle, 0.0f, c, 0, 0, 0);
	}

	/**
	 * Create a light and add it to the scene.
	 * 
	 * @param name
	 *            The name of the light.
	 * @param pos
	 *            The position of the light.
	 * @param type
	 *            The light's type (POINT | SPOT | DIRECTIONAL).
	 * @param dist
	 *            Light radius/distance.
	 * @param ang
	 *            Facing angle of the light.
	 * @param coneAngle
	 *            Cone angle of the light.
	 * @param softness
	 *            Softness of the light.
	 * @param c
	 *            The light's color.
	 * @param cat
	 *            Category bit.
	 * @param group
	 *            Group bit.
	 * @param mask
	 *            Mask bit.
	 */
	public void createLight(String name, Vector2 pos, LightType type, float dist, float ang, float coneAngle,
			float softness, Color c, int cat, int group, int mask) {
		createLight(name, pos.x, pos.y, type, dist, ang, coneAngle, softness, c, cat, group, mask);
	}

	/**
	 * Create a light and add it to the scene.
	 * 
	 * @param name
	 *            The name of the light.
	 * @param x
	 *            X position of the light.
	 * @param y
	 *            Y position of the light.
	 * @param type
	 *            The light's type (POINT | SPOT | DIRECTIONAL).
	 * @param dist
	 *            Light radius/distance.
	 * @param ang
	 *            Facing angle of the light.
	 * @param coneAngle
	 *            Cone angle of the light.
	 * @param softness
	 *            Softness of the light.
	 * @param c
	 *            The light's color.
	 * @param cat
	 *            Category bit.
	 * @param group
	 *            Group bit.
	 * @param mask
	 *            Mask bit.
	 */
	public void createLight(String name, float x, float y, LightType type, float dist, float ang, float coneAngle,
			float softness, Color c, int cat, int group, int mask) {
		doLater(() -> {
			switch (type) {
			case POINT:
				PointLight point = new PointLight(handler, lightRays);
				point.setPosition(x, y);
				point.setDistance(dist);
				point.setColor(c);
				point.setSoftnessLength(softness);
				point.setContactFilter((short) cat, (short) group, (short) mask);
				_lights.put(name, point);
				_lightangles.put(name, new Vector2(0.0f, 0.0f));
				break;
			case SPOT:
				ConeLight cone = new ConeLight(handler, lightRays, c, dist, x, y, ang, coneAngle);
				cone.setSoftnessLength(0);
				cone.setContactFilter((short) cat, (short) group, (short) mask);
				cone.setSoftnessLength(softness);
				_lights.put(name, cone);
				_lightangles.put(name, new Vector2(ang, coneAngle));
				break;
			case DIRECTIONAL:
				DirectionalLight dirLight = new DirectionalLight(handler, lightRays, c, ang);
				dirLight.setContactFilter((short) cat, (short) group, (short) mask);
				dirLight.setSoftnessLength(softness);
				_lights.put(name, dirLight);
				_lightangles.put(name, new Vector2(ang, 0.0f));
				break;
			default:
				return;

			}
		});
	}

	/**
	 * Set number of rays per light (Must be called before light is created).
	 * 
	 * @param rays
	 *            Rays per light object.
	 */
	public void setRays(int rays) {
		lightRays = rays;
	}

	/**
	 * Set the scene's parallax position.
	 * 
	 * @param newParallaxPos
	 *            The new parallax position to use.
	 */
	public void setParallaxPos(Vector2 newParallaxPos) {
		parallaxPos = newParallaxPos;
	}

	/**
	 * Gets the parallax position of the the scene.
	 * 
	 * @return The scene's current parallax position.
	 */
	public Vector2 getParallaxPos() {
		return parallaxPos;
	}

	/**
	 * Set the alpha (brightness) of a light.
	 * 
	 * @param name
	 *            The name of the light to modify.
	 * @param alpha
	 *            New alpha/brightness of the light.
	 */
	public void setLightAlpha(String name, float alpha) {
		doLater(() -> {
			if (_lights.get(name) != null) {
				Color last = _lights.get(name).getColor();
				_lights.get(name).setColor(last.r, last.g, last.b, alpha);
			}
		});
	}

	/**
	 * Gets the color of a given light.
	 * 
	 * @param name
	 *            The name of the light to poll.
	 * @return The color of the given light.
	 */
	public Color getLightColor(String name) {
		return _lights.get(name).getColor();
	}

	/**
	 * Gets the alpha of a given light.
	 * 
	 * @param name
	 *            The name of the light to poll.
	 * @return The alpha of the given light.
	 */
	public float getLightAlpha(String name) {
		return _lights.get(name).getColor().a;
	}

	/**
	 * Set the color of a light.
	 * 
	 * @param name
	 *            The name of the light to modify.
	 * @param r
	 *            Red value.
	 * @param g
	 *            Green value.
	 * @param b
	 *            Blue value.
	 */
	public void setLightColor(String name, Color col) {
		doLater(() -> {
			if (_lights.get(name) != null)
				_lights.get(name).setColor(col);
		});
	}

	/**
	 * Sets the potential distance of a light.
	 * 
	 * @param name
	 *            The name of the light to modify.
	 * @param dist
	 *            New maximum distance for the light's rays.
	 */
	public void setLightDistance(String name, float dist) {
		doLater(() -> {
			if (_lights.get(name) != null)
				_lights.get(name).setDistance(dist);
		});
	}

	/**
	 * Sets the facing angle of a spotlight or directional light.
	 * 
	 * @param name
	 *            The name of the light to modify.
	 * @param angle
	 *            New angle of the light.
	 */
	public void setLightAngle(String name, float angle) {
		doLater(() -> {
			if (_lights.get(name) != null)
				_lights.get(name).setDirection(angle);
		});
	}

	/**
	 * Sets the cone angle of a spotlight.
	 * 
	 * @param name
	 *            The name of the spotlight to modify.
	 * @param angle
	 *            The new cone angle of the light.
	 */
	public void setLightConeAngle(String name, float angle) {
		doLater(() -> {
			if (_lights.get(name) != null)
				if (_lights.get(name) instanceof ConeLight)
					((ConeLight) _lights.get(name)).setConeDegree(angle);
		});
	}

	/**
	 * Sets the position of a light.
	 * 
	 * @param name
	 *            The name of the light to modify.
	 * @param x
	 *            The new X position.
	 * @param y
	 *            The new Y position.
	 */
	public void setLightPos(String name, float x, float y) {
		doLater(() -> {
			if (_lights.get(name) != null)
				_lights.get(name).setPosition(x, y);
		});
	}

	/**
	 * Sets the position of a light.
	 * 
	 * @param name
	 *            The name of the light to modify.
	 * @param pos
	 *            The new position of the light.
	 */
	public void setLightPos(String name, Vector2 pos) {
		doLater(() -> {
			if (_lights.get(name) != null)
				_lights.get(name).setPosition(pos);
		});
	}

	/**
	 * Gets the position of a light.
	 * 
	 * @param name
	 *            The name of the light to poll.
	 * @return The position of the given light.
	 */
	public Vector2 getLightPos(String name) {
		return _lights.get(name).getPosition();
	}

	/**
	 * Gets the facing angle of a given (spot or directional) light.
	 * 
	 * @param name
	 *            The name of the light to poll.
	 * @return The facing angle of the given light.
	 */
	public float getLightAngle(String name) {
		return _lightangles.get(name).x;
	}

	/**
	 * Gets the cone angle of a given (spot) light.
	 * 
	 * @param name
	 *            The name of the light to poll.
	 * @return The cone angle of the given light.
	 */
	public float getLightConeAngle(String name) {
		return _lightangles.get(name).y;
	}

	/**
	 * Translates an object in the scene.
	 * 
	 * @param name
	 *            The name of the object to translate.
	 * @param xAmt
	 *            X translation.
	 * @param yAmt
	 *            Y translation.
	 */
	public void translateGObject(String name, float xAmt, float yAmt) {
		doLater(() -> {
			MGameObject gObj = _objects.get(name);
			if (gObj != null)
				gObj.getBody().setTransform(gObj.getBody().getPosition().x + xAmt,
						gObj.getBody().getPosition().y + yAmt, gObj.getRotation());
		});
	}

	/**
	 * Translates on object in the scene.
	 * 
	 * @param name
	 *            The name of the object to translate.
	 * @param trans
	 *            The translation to apply.
	 */
	public void translateGObject(String name, Vector2 trans) {
		doLater(() -> {
			MGameObject gObj = _objects.get(name);
			if (gObj != null)
				gObj.getBody().setTransform(gObj.getBody().getPosition().x + trans.x,
						gObj.getBody().getPosition().y + trans.y, gObj.getRotation());
		});
	}

	/**
	 * Applies a linear impulse to an object in the scene.
	 * 
	 * @param name
	 *            The name of object to affect.
	 * @param xAmt
	 *            X impulse.
	 * @param yAmt
	 *            Y impulse.
	 */
	public void applyImpulse(String name, float xAmt, float yAmt) {
		doLater(() -> {
			MGameObject gObj = _objects.get(name);
			if (gObj != null)
				gObj.getBody().applyLinearImpulse(new Vector2(xAmt, yAmt), gObj.getBody().getLocalCenter(), true);
		});
	}

	/**
	 * Applies a linear impulse to an object in the scene.
	 * 
	 * @param name
	 *            The name of the object to affect.
	 * @param imp
	 *            The impulse to apply.
	 */
	public void applyImpulse(String name, Vector2 imp) {
		doLater(() -> {
			MGameObject gObj = _objects.get(name);
			if (gObj != null)
				gObj.getBody().applyLinearImpulse(imp, gObj.getBody().getLocalCenter(), true);
		});
	}

	/**
	 * Applies a force to an object in the scene.
	 * 
	 * @param name
	 *            The name of the object to affect.
	 * @param xAmt
	 *            X force.
	 * @param yAmt
	 *            Y force.
	 */
	public void applyForce(String name, float xAmt, float yAmt) {
		doLater(() -> {
			MGameObject gObj = _objects.get(name);
			if (gObj != null)
				gObj.getBody().applyForceToCenter(new Vector2(xAmt, yAmt), true);
		});
	}

	/**
	 * Applies a force to an object in the scene.
	 * 
	 * @param name
	 *            Name of the object to affect.
	 * @param force
	 *            The Force to apply.
	 */
	public void applyForce(String name, Vector2 force) {
		doLater(() -> {
			MGameObject gObj = _objects.get(name);
			if (gObj != null)
				gObj.getBody().applyForceToCenter(force, true);
		});
	}

	/**
	 * Set an object's net velocity to 0.
	 * 
	 * @param name
	 *            The name of the object to stop.
	 */
	public void stopGObject(String name) {
		doLater(() -> {
			if (_objects.get(name) != null) {
				_objects.get(name).getBody().setLinearVelocity(0, 0);
				_objects.get(name).getBody().setAngularVelocity(0);
			}
		});
	}

	/**
	 * Applies torque to an object.
	 * 
	 * @param name
	 *            The name of the object to apply to torque to.
	 * @param amt
	 *            The torque to apply.
	 */
	public void applyTorque(String name, float amt) {
		doLater(() -> {
			MGameObject gObj = _objects.get(name);
			if (gObj != null)
				_objects.get(name).getBody().applyTorque(amt * gObj.getBody().getMass(), true);
		});
	}

	/**
	 * Rotates an object by a given angle.
	 * 
	 * @param name
	 *            The name of the object to rotate.
	 * @param angle
	 *            The rotation in degrees.
	 */
	public void rotateGObject(String name, float angle) {
		doLater(() -> {
			_objects.get(name).rotate(angle);
		});
	}

	/**
	 * Sets the position of an object.
	 * 
	 * @param name
	 *            The name of the object to modify.
	 * @param x
	 *            New X position of the object.
	 * @param y
	 *            New Y position of the object.
	 */
	public void setGObjectPos(String name, float x, float y) {
		doLater(() -> {
			if (_objects.get(name) != null)
				_objects.get(name).setPos(x, y);
		});
	}

	/**
	 * Sets the position of an object.
	 * 
	 * @param name
	 *            The name of the object to modify.
	 * @param pos
	 *            The new position.
	 */
	public void setGObjectPos(String name, Vector2 pos) {
		setGObjectPos(name, pos.x, pos.y);
	}

	/**
	 * Sets the rotation of an object.
	 * 
	 * @param name
	 *            The name of the object to modify.
	 * @param angle
	 *            New angle for the object.
	 */
	public void setGObjectRot(String name, float angle) {
		doLater(() -> {
			if (_objects.get(name) != null) {
				_objects.get(name).setRot(angle);
			}
		});

	}

	/**
	 * Get the position of an object.
	 * 
	 * @param name
	 *            The name of the object to poll.
	 * @return The position of the object.
	 */
	public Vector2 getGObjectPos(String name) {
		return _objects.get(name).getBody().getPosition();
	}

	/**
	 * Get the rotation of an object.
	 * 
	 * @param name
	 *            The name of the object to poll.
	 * @return The object's rotation.
	 */
	public float getGObjectRot(String name) {
		return _objects.get(name).getRotation();
	}

	/**
	 * Sets whether or not to flip an object's sprite.
	 * 
	 * @param name
	 *            Object to modify.
	 * @param x
	 *            X Flip.
	 * @param y
	 *            Y Flip.
	 */
	public void setGObjectFlip(String name, boolean x, boolean y) {
		doLater(() -> {
			if (_objects.get(name) != null) {
				_objects.get(name).setFlip(x, y);
			}
		});
	}

	/**
	 * Gets the X axis flip status of a given object.
	 * 
	 * @param name
	 *            The name of the object to poll.
	 * @return Whether or not the given object is flipped.
	 */
	public boolean getGObjectFlippedX(String name) {
		return _objects.get(name).isFlippedX();
	}

	/**
	 * Get the Y axis flip status of a given object.
	 * 
	 * @param name
	 *            The name of the object to poll.
	 * @return Whether or not the given object is flipped on the Y axis.
	 */
	public boolean getGObjectFlippedY(String name) {
		return _objects.get(name).isFlippedY();
	}

	/**
	 * Sets the physics world's gravity.
	 * 
	 * @param gX
	 *            Gravity on the X axis.
	 * @param gY
	 *            Gravity on the Y axis.
	 */
	public void setGrav(float gX, float gY) {
		doLater(() -> {
			grav = new Vector2(gX, gY);
			_world.setGravity(grav);
		});
	}

	/**
	 * Check for the existence of a given object.
	 * 
	 * @param name
	 *            Object to query.
	 * @return Whether or not said object is present in scene.
	 */
	public boolean doesGObjectExist(String name) {
		return _objects.containsKey(name);
	}

	/**
	 * Destroy an object, removing it from the scene.
	 * 
	 * @param name
	 *            Object to remove.
	 */
	public void destroyGObject(String name) {
		doLater(() -> {
			if (_objects.get(name) == null)
				return;
			_world.destroyBody(_objects.get(name).getBody());
			while (_objects.containsKey(name)) {
				_objects.get(name).remove();
				_objects.remove(name);
				_objectcnt--;
			}
		});
	}

	/**
	 * Gets the current number of objects in the scene.
	 * 
	 * @return The number of objects in the scene.
	 */
	public int getObjectCount() {
		return _objectcnt;
	}

	/**
	 * Set the scene camera's position.
	 * 
	 * @param pos
	 *            New position for the camera.
	 */
	public void setCamPos(Vector2 pos) {
		this.getCamera().position.set(pos, 0.0f);
		this.getCamera().update();
	}

	/**
	 * Gets the position of the camera.
	 * 
	 * @return The camera's position.
	 */
	public Vector2 getCamPos() {
		return new Vector2(this.getCamera().position.x, this.getCamera().position.y);
	}

	/**
	 * Translates the scene camera.
	 * 
	 * @param transVec
	 *            Translation to apply.
	 */
	public void translateCam(Vector2 transVec) {
		doLater(() -> {
			this.getCamera().translate(new Vector3(transVec, 0.0f));
			this.getCamera().update();
		});
	}

	/**
	 * Set opacity of an object.
	 * 
	 * @param name
	 *            The name of the object to modify.
	 * @param opacity
	 *            New opacity of the object.
	 */
	public void setGObjectOpacity(String name, float opacity) {
		doLater(() -> {
			if (_objects.get(name) != null)
				_objects.get(name).setOpacity(opacity);
		});
	}

	/**
	 * Gets the linear velocity of a given object.
	 * 
	 * @param name
	 *            Name of the object to poll.
	 * @return The object's linear velocity.
	 */
	public Vector2 getGObjectVelocity(String name) {
		return _objects.get(name).getBody().getLinearVelocity();
	}

	/**
	 * Returns a requested entity.
	 * 
	 * @param name
	 *            Name of entity to retrieve.
	 * @return The entity (if found).
	 */
	@SuppressWarnings("unchecked")
	public <T> T getEntity(String name) {
		return (T) _entities.get(name);
	}

	/**
	 * Add joint between two objects.
	 * 
	 * @param go0
	 *            First object.
	 * @param go1
	 *            Second object.
	 * @param type
	 *            Type of the joint.
	 * @param jDat
	 *            Type-specific data to pass the joint.
	 */
	public void addJoint(String go0, String go1, JointType type, MJointDat jDat) {
		doLater(() -> {
			if (_objects.get(go0) != null && _objects.get(go1) != null)
				switch (type) {
				case WeldJoint:

					WeldJointDef wJntDef = new WeldJointDef();
					wJntDef.type = type;
					wJntDef.initialize(wJntDef.bodyA = _objects.get(go0).getBody(),
							wJntDef.bodyA = _objects.get(go1).getBody(), new Vector2(0, 0));
					wJntDef.collideConnected = true;
					wJntDef.dampingRatio = 1000f;

					_world.createJoint(wJntDef);
					break;
				case DistanceJoint:
					DistanceJointDef dJntDef = new DistanceJointDef();
					dJntDef.initialize(_objects.get(go0).getBody(), _objects.get(go1).getBody(),
							_objects.get(go0).getBody().getWorldCenter(), _objects.get(go1).getBody().getWorldCenter());
					dJntDef.length = jDat.dist;
					dJntDef.dampingRatio = 1000f;
					dJntDef.frequencyHz = 1600;
					dJntDef.collideConnected = true;
					_world.createJoint(dJntDef);
					break;
				case FrictionJoint:
					break;
				case GearJoint:
					break;
				case MotorJoint:
					break;
				case MouseJoint:
					break;
				case PrismaticJoint:
					break;
				case PulleyJoint:
					break;
				case RevoluteJoint:
					RevoluteJointDef rJntDef = new RevoluteJointDef();
					rJntDef.initialize(_objects.get(go0).getBody(), _objects.get(go1).getBody(), jDat.anchor);
					rJntDef.enableLimit = jDat.limit;
					rJntDef.lowerAngle = (float) Math.toRadians(jDat.limitL);
					rJntDef.upperAngle = (float) Math.toRadians(jDat.limitU);
					rJntDef.collideConnected = true;
					_world.createJoint(rJntDef);
					break;
				case RopeJoint:
					RopeJointDef rpJntDef = new RopeJointDef();
					rpJntDef.bodyA = _objects.get(go0).getBody();
					rpJntDef.bodyB = _objects.get(go1).getBody();
					rpJntDef.maxLength = jDat.dist;
					rpJntDef.collideConnected = true;

					_world.createJoint(rpJntDef);
					break;
				case Unknown:
					break;
				case WheelJoint:
					break;
				default:
					break;
				}
		});
	}

	/**
	 * Gets list of object names that start with a specific string.
	 * 
	 * @param beginning
	 *            Beginning to match.
	 * @return List of objects with the given beginning.
	 */
	public ArrayList<String> objectsThatBeginWith(String beginning) {
		ArrayList<String> objectsList = new ArrayList<String>();
		for (String key : _objects.keySet()) {
			if (key.startsWith(beginning))
				objectsList.add(key);
		}
		return objectsList;
	}

	/**
	 * Gets list of light names that start with a specific string.
	 * 
	 * @param beginning
	 *            Beginning to match.
	 * @return List of lights with the given beginning.
	 */
	public ArrayList<String> lightsThatBeginWith(String beginning) {
		ArrayList<String> lightList = new ArrayList<String>();
		for (String key : _lights.keySet()) {
			if (key.startsWith(beginning))
				lightList.add(key);
		}
		return lightList;
	}

	/**
	 * Attaches a light to a body.
	 * 
	 * @param light
	 *            Light to attach.
	 * @param body
	 *            Object to attach to.
	 */
	public void addLightToBody(String light, String body) {
		doLater(() -> {
			if (_lights.get(light) != null && _objects.get(body) != null)
				_lights.get(light).attachToBody(_objects.get(body).getBody());
		});
	}

	/**
	 * Remove a light from the scene.
	 * 
	 * @param light
	 *            Light to remove.
	 */
	public void destroyLight(String light) {
		doLater(() -> {
			if (_lights.get(light) == null)
				return;
			_lights.get(light).remove();
			_lights.remove(light);
			_lightangles.remove(light);
		});
	}

	/**
	 * Checks whether or not light exists in scene.
	 * 
	 * @param light
	 *            Light name to query.
	 * @return Whether or not the light exists in the scene.
	 */
	public boolean doesLightExist(String light) {
		return _lights.containsKey(light);
	}

	/**
	 * Add background to the scene.
	 * 
	 * @param name
	 *            The name of the background.
	 * @param offset
	 *            Offset of the background.
	 * @param bgImg
	 *            Filename of the background image.
	 * @param parallax
	 *            Background parallax amount.
	 * @param opacity
	 *            Opacity of the background.
	 * @param scale
	 *            Background scale.
	 * @param isStatic
	 *            Whether or not the background stays fixed.
	 * @param index
	 *            Index of the background.
	 */
	public void addBackground(String name, Vector2 offset, String bgImg, float parallax, float opacity, float scale,
			boolean isStatic, int index) {
		MBackground background = new MBackground(bgImg);
		background.setOpacity(opacity);
		background.setParallax(parallax);
		background.setScale(scale);
		background.setOpacity(opacity);
		background.setStatic(isStatic);
		background.setOffset(offset);
		_backgrounds.put(name, background);
		_backgroundList.add(index, background);
	}

	/**
	 * Sets the transparency of a background.
	 * 
	 * @param name
	 *            The name of the background to modify.
	 * @param alpha
	 *            New alpha of the background.
	 */
	public void setBackgroundAlpha(String name, float alpha) {
		if (_backgrounds.get(name) != null)
			_backgrounds.get(name).getBackground().setAlpha(alpha);
	}

	/**
	 * Adds a particle system to the scene.
	 * 
	 * @param name
	 *            The name of the effect to be added.
	 * @param effectName
	 *            The name of the effect file to load.
	 * @param imgFolder
	 *            The name of the folder to find the particle images.
	 * @param loop
	 *            Whether or not to loop the effect.
	 */
	public void addParticleSystem(String name, String effectName, String imgFolder, boolean loop) {
		MParticleSystem sys = new MParticleSystem(effectName, imgFolder);
		sys.setContinuous(loop);
		_particleSystems.put(name, sys);
	}

	/**
	 * Starts a particle emitter.
	 * 
	 * @param name
	 *            The name of the emitter to start.
	 */
	public void startParticleSystem(String name) {
		if (_particleSystems.get(name) != null)
			_particleSystems.get(name).start();
	}

	/**
	 * Sets the position of a particle system.
	 * 
	 * @param name
	 *            The name of the particle emitter to be modified.
	 * @param pos
	 *            The new position of the emitter.
	 */
	public void setParticleSystemPos(String name, Vector2 pos) {
		if (_particleSystems.get(name) != null)
			_particleSystems.get(name).setPos(pos);
	}

	/**
	 * Gets the position of a particle system.
	 * 
	 * @param name
	 *            The name of the emitter to poll.
	 * @return The position of the given emitter.
	 */
	public Vector2 getParticleSystemPos(String name) {
		return _particleSystems.get(name).getPos();
	}

	/**
	 * Sets a particle emitter as continuous.
	 * 
	 * @param name
	 *            The name of the emitter to modify.
	 * @param continuous
	 *            Whether or not to loop the emitter.
	 */
	public void setParticleSystemContinuous(String name, boolean continuous) {
		if (_particleSystems.get(name) != null)
			_particleSystems.get(name).setContinuous(continuous);
	}

	/**
	 * Gets whether or not a particle emitter is continuous.
	 * 
	 * @param name
	 *            The name of the emitter to poll.
	 * @return Whether or not the given particle system is continuous.
	 */
	public boolean isParticleSystemContinuous(String name) {
		return _particleSystems.get(name).getContinuous();
	}

	/**
	 * Draws the scene.
	 */
	@Override
	public void draw() {
		// Clear the screen
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		// Draw backgrounds
		this.getBatch().begin();
		for (MBackground bg : _backgroundList)
			bg.getBackground().draw(this.getBatch());
		this.getBatch().end();

		super.draw();

		this.getBatch().begin();
		for (MParticleSystem sys : _particleSystems.values())
			sys.draw(this.getBatch());
		this.getBatch().end();

		handler.setCombinedMatrix(this.getCamera().combined, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		handler.render();

		// b2dr.render(_world, this.getCamera().projection);
	}

	/**
	 * Resizes the lightmap FBO.
	 * 
	 * @param width
	 *            The new width of the FBO.
	 * @param height
	 *            The new height of the FBO.
	 */
	public void resizeLightmapFBO(float width, float height) {
		handler.resizeFBO((int) width, (int) height);
	}

	/**
	 * Adds a physics hook to the scene.
	 * 
	 * @param name
	 *            The name of the hook.
	 * @param hook
	 *            The hook to be added.
	 */
	public void addPhysHook(String name, MPhysHook hook) {
		_physHooks.put(name, hook);
	}

	/**
	 * Removes a physics hook from the scene.
	 * 
	 * @param name
	 *            The name of the physics hook to remove.
	 */
	public void removePhysHook(String name) {
		if (_physHooks.containsKey(name))
			_physHooks.remove(name);
	}

	/**
	 * Updates the scene.
	 * 
	 * @param delta
	 *            Delta time scalar.
	 */
	public void update(float delta) {
		while (!dlq.isEmpty()) {
			if (dlq.peek() != null)
				dlq.pop().call();
		}
		_world.step(1.0f / (60.0f * _timeScale), 10, 8);
		super.act();
		for (MBackground bg : _backgroundList) {
			if (bg.isStatic()) {
				bg.getBackground().setPosition(this.getCamera().position.x - this.getWidth() / 2,
						this.getCamera().position.y - this.getHeight() / 2);
				bg.getBackground().setOrigin(bg.getBackground().getWidth() / 2.0f,
						bg.getBackground().getHeight() / 2.0f);
				bg.getBackground().setSize(this.getWidth(), this.getHeight());
			} else {
				bg.getBackground().setPosition(
						((this.getCamera().position.x - bg.getBackground().getWidth() / 2) + bg.getOffset().x)
								+ this.getParallaxPos().x * bg.getParallax(),

						((this.getCamera().position.y - bg.getBackground().getHeight() / 2) + bg.getOffset().y)
								+ this.getParallaxPos().y * bg.getParallax());
				bg.getBackground().setSize(bg.getBackground().getTexture().getWidth(),
						bg.getBackground().getTexture().getHeight());
			}
			bg.getBackground().setScale(bg.getScale());
		}
		for (MGameObject o : _objects.values())
			o.update();
		for (MGameEntity e : _entities.values())
			e.update(delta);
		for (MParticleSystem sys : _particleSystems.values())
			sys.update(delta);

		Gdx.gl.glFinish();

		handler.update();
	}

	/**
	 * Add a do-later event.
	 * 
	 * @param callback
	 *            Callback to do later.
	 */
	public void doLater(MCallback callback) {
		dlq.add(callback);
	}

	/**
	 * Sets the timescale of the physics system and animation system.
	 * 
	 * @param timeScale
	 *            The new timescale.
	 */
	public void setTimeScale(float timeScale) {
		_timeScale = timeScale;
	}

	/**
	 * Sets the global volume multiplier for the scene.
	 * 
	 * @param vol
	 *            The new volume multiplier.
	 */
	public void setVolume(float vol) {
		_volume = vol;
	}

	/**
	 * Gets the global volume multiplier for the scene.
	 * 
	 * @return The current volume multiplier.
	 */
	public float getVolume() {
		return _volume;
	}

	/**
	 * Sets the global pitch multiplier for the scene.
	 * 
	 * @param pitch
	 *            The new pitch multiplier.
	 */
	public void setPitch(float pitch) {
		_pitch = pitch;
	}

	/**
	 * Gets the global pitch multiplier for the scene.
	 * 
	 * @return The current pitch multiplier.
	 */
	public float getPitch() {
		return _pitch;
	}

	/**
	 * Sets the global pan multiplier for the scene.
	 * 
	 * @param pan
	 *            The new pan multiplier.
	 */
	public void setPan(float pan) {
		_pan = pan;
	}

	/**
	 * Gets the global pan multiplier for the scene.
	 * 
	 * @return The current pan multiplier.
	 */
	public float getPan() {
		return _pan;
	}

	/**
	 * Gets the timescale of the scene.
	 * 
	 * @return The scene's timescale.
	 */
	public float getTimeScale() {
		return _timeScale;
	}

	/**
	 * Gets a reference to a given object name.
	 * 
	 * @param name
	 *            The name of the object to reference.
	 * @return The new MObjectRef.
	 */
	public MObjectRef getGObjectRef(String name) {
		return new MObjectRef(name, this);
	}

	/**
	 * Gets a reference to a given light name.
	 * 
	 * @param name
	 *            The name of the light to reference.
	 * @return The new MLightRef.
	 */
	public MLightRef getLightRef(String name) {
		return new MLightRef(name, this);
	}

	/**
	 * Resizes the scene.
	 * 
	 * @param width
	 *            The new width of the scene.
	 * @param height
	 *            The new height of the scene.
	 */
	public void resize(float width, float height) {
		this.getCamera().viewportWidth = Gdx.graphics.getWidth();
		this.getCamera().viewportHeight = Gdx.graphics.getHeight();
		handler.useCustomViewport(0, 0, (int) width, (int) height);
	}

	/**
	 * Gets the center point of the scene.
	 * 
	 * @return The scene's center point.
	 */
	public Vector2 getCenter() {
		return new Vector2(getWidth() / 2, getHeight() / 2);
	}

	/**
	 * Clears the scene.
	 */
	public void clearScene() {
		for (String object : _objects.keySet()) {
			destroyGObject(object);
		}
		for (String ent : _entities.keySet()) {
			destroyEnt(ent);
		}
		for (String light : _lights.keySet()) {
			destroyLight(light);
		}
	}

	/**
	 * Disposes of the scene and all of it's resources.
	 */
	@Override
	public void dispose() {
		super.dispose();

		b2dr.dispose();
		_world.dispose();
		for (MParticleSystem sys : _particleSystems.values())
			sys.dispose();
		for (Sound s : _sounds.values())
			s.dispose();
		for (Music m : _music.values())
			m.dispose();
		if (shader != null)
			shader.dispose();
		clearScene();
		System.gc();
	}
}
