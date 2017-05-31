package com.example.android.cameragallery.model;


import java.io.Serializable;

/**
 * Created by Awinash on 20-05-2017.
 */

public class GalleryModel implements Serializable{

    private String galleryImage;


    public GalleryModel(String galleryImage) {
        this.galleryImage = galleryImage;
    }

    public String getGalleryImage() {
        return galleryImage;
    }

    public void setGalleryImage(String galleryImage) {
        this.galleryImage = galleryImage;
    }
}
