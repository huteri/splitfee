package com.splitfee.app.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by huteri on 5/4/17.
 */

@RealmClass
public class Expense implements RealmModel {

    @PrimaryKey
    private String id;

    private String note;

    private double amount;

    private Category category;

    private RealmList<Split> payers;

    private RealmList<Split> splits;

    private Date createdAt = new Date();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<Split> getPayers() {
        return payers;
    }

    public void setPayers(List<Split> payers) {
        if (payers != null)
            this.payers = new RealmList<>(payers.toArray(new Split[payers.size()]));
    }

    public List<Split> getSplits() {
        return splits;
    }

    public void setSplits(List<Split> splits) {
        if (splits != null)
            this.splits = new RealmList<>(splits.toArray(new Split[splits.size()]));
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
