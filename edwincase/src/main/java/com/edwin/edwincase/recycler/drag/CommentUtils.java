package com.edwin.edwincase.recycler.drag;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.edwin.edwincase.EdwinApplication;

import java.io.File;

/**
 * Created by Edwin on 2017/12/17.
 *
 * @author Edwin
 */

public class CommentUtils {
    private static CommentUtils mInstance;
    private CommentUtils() {

    }

    public static CommentUtils getInstance() {
        if (mInstance == null) {
            mInstance = new CommentUtils();
        }
        return mInstance;
    }

    /**
     * 获取dimens定义的大小
     *
     * @param dimensionId
     * @return
     */
    public int getPixelById(int dimensionId) {
        return EdwinApplication.getContext().getResources().getDimensionPixelSize(dimensionId);
    }

    /**
     * 通过Uri 和 selection来获得真实的图片路径
     *
     * @param uri
     * @param selection
     * @return
     */
    public String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = EdwinApplication.getContext().getContentResolver().query(
                uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
        }
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

}
