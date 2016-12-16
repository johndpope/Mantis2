package com.mantis.scene;

import com.badlogic.gdx.physics.box2d.Fixture;

public interface MPhysHook {

	public void check(String name[], Fixture fix[], boolean begin);

}
