package com.splitfee.app.data.usecase;

import com.splitfee.app.data.usecase.viewparam.ExpenseViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;

import io.reactivex.Completable;

/**
 * Created by huteri on 5/14/17.
 */

public interface ManageExpense {
    Completable saveExpense(TripViewParam trip, ExpenseViewParam expenseViewParam);
    Completable deleteExpense(String id);
}
