package com.splitfee.app.data.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by huteri on 5/24/17.
 */

public class AppConfigDaoImpl implements AppConfigDao {

    public static final String PREF_MINIMUM_MONEY = "PREF_MINIMUM_MONEY";
    public static final String PREF_CURRENCY = "PREF_CURRENCY";
    private final Context context;
    private final SharedPreferences preferences;

    public AppConfigDaoImpl(Context context) {
        this.context = context;

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public Completable saveMinimumMoney(int decimal) {
        preferences.edit().putInt(PREF_MINIMUM_MONEY, decimal).apply();
        return Completable.complete();
    }

    @Override
    public Single<Integer> getMinimumMoney() {
        return Single.just(preferences.getInt(PREF_MINIMUM_MONEY, 2));
    }

    @Override
    public Completable saveCurrency(String currency) {
        preferences.edit().putString(PREF_CURRENCY, currency).apply();
        return Completable.complete();
    }

    @Override
    public Single<String> getCurrency() {
        return Single.just(preferences.getString(PREF_CURRENCY, "USD"));
    }
}
