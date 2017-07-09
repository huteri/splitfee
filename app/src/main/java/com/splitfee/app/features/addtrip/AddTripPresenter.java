package com.splitfee.app.features.addtrip;

import com.splitfee.app.data.usecase.DisplayPerson;
import com.splitfee.app.data.usecase.DisplayTrip;
import com.splitfee.app.data.usecase.viewparam.PersonViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.features.BasePresenter;
import com.splitfee.app.utils.schedulers.BaseSchedulerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by huteri on 4/18/17.
 */

public class AddTripPresenter extends BasePresenter<AddTripView> {

    private final DisplayTrip displayTrip;
    private final BaseSchedulerProvider scheduler;
    private final DisplayPerson displayPerson;
    private List<PersonViewParam> persons = new ArrayList<>();
    private String selectedCover;
    private TripViewParam trip;

    @Inject
    public AddTripPresenter(DisplayTrip displayTrip, DisplayPerson displayPerson, BaseSchedulerProvider schedulerProvider) {
        this.displayTrip = displayTrip;
        this.scheduler = schedulerProvider;
        this.displayPerson = displayPerson;
    }

    public void setTrip(TripViewParam tripViewParam) {
        this.trip = tripViewParam;
    }

    @Override
    public void onCreateView(AddTripView view) {
        super.onCreateView(view);

        loadPersons();

        if (trip != null) {
            loadTrip();
        }
    }

    public void tapBtnSave(String title, List<PersonViewParam> selectedChipList) {

        if (title.length() == 0) {
            getView().showTitleRequired();
            return;
        } else if (selectedChipList.size() < 2) {
            getView().showParticipantMinimumRequired();
            return;
        }

        Single<TripViewParam> single = null;
        if (trip != null) {

            single = displayTrip.updateTrip(trip.getId(), trip.getCreatedAt(), title, selectedChipList, selectedCover);
        } else {
            single = displayTrip.saveTrip(title, selectedChipList, selectedCover);
        }

        single.subscribeOn(scheduler.computation())
                .observeOn(scheduler.ui())
                .subscribe(new SingleObserver<TripViewParam>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(TripViewParam value) {

                        if (!isViewAttached())
                            return;

                        getView().navigateBackToMainActivity(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public void chipsTextChanged(CharSequence text) {
        PersonViewParam person = new PersonViewParam(UUID.randomUUID().toString(), text.toString());

        List<PersonViewParam> newChips = new ArrayList<>();
        newChips.add(person);

        getView().additionalChips(newChips);

    }

    public void selectCover(String s) {
        selectedCover = s;

    }

    private void loadTrip() {
        getView().setTitle(trip.getName());
        getView().setCover(trip.getCover());
    }

    private void loadPersons() {

        displayPerson.getPersons()
                .subscribeOn(scheduler.computation())
                .observeOn(scheduler.ui())
                .subscribe(new SingleObserver<List<PersonViewParam>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<PersonViewParam> value) {
                        if (!isViewAttached())
                            return;


                        persons.addAll(value);
                        getView().showPersonChips(persons);

                        if (trip != null)
                            getView().setPersons(trip.getPersons());

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
