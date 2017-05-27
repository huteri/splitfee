package com.splitfee.app.data.usecase;

import com.splitfee.app.data.dao.TripDao;
import com.splitfee.app.data.usecase.viewparam.PersonViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.model.Trip;

import java.util.List;
import java.util.UUID;

import io.reactivex.Single;

/**
 * Created by huteri on 4/11/17.
 */

public class DisplayTripImpl implements DisplayTrip {

    private static DisplayTripImpl sInstance;
    private final TripDao tripDao;

    public static DisplayTripImpl getInstance(TripDao tripDao) {

        if (sInstance == null) {
            sInstance = new DisplayTripImpl(tripDao);
        }

        return sInstance;
    }

    public DisplayTripImpl(TripDao tripDao) {
        this.tripDao = tripDao;
    }

    @Override
    public Single<List<TripViewParam>> getTrips() {
        return tripDao.getTrips().map(TripViewParam::create);
    }

    @Override
    public Single<TripViewParam> getTrip(String id) {
        return tripDao.getTrip(id).map(TripViewParam::create);
    }

    @Override
    public Single<TripViewParam> saveTrip(String title, List<PersonViewParam> selectedChipList, String cover) {


        Trip trip = new Trip();
        trip.setId(UUID.randomUUID().toString());
        trip.setName(title);
        trip.setPersons(PersonViewParam.toPersons(selectedChipList));
        trip.setCover("https://source.unsplash.com/featured/"+trip.getId()+"/?" + cover);

        return tripDao.saveTrip(trip).map(TripViewParam::create);
    }
}
