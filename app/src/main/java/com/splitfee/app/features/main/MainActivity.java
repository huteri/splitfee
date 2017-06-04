package com.splitfee.app.features.main;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.splitfee.app.BaseApplication;
import com.splitfee.app.R;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.features.BaseActivity;
import com.splitfee.app.features.adapters.TripListAdapter;
import com.splitfee.app.features.addtrip.AddTripActivity;
import com.splitfee.app.features.components.TwoLinesTextToolbar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainView, AppBarLayout.OnOffsetChangedListener, TripListAdapter.ItemClickListener {

    public static String EXTRA_TRIP = "EXTRA_TRIP_ID";

    private static final int RC_TAP_FAB = 1;

    @Inject
    MainPresenter presenter;

    @BindView(R.id.toolbar_header_view)
    TwoLinesTextToolbar toolbarHeaderView;

    @BindView(R.id.float_header_view)
    TwoLinesTextToolbar floatHeaderView;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.nv_menu)
    NavigationView navMenu;

    @BindView(R.id.rv_main)
    RecyclerView rvMain;

    @BindView(R.id.fab_book)
    FloatingActionButton fab;

    @BindView(R.id.scroll_view)
    NestedScrollView scrollView;

    ImageView ivHeader;

    private boolean isHideToolbarView = false;

    private TripListAdapter tripListAdapter;

    @Override
    protected void setupApplicationComponent() {
        ((BaseApplication) getApplication()).getApplicationComponent().inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);


        appBarLayout.addOnOffsetChangedListener(this);
        navMenu.setNavigationItemSelectedListener(item -> {
            presenter.selectMenuItem(item);
            drawerLayout.closeDrawers();
            return true;
        });

        ivHeader = (ImageView) navMenu.getHeaderView(0).findViewById(R.id.iv_header);

        iniRvMain();

        presenter.onCreateView(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
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
        if(requestCode == RC_TAP_FAB && resultCode == RESULT_OK && data.getParcelableExtra(EXTRA_TRIP) != null) {
            presenter.resultOkFromFab(data.getParcelableExtra(EXTRA_TRIP));
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }

    @Override
    public void showTitle(String story, String s) {
        toolbarHeaderView.bindTo(story, s);
        floatHeaderView.bindTo(story, s);
    }

    @Override
    public void showTrips(List<TripViewParam> value) {
        tripListAdapter.setData(value);
    }

    @Override
    public void addTrip(TripViewParam trip) {
        tripListAdapter.addData(trip);
        rvMain.smoothScrollToPosition(0);
    }

    @Override
    public void navigateToTripDetailActivity(String trip) {
        getNavigator().navigateToTripDetailActivity(this, trip);
    }

    @Override
    public void navigateToSettings() {
        getNavigator().navigateToSettings(this);
    }

    @Override
    public void showHeader() {
        Glide.with(this).load(R.drawable.ic_logo).into(ivHeader);
    }

    @Override
    public void showAboutDialog() {
        String appVersion = "";
        try {
            appVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("About");
        dialog.setMessage("Version " + appVersion + "\n\n\u00a9 Copyright 2017. All rights reserved.\n\nDesigned by Sheila Stefani, developed by Huteri Manza");
        dialog.setPositiveButton(android.R.string.ok, (dialog1, which) -> dialog1.dismiss());
        dialog.show();

    }

    @Override
    public void navigateToEmail() {
        getNavigator().navigateToSendEmail(this);
    }

    @Override
    public void onTripClick(TripViewParam trip) {
        presenter.tapItem(trip);
    }

    @OnClick(R.id.fab_book)
    void tapFab() {
        Intent intent = new Intent(this, AddTripActivity.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = null;
            options = ActivityOptions.makeSceneTransitionAnimation(this, fab, getString(R.string.transition_dialog));
            startActivityForResult(intent, RC_TAP_FAB, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    private void iniRvMain() {
        tripListAdapter = new TripListAdapter(this);
        tripListAdapter.setItemClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvMain.setLayoutManager(layoutManager);
        rvMain.setAdapter(tripListAdapter);
        rvMain.setHasFixedSize(true);
        rvMain.setNestedScrollingEnabled(false);
    }
}
