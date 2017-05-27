package com.splitfee.app.features.summarydetail;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.splitfee.app.BaseApplication;
import com.splitfee.app.R;
import com.splitfee.app.data.usecase.viewparam.SummaryDetailViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.features.BaseFragment;
import com.splitfee.app.features.adapters.SummaryAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by huteri on 5/20/17.
 */

public class SummaryDetailFragment extends BaseFragment implements SummaryDetailView {

    private static final String EXTRA_DATA = "EXTRA_DATA";
    @BindView(R.id.rv_summary)
    RecyclerView rvSummary;

    @Inject
    SummaryDetailPresenter presenter;
    private SummaryAdapter summaryAdapter;

    public static Fragment newInstance(List<SummaryDetailViewParam> trip) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_DATA, (ArrayList<? extends Parcelable>) trip);

        Fragment fragment = new SummaryDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_summary_detail;
    }

    @Override
    protected void inject() {
        ((BaseApplication) getActivity().getApplication()).getApplicationComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        initRvSummary();

        presenter.setData(getArguments().getParcelableArrayList(EXTRA_DATA));
        presenter.onCreateView(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.onAttachView(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        presenter.onDetachView();
    }

    @Override
    public void showSummary(List<SummaryDetailViewParam> summary) {
        summaryAdapter.setList(summary);
    }

    private void initRvSummary() {

        summaryAdapter = new SummaryAdapter(getActivity());

        rvSummary.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvSummary.setAdapter(summaryAdapter);
    }
}
