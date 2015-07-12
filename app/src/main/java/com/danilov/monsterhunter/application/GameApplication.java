package com.danilov.monsterhunter.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by Semyon on 13.07.2015.
 */
public class GameApplication extends Application {

    private static Context CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;
    }

    public static Context getContext() {
        return CONTEXT;
    }

}