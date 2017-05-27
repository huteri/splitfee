package com.splitfee.app.data.usecase;

import com.splitfee.app.data.dao.PersonDao;
import com.splitfee.app.data.usecase.viewparam.PersonViewParam;
import com.splitfee.app.model.Person;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by huteri on 5/3/17.
 */

public class DisplayPersonImpl implements DisplayPerson {

    private static DisplayPersonImpl instance;
    private final PersonDao personDao;

    public static DisplayPersonImpl getInstance(PersonDao personDao) {

        if (instance == null) {
            instance = new DisplayPersonImpl(personDao);
        }
        return instance;
    }

    public DisplayPersonImpl(PersonDao personDao) {
        this.personDao = personDao;

    }

    @Override
    public Single<List<PersonViewParam>> getPersons() {
        return personDao.getPersons().map(PersonViewParam::create);
    }
}
