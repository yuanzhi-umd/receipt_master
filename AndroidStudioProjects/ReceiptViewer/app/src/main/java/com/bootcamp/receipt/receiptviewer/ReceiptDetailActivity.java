package com.bootcamp.receipt.receiptviewer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;


/**
 * An activity representing a single Receipt detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ReceiptListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link ReceiptDetailFragment}.
 */
public class ReceiptDetailActivity extends Activity {

    private Button camera;
    private String LOG_TAG = ReceiptDetailActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_detail);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        camera = (Button) findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ReceiptDetailActivity.this, CameraActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        Button save_button = (Button) findViewById(R.id.save_entry);
        save_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create database entry
                // Update ReceiptList view

            }
        });
        Button delete_button = (Button) findViewById(R.id.delete_entry);
        delete_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Delete database entry
                // Delete receipt photo
                // Update ReceiptList view

            }
        });
        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            //Bundle arguments = new Bundle();
            //arguments.putString(ReceiptDetailFragment.ARG_ITEM_ID,
            //        getIntent().getStringExtra(ReceiptDetailFragment.ARG_ITEM_ID));
            //ReceiptDetailFragment fragment = new ReceiptDetailFragment();
            //fragment.setArguments(arguments);
            //getFragmentManager().beginTransaction()
            //        .add(R.id.receipt_detail_container, fragment)
            //        .commit();
        }
    }

    long receipt_id;
    String photo_data_path;
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                receipt_id = data.getLongExtra("receipt_id", 0);
                Log.i(LOG_TAG, "got receipt_id: " + receipt_id);
            }
            ReceiptDAO dao = new ReceiptDAO(this);
            dao.open();
            Receipt r = dao.getReceipt(receipt_id);
            photo_data_path = r.getImageUrl();
            dao.close();
            Log.i(LOG_TAG, "saved photo path is: " + photo_data_path);

            File imgFile = new File(photo_data_path);
            if(imgFile.exists()){
                Log.i(LOG_TAG, "image file exists!");
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ImageView myImage = (ImageView) findViewById(R.id.receiptView);
                // myImage.setImageBitmap(myBitmap);
                displayReceiptScaled(myImage, photo_data_path);
            }
        }
    }

    private void displayReceiptScaled(ImageView mImageView, String mCurrentPhotoPath) {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();
        Log.i(LOG_TAG, "targetW: " + targetW);
        Log.i(LOG_TAG, "targetH: " + targetH);

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, ReceiptListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
