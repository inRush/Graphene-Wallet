package com.gxb.gxswallet.db.asset;

import android.os.Parcel;
import android.os.Parcelable;

import com.gxb.gxswallet.db.converter.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import java.util.Objects;

/**
 * @author inrush
 * @date 2018/3/21.
 */
@Entity
public class AssetData implements Parcelable {

    @Id
    private Long id;
    private String name;
    private String assetId;
    @Convert(columnType = String.class, converter = StringConverter.class)
    private List<String> nets;
    private String prefix;
    private boolean enable;
    private boolean isTest;


    public Long getId() {
        return this.id;
    }

    public String getIcon() {
        return this.name + ".png";
    }

    public String getName() {
        return this.name;
    }

    public String getAssetId() {
        return this.assetId;
    }

    public List<String> getNets() {
        return nets;
    }

    public boolean isEnable() {
        return enable;
    }

    public boolean getEnable() {
        return this.enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }



    @Generated(hash = 1524562618)
    public AssetData(Long id, String name, String assetId, List<String> nets, String prefix,
                     boolean enable, boolean isTest) {
        this.id = id;
        this.name = name;
        this.assetId = assetId;
        this.nets = nets;
        this.prefix = prefix;
        this.enable = enable;
        this.isTest = isTest;
    }

    @Generated(hash = 220564739)
    public AssetData() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public void setNets(List<String> nets) {
        this.nets = nets;
    }

    public boolean getIsTest() {
        return this.isTest;
    }

    public void setIsTest(boolean isTest) {
        this.isTest = isTest;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AssetData && hashCode() == obj.hashCode() && Objects.equals(id, ((AssetData) obj).id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.assetId);
        dest.writeStringList(this.nets);
        dest.writeByte(this.enable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isTest ? (byte) 1 : (byte) 0);
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    protected AssetData(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.assetId = in.readString();
        this.nets = in.createStringArrayList();
        this.enable = in.readByte() != 0;
        this.isTest = in.readByte() != 0;
    }

    public static final Parcelable.Creator<AssetData> CREATOR = new Parcelable.Creator<AssetData>() {
        @Override
        public AssetData createFromParcel(Parcel source) {
            return new AssetData(source);
        }

        @Override
        public AssetData[] newArray(int size) {
            return new AssetData[size];
        }
    };
}
