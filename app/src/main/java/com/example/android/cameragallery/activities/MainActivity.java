package com.example.android.cameragallery.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cameragallery.R;
import com.example.android.cameragallery.adapter.GalleryAdapter;
import com.example.android.cameragallery.adapter.GalleryImageAdapter;
import com.example.android.cameragallery.database.DatabaseHelper;
import com.example.android.cameragallery.database.DatabaseManager;
import com.example.android.cameragallery.model.CameraImage;
import com.example.android.cameragallery.model.GalleryModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private Uri fileUri; // file url to store image/video

    RecyclerView mPictureRecyclerView;
    TextView mTextMessage;
    Button mTakePicture;
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager mGridLayoutManager;
    private ArrayList<GalleryModel> listOfImagesPath;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
    DatabaseManager mDatabaseManager;

    ArrayList<CameraImage> imageArrayList = new ArrayList<CameraImage>();

    boolean isDataSaved = false;



    public static final String GridViewDemo_ImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GalleryPhoto/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPictureRecyclerView = (RecyclerView) findViewById(R.id.picture_recycler_view);
        mTextMessage = (TextView) findViewById(R.id.text_msg);
        mTakePicture = (Button) findViewById(R.id.capture_btn);

        mPictureRecyclerView.setHasFixedSize(true);
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mPictureRecyclerView.setLayoutManager(mGridLayoutManager);

        /**
         * create DatabaseManager object
         */
        mDatabaseManager = new DatabaseManager(this);
        mDatabaseManager.open();

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }

        mTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // capture picture
                captureImage();
            }
        });

        Log.d(TAG, "inside onCreate .............");

        if(mDatabaseManager.getCaptureImageCount() > 0) {
            displayImageInGrid();
        }

        // This code use for retrive image path list from file and display in grid
        /*
        listOfImagesPath = null;
        listOfImagesPath = RetriveCapturedImagePath();
        Log.d("XYZ", "inside onCreate lmage path = " + listOfImagesPath);
        if (listOfImagesPath != null && listOfImagesPath.size() > 0) {

            mTextMessage.setVisibility(View.GONE);
            mPictureRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = null;
            mAdapter = new GalleryAdapter(getApplicationContext(), listOfImagesPath);
            mPictureRecyclerView.setAdapter(mAdapter);
        }

        */

    }

    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                saveImageInDatabase(data);
                if(isDataSaved) {
                    displayImageInGrid();
                }

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (mAdapter != null) {

            mAdapter.notifyDataSetChanged();
        }
    }

    void saveImageInDatabase(Intent data){
        // successfully captured the image
        Bundle extras = data.getExtras();
        if(extras != null) {
            Bitmap myImage = extras.getParcelable("data");

            // convert bitmap to byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            myImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte imageInByte[] = stream.toByteArray();

            // Inserting image
            Log.d("Insert: ", "Inserting ..");
            boolean isImageAdded = mDatabaseManager.addImage(new CameraImage(imageInByte));
            if(isImageAdded){
                Log.d(TAG,"Successfully added image into database");
                isDataSaved = true;
            } else {
                Log.d(TAG,"something wrong not added image into database");
                isDataSaved = false;
            }
        } else {
            Log.d(TAG, "Extra data is null ..");
        }
    }

    private ArrayList<CameraImage> getDataFromDatabase(){

        /**
         * Reading and getting all records from database
         */
        ArrayList<CameraImage> cameraImages = mDatabaseManager.getAllImages();
       // Log.d("XYZ", "inside getDataFromDatabase cameraImages size = " + cameraImages.size());
        imageArrayList.clear();
        for (CameraImage cImage : cameraImages){

            imageArrayList.add(cImage);
        }

        return imageArrayList;
    }

    private void displayImageInGrid(){

        imageArrayList = getDataFromDatabase();

        if(imageArrayList != null && imageArrayList.size() > 0){

            mTextMessage.setVisibility(View.GONE);
            mPictureRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = null;
            mAdapter = new GalleryImageAdapter(getApplicationContext(), imageArrayList);
            mPictureRecyclerView.setAdapter(mAdapter);
        }
    }


    // save capture image in file
    private ArrayList<GalleryModel> RetriveCapturedImagePath() {
        ArrayList<GalleryModel> imageFileList = new ArrayList<GalleryModel>();
        File fileObj = new File(GridViewDemo_ImagePath);

        if (fileObj.exists()) {

            File[] files = fileObj.listFiles();
            Arrays.sort(files);

            for (int i = 0; i < files.length; i++) {

                File file = files[i];
                if (file.isDirectory())
                    continue;
                imageFileList.add(new GalleryModel(file.getPath()));

            }
        }
        return imageFileList;
    }

    void saveImageInFile(Intent data){
        // successfully captured the image
        Bundle extras = data.getExtras();
        Bitmap thePic = extras.getParcelable("data");
        String imgcurTime = dateFormat.format(new Date());
        File imageDirectory = new File(GridViewDemo_ImagePath);
        boolean dir = imageDirectory.mkdirs();

        String _path = GridViewDemo_ImagePath + imgcurTime + ".jpg";

        try {
            FileOutputStream out = new FileOutputStream(_path);
            thePic.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
        } catch (FileNotFoundException e) {
            e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listOfImagesPath = null;
        listOfImagesPath = RetriveCapturedImagePath();
        if (listOfImagesPath != null && listOfImagesPath.size() > 0) {

            mTextMessage.setVisibility(View.GONE);
            mPictureRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = null;
            mAdapter = new GalleryAdapter(getApplicationContext(), listOfImagesPath);
            mPictureRecyclerView.setAdapter(mAdapter);

        }

    }



}
