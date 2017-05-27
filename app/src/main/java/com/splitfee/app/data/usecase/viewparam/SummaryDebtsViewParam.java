package com.splitfee.app.data.usecase.viewparam;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by huteri on 5/23/17.
 */

public class SummaryDebtsViewParam implements Parcelable {

    private PersonViewParam payer, receiver;
    private BigDecimal amount;

    public SummaryDebtsViewParam() {
    }

    protected SummaryDebtsViewParam(Parcel in) {
        payer = in.readParcelable(PersonViewParam.class.getClassLoader());
        receiver = in.readParcelable(PersonViewParam.class.getClassLoader());

        amount = new BigDecimal(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(payer, flags);
        dest.writeParcelable(receiver, flags);

        dest.writeString(amount.toString());
    }

    public PersonViewParam getPayer() {
        return payer;
    }

    public void setPayer(PersonViewParam payer) {
        this.payer = payer;
    }

    public PersonViewParam getReceiver() {
        return receiver;
    }

    public void setReceiver(PersonViewParam receiver) {
        this.receiver = receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SummaryDebtsViewParam> CREATOR = new Creator<SummaryDebtsViewParam>() {
        @Override
        public SummaryDebtsViewParam createFromParcel(Parcel in) {
            return new SummaryDebtsViewParam(in);
        }

        @Override
        public SummaryDebtsViewParam[] newArray(int size) {
            return new SummaryDebtsViewParam[size];
        }
    };
}
