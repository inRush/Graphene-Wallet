package com.gxb.gxswallet.db.wallet;

import android.os.Parcel;
import android.os.Parcelable;

import com.gxb.sdk.models.wallet.AccountBalance;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.HashMap;

/**
 * @author inrush
 * @date 2018/3/18.
 */
@Entity
public class WalletData implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String passwordPubKey;
    private String encryptionKey;
    private String encryptedWifkey;
    private boolean isBackUp;
    private String brainKey;
    @Transient
    private HashMap<String, Double> balances = new HashMap<>();

    @Generated(hash = 1784945456)
    public WalletData(Long id, String name, String passwordPubKey,
                      String encryptionKey, String encryptedWifkey, boolean isBackUp,
                      String brainKey) {
        this.id = id;
        this.name = name;
        this.passwordPubKey = passwordPubKey;
        this.encryptionKey = encryptionKey;
        this.encryptedWifkey = encryptedWifkey;
        this.isBackUp = isBackUp;
        this.brainKey = brainKey;
    }

    @Generated(hash = 1489983563)
    public WalletData() {
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordPubKey() {
        return this.passwordPubKey;
    }

    public void setPasswordPubKey(String passwordPubKey) {
        this.passwordPubKey = passwordPubKey;
    }

    public String getEncryptionKey() {
        return this.encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getEncryptedWifkey() {
        return this.encryptedWifkey;
    }

    public void setEncryptedWifkey(String encryptedWifkey) {
        this.encryptedWifkey = encryptedWifkey;
    }

    public boolean getIsBackUp() {
        return this.isBackUp;
    }

    public void setIsBackUp(boolean isBackUp) {
        this.isBackUp = isBackUp;
    }


    public Double getBalances(String name) {
        name = name.toUpperCase();
        return balances.get(name);
    }

    public void setBalances(String name, Double balance) {
        name = name.toUpperCase();
        this.balances.put(name, balance);
    }

    public void setBalances(HashMap<String, Double> balances) {
        this.balances = balances;
    }

    public String getBrainKey() {
        return brainKey;
    }

    public void setBrainKey(String brainKey) {
        this.brainKey = brainKey;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.passwordPubKey);
        dest.writeString(this.encryptionKey);
        dest.writeString(this.encryptedWifkey);
        dest.writeByte(this.isBackUp ? (byte) 1 : (byte) 0);
        dest.writeString(this.brainKey);
        dest.writeMap(this.balances);
    }

    @SuppressWarnings("unchecked")
    protected WalletData(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.passwordPubKey = in.readString();
        this.encryptionKey = in.readString();
        this.encryptedWifkey = in.readString();
        this.isBackUp = in.readByte() != 0;
        this.brainKey = in.readString();
        this.balances = in.readHashMap(AccountBalance.class.getClassLoader());
    }

    public static final Creator<WalletData> CREATOR = new Creator<WalletData>() {
        @Override
        public WalletData createFromParcel(Parcel source) {
            return new WalletData(source);
        }

        @Override
        public WalletData[] newArray(int size) {
            return new WalletData[size];
        }
    };
}
