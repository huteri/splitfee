package com.splitfee.app.features.tripdetail;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.splitfee.app.BaseApplication;
import com.splitfee.app.R;
import com.splitfee.app.data.usecase.viewparam.ExpenseViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.features.BaseActivity;
import com.splitfee.app.features.Navigator;
import com.splitfee.app.features.adapters.ExpenseAdapter;
import com.splitfee.app.features.components.TwoLinesTextToolbar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huteri on 5/4/17.
 */

public class TripDetailActivity extends BaseActivity implements TripDetailView, AppBarLayout.OnOffsetChangedListener {

    public static final String EXTRA_TRIP_ID = "EXTRA_TRIP_ID";
    public static final String INTENT_EXTRA_EXPENSE = "EXTRA_EXPENSE";

    @BindView(R.id.toolbar_header_view)
    TwoLinesTextToolbar toolbarHeaderView;

    @BindView(R.id.float_header_view)
    TwoLinesTextToolbar floatHeaderView;

    @BindView(R.id.rv_items)
    RecyclerView rvTrips;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.iv_background)
    ImageView ivCover;

    @Inject
    TripDetailPresenter presenter;

    private ExpenseAdapter expenseAdapter;
    private boolean isHideToolbarView;
    private LinearLayoutManager layoutManager;
    private Drawable upArrow;

    @Override
    protected void setupApplicationComponent() {
        ((BaseApplication) getApplication()).getApplicationComponent().inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_trip_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRvTrips();
        initNavigationBack();

        appBarLayout.addOnOffsetChangedListener(this);

        presenter.setTripId(getIntent().getStringExtra(EXTRA_TRIP_ID));
        presenter.onCreateView(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.onAttachView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        presenter.onDetachView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        presenter.onAttachView(this);

        if(requestCode == Navigator.REQUEST_CODE_ADD_EXPENSE && resultCode == RESULT_OK) {
            presenter.activityResult(data.getParcelableExtra(TripDetailActivity.INTENT_EXTRA_EXPENSE));
        }
    }

    @Override
    public void showTitle(String name, String format) {
        toolbarHeaderView.bindTo(name, format);
        floatHeaderView.bindTo(name, format);
    }

    @Override
    public void navigateToAddExpenseActivity(TripViewParam trip) {
        getNavigator().navigateToAddExpenseActivity(this, trip, null, true);
    }

    @Override
    public void showExpenses(List<ExpenseViewParam> expenses) {
        expenseAdapter.setData(expenses);
    }

    @Override
    public void addExpense(ExpenseViewParam expenses) {
        expenseAdapter.addExpense(expenses);

        if(layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
            layoutManager.scrollToPosition(0);
        }
    }

    @Override
    public void navigateToAddExpenseActivity(TripViewParam trip, ExpenseViewParam expenseViewParam) {
        getNavigator().navigateToAddExpenseActivity(this, trip, expenseViewParam, false);
    }

    @Override
    public void navigateToSummary(String tripId) {
        getNavigator().navigateToSummary(this, tripId);
    }

    @Override
    public void showCover(String cover) {
        Glide.with(this).load(cover).into(ivCover);
    }

    @Override
    public void deleteExpense(int pos) {
        expenseAdapter.deleteExpense(pos);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

            upArrow.setColorFilter(Color.argb(127, 0, 0, 0) ,PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;

            upArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }

    @OnClick(R.id.fab_add)
    void tapFabAdd() {
        presenter.tapFabAdd();
    }

    @OnClick(R.id.tv_summary)
    void tapSummary() {
        presenter.tapSummary();
    }

    @OnClick(R.id.tv_send_feedback)
    void tapSendFeedback() {
        getNavigator().navigateToSendEmail(this);
    }


    private void initRvTrips() {
        expenseAdapter = new ExpenseAdapter(this);
        expenseAdapter.setListener(new ExpenseAdapter.ExpenseListener() {
            @Override
            public void onExpenseClick(ExpenseViewParam expenseViewParam) {
                presenter.tapExpense(expenseViewParam);
            }

            @Override
            public void onExpenseDelete(int pos, ExpenseViewParam expenseViewParam) {
                presenter.tapExpenseDelete(pos, expenseViewParam);
            }
        });
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvTrips.setLayoutManager(layoutManager);
        rvTrips.setAdapter(expenseAdapter);
        rvTrips.setNestedScrollingEnabled(false);

    }

    private void initNavigationBack() {
        upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);

    }
}
