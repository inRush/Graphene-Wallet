package com.gxb.gxswallet.db.contact;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * @author inrush
 * @date 2018/3/23.
 */
@Entity
public class ContactData implements Parcelable {
    @Id
    private Long id;
    private String name;
    @Unique
    private String address;
    private String phone;
    private String memo;
    @Generated(hash = 1171032755)
    public ContactData(Long id, String name, String address, String phone, String memo) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.memo = memo;
    }
    @Generated(hash = 471005222)
    public ContactData() {
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getMemo() {
        return this.memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.phone);
        dest.writeString(this.memo);
    }

    protected ContactData(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.address = in.readString();
        this.phone = in.readString();
        this.memo = in.readString();
    }

    public static final Creator<ContactData> CREATOR = new Creator<ContactData>() {
        @Override
        public ContactData createFromParcel(Parcel source) {
            return new ContactData(source);
        }

        @Override
        public ContactData[] newArray(int size) {
            return new ContactData[size];
        }
    };
}
