package com.bootcamp.receipt.receiptviewer;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;

/**
 * Receipt database type
 */
public class ReceiptDAO {

    private SQLiteDatabase database;
    private ReceiptDBHelper dbHelper;

    public ReceiptDAO(Context context) {
        dbHelper = new ReceiptDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Receipt getReceipt(long receiptId) {
        Cursor cursor = database.query(ReceiptDBHelper.RECEIPTS_TABLE_NAME,
                ReceiptDBHelper.DATABASE_COLUMNS, ReceiptDBHelper.RECEIPTS_TABLE_ID + " = " + receiptId, null,
                null, null, null);
        cursor.moveToFirst();
        Receipt newReceipt = cursorToReceipt(cursor);
        cursor.close();
        return newReceipt;
    }

    public long insertReceipt(ContentValues cv) {
        long insertId = database.insert(ReceiptDBHelper.RECEIPTS_TABLE_NAME, null, cv);
        return insertId;
    }

    public long insertReceipt(long purchaseTimestamp, String receiptDescription,
                              int totalAmount, String category, String vendor, String imageUrl) {
        ContentValues cv = new ContentValues();
        cv.put(ReceiptDBHelper.DATABASE_COLUMNS[1], purchaseTimestamp);
        cv.put(ReceiptDBHelper.DATABASE_COLUMNS[2], receiptDescription);
        cv.put(ReceiptDBHelper.DATABASE_COLUMNS[3], totalAmount);
        cv.put(ReceiptDBHelper.DATABASE_COLUMNS[4], category);
        cv.put(ReceiptDBHelper.DATABASE_COLUMNS[5], vendor);
        cv.put(ReceiptDBHelper.DATABASE_COLUMNS[6], imageUrl);

        return insertReceipt(cv);
    }

    public void deleteReceipt(Receipt receipt) {
        long receiptId = receipt.getId();
        deleteReceipt(receiptId);
    }

    public void deleteReceipt(long receiptId) {
        System.out.println("Deleting receipt with id: " + receiptId);
        database.delete(ReceiptDBHelper.RECEIPTS_TABLE_NAME, ReceiptDBHelper.RECEIPTS_TABLE_ID
                + " = " + receiptId, null);
    }

    public void updateReceipt(ContentValues cv) {
        database.update(ReceiptDBHelper.RECEIPTS_TABLE_NAME, cv, ReceiptDBHelper.RECEIPTS_TABLE_ID + "=" +
                cv.getAsString(ReceiptDBHelper.RECEIPTS_TABLE_ID), null);
    }

    public void updateReceipt(Receipt receipt) {
        ContentValues cv = new ContentValues();
        cv.put(ReceiptDBHelper.DATABASE_COLUMNS[0], receipt.getId());
        cv.put(ReceiptDBHelper.DATABASE_COLUMNS[1], receipt.getTimeStamp());
        cv.put(ReceiptDBHelper.DATABASE_COLUMNS[2], receipt.getDescription());
        cv.put(ReceiptDBHelper.DATABASE_COLUMNS[3], receipt.getTotalAmount());
        cv.put(ReceiptDBHelper.DATABASE_COLUMNS[4], receipt.getCategory());
        cv.put(ReceiptDBHelper.DATABASE_COLUMNS[5], receipt.getVendor());
        cv.put(ReceiptDBHelper.DATABASE_COLUMNS[6], receipt.getImageUrl());

        updateReceipt(cv);
    }

    public List<Receipt> getAllReceipts() {
        List<Receipt> receipts = new ArrayList<Receipt>();

        Cursor cursor = database.query(ReceiptDBHelper.RECEIPTS_TABLE_NAME,
                ReceiptDBHelper.DATABASE_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Receipt receipt = cursorToReceipt(cursor);
            receipts.add(receipt);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return receipts;
    }

    private Receipt cursorToReceipt(Cursor cursor) {
        Receipt receipt = new Receipt();

        receipt.setId(cursor.getLong(0));
        receipt.setTimeStamp(cursor.getInt(1));
        receipt.setDescription(cursor.getString(2));
        receipt.setTotalAmount(cursor.getInt(3));
        receipt.setCategory(cursor.getString(4));
        receipt.setVendor(cursor.getString(5));
        receipt.setImageUrl(cursor.getString(6));

        return receipt;
    }

}
