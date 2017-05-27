package com.splitfee.app.features.summarydebts;


import com.splitfee.app.data.usecase.viewparam.SummaryDebtsViewParam;
import com.splitfee.app.features.BasePresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by huteri on 5/23/17.
 */

class SummaryDebtsPresenter extends BasePresenter<SummaryDebtsView> {

    private List<SummaryDebtsViewParam> data;

    @Inject
    public SummaryDebtsPresenter() {
    }

    public void setData(List<SummaryDebtsViewParam> data) {
        this.data = data;

    }

    @Override
    public void onCreateView(SummaryDebtsView view) {
        super.onCreateView(view);

        if (data == null)
            return;

        getView().showSummaryDebts(data);

    }
}
