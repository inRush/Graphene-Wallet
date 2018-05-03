package com.gxb.gxswallet.db.asset;

import android.content.Context;
import android.util.ArrayMap;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.BuildConfig;
import com.gxb.gxswallet.db.BaseDaoManager;
import com.gxb.gxswallet.utils.SharedPreferenceUtils;
import com.ping.greendao.gen.AssetDataDao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inrush
 * @date 2018/3/21.
 */

public class AssetDataManager extends BaseDaoManager<AssetData, Long> {

    public static final double AMOUNT_SIZE = 100000;

    private static ArrayMap<String, AssetData> assetsProduct;
    private static ArrayMap<String, AssetData> assetsTest;
    private static List<AssetData> sAssetData_Test;
    private static List<AssetData> sAssetData_Product;
    private static List<AssetData> sAssetData_Test_Enable;
    private static List<AssetData> sAssetData_Product_Enable;
    private static final String SP_DEFAULT_KEY_TEST = "default_asset_test";
    private static final String SP_DEFAULT_KEY_PRODUCT = "default_asset_product";
    private static boolean isUpdate = false;

    public AssetDataManager(Context context) {
        super(context);
    }

    @Override
    protected AbstractDao<AssetData, Long> getDao() {
        return mManager.getDaoSession().getAssetDataDao();
    }

    public List<AssetData> queryEnableAsset() {
        QueryBuilder<AssetData> queryBuilder = getDao().queryBuilder();
        return queryBuilder.where(AssetDataDao.Properties.Enable.eq(true)).list();
    }

    public List<AssetData> queryTestAsset() {
        QueryBuilder<AssetData> queryBuilder = getDao().queryBuilder();
        return queryBuilder.where(AssetDataDao.Properties.IsTest.eq(true)).list();
    }

    public List<AssetData> queryProductAsset() {
        QueryBuilder<AssetData> queryBuilder = getDao().queryBuilder();
        return queryBuilder.where(AssetDataDao.Properties.IsTest.eq(false)).list();
    }

    public List<AssetData> queryEnableTestAsset() {
        QueryBuilder<AssetData> queryBuilder = getDao().queryBuilder();
        return queryBuilder.where(AssetDataDao.Properties.IsTest.eq(true),
                AssetDataDao.Properties.Enable.eq(true)).list();
    }

    public List<AssetData> queryEnableProductAsset() {
        QueryBuilder<AssetData> queryBuilder = getDao().queryBuilder();
        return queryBuilder.where(AssetDataDao.Properties.IsTest.eq(true),
                AssetDataDao.Properties.Enable.eq(true)).list();
    }

    public static List<AssetData> getEnableList() {
        if (BuildConfig.DEBUG) {
            return sAssetData_Test_Enable;
        }
        return sAssetData_Product_Enable;
    }

    public static List<AssetData> getAll() {
        if (BuildConfig.DEBUG) {
            return sAssetData_Test;
        }
        return sAssetData_Product;
    }

    public boolean enableAsset(AssetData assetData) {
        assetData.setEnable(true);
        if (update(assetData)) {
            if (BuildConfig.DEBUG) {
                sAssetData_Test_Enable = queryEnableTestAsset();
            } else {
                sAssetData_Product_Enable = queryEnableProductAsset();
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean disableAsset(AssetData assetData) {
        assetData.setEnable(false);
        if (update(assetData)) {
            if (BuildConfig.DEBUG) {
                sAssetData_Test_Enable = queryEnableTestAsset();
            } else {
                sAssetData_Product_Enable = queryEnableProductAsset();
            }
            return true;
        } else {
            return false;
        }
    }


    public static AssetData getDefault() {
        AssetData assetData = getEnableList().get(0);
        if (BuildConfig.DEBUG) {
            String asset = SharedPreferenceUtils.getString(App.getInstance(), SP_DEFAULT_KEY_TEST, assetData.getName());
            if (!asset.equals(assetData.getName())) {
                assetData = get(asset);
            }
        } else {
            String asset = SharedPreferenceUtils.getString(App.getInstance(), SP_DEFAULT_KEY_PRODUCT, assetData.getName());
            if (!asset.equals(assetData.getName())) {
                assetData = get(asset);
            }
        }
        return assetData;
    }

    public static void setDefault(AssetData asset) {
        if (BuildConfig.DEBUG) {
            SharedPreferenceUtils.putString(App.getInstance(), SP_DEFAULT_KEY_TEST, asset.getName());
        } else {
            SharedPreferenceUtils.getString(App.getInstance(), SP_DEFAULT_KEY_PRODUCT, asset.getName());
        }
    }

    public static AssetData get(String assetName) {
        if (BuildConfig.DEBUG) {
            return assetsTest.get(assetName);
        } else {
            return assetsProduct.get(assetName);
        }
    }

    public static void initCoin(Context context) {
        assetsProduct = new ArrayMap<>();
        assetsTest = new ArrayMap<>();
        AssetDataManager dataManager = new AssetDataManager(context);
        List<AssetData> data = dataManager.queryAll();
        if (data.size() == 0 || isUpdate) {
            data.clear();
            sAssetData_Test = new ArrayList<>();
            sAssetData_Product = new ArrayList<>();
            sAssetData_Test_Enable = new ArrayList<>();
            sAssetData_Product_Enable = new ArrayList<>();
            initProduct();
            initTest();
            data.addAll(sAssetData_Test);
            data.addAll(sAssetData_Product);
            dataManager.deleteAll();
            dataManager.insertMult(data);
        } else {
            sAssetData_Test = dataManager.queryTestAsset();
            sAssetData_Product = dataManager.queryProductAsset();
            sAssetData_Test_Enable = dataManager.queryEnableTestAsset();
            sAssetData_Product_Enable = dataManager.queryEnableProductAsset();
            for (AssetData assetData : sAssetData_Test) {
                assetsTest.put(assetData.getName(), assetData);
            }
            for (AssetData assetData : sAssetData_Product) {
                assetsProduct.put(assetData.getName(), assetData);
            }
        }
    }

    private static void initTest() {
        List<String> nets = new ArrayList<>();
        nets.add("ws://106.14.180.117:28090");
        AssetData GxsTest = new AssetData(
                null,
                AssetSymbol.GXS.TEST,
                "1.3.1",
                nets,
                "GXC",
                true,
                true);
//        nets.add("wss://node.testnet.bitshares.eu/ws");
        nets = new ArrayList<>();
        nets.add("ws://192.168.42.73:11011");
//        AssetData mBtsTest = new AssetData(
//                null,
//                AssetSymbol.BTS.TEST,
//                "1.3.0",
//                nets,
//                "TEST",
//                false,
//                true);
        AssetData BtsTest = new AssetData(
                null,
                "BTS",
                "1.3.0",
                nets,
                "BTS",
                false,
                true);
        assetsTest.put(AssetSymbol.GXS.TEST, GxsTest);
        assetsTest.put(AssetSymbol.BTS.TEST, BtsTest);
        sAssetData_Test.add(GxsTest);
        sAssetData_Test.add(BtsTest);
        sAssetData_Test_Enable.add(GxsTest);
    }

    private static void initProduct() {
        List<String> nets = new ArrayList<>();
        nets.add("wss://node1.gxb.io");
        nets.add("wss://node5.gxb.io");
        nets.add("wss://node8.gxb.io");
        nets.add("wss://node11.gxb.io");
        nets.add("wss://node15.gxb.io");
        nets.add("wss://node16.gxb.io");
        nets.add("wss://node17.gxb.io");
        AssetData GxsProduct = new AssetData(
                null,
                AssetSymbol.GXS.PRODUCT,
                "1.3.1",
                nets,
                "GXC",
                true,
                false);
        nets = new ArrayList<>();
        nets.add("wss://bitshares.dacplay.org/ws");
        AssetData BtsProduct = new AssetData(
                null,
                AssetSymbol.BTS.PRODUCT,
                "1.3.0",
                nets,
                "BTS",
                false,
                false);
        assetsProduct.put(AssetSymbol.GXS.PRODUCT, GxsProduct);
        assetsProduct.put(AssetSymbol.BTS.PRODUCT, BtsProduct);
        sAssetData_Product.add(GxsProduct);
        sAssetData_Product.add(BtsProduct);
        sAssetData_Product_Enable.add(GxsProduct);
    }
}
