package com.aftershock.mantis.scene.animation;

import com.aftershock.mantis.MCallback;
import com.aftershock.mantis.scene.MAnimation;
import com.aftershock.mantis.scene.MScene2D;
import com.badlogic.gdx.math.Vector2;

public class MCamPosAnim implements MAnimation {
	Vector2 pos0, pos1;
	public float length, elapsed = 0.0f;
	boolean reverse = false;
	boolean loop = false;
	public MCallback callback;
	MScene2D owningScene;

	public MCamPosAnim(Vector2 begin, Vector2 end, float len, boolean loopAnim, MCallback cback, MScene2D scene) {
		pos0 = begin;
		pos1 = end;
		length = len;
		loop = loopAnim;
		callback = cback;
		owningScene = scene;
	}

	@Override
	public boolean tick(float delta) {
		delta /= owningScene.getTimeScale();

		Vector2 pos0Copy = new Vector2(pos0.x, pos0.y);
		Vector2 pos1Copy = new Vector2(pos1.x, pos1.y);
		pos0Copy.lerp(pos1Copy, elapsed / length);
		owningScene.setCamPos(pos0Copy);

		if (reverse)
			elapsed -= delta;
		else
			elapsed += delta;
		if (reverse) {
			if (elapsed < 0) {
				owningScene.setCamPos(new Vector2(pos0.x, pos0.y));
				return true;
			}
		} else {
			if (elapsed > length) {
				owningScene.setCamPos(new Vector2(pos1.x, pos1.y));
				return true;
			}
		}

		return false;
	}

	@Override
	public void reverse() {
		reverse = !reverse;
	}

	@Override
	public boolean doesLoop() {
		return loop;
	}

	@Override
	public void callCallback() {
		if (callback != null)
			callback.call();
	}
}
