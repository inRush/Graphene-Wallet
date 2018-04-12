package com.gxb.gxswallet.page.send.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gxb.gxswallet.db.wallet.WalletData;

/**
 * @author inrush
 * @date 2018/3/21.
 */

public class Sender implements Parcelable {
    private WalletData from;
    private String to;
    private String amount;
    private String coin;

    public Sender(WalletData from, String to, String amount, String coin) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.coin = coin;
    }

    public WalletData getFrom() {
        return from;
    }

    public void setFrom(WalletData from) {
        this.from = from;
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


    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.from, flags);
        dest.writeString(this.to);
        dest.writeString(this.amount);
        dest.writeString(this.coin);
    }

    protected Sender(Parcel in) {
        this.from = in.readParcelable(WalletData.class.getClassLoader());
        this.to = in.readString();
        this.amount = in.readString();
        this.coin = in.readString();
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
