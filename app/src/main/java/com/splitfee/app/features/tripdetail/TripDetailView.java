package com.splitfee.app.features.tripdetail;

import com.splitfee.app.data.usecase.viewparam.ExpenseViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.features.BaseView;

import java.util.List;

/**
 * Created by huteri on 5/4/17.
 */

interface TripDetailView extends BaseView {
    void showTitle(String name, String format);
    void navigateToAddExpenseActivity(TripViewParam trip);

    void showExpenses(List<ExpenseViewParam> expenses);

    void addExpense(ExpenseViewParam expenses);

    void navigateToAddExpenseActivity(TripViewParam trip, ExpenseViewParam expenseViewParam);

    void navigateToSummary(String tripId);

    void showCover(String cover);

    void deleteExpense(int pos);
}
