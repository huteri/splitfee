package com.splitfee.app.features.summarydebts;

import com.splitfee.app.data.usecase.viewparam.SummaryDebtsViewParam;
import com.splitfee.app.features.BaseView;

import java.util.List;

/**
 * Created by huteri on 5/23/17.
 */

interface SummaryDebtsView extends BaseView {
    void showSummaryDebts(List<SummaryDebtsViewParam> data);
}
