package com.splitfee.app.features.addexpense;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.splitfee.app.BaseApplication;
import com.splitfee.app.R;
import com.splitfee.app.data.usecase.viewparam.CategoryViewParam;
import com.splitfee.app.data.usecase.viewparam.ExpenseViewParam;
import com.splitfee.app.data.usecase.viewparam.SplitViewParam;
import com.splitfee.app.features.BaseActivity;
import com.splitfee.app.features.adapters.CategoriesAdapter;
import com.splitfee.app.features.adapters.ExpenseSplitAdapter;
import com.splitfee.app.features.tripdetail.TripDetailActivity;
import com.splitfee.app.utils.PriceUtil;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huteri on 5/7/17.
 */

public class AddExpenseActivity extends BaseActivity implements AddExpenseView {
    public static final String EXTRA_TRIP = "EXTRA_TRIP_ID";
    public static final String EXTRA_EXPENSE = "EXTRA_EXPENSE";

    @BindView(R.id.rv_categories)
    RecyclerView rvCategories;

    @BindView(R.id.rv_payers)
    RecyclerView rvPayers;

    @BindView(R.id.rv_split)
    RecyclerView rvSplit;

    @BindView(R.id.tv_amount)
    TextView tvAmount;

    @BindView(R.id.et_expense)
    EditText etExpense;

    @BindView(R.id.tv_currency)
    TextView tvCurrency;

    @BindView(R.id.tv_date)
    TextView tvDate;

    @Inject
    AddExpensePresenter presenter;

    private CategoriesAdapter categoriesAdapter;
    private ExpenseSplitAdapter payersAdapter;
    private ExpenseSplitAdapter splitAdapter;

    @Override
    protected void setupApplicationComponent() {
        ((BaseApplication) getApplication()).getApplicationComponent().inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_add_expense;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRvCategories();
        initRvPayers();
        initRvSplit();

        presenter.setTrip(getIntent().getParcelableExtra(EXTRA_TRIP));
        presenter.setExpense(getIntent().getParcelableExtra(EXTRA_EXPENSE));
        presenter.onCreateView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_expense, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.add_expense:
                presenter.tapSaveExpense(etExpense.getText().toString());
                break;
            case R.id.send_feedback:
                presenter.tapSendFeedback();
        }
        return super.onOptionsItemSelected(item);
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
    public void showCategories(List<CategoryViewParam> categories) {
        categoriesAdapter.setData(categories);
    }

    @Override
    public void showPayers(List<SplitViewParam> persons) {
        payersAdapter.setData(persons);
    }

    @Override
    public void showSplits(List<SplitViewParam> splits) {
        splitAdapter.setData(splits);
    }

    @Override
    public void showExpenseEditDialog(BigDecimal amount, int reference, BigDecimal max, String currency) {
        NumberPickerBuilder npb = new NumberPickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                .setPlusMinusVisibility(View.INVISIBLE)
                .setReference(reference)
                .setLabelText(currency);

        if(amount.doubleValue() > 0)
            npb.setCurrentNumber(amount);

        if(max.doubleValue() > 0) {
            npb.setMaxNumber(max);
        }

        npb.addNumberPickerDialogHandler((reference1, number, decimal, isNegative, fullNumber) -> presenter.doneEditExpensePerPerson(reference1, fullNumber));
        npb.show();
    }

    @Override
    public void showAmount(BigDecimal s) {
        tvAmount.setText(PriceUtil.showPrice(this, s, true));
    }

    @Override
    public void showAmountError() {
        Toast.makeText(this, "Please input total expense first", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void performEditExpense() {
        tvAmount.performClick();
    }

    @Override
    public void showExpenseName(String name) {
        etExpense.setText(name);
    }

    @Override
    public void showLastSplitError() {
        Toast.makeText(this, "Selected user should be at least one", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCantCalculate() {
        Toast.makeText(this, "Something went wrong with this calculation", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateBackToTripDetail(ExpenseViewParam expense) {
        Intent intent = getIntent();
        intent.putExtra(TripDetailActivity.INTENT_EXTRA_EXPENSE, expense);

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void showTotalPayersNotEqual(String s) {
        Toast.makeText(this, "Total Payment ("+s+") not equal to your expense", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTotalSplitNotEqual(String s) {
        Toast.makeText(this, "Total Splits ("+s+") not equal to your expense", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void selectCategory(CategoryViewParam category) {
        categoriesAdapter.setSelected(category.getName());
    }

    @Override
    public void showCurrency(String s) {
        tvCurrency.setText(s);
    }

    @Override
    public void showDate(String format) {
        tvDate.setText(format);
    }

    @Override
    public void navigateToSendFeedback() {
        getNavigator().navigateToSendEmail(this);
    }

    @OnClick(R.id.tv_amount)
    void tapEditExpense() {
        presenter.tapEditExpenseName(tvAmount.getText().toString());
    }

    private void initRvCategories() {

        categoriesAdapter = new CategoriesAdapter(this);
        categoriesAdapter.setListener(categoryViewParam -> presenter.tapCategory(categoryViewParam, etExpense.getText().toString()));

        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCategories.setAdapter(categoriesAdapter);
    }

    private void initRvPayers() {
        payersAdapter = new ExpenseSplitAdapter(this);
        payersAdapter.setListener(new ExpenseSplitAdapter.ExpenseSplitListener() {
            @Override
            public void onClickExpense(int position) {
                presenter.tapPayerExpense(position);
            }

            @Override
            public void onClickContainer(int adapterPosition) {
                presenter.tapPayerContainer(adapterPosition);
            }
        });

        rvPayers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPayers.setAdapter(payersAdapter);
    }

    private void initRvSplit() {

        splitAdapter = new ExpenseSplitAdapter(this);
        splitAdapter.setListener(new ExpenseSplitAdapter.ExpenseSplitListener() {
            @Override
            public void onClickExpense(int position) {
                presenter.tapSplitExpense(position);
            }

            @Override
            public void onClickContainer(int adapterPosition) {
                presenter.tapSplitContainer(adapterPosition);
            }
        });

        rvSplit.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvSplit.setAdapter(splitAdapter);

    }

}
