package com.splitfee.app.data.dao;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by huteri on 5/24/17.
 */

public interface AppConfigDao {

    Completable saveMinimumMoney(int decimal);
    Single<Integer> getMinimumMoney();

    Completable saveCurrency(String currency);
    Single<String> getCurrency();
}
