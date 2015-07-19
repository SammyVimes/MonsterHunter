package com.danilov.monsterhunter.core.game.weather;

import android.opengl.GLES20;

import com.danilov.monsterhunter.core.game.Textures;
import com.danilov.monsterhunter.core.game.manager.GameManager;
import com.danilov.monsterhunter.core.graphics.GameTexture;
import com.danilov.monsterhunter.core.graphics.GameTextureManager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityFactory;
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
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.IMatcher;
import org.andengine.util.adt.pool.Pool;
import org.andengine.util.modifier.IModifier;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Created by Semyon on 19.07.2015.
 */
public class SnowyWeather extends Weather {

    private Queue<PositionXSwingModifier> pool = new ArrayDeque<>();

    private BatchedPseudoSpriteParticleSystem mParticleSystem = null;
    private RectangleParticleEmitter particleEmitter = null;
    private TextureRegion snowParticle;
    private float mWidth;
    private float mHeight;

    public SnowyWeather(final Engine pEngine, final Camera camera, final float mWidth, final float mHeight) {
        super(pEngine, camera);
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        final GameTextureManager gameTextureManager = GameManager.getInstance().getGameTextureManager();
        snowParticle = gameTextureManager.get(Textures.SNOW_PARTICLE.pTextureId).getTexture();
    }

    @Override
    public void init() {
        particleEmitter = new RectangleParticleEmitter(mWidth / 2, mHeight, mWidth, 1);
        mParticleSystem = new BatchedPseudoSpriteParticleSystem(
                particleEmitter,
                6, 15, 300, snowParticle,
                mEngine.getVertexBufferObjectManager()
        );
        mParticleSystem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
        mParticleSystem.addParticleInitializer(new VelocityParticleInitializer<Entity>(-3, 3, -20, -40));
        mParticleSystem.addParticleInitializer(new AccelerationParticleInitializer<Entity>(-3, 3, -6, -10));
        mParticleSystem.addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 360.0f));
        mParticleSystem.addParticleInitializer(new ExpireParticleInitializer<Entity>(15f));
        mParticleSystem.addParticleInitializer(new ScaleParticleInitializer<Entity>(0.2f, 0.5f));
        mParticleSystem.addParticleInitializer(new RegisterXSwingEntityModifierInitializer<Entity>(10f, 0f, (float) Math.PI * 8, 3f, 25f));
        mParticleSystem.addParticleModifier(new AlphaParticleModifier<Entity>(10f, 15f, 1.0f, 0.0f));
    }

    private Entity entity;

    @Override
    public void attachToScene(final Scene pScene) {
        entity = new Entity();
        entity.attachChild(mParticleSystem);
        pScene.attachChild(entity);
        entity.setWidth(mWidth);
        entity.setHeight(mHeight);
        entity.setPosition(pScene);
    }

    @Override
    public void onCameraUpdate(final Camera mCamera) {
        entity.setPosition(mCamera.getCenterX(), mCamera.getCenterY());
    }

    public class PositionXSwingModifier extends SingleValueSpanEntityModifier {
        private float mInitialX;

        private float mFromMagnitude;
        private float mToMagnitude;

        public PositionXSwingModifier(float pDuration, float pFromValue, float pToValue,
                                      float pFromMagnitude, float pToMagnitude) {
            super(pDuration, pFromValue, pToValue);
            mFromMagnitude = pFromMagnitude;
            mToMagnitude = pToMagnitude;
        }

        @Override
        protected void onSetInitialValue(IEntity pItem, float pValue) {
            mInitialX = pItem.getX();
        }

        @Override
        protected void onModifierFinished(final IEntity pItem) {
            super.onModifierFinished(pItem);
            pool.add(this);
        }

        @Override
        protected void onSetValue(IEntity pItem, float pPercentageDone, float pValue) {
            float currentMagnitude = mFromMagnitude + (mToMagnitude - mFromMagnitude) * pPercentageDone;
            float currentSinValue = (float) Math.sin(pValue);
            pItem.setX(mInitialX + currentMagnitude * currentSinValue);
        }

        @Override
        public PositionXSwingModifier deepCopy() throws DeepCopyNotSupportedException {
            throw new DeepCopyNotSupportedException();
        }

    }

    public class RegisterXSwingEntityModifierInitializer<T extends IEntity> implements
            IParticleInitializer<T> {

        private float mDuration;
        private float mFromValue;
        private float mToValue;
        private float mFromMagnitude;
        private float mToMagnitude;

        private final Random RANDOM = new Random(System.currentTimeMillis());

        public RegisterXSwingEntityModifierInitializer(float pDuration, float pFromValue, float pToValue,
                                                       float pFromMagnitude, float pToMagnitude) {
            mDuration = pDuration;
            mFromValue = pFromValue;
            mToValue = pToValue;
            mFromMagnitude = pFromMagnitude;
            mToMagnitude = pToMagnitude;
        }

        @Override
        public void onInitializeParticle(Particle<T> pParticle) {
            PositionXSwingModifier modifier = pool.poll();
            if (modifier == null) {
                modifier = new PositionXSwingModifier(mDuration,
                                mFromValue, mFromValue + RANDOM.nextFloat() * (mToValue - mFromValue),
                                mFromMagnitude, mFromMagnitude + RANDOM.nextFloat() * (mToMagnitude - mFromMagnitude));
            }
            pParticle.getEntity().registerEntityModifier(modifier);
        }



    }


}
