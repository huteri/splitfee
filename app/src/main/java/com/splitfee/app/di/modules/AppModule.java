package com.splitfee.app.di.modules;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.splitfee.app.utils.schedulers.BaseSchedulerProvider;
import com.splitfee.app.utils.schedulers.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by huteri on 4/11/17.
 */

@Module
public class AppModule {

    Application mApplication;

    public AppModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Singleton
    @Provides
    BaseSchedulerProvider provideScheduler() {
        return SchedulerProvider.getInstance();
    }

    @Singleton
    @Provides
    Context provideContext(Application application) { return application.getApplicationContext(); }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
    }

}
