package com.danilov.monsterhunter.core.graphics;

import android.content.Context;

import com.danilov.monsterhunter.core.game.Textures;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Semyon on 13.07.2015.
 */
public class GameTextureManager {

    private Map<Integer, GameTexture> mTextures = new HashMap<>();
    private Context mContext;

    public GameTextureManager(final Context pContext) {
        this.mContext = pContext;
    }

    public BuildableBitmapTextureAtlas createAtlas(final TextureManager pTextureManager, final int pWidth, final int pHeight) {
        return new BuildableBitmapTextureAtlas(pTextureManager, pWidth, pHeight);
    }
    public GameTexture createTexture(final BuildableBitmapTextureAtlas pAtlas, final Textures pTexture) {
        final GameTexture gameTexture;
        if (pTexture.isAnim) {
            gameTexture = new GameTexture(true, BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(pAtlas, mContext, pTexture.pTextureSrc, pTexture.pTileColumns, pTexture.pTileRows));
        } else {
            gameTexture = new GameTexture(false, BitmapTextureAtlasTextureRegionFactory.createFromAsset(pAtlas, mContext, pTexture.pTextureSrc));
        }
        mTextures.put(pTexture.pTextureId, gameTexture);
        return gameTexture;
    }

    public GameTexture put(final Integer pKey, final GameTexture pGameTexture) {
        return mTextures.put(pKey, pGameTexture);
    }

    public GameTexture get(final Integer pKey) {
        return mTextures.get(pKey);
    }

}
