package com.gdfdfdfdfd.contactapp;


import android.os.Parcel;
import android.os.Parcelable;

public class ListItems implements Parcelable {
    private Boolean avatar;
    private String name;
    private String phone;

    public ListItems(){}

    public ListItems(Boolean avatar, String name, String phone) {
        this.avatar = avatar;
        this.name = name;
        this.phone = phone;
    }

    protected ListItems(Parcel in) {
        byte tmpAvatar = in.readByte();
        avatar = tmpAvatar == 0 ? null : tmpAvatar == 1;
        name = in.readString();
        phone = in.readString();
    }

    public static final Creator<ListItems> CREATOR = new Creator<ListItems>() {
        @Override
        public ListItems createFromParcel(Parcel in) {
            return new ListItems(in);
        }

        @Override
        public ListItems[] newArray(int size) {
            return new ListItems[size];
        }
    };

    public Boolean getAvatar() {
        return avatar;
    }

    public void setAvatar(Boolean avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (avatar == null ? 0 : avatar ? 1 : 2));
        dest.writeString(name);
        dest.writeString(phone);
    }
}
