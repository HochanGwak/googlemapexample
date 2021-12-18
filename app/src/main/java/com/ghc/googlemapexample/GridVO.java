package com.ghc.googlemapexample;

import android.net.Uri;

public class GridVO {
    private String title;
    private Uri imageUri;

    private int album_num;

    public GridVO(String title, Uri imageUri, int album_num) {
        this.title = title;
        this.imageUri = imageUri;
        this.album_num = album_num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public int getNumber() {
        return album_num;
    }
}
