package com.gxb.gxswallet.db.coin;

import android.os.Parcel;
import android.os.Parcelable;

import com.gxb.gxswallet.App;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inrush
 * @date 2018/3/21.
 */
@Entity
public class CoinData implements Parcelable {
    @Id
    private Long id;
    private String icon;
    private String name;
    private String assetId;
    private boolean enable;

    @Generated(hash = 1734870594)
    public CoinData(Long id, String icon, String name, String assetId, boolean enable) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.assetId = assetId;
        this.enable = enable;
    }

    @Generated(hash = 661693619)
    public CoinData() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssetId() {
        return this.assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }


    public static void initCoin() {
        String[] coinNames = new String[]{"GXS", "BTS"};
        String[] assetIds = new String[]{"1.3.1", "1.3.0"};
        CoinDataManager manager = new CoinDataManager(App.getInstance());
        int size = manager.queryAll().size();
        if (size != coinNames.length) {
            manager.deleteAll();
            List<CoinData> coinDatas = new ArrayList<>();
            for (int i = 0; i < coinNames.length; i++) {
                coinDatas.add(new CoinData(null, coinNames[i] + ".png", coinNames[i], assetIds[i], true));
            }
            manager.insertMult(coinDatas);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.icon);
        dest.writeString(this.name);
        dest.writeString(this.assetId);
    }

    public boolean getEnable() {
        return this.enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    protected CoinData(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.icon = in.readString();
        this.name = in.readString();
        this.assetId = in.readString();
    }

    public static final Parcelable.Creator<CoinData> CREATOR = new Parcelable.Creator<CoinData>() {
        @Override
        public CoinData createFromParcel(Parcel source) {
            return new CoinData(source);
        }

        @Override
        public CoinData[] newArray(int size) {
            return new CoinData[size];
        }
    };
}
