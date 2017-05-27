package com.splitfee.app.data.usecase.viewparam;

import android.os.Parcel;
import android.os.Parcelable;

import com.splitfee.app.model.Expense;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by huteri on 5/8/17.
 */

public class ExpenseViewParam implements Parcelable {

    private String id = UUID.randomUUID().toString();

    private String note;

    private double amount;

    private List<SplitViewParam> payers, split;

    private CategoryViewParam category;

    public ExpenseViewParam() {
    }

    protected ExpenseViewParam(Parcel in) {
        id = in.readString();
        note = in.readString();
        category = in.readParcelable(CategoryViewParam.class.getClassLoader());
        amount = in.readDouble();

        payers = new ArrayList<>();
        in.readTypedList(payers, SplitViewParam.CREATOR);

        split = new ArrayList<>();
        in.readTypedList(split, SplitViewParam.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(note);
        dest.writeParcelable(category, flags);
        dest.writeDouble(amount);

        dest.writeTypedList(payers);
        dest.writeTypedList(split);
    }

    public static List<ExpenseViewParam> create(List<Expense> expenses) {
        if(expenses == null)
            return null;

        List<ExpenseViewParam> result = new ArrayList<>();

        for (Expense expense : expenses) {
            result.add(create(expense));
        }
        return result;
    }

    public static ExpenseViewParam create(Expense expense) {
        ExpenseViewParam viewparam = new ExpenseViewParam();
        viewparam.setId(expense.getId());
        viewparam.setPayers(SplitViewParam.create(expense.getPayers()));
        viewparam.setSplit(SplitViewParam.create(expense.getSplits()));
        viewparam.setAmount(BigDecimal.valueOf(expense.getAmount()));
        viewparam.setNote(expense.getNote());
        viewparam.setCategory(CategoryViewParam.create(expense.getCategory()));
        return viewparam;
    }

    public Expense toExpense() {
        Expense expense = new Expense();
        expense.setId(id);
        expense.setNote(note);
        expense.setAmount(amount);
        expense.setPayers(SplitViewParam.toSplits(payers));
        expense.setSplits(SplitViewParam.toSplits(split));
        expense.setCategory(category.toCategory());
        return expense;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.doubleValue();
    }

    public void setPayers(List<SplitViewParam> payers) {
        this.payers = payers;
    }

    public void setSplit(List<SplitViewParam> split) {
        this.split = split;
    }

    public String getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public BigDecimal getAmount() {
        return BigDecimal.valueOf(amount);
    }

    public List<SplitViewParam> getPayers() {
        return payers;
    }

    public List<SplitViewParam> getSplit() {
        return split;
    }


    public CategoryViewParam getCategory() {
        return category;
    }

    public void setCategory(CategoryViewParam category) {
        this.category = category;
    }

    public BigDecimal getTotalPayers() {

        BigDecimal total = BigDecimal.ZERO;

        for (SplitViewParam payer : payers) {
            total = total.add(payer.getAmount());
        }

        return total;
    }

    public BigDecimal getTotalSplit() {

        BigDecimal total = BigDecimal.ZERO;

        for (SplitViewParam payer : split) {
            total = total.add(payer.getAmount());
        }

        return total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ExpenseViewParam> CREATOR = new Creator<ExpenseViewParam>() {
        @Override
        public ExpenseViewParam createFromParcel(Parcel in) {
            return new ExpenseViewParam(in);
        }

        @Override
        public ExpenseViewParam[] newArray(int size) {
            return new ExpenseViewParam[size];
        }
    };
}
