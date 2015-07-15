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
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
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

        TextureManager textureManager = mEngine.getTextureManager();

        BuildableBitmapTextureAtlas atlas = gameTextureManager.createAtlas(textureManager, 1024, 1024);
        GameTexture knightIdle = gameTextureManager.createTexture(atlas, Textures.KNIGHT_IDLE);
        GameTexture knightRun = gameTextureManager.createTexture(atlas, Textures.KNIGHT_RUN);
        try {
            atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            e.printStackTrace();
        }
        atlas.load();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(final OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
        this.mScene = new Scene();
        mScene.setBackground(new Background(0, 0, 1));
        final GameTextureManager gameTextureManager = GameManager.getInstance().getGameTextureManager();
        mScene.setOnAreaTouchListener(new IOnAreaTouchListener() {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final ITouchArea pTouchArea, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                GameTexture knightRun = gameTextureManager.get(Textures.KNIGHT_RUN.pTextureId);
                VertexBufferObjectManager vertexBufferObjectManager = mEngine.getVertexBufferObjectManager();
                ITiledTextureRegion texture = knightRun.getTexture();
                AnimatedSprite animatedSprite = new AnimatedSprite(pSceneTouchEvent.getX(),  pSceneTouchEvent.getY(), texture, vertexBufferObjectManager);
                animatedSprite.animate(100);
                mScene.attachChild(animatedSprite);
                return false;
            }
        });
        GameTexture knightRun = gameTextureManager.get(Textures.KNIGHT_RUN.pTextureId);
        VertexBufferObjectManager vertexBufferObjectManager = mEngine.getVertexBufferObjectManager();
        ITiledTextureRegion texture = knightRun.getTexture();
        AnimatedSprite animatedSprite = new AnimatedSprite(50,  50, texture, vertexBufferObjectManager);
        animatedSprite.animate(100);
        mScene.attachChild(animatedSprite);
        pOnCreateSceneCallback.onCreateSceneFinished(this.mScene);
    }

    @Override
    public void onPopulateScene(final Scene pScene, final OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

}
