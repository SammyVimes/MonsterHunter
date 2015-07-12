package com.danilov.monsterhunter.activity;

import com.danilov.monsterhunter.core.game.Textures;
import com.danilov.monsterhunter.core.game.manager.GameManager;
import com.danilov.monsterhunter.core.graphics.GameTexture;
import com.danilov.monsterhunter.core.graphics.GameTextureManager;

import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.AudioOptions;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.ui.activity.BaseGameActivity;

import java.io.IOException;

/**
 * Created by Semyon on 12.07.2015.
 */
public class GameActivity extends BaseGameActivity {

    //engine options
    private static final float HEIGHT = 768;
    private static final float WIDTH = 1024;
    private static final int FPS = 60;
    private Camera mCamera;

    //game options
    private Scene mScene;


    @Override
    public EngineOptions onCreateEngineOptions() {
        mCamera = new Camera(0, 0, WIDTH, HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true,
                ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(WIDTH, HEIGHT), mCamera);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        AudioOptions audioOptions = engineOptions.getAudioOptions();
        audioOptions.setNeedsMusic(true);
        audioOptions.setNeedsSound(true);
        return engineOptions;
    }

    @Override
    public Engine onCreateEngine(final EngineOptions pEngineOptions) {
        return new FixedStepEngine(pEngineOptions, FPS);
    }

    @Override
    public void onCreateResources(final OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
        SoundFactory.setAssetBasePath("sfx/");
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        GameTextureManager gameTextureManager = GameManager.getInstance().getGameTextureManager();

        BuildableBitmapTextureAtlas atlas = gameTextureManager.createAtlas(mEngine.getTextureManager(), 1024, 1024);
        GameTexture knightIdle = gameTextureManager.createTexture(atlas, Textures.KNIGHT_IDLE);
        GameTexture knightRun = gameTextureManager.createTexture(atlas, Textures.KNIGHT_RUN);

        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(final OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
        this.mScene = new Scene();
        pOnCreateSceneCallback.onCreateSceneFinished(this.mScene);
    }

    @Override
    public void onPopulateScene(final Scene pScene, final OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

}
