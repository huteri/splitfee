package com.splitfee.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.splitfee.app.data.dao.AppConfigDaoImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by huteri on 5/24/17.
 */

public class PriceUtil {


    public static String showPrice(Context context, BigDecimal value, boolean withoutCurrency) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String currency = preferences.getString(AppConfigDaoImpl.PREF_CURRENCY, "USD");
        int decimaPoint = preferences.getInt(AppConfigDaoImpl.PREF_MINIMUM_MONEY, 2);

        BigDecimal bigDecimal = value.setScale(decimaPoint, RoundingMode.HALF_UP);

        String numberSigns = "";
        for (int i = 0; i < decimaPoint; i++) {
            numberSigns += "0";
        }

        DecimalFormat decimalFormat = new DecimalFormat();

        if (numberSigns.length() > 0) {
            decimalFormat.applyPattern("#,##0." + numberSigns);
        } else {
            decimalFormat.applyPattern("#,##0");
        }

        if (withoutCurrency)
            return decimalFormat.format(bigDecimal);
        else
            return currency + decimalFormat.format(bigDecimal);

    }
}
