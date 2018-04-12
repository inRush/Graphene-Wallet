package com.gxb.sdk.models.wallet;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author inrush
 * @date 2018/3/14.
 */

public class AccountBalance implements Parcelable {

    /**
     * amount : 64996705
     * asset_id : 1.3.1
     */

    private int amount;
    private String asset_id;

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setAssetId(String asset_id) {
        this.asset_id = asset_id;
    }

    public int getAmount() {
        return amount;
    }

    public String getAssetId() {
        return asset_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.amount);
        dest.writeString(this.asset_id);
    }

    public AccountBalance() {
    }

    protected AccountBalance(Parcel in) {
        this.amount = in.readInt();
        this.asset_id = in.readString();
    }

    public static final Parcelable.Creator<AccountBalance> CREATOR = new Parcelable.Creator<AccountBalance>() {
        @Override
        public AccountBalance createFromParcel(Parcel source) {
            return new AccountBalance(source);
        }

        @Override
        public AccountBalance[] newArray(int size) {
            return new AccountBalance[size];
        }
    };
}
