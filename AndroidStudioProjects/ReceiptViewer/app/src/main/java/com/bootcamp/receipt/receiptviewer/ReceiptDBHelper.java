package com.bootcamp.receipt.receiptviewer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by vkb on 11/18/14.
 */
public class ReceiptDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "receipts_db";
    public static final String TABLE_NAME = "receipts_table";
    public static final String RECEIPTS_TABLE_ID = "receipt_id";

    private static String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
            "receipt_id INTEGER primary key autoincrement, " +
            "purchase_ts INTEGER, " +
            "receipt_desc TEXT, " +
            "total_amount REAL, " +
            "category TEXT, " +
            "vendor TEXT, " +
            "image_url TEXT); ";

    ReceiptDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        Log.w(ReceiptDBHelper.class.getName(),
                "Upgrading database... which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
