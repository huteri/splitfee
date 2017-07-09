package com.splitfee.app.features.summary;

import com.splitfee.app.data.usecase.viewparam.SummaryDebtsViewParam;
import com.splitfee.app.data.usecase.viewparam.SummaryDetailViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.features.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huteri on 5/20/17.
 */

interface SummaryView extends BaseView {
    void showTripsSummary(List<SummaryDetailViewParam> summaryDetail, List<SummaryDebtsViewParam> summaryDebts);

    void showShareIntent(TripViewParam trip, ArrayList<SummaryDebtsViewParam> summaryDebts);
}
