package com.gxb.gxswallet.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author inrush
 * @date 2018/3/18.
 */

public class SqliteWalletBacking implements SecureKeyValueStoreBacking {
    private static final String TABLE_KV = "kv";

    private SQLiteDatabase _database;

    public SqliteWalletBacking(){

    }

    @Override
    public byte[] getValue(byte[] id) {
        return new byte[0];
    }

    @Override
    public void setValue(byte[] id, byte[] plaintextValue) {

    }

    @Override
    public void deleteValue(byte[] id) {

    }

}
