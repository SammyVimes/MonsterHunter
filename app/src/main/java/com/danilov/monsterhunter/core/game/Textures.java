package com.danilov.monsterhunter.core.game;


/**
 * Created by Semyon on 13.07.2015.
 */
public enum Textures {


    KNIGHT_IDLE("knight_idle.png", 0, 0, 0, false),
    KNIGHT_RUN("knight_run_animated.png", 1, 3, 1, true),
    CONTROL_KNOB("onscreen_control_knob.png", 2, 0, 0, false),
    CONTROL_BASE("onscreen_control_base.png", 3, 0, 0, false),
    SNOW_PARTICLE("snow_particle.png", 4, 0, 0, false),
    BG_LAYER1("BG_Layer1.png", 5, 0, 0, false),
    BG_LAYER2("BG_Layer2.png", 6, 0, 0, false),
    BG_LAYER3("BG_Layer3.png", 7, 0, 0, false),
    BG_LAYER4("BG_Layer4.png", 8, 0, 0, false),
    GRASS("Grass_TopLayer.png", 9, 0, 0, false);

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
