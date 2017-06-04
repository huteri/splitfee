package com.splitfee.app.features.addexpense;

import com.splitfee.app.data.usecase.viewparam.CategoryViewParam;
import com.splitfee.app.data.usecase.viewparam.ExpenseViewParam;
import com.splitfee.app.data.usecase.viewparam.PersonViewParam;
import com.splitfee.app.data.usecase.viewparam.SplitViewParam;
import com.splitfee.app.features.BaseView;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by huteri on 5/7/17.
 */

interface AddExpenseView extends BaseView {
    void showCategories(List<CategoryViewParam> categories);

    void showPayers(List<SplitViewParam> persons);

    void showSplits(List<SplitViewParam> persons);

    void showExpenseEditDialog(BigDecimal amount, int reference, BigDecimal max, String currency);

    void showAmount(BigDecimal s);

    void showAmountError();

    void performEditExpense();

    void showExpenseName(String name);

    void showLastSplitError();

    void showCantCalculate();

    void navigateBackToTripDetail(ExpenseViewParam expense);

    void showTotalPayersNotEqual(String s);

    void showTotalSplitNotEqual(String s);

    void selectCategory(CategoryViewParam category);

    void showCurrency(String s);

    void showDate(String format);
}
