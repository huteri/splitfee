package com.splitfee.app.features.settings;

import com.splitfee.app.data.dao.AppConfigDao;
import com.splitfee.app.features.BasePresenter;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.inject.Inject;

import io.reactivex.functions.Action;

/**
 * Created by huteri on 5/24/17.
 */

class SettingsPresenter extends BasePresenter<SettingsView> {

    private final AppConfigDao appConfig;
    private String currency;

    @Inject
    public SettingsPresenter(AppConfigDao appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public void onCreateView(SettingsView view) {
        super.onCreateView(view);

        currency = appConfig.getCurrency().blockingGet();

        getView().selectSeekbar(appConfig.getMinimumMoney().blockingGet() + 10);
        getView().showCurrency(currency);
    }

    public void saveCurrency(String currency) {
        this.currency = currency;
        appConfig.saveCurrency(currency).subscribe(() -> getView().showCurrency(currency));
    }

    public void seekbarChanged(int i) {

        if (!isViewAttached())
            return;

        i -= 10;

        BigDecimal decimal = BigDecimal.ONE;

        if (i < 0) {
            decimal = decimal.multiply(BigDecimal.valueOf(Math.pow(10, i * -1)));
            decimal = decimal.setScale(0);
        } else if (i > 0) {
            decimal = decimal.divide(BigDecimal.valueOf(Math.pow(10, i)), i, RoundingMode.HALF_DOWN);
        }

        BigDecimal finalDecimal = decimal;
        appConfig.saveMinimumMoney(i).subscribe(() -> {
            getView().showSample(currency+" "+ finalDecimal.toPlainString());
        });

    }

    public void clickEditCurrency() {
        getView().showEditCurrencyDialog(currency);
    }
}
