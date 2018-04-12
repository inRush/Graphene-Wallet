package com.gxb.gxswallet.utils;

import com.gxb.sdk.models.Wallet;
import com.gxb.sdk.models.global.Account;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/14.
 */

public class DataUtil {
    private static List<Wallet> sWallets;
    private static List<Account> sAccounts;

    public static List<Wallet> getWallet() {
        return sWallets;
    }

    public static void setWallet(List<Wallet> wallet) {
        sWallets = wallet;
    }

    public static void setAccounts(List<Account> accounts) {
        sAccounts = accounts;
    }

    public static List<Account> getAccount() {
        return sAccounts;
    }
}
