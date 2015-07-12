package com.danilov.monsterhunter.core.game.manager;

import android.content.Context;

import com.danilov.monsterhunter.application.GameApplication;
import com.danilov.monsterhunter.core.graphics.GameTextureManager;

/**
 * Created by Semyon on 12.07.2015.
 */
public class GameManager {

    private static GameManager INSTANCE = new GameManager();

    public static GameManager getInstance() {
        return INSTANCE;
    }

    private GameTextureManager gameTextureManager;

    private GameManager() {
        final Context context = GameApplication.getContext();
        gameTextureManager = new GameTextureManager(context);
    }

    public GameTextureManager getGameTextureManager() {
        return gameTextureManager;
    }

}