package com.splitfee.app.data.usecase;

import com.splitfee.app.data.usecase.viewparam.PersonViewParam;
import com.splitfee.app.model.Person;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by huteri on 5/3/17.
 */

public interface DisplayPerson {

    Single<List<PersonViewParam>> getPersons();
}
