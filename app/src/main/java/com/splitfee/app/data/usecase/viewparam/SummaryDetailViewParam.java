package com.splitfee.app.data.usecase.viewparam;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by huteri on 5/22/17.
 */

public class SummaryDetailViewParam implements Parcelable, Cloneable {

    private PersonViewParam person;
    private BigDecimal paid = BigDecimal.ZERO, spent = BigDecimal.ZERO, summary;

    public SummaryDetailViewParam() {
    }

    protected SummaryDetailViewParam(Parcel in) {
        person = in.readParcelable(PersonViewParam.class.getClassLoader());

        paid = new BigDecimal(in.readString());
        spent = new BigDecimal(in.readString());

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(person, flags);

        dest.writeString(paid.toString());
        dest.writeString(spent.toString());
    }

    public PersonViewParam getPerson() {
        return person;
    }

    public void setPerson(PersonViewParam person) {
        this.person = person;
    }

    public BigDecimal getPaid() {
        return paid;
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
    }

    public BigDecimal getSpent() {
        return spent;
    }

    public void setSpent(BigDecimal spent) {
        this.spent = spent;
    }

    public BigDecimal getSummary() {
        return summary == null ? paid.subtract(spent) : summary;
    }

    public void setSummary(BigDecimal summary) {
        this.summary = summary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SummaryDetailViewParam> CREATOR = new Creator<SummaryDetailViewParam>() {
        @Override
        public SummaryDetailViewParam createFromParcel(Parcel in) {
            return new SummaryDetailViewParam(in);
        }

        @Override
        public SummaryDetailViewParam[] newArray(int size) {
            return new SummaryDetailViewParam[size];
        }
    };

    public SummaryDetailViewParam copy() {
        SummaryDetailViewParam viewParam = new SummaryDetailViewParam();
        viewParam.setSummary(summary);
        viewParam.setPaid(paid);
        viewParam.setPerson(person);
        viewParam.setSpent(spent);

        return viewParam;
    }
}
