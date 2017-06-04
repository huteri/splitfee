package com.splitfee.app.data.usecase;

import com.splitfee.app.data.dao.ExpenseDao;
import com.splitfee.app.data.usecase.viewparam.ExpenseViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;

import io.reactivex.Completable;

/**
 * Created by huteri on 5/14/17.
 */

public class ManageExpenseImpl implements ManageExpense {

    private static ManageExpenseImpl instance;
    private final ExpenseDao expenseDao;

    public static ManageExpenseImpl getInstance(ExpenseDao expenseDao) {
        if (instance == null) {
            instance = new ManageExpenseImpl(expenseDao);
        }

        return instance;
    }

    public ManageExpenseImpl(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;

    }

    @Override
    public Completable saveExpense(TripViewParam trip, ExpenseViewParam expenseViewParam) {
        return expenseDao.saveExpense(trip.toTrip(), expenseViewParam.toExpense());
    }

    @Override
    public Completable deleteExpense(String id) {
        return expenseDao.deleteExpense(id);
    }
}
