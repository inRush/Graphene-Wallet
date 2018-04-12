package com.ping.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.gxb.gxswallet.db.wallet.WalletData;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "WALLET_DATA".
*/
public class WalletDataDao extends AbstractDao<WalletData, Long> {

    public static final String TABLENAME = "WALLET_DATA";

    /**
     * Properties of entity WalletData.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property AccountId = new Property(1, String.class, "accountId", false, "ACCOUNT_ID");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property PasswordPubKey = new Property(3, String.class, "passwordPubKey", false, "PASSWORD_PUB_KEY");
        public final static Property EncryptionKey = new Property(4, String.class, "encryptionKey", false, "ENCRYPTION_KEY");
        public final static Property EncryptedWifkey = new Property(5, String.class, "encryptedWifkey", false, "ENCRYPTED_WIFKEY");
        public final static Property IsBackUp = new Property(6, boolean.class, "isBackUp", false, "IS_BACK_UP");
        public final static Property BrainKey = new Property(7, String.class, "brainKey", false, "BRAIN_KEY");
    }


    public WalletDataDao(DaoConfig config) {
        super(config);
    }
    
    public WalletDataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"WALLET_DATA\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"ACCOUNT_ID\" TEXT NOT NULL ," + // 1: accountId
                "\"NAME\" TEXT NOT NULL ," + // 2: name
                "\"PASSWORD_PUB_KEY\" TEXT NOT NULL ," + // 3: passwordPubKey
                "\"ENCRYPTION_KEY\" TEXT NOT NULL ," + // 4: encryptionKey
                "\"ENCRYPTED_WIFKEY\" TEXT NOT NULL ," + // 5: encryptedWifkey
                "\"IS_BACK_UP\" INTEGER NOT NULL ," + // 6: isBackUp
                "\"BRAIN_KEY\" TEXT);"); // 7: brainKey
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"WALLET_DATA\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, WalletData entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getAccountId());
        stmt.bindString(3, entity.getName());
        stmt.bindString(4, entity.getPasswordPubKey());
        stmt.bindString(5, entity.getEncryptionKey());
        stmt.bindString(6, entity.getEncryptedWifkey());
        stmt.bindLong(7, entity.getIsBackUp() ? 1L: 0L);
 
        String brainKey = entity.getBrainKey();
        if (brainKey != null) {
            stmt.bindString(8, brainKey);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, WalletData entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getAccountId());
        stmt.bindString(3, entity.getName());
        stmt.bindString(4, entity.getPasswordPubKey());
        stmt.bindString(5, entity.getEncryptionKey());
        stmt.bindString(6, entity.getEncryptedWifkey());
        stmt.bindLong(7, entity.getIsBackUp() ? 1L: 0L);
 
        String brainKey = entity.getBrainKey();
        if (brainKey != null) {
            stmt.bindString(8, brainKey);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public WalletData readEntity(Cursor cursor, int offset) {
        WalletData entity = new WalletData( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // accountId
            cursor.getString(offset + 2), // name
            cursor.getString(offset + 3), // passwordPubKey
            cursor.getString(offset + 4), // encryptionKey
            cursor.getString(offset + 5), // encryptedWifkey
            cursor.getShort(offset + 6) != 0, // isBackUp
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // brainKey
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, WalletData entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAccountId(cursor.getString(offset + 1));
        entity.setName(cursor.getString(offset + 2));
        entity.setPasswordPubKey(cursor.getString(offset + 3));
        entity.setEncryptionKey(cursor.getString(offset + 4));
        entity.setEncryptedWifkey(cursor.getString(offset + 5));
        entity.setIsBackUp(cursor.getShort(offset + 6) != 0);
        entity.setBrainKey(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(WalletData entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(WalletData entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(WalletData entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
