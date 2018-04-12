package com.gxb.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/25.
 */

public class ExchangeLineData implements Parcelable {

    /**
     * price : 0.005154
     * time : 13:18
     */

    private String price;
    private String time;

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }


    public static List<ExchangeLineData> fromJsonToList(String json){
        return new Gson().fromJson(json, new TypeToken<List<ExchangeLineData>>() {
        }.getType());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.price);
        dest.writeString(this.time);
    }

    public ExchangeLineData(String price, String time) {
        this.price = price;
        this.time = time;
    }

    public ExchangeLineData() {
    }

    protected ExchangeLineData(Parcel in) {
        this.price = in.readString();
        this.time = in.readString();
    }

    public static final Parcelable.Creator<ExchangeLineData> CREATOR = new Parcelable.Creator<ExchangeLineData>() {
        @Override
        public ExchangeLineData createFromParcel(Parcel source) {
            return new ExchangeLineData(source);
        }

        @Override
        public ExchangeLineData[] newArray(int size) {
            return new ExchangeLineData[size];
        }
    };
}
