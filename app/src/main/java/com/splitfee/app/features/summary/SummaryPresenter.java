package com.splitfee.app.features.summary;

import com.splitfee.app.data.usecase.DisplayTrip;
import com.splitfee.app.data.usecase.viewparam.ExpenseViewParam;
import com.splitfee.app.data.usecase.viewparam.PersonViewParam;
import com.splitfee.app.data.usecase.viewparam.SplitViewParam;
import com.splitfee.app.data.usecase.viewparam.SummaryDebtsViewParam;
import com.splitfee.app.data.usecase.viewparam.SummaryDetailViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.features.BasePresenter;
import com.splitfee.app.model.Person;
import com.splitfee.app.utils.PriceUtil;
import com.splitfee.app.utils.schedulers.BaseSchedulerProvider;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by huteri on 5/20/17.
 */

public class SummaryPresenter extends BasePresenter<SummaryView> {

    private final DisplayTrip displayTrip;
    private final BaseSchedulerProvider scheduler;
    private String tripId;
    private ArrayList<SummaryDebtsViewParam> summaryDebts;
    private ArrayList<SummaryDetailViewParam> summaryDetail;
    private TripViewParam trip;

    @Inject
    public SummaryPresenter(DisplayTrip displayTrip, BaseSchedulerProvider schedulerProvider) {
        this.displayTrip = displayTrip;
        this.scheduler = schedulerProvider;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    @Override
    public void onCreateView(SummaryView view) {
        super.onCreateView(view);

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
                        loadTripSummary(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void loadTripSummary(TripViewParam trip) {

        summaryDetail = new ArrayList<>();
        summaryDebts = new ArrayList<>();

        for (PersonViewParam personViewParam : trip.getPersons()) {

            SummaryDetailViewParam summaryViewParam = new SummaryDetailViewParam();
            summaryViewParam.setPerson(personViewParam);

            for (ExpenseViewParam expenseViewParam : trip.getExpenses()) {
                for (SplitViewParam splitViewParam : expenseViewParam.getPayers()) {
                    if (splitViewParam.getPerson().equals(personViewParam)) {
                        summaryViewParam.setPaid(summaryViewParam.getPaid().add(splitViewParam.getAmount()));
                    }
                }

                for (SplitViewParam splitViewParam : expenseViewParam.getSplit()) {
                    if (splitViewParam.getPerson().equals(personViewParam)) {
                        summaryViewParam.setSpent(summaryViewParam.getSpent().add(splitViewParam.getAmount()));
                    }
                }
            }

            summaryDetail.add(summaryViewParam);
        }

        List<SummaryDetailViewParam> positives = new ArrayList<>();

        List<SummaryDetailViewParam> negatives = new ArrayList<>();

        for (SummaryDetailViewParam summaryDetailViewParam : summaryDetail) {

            if (summaryDetailViewParam.getSummary().compareTo(BigDecimal.ZERO) >= 0) {
                positives.add(summaryDetailViewParam.copy());
            } else {
                negatives.add(summaryDetailViewParam.copy());
            }
        }

        Collections.sort(negatives, (o1, o2) -> o1.getSummary().compareTo(o2.getSummary()));

        int currentPosition = 0;

        while(currentPosition < negatives.size()) {
            SummaryDetailViewParam negative = negatives.get(currentPosition);

            double minDiff = Integer.MAX_VALUE;
            int index = Integer.MAX_VALUE;

            for (int i = 0; i < positives.size(); i++) {

                double diff = positives.get(i).getSummary().subtract(negative.getSummary().abs()).doubleValue();
                if (Math.abs(diff) < minDiff && positives.get(i).getSummary().compareTo(BigDecimal.ZERO) > 0) {
                    minDiff = Math.abs(diff);
                    index = i;
                }
            }

            SummaryDebtsViewParam data = new SummaryDebtsViewParam();
            data.setPayer(negative.getPerson());
            data.setReceiver(positives.get(index).getPerson());

            BigDecimal amount = positives.get(index).getSummary().subtract(negative.getSummary().abs());

            if(amount.compareTo(BigDecimal.ZERO) >= 0) {
                positives.get(index).setSummary(amount);

                data.setAmount(negative.getSummary().abs());
                negative.setSummary(BigDecimal.ZERO);
                currentPosition++;
            } else if(amount.compareTo(BigDecimal.ZERO) < 0){
                negative.setSummary(amount);

                data.setAmount(positives.get(index).getSummary().abs());
                positives.get(index).setSummary(BigDecimal.ZERO);
            }

            summaryDebts.add(data);
        }

        getView().showTripsSummary(summaryDetail, summaryDebts);

    }

    public void tapShareMenu() {

        if(trip == null)
            return;

        getView().showShareIntent(trip, summaryDebts);

    }
}
