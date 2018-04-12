package com.gxb.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/15.
 */

public class GXSExchange implements Parcelable {

    /**
     * high : 0.00035
     * low : 0.00032837
     * name : Bit-Z
     * price : 0.00034181
     * price_dollar : 2.77550
     * price_rmb : 18.30024
     * quote : 3.99
     * symbol : GXS_BTC
     * volume : 428143.5117
     */

    private String high;
    private String low;
    private String name;
    private String price;
    private String price_dollar;
    private String price_rmb;
    private double quote;
    private String symbol;
    private String volume;

    public void setHigh(String high) {
        this.high = high;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setPrice_dollar(String price_dollar) {
        this.price_dollar = price_dollar;
    }

    public void setPrice_rmb(String price_rmb) {
        this.price_rmb = price_rmb;
    }

    public void setQuote(double quote) {
        this.quote = quote;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getPrice_dollar() {
        return price_dollar;
    }

    public String getPrice_rmb() {
        return price_rmb;
    }

    public double getQuote() {
        return quote;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getVolume() {
        return volume;
    }

    public static GXSExchange fromJson(String json) {
        return new Gson().fromJson(json, GXSExchange.class);
    }

    public static List<GXSExchange> fromJsonToList(String json) {
        return new Gson().fromJson(json, new TypeToken<List<GXSExchange>>() {
        }.getType());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.high);
        dest.writeString(this.low);
        dest.writeString(this.name);
        dest.writeString(this.price);
        dest.writeString(this.price_dollar);
        dest.writeString(this.price_rmb);
        dest.writeDouble(this.quote);
        dest.writeString(this.symbol);
        dest.writeString(this.volume);
    }

    public GXSExchange() {
    }

    protected GXSExchange(Parcel in) {
        this.high = in.readString();
        this.low = in.readString();
        this.name = in.readString();
        this.price = in.readString();
        this.price_dollar = in.readString();
        this.price_rmb = in.readString();
        this.quote = in.readDouble();
        this.symbol = in.readString();
        this.volume = in.readString();
    }

    public static final Parcelable.Creator<GXSExchange> CREATOR = new Parcelable.Creator<GXSExchange>() {
        @Override
        public GXSExchange createFromParcel(Parcel source) {
            return new GXSExchange(source);
        }

        @Override
        public GXSExchange[] newArray(int size) {
            return new GXSExchange[size];
        }
    };
}
