package com.gxb.gxswallet.page.send.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gxb.gxswallet.db.asset.AssetData;

/**
 * @author inrush
 * @date 2018/3/21.
 */

public class Sender implements Parcelable {
    private String to;
    private String amount;
    private AssetData coin;
    private String memo;

    public Sender(String to, String amount, AssetData coin, String memo) {
        this.to = to;
        this.amount = amount;
        this.coin = coin;
        this.memo = memo;
    }



    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public AssetData getAsset() {
        return coin;
    }

    public void setCoin(AssetData coin) {
        this.coin = coin;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.to);
        dest.writeString(this.amount);
        dest.writeParcelable(this.coin, flags);
        dest.writeString(this.memo);
    }

    protected Sender(Parcel in) {
        this.to = in.readString();
        this.amount = in.readString();
        this.coin = in.readParcelable(AssetData.class.getClassLoader());
        this.memo = in.readString();
    }

    public static final Creator<Sender> CREATOR = new Creator<Sender>() {
        @Override
        public Sender createFromParcel(Parcel source) {
            return new Sender(source);
        }

        @Override
        public Sender[] newArray(int size) {
            return new Sender[size];
        }
    };
}
