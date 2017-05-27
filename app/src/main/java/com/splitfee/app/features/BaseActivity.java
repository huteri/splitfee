package com.splitfee.app.features;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.splitfee.app.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huteri on 4/11/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @Inject
    Navigator navigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ButterKnife.bind(this);

        setupApplicationComponent();

        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    public Navigator getNavigator() {
        return navigator;
    }

    @Nullable
    public Toolbar getToolbar() {
        return toolbar;
    }

    public void animateToolbarHide() {
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    public void animateToolbarShow() {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }


    protected abstract void setupApplicationComponent();

    public abstract int getLayoutResource();
}
