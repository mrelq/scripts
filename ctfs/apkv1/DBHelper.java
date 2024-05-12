package com.v2.onlinebanking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class DBHelper extends SQLiteOpenHelper {
    private static String name = "bankDB.db";
    public static final String secret = "IzZGvYWoNBs5ejRhuJisRXjV244UIx7/Wv81OykpIgp=";
    private final Context context;
    private String path;

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public DBHelper(Context context2) {
        super(context2, name, (SQLiteDatabase.CursorFactory) null, 1);
        this.context = context2;
        this.path = context2.getDatabasePath(name).getPath();
    }

    private boolean checkDB() {
        return new File(this.path).exists();
    }

    private void copyDB() {
        try {
            InputStream open = this.context.getAssets().open(name);
            FileOutputStream fileOutputStream = new FileOutputStream(this.context.getDatabasePath(name).getPath());
            byte[] bArr = new byte[1024];
            while (true) {
                int read = open.read(bArr);
                if (read > 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    open.close();
                    return;
                }
            }
        } catch (Exception e) {
            Log.e("TAG", "Database copy failed " + e);
        }
    }

    public void createDB() {
        if (!checkDB()) {
            getReadableDatabase();
            copyDB();
        }
    }

    public Boolean authenticateUser(String str, String str2) {
        try {
            return Boolean.valueOf(getWritableDatabase().rawQuery("select * from customerDetails where accountNumber = ?", new String[]{str}).getCount() > 0 && str2.equals(Util.d0(Util.r0(secret, 5))));
        } catch (Exception unused) {
            Log.d("vBank", "authenticateUser: error authenticating");
            return Boolean.FALSE;
        }
    }

    public Cursor checkBalance(String str) {
        return getWritableDatabase().rawQuery("select balance from customerDetails where  pin = ?", new String[]{str});
    }

    public Boolean accountExistance(String str) {
        return Boolean.valueOf(getWritableDatabase().rawQuery("select accountNumber from customerDetails where accountNumber = ?", new String[]{str}).getCount() > 0);
    }

    public Cursor readBalance(String str) {
        return getReadableDatabase().rawQuery("select balance from customerDetails where  accountNumber = ?", new String[]{str});
    }

    public Boolean updateBalance(String str, String str2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("balance", str2);
        boolean z = false;
        if (writableDatabase.rawQuery("select balance from customerDetails where accountNumber = ?", new String[]{str}).getCount() <= 0) {
            return false;
        }
        if (Long.valueOf((long) writableDatabase.update("customerDetails", contentValues, "accountNumber = ?", new String[]{str})).longValue() != 1) {
            z = true;
        }
        return Boolean.valueOf(z);
    }

    public Boolean checkUserPin(String str) {
        return Boolean.valueOf(getWritableDatabase().rawQuery("select pin from customerDetails where pin = ?", new String[]{str}).getCount() > 0);
    }

    public Cursor readCustomerName(String str) {
        return getReadableDatabase().rawQuery("select name from customerDetails where  accountNumber = ?", new String[]{str});
    }

    public Cursor readCustomerID(String str) {
        return getReadableDatabase().rawQuery("select id from customerDetails where  accountNumber = ?", new String[]{str});
    }
}
