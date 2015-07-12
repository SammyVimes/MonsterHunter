package com.danilov.monsterhunter.core.graphics;

import org.andengine.opengl.texture.region.ITextureRegion;

/**
 * Created by Semyon on 13.07.2015.
 */
public class GameTexture {

    private boolean mIsAnim;

    private ITextureRegion mTexture;

    public GameTexture(final boolean mIsAnim, final ITextureRegion mTexture) {
        this.mIsAnim = mIsAnim;
        this.mTexture = mTexture;
    }

    public boolean isAnim() {
        return mIsAnim;
    }

    public <T extends ITextureRegion> T getTexture() {
        return (T) mTexture;
    }

}
