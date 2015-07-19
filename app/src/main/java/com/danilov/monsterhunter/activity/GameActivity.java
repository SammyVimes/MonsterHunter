package com.danilov.monsterhunter.activity;

import android.opengl.GLES20;

import com.danilov.monsterhunter.core.game.Textures;
import com.danilov.monsterhunter.core.game.manager.GameManager;
import com.danilov.monsterhunter.core.game.weather.SnowyWeather;
import com.danilov.monsterhunter.core.game.weather.Weather;
import com.danilov.monsterhunter.core.graphics.GameTexture;
import com.danilov.monsterhunter.core.graphics.GameTextureManager;
import com.danilov.monsterhunter.core.layer.ParallaxLayer;

import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.options.AudioOptions;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.SingleValueSpanEntityModifier;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.emitter.RectangleParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.IParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
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
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

import java.io.IOException;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

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
    private DigitalOnScreenControl mDigitalOnScreenControl;


    @Override
    public EngineOptions onCreateEngineOptions() {
        BoundCamera camera = new BoundCamera(0, 0, WIDTH, HEIGHT);
        camera.setBounds(-1000000, 0, 100000, 1000000);
        camera.setBoundsEnabled(true);
        mCamera = camera;
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
        GameTexture controlBase = gameTextureManager.createTexture(atlas, Textures.CONTROL_BASE);
        GameTexture controlKnob = gameTextureManager.createTexture(atlas, Textures.CONTROL_KNOB);
        GameTexture knightRun = gameTextureManager.createTexture(atlas, Textures.KNIGHT_RUN);
        GameTexture snowParticle = gameTextureManager.createTexture(atlas, Textures.SNOW_PARTICLE);


        BuildableBitmapTextureAtlas bgAtlas = gameTextureManager.createRepeatingAtlas(textureManager, 1024, 1024);
        GameTexture bgLayer1 = gameTextureManager.createTexture(bgAtlas, Textures.BG_LAYER1);
        GameTexture bgLayer2 = gameTextureManager.createTexture(bgAtlas, Textures.BG_LAYER2);
        GameTexture bgLayer3 = gameTextureManager.createTexture(bgAtlas, Textures.BG_LAYER3);
        GameTexture bgLayer4 = gameTextureManager.createTexture(bgAtlas, Textures.BG_LAYER4);

        BuildableBitmapTextureAtlas repeatingAtlas = gameTextureManager.createRepeatingAtlas(textureManager, 1024, 1024);
        GameTexture grassLayer = gameTextureManager.createTexture(repeatingAtlas, Textures.GRASS);

        try {
            atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
            bgAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
            repeatingAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            e.printStackTrace();
        }
        atlas.load();
        bgAtlas.load();
        repeatingAtlas.load();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(final OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
        this.mScene = new Scene();
        VertexBufferObjectManager vertexBufferObjectManager = mEngine.getVertexBufferObjectManager();
        mScene.setBackground(new Background(0.15f, 0.05f, 0.35f));
        final GameTextureManager gameTextureManager = GameManager.getInstance().getGameTextureManager();


        GameTexture knightRun = gameTextureManager.get(Textures.KNIGHT_RUN.pTextureId);
        ITiledTextureRegion texture = knightRun.getTexture();
        final AnimatedSprite animatedSprite = new AnimatedSprite(350,  50, texture, vertexBufferObjectManager);

        final PhysicsHandler physicsHandler = new PhysicsHandler(animatedSprite);
        mEngine.registerUpdateHandler(physicsHandler);

        GameTexture controlBase = gameTextureManager.get(Textures.CONTROL_BASE.pTextureId);
        GameTexture controlKnob = gameTextureManager.get(Textures.CONTROL_KNOB.pTextureId);
        final float x = controlBase.getTexture().getWidth();
        final float y = controlBase.getTexture().getHeight();
        // controls
        this.mDigitalOnScreenControl = new DigitalOnScreenControl(x, y, this.mCamera, controlBase.getTexture(), controlKnob.getTexture(), 0.1f, false, mEngine.getVertexBufferObjectManager(), new BaseOnScreenControl.IOnScreenControlListener() {
            @Override
            public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
                if (pValueX > 0) {
                    physicsHandler.setVelocityX(100);
                } else if (pValueX < 0) {
                    physicsHandler.setVelocityX(-100);
                } else {
                    physicsHandler.setVelocityX(0);
                }
            }
        });
        this.mDigitalOnScreenControl.refreshControlKnobPosition();

        mScene.setChildScene(this.mDigitalOnScreenControl);

        TextureRegion grassLayer = gameTextureManager.get(Textures.GRASS.pTextureId).getTexture();

        final Sprite grass = new Sprite(grassLayer.getWidth() / 2, grassLayer.getTextureY() / 2, 5000, grassLayer.getHeight(), grassLayer, vertexBufferObjectManager);

        mScene.attachChild(grass);

        GameTexture bgLayer1 = gameTextureManager.get(Textures.BG_LAYER1.pTextureId);
        GameTexture bgLayer2 = gameTextureManager.get(Textures.BG_LAYER2.pTextureId);
        GameTexture bgLayer3 = gameTextureManager.get(Textures.BG_LAYER3.pTextureId);
        GameTexture bgLayer4 = gameTextureManager.get(Textures.BG_LAYER4.pTextureId);

        final ParallaxLayer BGParallaxLayer = new ParallaxLayer(this.mCamera, true);


        final Sprite BG4 = new Sprite(0f, 256f, bgLayer4.getTexture(), vertexBufferObjectManager);
        BGParallaxLayer.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(0.25f, BG4, true));

        final Sprite BG3 = new Sprite(0f, 240f, bgLayer3.getTexture(), vertexBufferObjectManager);
        BGParallaxLayer.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(0.5f, BG3, true));

        final Sprite BG2 = new Sprite(0f, 95f, bgLayer2.getTexture(), vertexBufferObjectManager);
        BGParallaxLayer.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(0.75f, BG2, true));

        final Sprite BG1 = new Sprite(0f, 64f, bgLayer1.getTexture(), vertexBufferObjectManager);
        BGParallaxLayer.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(1f, BG1, true));
        BG4.setAnchorCenter(0f, 0f);
        BG3.setAnchorCenter(0f, 0f);
        BG2.setAnchorCenter(0f, 0f);
        BG1.setAnchorCenter(0f, 0f);

        mScene.attachChild(BGParallaxLayer);


        animatedSprite.animate(100);
        mScene.attachChild(animatedSprite);
        mCamera.setChaseEntity(animatedSprite);

        Weather snowyWeather = new SnowyWeather(getEngine(), mCamera, WIDTH, HEIGHT);
        snowyWeather.init();
        snowyWeather.attachToScene(mScene);

        pOnCreateSceneCallback.onCreateSceneFinished(this.mScene);
    }

    @Override
    public void onPopulateScene(final Scene pScene, final OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

}
