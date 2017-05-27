package com.splitfee.app.data.usecase;

import com.splitfee.app.data.usecase.viewparam.PersonViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.model.Person;
import com.splitfee.app.model.Trip;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by huteri on 4/11/17.
 */

public interface DisplayTrip {
    Single<List<TripViewParam>> getTrips();
    Single<TripViewParam> getTrip(String id);
    Single<TripViewParam> saveTrip(String title, List<PersonViewParam> selectedChipList, String cover);
}
