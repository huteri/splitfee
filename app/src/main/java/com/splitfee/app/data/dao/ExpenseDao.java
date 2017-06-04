package com.splitfee.app.data.dao;

import com.splitfee.app.model.Expense;
import com.splitfee.app.model.Trip;

import io.reactivex.Completable;

/**
 * Created by huteri on 5/14/17.
 */

public interface ExpenseDao {

    Completable saveExpense(Trip trip, Expense expense);

    Completable deleteExpense(String id);
}
