package com.splitfee.app.data.dao;

import com.splitfee.app.model.Trip;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by huteri on 4/11/17.
 */

public interface TripDao {
    Single<List<Trip>> getTrips();
    Single<Trip> saveTrip(Trip trip);
    Single<Trip> getTrip(String id);
}
