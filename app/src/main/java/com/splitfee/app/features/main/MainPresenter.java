package com.splitfee.app.features.main;

import android.os.Parcelable;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.splitfee.app.R;
import com.splitfee.app.data.usecase.DisplayTrip;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.features.BasePresenter;
import com.splitfee.app.utils.schedulers.BaseSchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by huteri on 4/11/17.
 */

class MainPresenter extends BasePresenter<MainView>{

    private final DisplayTrip displayTrip;
    private final BaseSchedulerProvider scheduler;

    @Inject
    public MainPresenter(DisplayTrip displayTrip, BaseSchedulerProvider schedulerProvider) {

        this.displayTrip = displayTrip;
        scheduler = schedulerProvider;

    }

    @Override
    public void onCreateView(MainView view) {
        super.onCreateView(view);
        getView().showTitle("Splitfee", "Splitting made easy");
        getView().showHeader();
    }

    @Override
    public void onAttachView(MainView view) {
        super.onAttachView(view);

        displayTrip.getTrips()
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(new SingleObserver<List<TripViewParam>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        if(!isViewAttached())
                            d.dispose();
                    }

                    @Override
                    public void onSuccess(List<TripViewParam> value) {
                        getView().showTrips(value);

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public void selectMenuItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_settings:
                getView().navigateToSettings();
                break;
            case R.id.nav_about:
                getView().showAboutDialog();
                break;
            case R.id.nav_feedback:
                getView().navigateToEmail();
                break;
        }
    }

    public void resultOkFromFab(TripViewParam trip) {

        getView().addTrip(trip);
    }

    public void tapItem(TripViewParam trip) {
        getView().navigateToTripDetailActivity(trip.getId());
    }

    public void tapMenuEdit(TripViewParam tripViewParam) {
        getView().navigateToEditTrip(tripViewParam);
    }

    public void resultOKEditTrip(TripViewParam tripViewParam) {
//        getView().refreshTrip(tripViewParam);
    }
}
