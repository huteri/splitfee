package com.splitfee.app.features.summary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.splitfee.app.BaseApplication;
import com.splitfee.app.R;
import com.splitfee.app.data.usecase.viewparam.ExpenseViewParam;
import com.splitfee.app.data.usecase.viewparam.SummaryDebtsViewParam;
import com.splitfee.app.data.usecase.viewparam.SummaryDetailViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.features.BaseActivity;
import com.splitfee.app.features.adapters.SummaryPagerAdapter;
import com.splitfee.app.utils.PriceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by huteri on 5/20/17.
 */

public class SummaryActivity extends BaseActivity implements SummaryView {

    public static final String EXTRA_TRIP_ID = "EXTRA_TRIP_ID";
    @BindView(R.id.tab_layout)
    TabLayout tlMenu;

    @BindView(R.id.vp_content)
    ViewPager vpContent;

    @Inject
    SummaryPresenter presenter;

    private SummaryPagerAdapter tabAdapter;

    @Override
    protected void setupApplicationComponent() {
        ((BaseApplication) getApplication()).getApplicationComponent().inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_summary;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initVpContent();
        initTlMenu();

        presenter.setTripId(getIntent().getStringExtra(EXTRA_TRIP_ID));
        presenter.onCreateView(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.summary, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.share:
                presenter.tapShareMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showTripsSummary(List<SummaryDetailViewParam> summaryDetail, List<SummaryDebtsViewParam> summaryDebts) {
        tabAdapter.setData(summaryDetail, summaryDebts);
    }

    @Override
    public void showShareIntent(TripViewParam trip, ArrayList<SummaryDebtsViewParam> summaryDebts) {

        StringBuilder builder = new StringBuilder();
        builder.append(trip.getName() + " on " );
        builder.append(new SimpleDateFormat("EEE, MMMM dd").format(trip.getCreatedAt())+"\n\n");

        builder.append("Expenses \n");
        for (ExpenseViewParam expenseViewParam : trip.getExpenses()) {
            builder.append(expenseViewParam.getNote() + " "+ PriceUtil.showPrice(this, expenseViewParam.getAmount(), false) + "\n");
        }

        builder.append("\n\n");

        builder.append("Debts\n");

        for (SummaryDebtsViewParam summaryDebt : summaryDebts) {
            builder.append(summaryDebt.getPayer().getName());
            builder.append(" owes ");
            builder.append(summaryDebt.getReceiver().getName());
            builder.append(" for ");
            builder.append(PriceUtil.showPrice(this, summaryDebt.getAmount(), false));
            builder.append("\n");
        }

        builder.append("\n\n");
        builder.append("Created with Splitfee");


        Intent intent = new Intent(android.content.Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Trip Summary");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, builder.toString());
        startActivity(Intent.createChooser(intent, "Share using"));
    }


    private void initTlMenu() {

        tlMenu.setupWithViewPager(vpContent);
        tlMenu.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initVpContent() {

        tabAdapter = new SummaryPagerAdapter(getSupportFragmentManager());

        vpContent.setAdapter(tabAdapter);

    }
}
