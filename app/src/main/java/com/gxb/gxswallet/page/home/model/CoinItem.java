package com.gxb.gxswallet.page.home.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author inrush
 * @date 2018/3/21.
 */

public class CoinItem implements Parcelable {
    private Bitmap icon;
    private String name;
    private double amount;
    private String assetId;
    private double price;
    private boolean enable;

    public CoinItem(Bitmap icon, String name, double amount, String assetId, double price, boolean enable) {
        this.icon = icon;
        this.name = name;
        this.amount = amount;
        this.assetId = assetId;
        this.price = price;
        this.enable = enable;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.icon, flags);
        dest.writeString(this.name);
        dest.writeDouble(this.amount);
        dest.writeString(this.assetId);
        dest.writeDouble(this.price);
        dest.writeByte(this.enable ? (byte) 1 : (byte) 0);
    }

    protected CoinItem(Parcel in) {
        this.icon = in.readParcelable(Bitmap.class.getClassLoader());
        this.name = in.readString();
        this.amount = in.readDouble();
        this.assetId = in.readString();
        this.price = in.readDouble();
        this.enable = in.readByte() != 0;
    }

    public static final Creator<CoinItem> CREATOR = new Creator<CoinItem>() {
        @Override
        public CoinItem createFromParcel(Parcel source) {
            return new CoinItem(source);
        }

        @Override
        public CoinItem[] newArray(int size) {
            return new CoinItem[size];
        }
    };
}
