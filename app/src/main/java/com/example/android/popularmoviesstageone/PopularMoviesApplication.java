package com.example.android.popularmoviesstageone;

import java.util.Timer;

import timber.log.BuildConfig;
import timber.log.Timber;

/**
 * Created by akshayshahane on 05/08/17.
 */

public class PopularMoviesApplication extends android.app.Application {


    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());

        }
    }
}
