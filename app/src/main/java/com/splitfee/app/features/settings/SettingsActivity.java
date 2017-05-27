package com.splitfee.app.features.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pchmn.materialchips.util.ViewUtil;
import com.splitfee.app.BaseApplication;
import com.splitfee.app.R;
import com.splitfee.app.features.BaseActivity;
import com.splitfee.app.utils.ViewUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huteri on 5/24/17.
 */

public class SettingsActivity extends BaseActivity implements SettingsView {

    @BindView(R.id.sb_minimum)
    AppCompatSeekBar seekbar;

    @BindView(R.id.tv_sample)
    TextView tvSample;

    @BindView(R.id.tv_currency_value)
    TextView tvCurrency;

    @Inject
    SettingsPresenter presenter;

    @Override
    protected void setupApplicationComponent() {
        ((BaseApplication) getApplication()).getApplicationComponent().inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initSeekbar();

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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showSample(String s) {
        tvSample.setText(s);
    }

    @Override
    public void selectSeekbar(int i) {
        seekbar.setProgress(i);
    }

    @Override
    public void showCurrency(String s) {
        tvCurrency.setText(s);
    }

    @Override
    public void showEditCurrencyDialog(String currency) {
        EditText editText = new EditText(this);
        editText.setText(currency);
        editText.setPadding(ViewUtil.dpToPx(20), ViewUtil.dpToPx(16), ViewUtil.dpToPx(16), ViewUtil.dpToPx(16));

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.pref_dialog_edit_currency_title);
        dialog.setView(editText);
        dialog.setPositiveButton(R.string.general_save, (dialog12, which) -> {
            presenter.saveCurrency(editText.getText().toString());
            dialog12.dismiss();
        });

        dialog.setNegativeButton(android.R.string.cancel, (dialog1, which) -> {

            dialog1.dismiss();
        });

        dialog.show();
    }

    @OnClick(R.id.ll_currency)
    void clickEditCurrency() {
        presenter.clickEditCurrency();
    }

    private void initSeekbar() {
        seekbar.setMax(15);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.seekbarChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
