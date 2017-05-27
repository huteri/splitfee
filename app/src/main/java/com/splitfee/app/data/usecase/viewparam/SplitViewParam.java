package com.splitfee.app.data.usecase.viewparam;

import android.os.Parcel;
import android.os.Parcelable;

import com.splitfee.app.model.Split;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huteri on 5/9/17.
 */

public class SplitViewParam implements Parcelable {

    private PersonViewParam person;
    private double amount = 0;

    public SplitViewParam() {
    }

    public SplitViewParam(PersonViewParam person, double amount) {
        this.person = person;
        this.amount = amount;
    }

    protected SplitViewParam(Parcel in) {
        person = in.readParcelable(PersonViewParam.class.getClassLoader());
        amount = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(person, flags);
        dest.writeDouble(amount);

    }

    public static List<SplitViewParam> create(List<Split> payers) {
        List<SplitViewParam> data = new ArrayList<>();

        for (Split payer : payers) {
            data.add(create(payer));
        }
        return data;
    }

    private static SplitViewParam create(Split payer) {
        SplitViewParam data = new SplitViewParam();
        data.amount = payer.getAmount();
        data.person = PersonViewParam.create(payer.getPerson());
        return data;
    }

    public Split toSplit() {

        Split split = new Split();
        split.setPerson(person.toPerson());
        split.setAmount(amount);

        return split;
    }

    public static List<Split> toSplits(List<SplitViewParam> payers) {

        List<Split> splits = new ArrayList<>();

        for (SplitViewParam payer : payers) {
            splits.add(payer.toSplit());
        }

        return splits;
    }

    public PersonViewParam getPerson() {
        return person;
    }

    public BigDecimal getAmount() {
        return BigDecimal.valueOf(amount);
    }

    public void setPerson(PersonViewParam person) {
        this.person = person;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.doubleValue();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SplitViewParam> CREATOR = new Creator<SplitViewParam>() {
        @Override
        public SplitViewParam createFromParcel(Parcel in) {
            return new SplitViewParam(in);
        }

        @Override
        public SplitViewParam[] newArray(int size) {
            return new SplitViewParam[size];
        }
    };
}
