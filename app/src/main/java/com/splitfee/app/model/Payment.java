package com.splitfee.app.model;

import io.realm.RealmModel;

/**
 * Created by huteri on 5/4/17.
 */

public class Payment implements RealmModel {

    private Person payer;
    private double amount;

    public Person getPayer() {
        return payer;
    }

    public void setPayer(Person payer) {
        this.payer = payer;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
