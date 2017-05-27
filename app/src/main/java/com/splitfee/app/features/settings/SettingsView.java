package com.splitfee.app.features.settings;

import com.splitfee.app.features.BaseView;

/**
 * Created by huteri on 5/24/17.
 */

interface SettingsView extends BaseView {
    void showSample(String s);

    void selectSeekbar(int i);

    void showCurrency(String s);

    void showEditCurrencyDialog(String currency);
}
