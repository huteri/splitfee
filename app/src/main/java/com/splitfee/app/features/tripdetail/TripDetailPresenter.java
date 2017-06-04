package com.splitfee.app.features.tripdetail;

import com.splitfee.app.data.usecase.DisplayTrip;
import com.splitfee.app.data.usecase.ManageExpense;
import com.splitfee.app.data.usecase.viewparam.ExpenseViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.features.BasePresenter;
import com.splitfee.app.utils.schedulers.BaseSchedulerProvider;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

/**
 * Created by huteri on 5/4/17.
 */

public class TripDetailPresenter extends BasePresenter<TripDetailView> {

    private final DisplayTrip displayTrip;
    private final BaseSchedulerProvider scheduler;
    private final ManageExpense manageExpense;
    String tripId;
    private TripViewParam trip;

    @Inject
    public TripDetailPresenter(DisplayTrip displayTrip, ManageExpense manageExpense, BaseSchedulerProvider baseSchedulerProvider) {
        this.displayTrip = displayTrip;
        this.scheduler = baseSchedulerProvider;
        this.manageExpense = manageExpense;

    }

    public void setTripId(String trip) {
        this.tripId = trip;
    }

    @Override
    public void onAttachView(TripDetailView view) {
        super.onAttachView(view);

        loadTrip();

    }

    public void tapFabAdd() {
        getView().navigateToAddExpenseActivity(trip);
    }

    public void activityResult(ExpenseViewParam expenseViewParam) {

    }

    public void tapExpense(ExpenseViewParam expenseViewParam) {
        getView().navigateToAddExpenseActivity(trip, expenseViewParam);
    }

    public void tapSummary() {
        getView().navigateToSummary(tripId);
    }


    public void tapExpenseDelete(int pos, ExpenseViewParam expenseViewParam) {
        manageExpense.deleteExpense(expenseViewParam.getId())
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(() -> {

                    if(!isViewAttached())
                        return;

                    getView().deleteExpense(pos);

                });
    }

    private void loadTrip() {

        displayTrip.getTrip(tripId)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(new SingleObserver<TripViewParam>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(TripViewParam value) {

                        if (!isViewAttached())
                            return;

                        trip = value;
                        getView().showTitle(value.getName(), new SimpleDateFormat("dd MMMM yyyy").format(value.getCreatedAt()));

                        getView().showExpenses(value.getExpenses());
                        getView().showCover(value.getCover());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
