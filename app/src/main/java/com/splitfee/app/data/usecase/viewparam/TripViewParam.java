package com.splitfee.app.data.usecase.viewparam;

import android.os.Parcel;
import android.os.Parcelable;

import com.splitfee.app.model.Trip;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huteri on 4/11/17.
 */

public class TripViewParam implements Parcelable{

    private String name;
    private String id;
    private List<PersonViewParam> persons;
    private List<ExpenseViewParam> expenses;
    private Date createdAt;
    private String cover;

    public TripViewParam() {
    }

    public static TripViewParam create(Trip trip) {
        TripViewParam tripViewParam = new TripViewParam();
        tripViewParam.name = trip.getName();
        tripViewParam.id = trip.getId();
        tripViewParam.cover = trip.getCover();
        tripViewParam.persons = PersonViewParam.create(trip.getPersons());
        tripViewParam.createdAt = trip.getCreatedAt();
        tripViewParam.expenses = ExpenseViewParam.create(trip.getExpenses());

        return tripViewParam;
    }

    protected TripViewParam(Parcel in) {
        name = in.readString();
        id = in.readString();
        createdAt = new Date(in.readLong());
        cover = in.readString();

        persons = new ArrayList<>();
        in.readTypedList(persons, PersonViewParam.CREATOR);

        expenses = new ArrayList<>();
        in.readTypedList(expenses, ExpenseViewParam.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeLong(createdAt.getTime());
        dest.writeString(cover);

        dest.writeTypedList(persons);
        dest.writeTypedList(expenses);
    }

    public static List<TripViewParam> create(List<Trip> trips) {
        List<TripViewParam> list = new ArrayList<>();

        for (Trip trip : trips) {
            list.add(create(trip));
        }

        return list;
    }

    public Trip toTrip() {
        Trip trip = new Trip();
        trip.setId(id);
        trip.setCreatedAt(createdAt);
        trip.setPersons(PersonViewParam.toPersons(persons));
        trip.setName(name);
        trip.setCover(cover);
        return trip;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<PersonViewParam> getPersons() {
        return persons;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public List<ExpenseViewParam> getExpenses() {
        return expenses;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TripViewParam> CREATOR = new Creator<TripViewParam>() {
        @Override
        public TripViewParam createFromParcel(Parcel in) {
            return new TripViewParam(in);
        }

        @Override
        public TripViewParam[] newArray(int size) {
            return new TripViewParam[size];
        }
    };

    public String getCover() {
        return cover;
    }
}
