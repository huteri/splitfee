package com.splitfee.app.di.modules;

import android.content.Context;

import com.google.gson.Gson;
import com.splitfee.app.data.dao.AppConfigDao;
import com.splitfee.app.data.dao.AppConfigDaoImpl;
import com.splitfee.app.data.dao.ExpenseDao;
import com.splitfee.app.data.dao.ExpenseDaoImpl;
import com.splitfee.app.data.dao.PersonDao;
import com.splitfee.app.data.dao.PersonDaoImpl;
import com.splitfee.app.data.dao.TripDao;
import com.splitfee.app.data.dao.TripDaoImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by huteri on 4/11/17.
 */

@Module
public class DaoModule {

    @Singleton
    @Provides
    TripDao provideTripDao(Gson gson) {
        return new TripDaoImpl(gson);
    }

    @Singleton
    @Provides
    PersonDao providePersonDao() {
        return new PersonDaoImpl();
    }

    @Singleton
    @Provides
    ExpenseDao provideExpenseDao() {
        return new ExpenseDaoImpl();
    }

    @Singleton
    @Provides
    AppConfigDao provideAppConfig(Context context) {
        return new AppConfigDaoImpl(context);
    }
}
