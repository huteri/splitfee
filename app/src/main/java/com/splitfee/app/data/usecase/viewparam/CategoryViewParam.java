package com.splitfee.app.data.usecase.viewparam;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

import com.splitfee.app.model.Category;

/**
 * Created by huteri on 5/7/17.
 */

public class CategoryViewParam implements Parcelable {

    private String icon;

    private String name;

    public CategoryViewParam() {
    }

    public CategoryViewParam(String icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    protected CategoryViewParam(Parcel in) {
        icon = in.readString();
        name = in.readString();
    }

    public static CategoryViewParam create(Category category) {
        CategoryViewParam viewParam = new CategoryViewParam();
        viewParam.setName(category.getName());
        viewParam.setIcon(category.getIcon());

        return viewParam;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(icon);
        dest.writeString(name);
    }

    public Category toCategory() {
        Category category = new Category();
        category.setName(name);
        category.setIcon(icon);

        return category;
    }


    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryViewParam> CREATOR = new Creator<CategoryViewParam>() {
        @Override
        public CategoryViewParam createFromParcel(Parcel in) {
            return new CategoryViewParam(in);
        }

        @Override
        public CategoryViewParam[] newArray(int size) {
            return new CategoryViewParam[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
