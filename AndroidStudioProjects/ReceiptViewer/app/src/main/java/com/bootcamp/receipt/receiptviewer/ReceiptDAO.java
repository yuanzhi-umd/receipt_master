package com.bootcamp.receipt.receiptviewer;

/**
 * Receipt database type
 */
public class ReceiptDAO {
    int receiptID = 0;
    float totalAmount = 0;
    String timeStamp = null;
    String description = null;
    String expenseCategory = null;
    String vendor = null;
    String imageUrl = null;

    public ReceiptDAO(String timeStamp, String description, String expenseCategory,
                      String vendor, String imageUrl){

        receiptID = createReceiptID();
        timeStamp = timeStamp;
        description = description;
        expenseCategory = expenseCategory;
        vendor = vendor;
        imageUrl = imageUrl;
    }

}
