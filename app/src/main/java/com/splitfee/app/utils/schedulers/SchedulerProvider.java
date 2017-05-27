package com.splitfee.app.utils.schedulers;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huteri on 12/20/16.
 */

public class SchedulerProvider implements BaseSchedulerProvider {
    private static BaseSchedulerProvider sInstance;

    public static BaseSchedulerProvider getInstance() {
        if(sInstance == null) {
            sInstance = new SchedulerProvider();
        }
        return sInstance;
    }

    private SchedulerProvider() {

    }

    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}
