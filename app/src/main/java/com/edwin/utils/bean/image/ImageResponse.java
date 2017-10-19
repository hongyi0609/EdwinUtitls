package com.edwin.utils.bean.image;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edwin on 2016/3/21 0021.
 */
public class ImageResponse {
    @SerializedName("data")
    private ImageData data;

    public ImageData getData() {
        return data;
    }

    public void setData(ImageData data) {
        this.data = data;
    }
}
