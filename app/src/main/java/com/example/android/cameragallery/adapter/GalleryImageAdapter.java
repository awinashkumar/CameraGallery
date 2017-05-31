package com.example.android.cameragallery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.cameragallery.R;
import com.example.android.cameragallery.model.CameraImage;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Created by Awinash on 24-05-2017.
 */

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.DataObjectHolder> {

    Context mContext;
    ArrayList<CameraImage> mImageList;

    public GalleryImageAdapter(Context context, ArrayList<CameraImage> imageArrayList) {
        this.mContext = context;
        this.mImageList = imageArrayList;

    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_image,parent,false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        CameraImage cImage = mImageList.get(position);

        //convert byte to bitmap take from contact class
        byte[] outImage = cImage.getImage();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap bitmapImage = BitmapFactory.decodeStream(imageStream);

        if(bitmapImage != null) {
            holder.mPictureView.setImageBitmap(bitmapImage);
        }
    }


    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {

        ImageView mPictureView;
        public DataObjectHolder(View itemView) {
            super(itemView);
            mPictureView = (ImageView) itemView.findViewById(R.id.iv_picture);
        }

    }

}
