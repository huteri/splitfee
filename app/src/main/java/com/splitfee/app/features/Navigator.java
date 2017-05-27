package com.splitfee.app.features;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.splitfee.app.data.usecase.viewparam.ExpenseViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.features.addexpense.AddExpenseActivity;
import com.splitfee.app.features.main.MainActivity;
import com.splitfee.app.features.settings.SettingsActivity;
import com.splitfee.app.features.summary.SummaryActivity;
import com.splitfee.app.features.tripdetail.TripDetailActivity;

import javax.inject.Inject;

/**
 * Created by huteri on 4/11/17.
 */

public class Navigator {

    public static final int REQUEST_CODE_ADD_EXPENSE = 100;

    @Inject
    public Navigator() {
    }

    public void navigateToTripDetailActivity(Context context, String trip) {
        Intent intent = new Intent(context, TripDetailActivity.class);
        intent.putExtra(TripDetailActivity.EXTRA_TRIP_ID, trip);
        context.startActivity(intent);
    }

    public void navigateToAddExpenseActivity(Activity activity, TripViewParam trip, ExpenseViewParam expenseViewParam, boolean expectResult) {
        Intent intent = new Intent(activity, AddExpenseActivity.class);
        intent.putExtra(AddExpenseActivity.EXTRA_TRIP, trip);
        intent.putExtra(AddExpenseActivity.EXTRA_EXPENSE, expenseViewParam);

        if (expectResult)
            activity.startActivityForResult(intent, REQUEST_CODE_ADD_EXPENSE);
        else
            activity.startActivity(intent);
    }

    public void navigateToSummary(Context context, String tripId) {
        Intent intent = new Intent(context, SummaryActivity.class);
        intent.putExtra(SummaryActivity.EXTRA_TRIP_ID, tripId);

        context.startActivity(intent);
    }

    public void navigateToSettings(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    public void navigateToSendEmail(Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","hut3ri@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Splitfee Feedback");
        context.startActivity(Intent.createChooser(emailIntent, "Send Feedback"));
    }
}
