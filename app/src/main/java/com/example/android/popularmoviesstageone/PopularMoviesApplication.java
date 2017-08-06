package com.example.android.popularmoviesstageone;

import timber.log.Timber;

/**
 * Created by akshayshahane on 05/08/17.
 */

public class PopularMoviesApplication extends android.app.Application {


    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });

        }
    }
}
