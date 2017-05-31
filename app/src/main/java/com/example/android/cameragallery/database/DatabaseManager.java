package com.example.android.cameragallery.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.android.cameragallery.model.CameraImage;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.cameragallery.database.DatabaseHelper.IMAGE_PATH;
import static com.example.android.cameragallery.database.DatabaseHelper.KEY_ID;

/**
 * Created by Awinash on 23-05-2017.
 */

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";

    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;


    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException{

        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    //Insert values to the table photo_gallery
    public boolean addImage(CameraImage cameraImage){
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        ContentValues values=new ContentValues();

        values.put(DatabaseHelper.IMAGE_PATH, cameraImage.getImage() );

        long rowInserted = database.insert(DatabaseHelper.TABLE_NAME, null, values);
        if (rowInserted != -1) {
            // close db connection
            database.close();
            return true;
        } else {
            // close db connection
            database.close();
            return false;
        }
        //database.close();
    }


    /**
     *Getting All Images
     **/

    public ArrayList<CameraImage> getAllImages() {
        ArrayList<CameraImage> imageList = new ArrayList<CameraImage>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NAME;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor != null && cursor.moveToFirst()) {
                do {

                    CameraImage cameraImage = new CameraImage();
                    cameraImage.setId(Integer.parseInt(cursor.getString(0)));
                    cameraImage.setImage(cursor.getBlob(1));

                    // Adding contact to list
                    imageList.add(cameraImage);
                } while (cursor.moveToNext());
            }
        }catch (CursorIndexOutOfBoundsException ex){
            Log.d(TAG,"cursor error message - " + ex.getMessage());

        }catch (Exception e){
            Log.d(TAG,"error message - " + e.getMessage());
        }

        // return image list
        return imageList;
    }

    public int getCaptureImageCount(){

        String countQuery = "SELECt * FROM " + dbHelper.TABLE_NAME;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        int count = 0;

        try {

            cursor = db.rawQuery(countQuery, null);
            if(cursor != null && !cursor.isClosed()) {

                count = cursor.getCount();
                cursor.close();
            }

        }catch (Exception e) {

        }

        return count;
    }

}
