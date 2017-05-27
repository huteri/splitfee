package com.splitfee.app.data.dao;

import com.splitfee.app.model.Person;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by huteri on 5/3/17.
 */

public interface PersonDao {
    Single<List<Person>> getPersons();
}
