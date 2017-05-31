package com.example.android.cameragallery.model;

/**
 * Created by Awinash on 23-05-2017.
 */

public class CameraImage {

    // private variables
    int id;
    byte[] image;

    public CameraImage(){

    }

    public CameraImage(byte[] image) {
        this.image = image;
    }

    public CameraImage(int id) {
        this.id = id;
    }

    public CameraImage(int id, byte[] image) {
        this.id = id;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
