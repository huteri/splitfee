package com.splitfee.app.di.modules;

import com.splitfee.app.data.dao.ExpenseDao;
import com.splitfee.app.data.dao.PersonDao;
import com.splitfee.app.data.dao.TripDao;
import com.splitfee.app.data.usecase.DisplayPerson;
import com.splitfee.app.data.usecase.DisplayPersonImpl;
import com.splitfee.app.data.usecase.DisplayTrip;
import com.splitfee.app.data.usecase.DisplayTripImpl;
import com.splitfee.app.data.usecase.ManageExpense;
import com.splitfee.app.data.usecase.ManageExpenseImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by huteri on 4/11/17.
 */

@Module
public class UseCaseModule {

    @Singleton
    @Provides
    DisplayTrip provideDisplayTrip(TripDao tripDao) {
        return DisplayTripImpl.getInstance(tripDao);
    }

    @Singleton
    @Provides
    DisplayPerson provideDisplayPerson(PersonDao personDao) { return DisplayPersonImpl.getInstance(personDao); }

    @Singleton
    @Provides
    ManageExpense provideManageExpense(ExpenseDao expenseDao) { return ManageExpenseImpl.getInstance(expenseDao); }
}
