package com.splitfee.app.data.usecase.viewparam;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.pchmn.materialchips.model.ChipInterface;
import com.splitfee.app.model.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huteri on 4/11/17.
 */

public class PersonViewParam implements Parcelable, ChipInterface {
    private String id, name;

    public PersonViewParam() {
    }

    public PersonViewParam(String id, String name) {
        this.id = id;
        this.name = name;
    }

    protected PersonViewParam(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static List<PersonViewParam> create(List<Person> persons) {
        List<PersonViewParam> list = new ArrayList<>();

        for (Person person : persons) {
            list.add(create(person));
        }
        return list;
    }

    public static PersonViewParam create(Person person) {
        PersonViewParam personViewParam = new PersonViewParam();
        personViewParam.name = person.getName();
        personViewParam.id = person.getId();

        return personViewParam;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    public static List<Person> toPersons(List<PersonViewParam> selectedChipList) {
        List<Person> persons = new ArrayList<>();

        for (PersonViewParam personViewParam : selectedChipList) {
            persons.add(personViewParam.toPerson());
        }

        return persons;
    }

    public Person toPerson() {
        Person person = new Person();
        person.setId(id);
        person.setName(name);

        return person;
    }

    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    @Override
    public Uri getAvatarUri() {
        return null;
    }

    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }

    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return ((PersonViewParam) obj).getId().contentEquals(getId());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PersonViewParam> CREATOR = new Creator<PersonViewParam>() {
        @Override
        public PersonViewParam createFromParcel(Parcel in) {
            return new PersonViewParam(in);
        }

        @Override
        public PersonViewParam[] newArray(int size) {
            return new PersonViewParam[size];
        }
    };
}
