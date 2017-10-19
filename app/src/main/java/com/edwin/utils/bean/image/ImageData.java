package com.edwin.utils.bean.image;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Edwin on 2016/3/21 0021.
 */
public class ImageData {
    @SerializedName("images")
    private ArrayList<ImageItem> images;

    public ArrayList<ImageItem> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageItem> images) {
        this.images = images;
    }
}
