package com.splitfee.app.features.main;

import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.features.BaseView;

import java.util.List;

/**
 * Created by huteri on 4/11/17.
 */

interface MainView extends BaseView {
    void showTitle(String story, String s);

    void showTrips(List<TripViewParam> value);

    void addTrip(TripViewParam trip);

    void navigateToTripDetailActivity(String trip);

    void navigateToSettings();

    void showHeader();

    void showAboutDialog();

    void navigateToEmail();
}
