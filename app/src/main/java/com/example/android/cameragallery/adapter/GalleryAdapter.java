package com.example.android.cameragallery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.android.cameragallery.R;
import com.example.android.cameragallery.model.GalleryModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Awinash on 20-05-2017.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.DataObjectHolder>{

    private static ArrayList<GalleryModel> mDataset;
    private static Context context;

    Bitmap bitmapImage;

    public GalleryAdapter(Context context, ArrayList<GalleryModel> pictureDataset) {
        this.context = context;
        this.mDataset = pictureDataset;
       // Log.d("XYZ","inside GalleryAdapter ctor");
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // Log.d("XYZ","inside GalleryAdapter onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_image,parent,false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Log.d("XYZ","inside GalleryAdapter onBindViewHolder");
        fetchCapturedImagePath(position);
        if(bitmapImage != null){
            Log.d("XYZ","inside GalleryAdapter onBindViewHolder 11");
            holder.mPictureView.setImageBitmap(bitmapImage);
        }
    }

    @Override
    public int getItemCount() {
       // Log.d("XYZ","inside GalleryAdapter getItemCount size = " + mDataset.size());
        return mDataset.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {

        ImageView mPictureView;
        public DataObjectHolder(View itemView) {
            super(itemView);
            mPictureView = (ImageView) itemView.findViewById(R.id.iv_picture);
        }

    }

    void fetchCapturedImagePath(int index){


        BitmapFactory.Options bfOptions = new BitmapFactory.Options();
       // bfOptions.inDither = false;                     //Disable Dithering mode
      //  bfOptions.inPurgeable = true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
      //  bfOptions.inInputShareable = true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
       // bfOptions.inTempStorage = new byte[32 * 1024];

        FileInputStream fileInput = null;
       // Bitmap bm;
        try {

            fileInput = new FileInputStream(new File(mDataset.get(index).getGalleryImage()));

            if (fileInput != null) {

                bitmapImage = BitmapFactory.decodeFileDescriptor(fileInput.getFD(), null, bfOptions);
               // imageView.setImageBitmap(bm);
                //imageView.setId(position);
               // imageView.setLayoutParams(new GridView.LayoutParams(200, 160));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
