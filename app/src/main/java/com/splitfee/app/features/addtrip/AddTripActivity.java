package com.splitfee.app.features.addtrip;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatSpinner;
import android.transition.ArcMotion;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;
import com.splitfee.app.BaseApplication;
import com.splitfee.app.R;
import com.splitfee.app.data.usecase.viewparam.PersonViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.features.BaseActivity;
import com.splitfee.app.features.components.MorphDialogToFab;
import com.splitfee.app.features.components.MorphFabToDialog;
import com.splitfee.app.features.main.MainActivity;
import com.splitfee.app.model.Person;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huteri on 4/17/17.
 */

public class AddTripActivity extends BaseActivity implements AddTripView {

    @BindView(R.id.container)
    ViewGroup container;

    @BindView(R.id.chips_input)
    ChipsInput chipsInput;

    @BindView(R.id.et_title)
    EditText etTitle;

    @BindView(R.id.sp_cover)
    AppCompatSpinner spCover;

    @Inject
    AddTripPresenter presenter;

    @Override
    protected void setupApplicationComponent() {
        ((BaseApplication) getApplication()).getApplicationComponent().inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_add_trip;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupSharedEelementTransitions1();
        }


        initSpCover();
        initChips();

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
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }

    @Override
    public void showPersonChips(List<PersonViewParam> chipsList) {
        chipsInput.setFilterableList(chipsList);
    }

    @Override
    public void additionalChips(List<PersonViewParam> newChips) {
        chipsInput.addAdditionalChips(newChips);
    }

    @Override
    public void navigateBackToMainActivity(TripViewParam trip) {
        Intent intent = getIntent();
        intent.putExtra(MainActivity.EXTRA_TRIP, trip);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void showParticipantMinimumRequired() {
        Toast.makeText(this, "Participant at least 2 people", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTitleRequired() {
        Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_save)
    void tapBtnSave() {
        presenter.tapBtnSave(etTitle.getText().toString(), (List<PersonViewParam>) chipsInput.getSelectedChipList());
    }

    @OnClick(R.id.btn_cancel)
    void tapBtnCancel() {
        dismiss();
    }

    private void dismiss() {
        setResult(RESULT_CANCELED);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupSharedEelementTransitions1() {
        ArcMotion arcMotion = new ArcMotion();
        arcMotion.setMinimumHorizontalAngle(50f);
        arcMotion.setMinimumVerticalAngle(50f);

        Interpolator easeInOut = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in);

        MorphFabToDialog sharedEnter = new MorphFabToDialog();
        sharedEnter.setPathMotion(arcMotion);
        sharedEnter.setInterpolator(easeInOut);

        MorphDialogToFab sharedReturn = new MorphDialogToFab();
        sharedReturn.setPathMotion(arcMotion);
        sharedReturn.setInterpolator(easeInOut);

        if (container != null) {
            sharedEnter.addTarget(container);
            sharedReturn.addTarget(container);
        }
        getWindow().setSharedElementEnterTransition(sharedEnter);
        getWindow().setSharedElementReturnTransition(sharedReturn);
    }

    private void initChips() {

        chipsInput.addChipsListener(new ChipsInput.ChipsListener() {
            @Override
            public void onChipAdded(ChipInterface chip, int newSize) {

            }

            @Override
            public void onChipRemoved(ChipInterface chip, int newSize) {

            }

            @Override
            public void onTextChanged(CharSequence text) {
                presenter.chipsTextChanged(text);
            }
        });
    }


    private void initSpCover() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cover_type, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCover.setAdapter(adapter);
        spCover.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.selectCover(getResources().getStringArray(R.array.cover_type)[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
