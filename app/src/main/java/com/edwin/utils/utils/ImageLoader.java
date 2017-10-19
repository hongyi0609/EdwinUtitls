package com.edwin.utils.utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.edwin.utils.config.Config;


/**
 * Created by Edwin on 2016/5/23 0023.
 */

public class ImageLoader {
    private ImageLoader() {
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        if (Config.isNight) {
            imageView.setAlpha(0.2f);
            imageView.setBackgroundColor(Color.BLACK);
        }
        Glide.with(context).load(url).into(imageView);
    }
}
