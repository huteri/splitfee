package com.splitfee.app.data.dao;

import com.splitfee.app.model.Trip;

import java.util.List;

import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by huteri on 4/11/17.
 */

public class TripDaoImpl implements TripDao {


    public TripDaoImpl() {
    }

    @Override
    public Single<List<Trip>> getTrips() {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Trip> all = realm.where(Trip.class).findAllSorted("createdAt", Sort.DESCENDING);
        List<Trip> trips = realm.copyFromRealm(all);

        realm.close();

        return Single.just(trips);
    }

    @Override
    public Single<Trip> saveTrip(final Trip trip) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            realm1.insertOrUpdate(trip);
        });

        realm.close();

        return Single.just(trip);
    }

    @Override
    public Single<Trip> getTrip(String id) {

        Realm realm = Realm.getDefaultInstance();

        Trip id1 = realm.where(Trip.class).equalTo("id", id).findFirst();

        Trip trip = null;
        if (id1 != null) {
            trip = realm.copyFromRealm(id1);
        }

        realm.close();
        return trip != null ? Single.just(trip) : null;
    }
}
