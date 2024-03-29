package com.splitfee.app.data.dao;

import com.splitfee.app.model.Expense;
import com.splitfee.app.model.Trip;

import io.reactivex.Completable;
import io.realm.Realm;

/**
 * Created by huteri on 5/14/17.
 */

public class ExpenseDaoImpl implements ExpenseDao {
    @Override
    public Completable saveExpense(Trip trip, Expense expense) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        Expense expense1 = realm.where(Expense.class).equalTo("id", expense.getId()).findFirst();

        if (expense1 == null) {
            Trip id = realm.where(Trip.class).equalTo("id", trip.getId()).findFirst();
            id.getExpenses().add(0, expense);
        } else {
            realm.insertOrUpdate(expense);
        }

        realm.commitTransaction();

        realm.close();

        return Completable.complete();
    }

    @Override
    public Completable deleteExpense(String id) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.where(Expense.class).equalTo("id", id).findAll().deleteFirstFromRealm());

        realm.close();
        return Completable.complete();
    }
}
