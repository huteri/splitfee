package com.splitfee.app.features.summarydebts;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.splitfee.app.BaseApplication;
import com.splitfee.app.R;
import com.splitfee.app.data.usecase.viewparam.SummaryDebtsViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.features.BaseFragment;
import com.splitfee.app.features.adapters.SummaryDebtsAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by huteri on 5/23/17.
 */

public class SummaryDebtsFragment extends BaseFragment implements SummaryDebtsView {

    private static final String EXTRA_DATA = "EXTRA_DATA";
    @BindView(R.id.rv_debts)
    RecyclerView rvDebts;

    @Inject
    SummaryDebtsPresenter presenter;

    private SummaryDebtsAdapter adapter;

    public static SummaryDebtsFragment newInstance(List<SummaryDebtsViewParam> trip) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_DATA, (ArrayList<? extends Parcelable>) trip);

        SummaryDebtsFragment fragment = new SummaryDebtsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public int getLayoutResource() {
        return R.layout.fragment_summary_debts;
    }

    @Override
    protected void inject() {
        ((BaseApplication) getActivity().getApplication()).getApplicationComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        initRvDebts();

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
    public void showSummaryDebts(List<SummaryDebtsViewParam> data) {

        adapter.setData(data);
    }

    private void initRvDebts() {

        adapter = new SummaryDebtsAdapter(getActivity());

        rvDebts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvDebts.setAdapter(adapter);
    }
}
