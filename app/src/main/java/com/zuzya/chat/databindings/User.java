package com.zuzya.chat.databindings;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.zuzya.chat.BR;

public class User extends BaseObservable {
    private String firstName;
    private String lastName;
    private int paddingLeft;

    public User(String firstName, String lastName)  {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    @Bindable
    public int getPaddingLeft() {
        return paddingLeft;
    }
    @Bindable
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }
    @Bindable
    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }
    @Bindable
    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
        notifyPropertyChanged(BR.paddingLeft);
    }
}