package com.danilov.monsterhunter.core.game;


/**
 * Created by Semyon on 13.07.2015.
 */
public enum Textures {


    KNIGHT_IDLE("knight_idle.png", 0, 0, 0, false),
    KNIGHT_RUN("knight_run_animated.png", 1, 3, 0, true);

    public final String pTextureSrc;
    public final int pTextureId;
    public final int pTileColumns;
    public final int pTileRows;
    public final boolean isAnim;

    Textures(final String pTextureSrc, final int pTextureId, final int pTileColumns, final int pTileRows, final boolean isAnim) {
        this.pTextureSrc = pTextureSrc;
        this.pTextureId = pTextureId;
        this.pTileColumns = pTileColumns;
        this.pTileRows = pTileRows;
        this.isAnim = isAnim;
    }

}
