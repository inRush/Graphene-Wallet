package com.gxb.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.gxb.sdk.models.wallet.AccountBalance;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/13.
 */

public class Wallet implements Parcelable {
    private String name;
    private String passwordPubKey;
    private String encryptionKey;
    private String encryptedWifkey;
    private List<AccountBalance> balances;

    public Wallet(String name, String passwordPubKey, String encryptionKey, String encryptedWifkey) {
        this.name = name;
        this.passwordPubKey = passwordPubKey;
        this.encryptionKey = encryptionKey;
        this.encryptedWifkey = encryptedWifkey;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordPubKey() {
        return passwordPubKey;
    }

    public void setPasswordPubKey(String passwordPubKey) {
        this.passwordPubKey = passwordPubKey;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getEncryptedWifkey() {
        return encryptedWifkey;
    }

    public void setEncryptedWifkey(String encryptedWifkey) {
        this.encryptedWifkey = encryptedWifkey;
    }

    public List<AccountBalance> getBalances() {
        return balances;
    }

    public void setBalances(List<AccountBalance> balances) {
        this.balances = balances;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.passwordPubKey);
        dest.writeString(this.encryptionKey);
        dest.writeString(this.encryptedWifkey);
        dest.writeTypedList(this.balances);
    }

    protected Wallet(Parcel in) {
        this.name = in.readString();
        this.passwordPubKey = in.readString();
        this.encryptionKey = in.readString();
        this.encryptedWifkey = in.readString();
        this.balances = in.createTypedArrayList(AccountBalance.CREATOR);
    }

    public static final Parcelable.Creator<Wallet> CREATOR = new Parcelable.Creator<Wallet>() {
        @Override
        public Wallet createFromParcel(Parcel source) {
            return new Wallet(source);
        }

        @Override
        public Wallet[] newArray(int size) {
            return new Wallet[size];
        }
    };
}
