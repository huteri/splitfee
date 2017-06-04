package com.splitfee.app;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.splitfee.app.di.components.ApplicationComponent;
import com.splitfee.app.di.components.DaggerApplicationComponent;
import com.splitfee.app.di.modules.AppModule;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by huteri on 4/11/17.
 */

public class BaseApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (!BuildConfig.DEBUG)
            Fabric.with(this, new Crashlytics());

        initInjector();
        initRealm();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    private void initInjector() {
        applicationComponent = DaggerApplicationComponent.builder()
                .appModule(new AppModule(this))
                .build();

        applicationComponent.inject(this);
    }

    private void initRealm() {
        Realm.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("splitfee-realm")
                .schemaVersion(1)
                .migration(new DatabaseMigration())
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
