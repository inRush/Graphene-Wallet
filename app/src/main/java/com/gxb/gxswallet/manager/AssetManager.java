package com.gxb.gxswallet.manager;

import android.app.Application;
import android.util.ArrayMap;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.BuildConfig;
import com.gxb.gxswallet.db.asset.AssetData;
import com.gxb.gxswallet.db.asset.AssetDataManager;
import com.gxb.gxswallet.db.asset.AssetSymbol;
import com.sxds.common.helper.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inrush
 * @date 2018/5/6.
 */

public class AssetManager {
    private static final String SP_DEFAULT_KEY_TEST = "default_asset_test";
    private static final String SP_DEFAULT_KEY_PRODUCT = "default_asset_product";
    public static final double AMOUNT_SIZE = 100000;
    private static boolean IS_UPDATE = false;
    private static AssetManager sAssetManager;

    private AssetDataManager mAssetDataManager;
    private ArrayMap<String, AssetData> assetsProduct;
    private ArrayMap<String, AssetData> assetsTest;

    public static AssetManager getInstance() {
        if (sAssetManager == null) {
            synchronized (AssetManager.class) {
                if (sAssetManager == null) {
                    sAssetManager = new AssetManager();
                }
            }
        }
        return sAssetManager;
    }

    private AssetManager() {
    }

    public List<AssetData> getEnableList() {
        if (BuildConfig.DEBUG) {
            return mAssetDataManager.queryEnableTestAsset();
        }
        return mAssetDataManager.queryEnableProductAsset();
    }

    public List<AssetData> getAll() {
        if (BuildConfig.DEBUG) {
            return mAssetDataManager.queryTestAsset();
        }
        return mAssetDataManager.queryProductAsset();
    }

    /**
     * 启用资产
     *
     * @param assetData 需要启用的资产
     * @return 是否启用成功
     */
    public boolean enableAsset(AssetData assetData) {
        assetData.setEnable(true);
        return updateAsset(assetData);
    }

    /**
     * 关闭资产
     *
     * @param assetData 需要关闭的资产
     * @return 是否关闭成功
     */
    public boolean disableAsset(AssetData assetData) {
        assetData.setEnable(false);
        return updateAsset(assetData);
    }

    /**
     * 更新资产
     *
     * @param assetData 需要更新的资产
     * @return 是否更新成功
     */
    private boolean updateAsset(AssetData assetData) {
        if (mAssetDataManager.update(assetData)) {
            if (BuildConfig.DEBUG) {
                assetsTest.put(assetData.getName(), assetData);
            } else {
                assetsProduct.put(assetData.getName(), assetData);
            }
            return true;
        }
        return false;
    }

    /**
     * 获取默认的资产
     *
     * @return {@link AssetData}
     */
    public AssetData getDefault() {
        String asset;
        if (BuildConfig.DEBUG) {
            asset = SharedPreferenceHelper.getString(App.getInstance(), SP_DEFAULT_KEY_TEST, null);
        } else {
            asset = SharedPreferenceHelper.getString(App.getInstance(), SP_DEFAULT_KEY_PRODUCT, null);
        }
        AssetData assetData = get(asset);
        if (assetData == null) {
            return getEnableList().get(0);
        }
        return assetData;
    }

    /**
     * 设置默认的资产
     *
     * @param asset 资产
     */
    public void setDefault(AssetData asset) {
        if (BuildConfig.DEBUG) {
            SharedPreferenceHelper.putString(App.getInstance(), SP_DEFAULT_KEY_TEST, asset.getName());
        } else {
            SharedPreferenceHelper.getString(App.getInstance(), SP_DEFAULT_KEY_PRODUCT, asset.getName());
        }
    }

    /**
     * 根据资产名称获取资产
     *
     * @param assetName 资产名称
     * @return {@link AssetData}
     */
    public AssetData get(String assetName) {
        if (BuildConfig.DEBUG) {
            return assetsTest.get(assetName);
        } else {
            return assetsProduct.get(assetName);
        }
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Application context) {
        mAssetDataManager = new AssetDataManager(context);
        assetsProduct = new ArrayMap<>();
        assetsTest = new ArrayMap<>();
        List<AssetData> productList;
        List<AssetData> testList;
        List<AssetData> data = mAssetDataManager.queryAll();
        if (data.size() == 0 || IS_UPDATE) {
            data.clear();
            productList = initProduct();
            testList = initTest();
            data.addAll(testList);
            data.addAll(productList);
            mAssetDataManager.deleteAll();
            mAssetDataManager.insertMult(data);
        } else {
            productList = mAssetDataManager.queryProductAsset();
            testList = mAssetDataManager.queryTestAsset();
        }
        for (AssetData assetData : testList) {
            assetsTest.put(assetData.getName(), assetData);
        }
        for (AssetData assetData : productList) {
            assetsProduct.put(assetData.getName(), assetData);
        }
    }

    private List<AssetData> initTest() {
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
        nets = new ArrayList<>();
        nets.add("ws://192.168.42.73:11011");
        AssetData BtsTest = new AssetData(
                null,
                "BTS",
                "1.3.0",
                nets,
                "BTS",
                false,
                true);
        List<AssetData> assetDataList = new ArrayList<>();
        assetDataList.add(GxsTest);
        assetDataList.add(BtsTest);
        return assetDataList;
    }

    private List<AssetData> initProduct() {
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
        List<AssetData> assetDataList = new ArrayList<>();
        assetDataList.add(GxsProduct);
        assetDataList.add(BtsProduct);
        return assetDataList;
    }
}
