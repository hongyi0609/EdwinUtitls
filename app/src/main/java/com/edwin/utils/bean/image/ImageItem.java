package com.edwin.utils.bean.image;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edwin on 2016/3/21 0021.
 * @author Edwin
 */
public class ImageItem {
    @SerializedName("description")
    private String description;
    @SerializedName("image_url")
    private String mImageUrl;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }
}
