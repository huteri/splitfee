package com.splitfee.app.features.summarydetail;

import com.splitfee.app.data.usecase.viewparam.SummaryDetailViewParam;
import com.splitfee.app.features.BasePresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by huteri on 5/20/17.
 */

public class SummaryDetailPresenter extends BasePresenter<SummaryDetailView> {

    private List<SummaryDetailViewParam> data;

    @Inject
    public SummaryDetailPresenter() {
    }

    @Override
    public void onCreateView(SummaryDetailView view) {
        super.onCreateView(view);

        if(data == null)
            return;

        getView().showSummary(data);
    }

    public void setData(List<SummaryDetailViewParam> data) {
        this.data = data;
    }
}
