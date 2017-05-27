package com.splitfee.app.data.dao;

import com.splitfee.app.model.Person;

import java.util.List;

import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by huteri on 5/3/17.
 */

public class PersonDaoImpl implements PersonDao {
    @Override
    public Single<List<Person>> getPersons() {

        Realm realm = Realm.getDefaultInstance();

        RealmResults<Person> all = realm.where(Person.class).findAll();
        List<Person> persons = realm.copyFromRealm(all);

        realm.close();
        return Single.just(persons);
    }
}
