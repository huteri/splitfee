package com.splitfee.app.model;

import java.util.UUID;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by huteri on 5/4/17.
 */

@RealmClass
public class Split implements RealmModel {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    private Person person;
    private double amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
