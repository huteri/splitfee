package com.splitfee.app.features;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by huteri on 5/20/17.
 */

public abstract class BaseFragment extends Fragment {

    @LayoutRes
    public abstract int getLayoutResource();

    protected abstract void inject();

    @Inject
    Navigator navigator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inject();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    public Navigator getNavigator() {
        return navigator;
    }
}
