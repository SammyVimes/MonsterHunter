package com.danilov.monsterhunter.core.game.weather;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;

/**
 * Created by Semyon on 19.07.2015.
 */
public abstract class Weather {

    protected Engine mEngine;
    protected Camera mCamera;

    public Weather(final Engine pEngine, final Camera pCamera) {
        this.mEngine = pEngine;
        this.mCamera = pCamera;
        mCamera.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(final float pSecondsElapsed) {
                onCameraUpdate(mCamera);
            }

            @Override
            public void reset() {

            }
        });
    }



    public abstract void init();

    public abstract void attachToScene(final Scene pScene);

    public abstract void onCameraUpdate(final Camera mCamera);

}
