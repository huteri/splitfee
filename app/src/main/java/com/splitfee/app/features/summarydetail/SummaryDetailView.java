package com.splitfee.app.features.summarydetail;

import com.splitfee.app.data.usecase.viewparam.SummaryDetailViewParam;
import com.splitfee.app.features.BaseView;

import java.util.List;

/**
 * Created by huteri on 5/20/17.
 */

interface SummaryDetailView extends BaseView {
    void showSummary(List<SummaryDetailViewParam> summary);
}
