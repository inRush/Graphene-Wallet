package com.gxb.gxswallet.page.send.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author inrush
 * @date 2018/4/12.
 */

public class Transaction implements Parcelable {
    private double amount;
    private String to;
    private String memo;
    private double fee;
    private String asset;

    public Transaction(double amount, String to, String memo, double fee, String asset) {
        this.amount = amount;
        this.to = to;
        this.memo = memo;
        this.fee = fee;
        this.asset = asset;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.amount);
        dest.writeString(this.to);
        dest.writeString(this.memo);
        dest.writeDouble(this.fee);
        dest.writeString(this.asset);
    }

    protected Transaction(Parcel in) {
        this.amount = in.readDouble();
        this.to = in.readString();
        this.memo = in.readString();
        this.fee = in.readDouble();
        this.asset = in.readString();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel source) {
            return new Transaction(source);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
}
