package com.splitfee.app.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmModule;

/**
 * Created by huteri on 4/11/17.
 */

@RealmClass
public class Trip implements RealmModel {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    private String name;

    private RealmList<Person> persons;

    private RealmList<Expense> expenses;

    private Date createdAt = new Date();
    private String cover;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        if(persons != null) {
            this.persons = new RealmList<>(persons.toArray(new Person[persons.size()]));

        }
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(RealmList<Expense> expenses) {
        this.expenses = expenses;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCover() {
        return cover;
    }
}
