package com.gxb.gxswallet.page.cointransaction.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import cy.agorise.graphenej.objects.Memo;


/**
 * @author inrush
 * @date 2018/4/4.
 */

public class TransactionHistory implements Parcelable {
    public enum TransactionType {
        /**
         * 发送
         */
        send,
        /**
         * 接收
         */
        receive
    }

    private String from;
    private String to;
    private double amount;
    private double fee;
    private String asset;
    private String date;
    private TransactionType type;
    private List<String> transactionIds;
    private Memo memo;

    public Memo getMemo() {
        return memo;
    }

    public void setMemo(Memo memo) {
        this.memo = memo;
    }

    public List<String> getTransactionIds() {
        return transactionIds;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setTransactionIds(List<String> transactionIds) {
        this.transactionIds = transactionIds;
    }

    public TransactionHistory() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.from);
        dest.writeString(this.to);
        dest.writeDouble(this.amount);
        dest.writeDouble(this.fee);
        dest.writeString(this.asset);
        dest.writeString(this.date);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeStringList(this.transactionIds);
        dest.writeParcelable(this.memo, flags);
    }

    protected TransactionHistory(Parcel in) {
        this.from = in.readString();
        this.to = in.readString();
        this.amount = in.readDouble();
        this.fee = in.readDouble();
        this.asset = in.readString();
        this.date = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : TransactionType.values()[tmpType];
        this.transactionIds = in.createStringArrayList();
        this.memo = in.readParcelable(Memo.class.getClassLoader());
    }

    public static final Creator<TransactionHistory> CREATOR = new Creator<TransactionHistory>() {
        @Override
        public TransactionHistory createFromParcel(Parcel source) {
            return new TransactionHistory(source);
        }

        @Override
        public TransactionHistory[] newArray(int size) {
            return new TransactionHistory[size];
        }
    };
}
