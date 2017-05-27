package com.splitfee.app.di.components;

import com.splitfee.app.BaseApplication;
import com.splitfee.app.di.modules.AppModule;
import com.splitfee.app.di.modules.DaoModule;
import com.splitfee.app.di.modules.UseCaseModule;
import com.splitfee.app.features.addexpense.AddExpenseActivity;
import com.splitfee.app.features.addtrip.AddTripActivity;
import com.splitfee.app.features.main.MainActivity;
import com.splitfee.app.features.settings.SettingsActivity;
import com.splitfee.app.features.summary.SummaryActivity;
import com.splitfee.app.features.summarydebts.SummaryDebtsFragment;
import com.splitfee.app.features.summarydetail.SummaryDetailFragment;
import com.splitfee.app.features.tripdetail.TripDetailActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by huteri on 4/11/17.
 */

@Singleton
@Component(modules = {
        AppModule.class,
        DaoModule.class,
        UseCaseModule.class
})

public interface ApplicationComponent {
    void inject(BaseApplication baseApplication);
    void inject(MainActivity mainActivity);

    void inject(AddTripActivity addTripActivity);

    void inject(TripDetailActivity tripDetailActivity);

    void inject(AddExpenseActivity addExpenseActivity);

    void inject(SummaryActivity summaryActivity);

    void inject(SummaryDetailFragment summaryDetailFragment);

    void inject(SummaryDebtsFragment summaryDebtsFragment);

    void inject(SettingsActivity settingsActivity);
}
