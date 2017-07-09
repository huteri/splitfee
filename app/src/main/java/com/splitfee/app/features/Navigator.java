package com.splitfee.app.features;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.splitfee.app.data.usecase.viewparam.ExpenseViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.features.addexpense.AddExpenseActivity;
import com.splitfee.app.features.main.MainActivity;
import com.splitfee.app.features.settings.SettingsActivity;
import com.splitfee.app.features.summary.SummaryActivity;
import com.splitfee.app.features.tripdetail.TripDetailActivity;

import java.util.Locale;

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
        emailIntent.putExtra(Intent.EXTRA_TEXT, additionalData(context));
        context.startActivity(Intent.createChooser(emailIntent, "Send Feedback"));
    }

    private String additionalData(Context context) {
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceName = Build.MODEL;
        String androidVersion = Build.VERSION.RELEASE;

        StringBuilder builder = new StringBuilder();
        builder.append("\n\n");
        builder.append("Device ID : " + deviceId);
        builder.append("\n");
        builder.append("Device Name : " + deviceName);
        builder.append("\n");
        builder.append("Android Version : " + androidVersion);
        builder.append("\n");
        try {
            builder.append("App Version : "+ context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        builder.append("\n");
        builder.append("Language : "+ Locale.getDefault().getDisplayName());
        return builder.toString();

    }

}
