package com.splitfee.app.features.addtrip;

import com.splitfee.app.data.usecase.viewparam.PersonViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.features.BaseView;
import com.splitfee.app.model.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huteri on 4/18/17.
 */

interface AddTripView extends BaseView {
    void showPersonChips(List<PersonViewParam> chipsList);

    void additionalChips(List<PersonViewParam> newChips);

    void navigateBackToMainActivity(TripViewParam trip);

    void showParticipantMinimumRequired();

    void showTitleRequired();

    void setTitle(String name);

    void setCover(String cover);

    void setPersons(List<PersonViewParam> persons);
}
