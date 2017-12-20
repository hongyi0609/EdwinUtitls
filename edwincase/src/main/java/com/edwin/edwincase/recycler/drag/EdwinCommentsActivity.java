package com.edwin.edwincase.recycler.drag;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.edwin.edwincase.EdwinBaseActivity;
import com.edwin.edwincase.R;
import com.hys.utils.ToastUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.util.ArrayList;


/**
 * Created by Edwin on 2017/12/18.
 *
 * @author Edwin
 */

public class EdwinCommentsActivity extends EdwinBaseActivity {
    private Context mContext;
    private Button mBtnObtainPhoto;
    private View.OnClickListener mOnClickListener;

    private final static int REQUEST_PHOTO_CODE = 1001;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_edwin_post_comments);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mContext = this;
        mBtnObtainPhoto = findViewById(R.id.btn_obtain_photos);
    }

    @Override
    protected void initData() {
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) &
                        ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            (Activity) mContext,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_PHOTO_CODE);
                } else {
                    Matisse.from(EdwinCommentsActivity.this)
                            .choose(MimeType.allOf())
                            .countable(true)
                            .theme(R.style.Matisse_Dracula)
                            .maxSelectable(CommonConstants.MAX_IMAGE_SUM)
//                            .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                            .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.moment_img_grid_size))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine())
                            .forResult(REQUEST_PHOTO_CODE);
                }
            }
        };
    }

    @Override
    protected void loadData() {
        mBtnObtainPhoto.setOnClickListener(mOnClickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHOTO_CODE && resultCode == RESULT_OK) {
            ArrayList<Uri> result = (ArrayList<Uri>) Matisse.obtainResult(data);
            ArrayList<String> stringArrayList = new ArrayList<>(9);
            for (Uri uri : result) {
                stringArrayList.add(CommentUtils.getInstance().getImagePath(uri, null));
            }
            EdwinPostCommentsImagesActivity.startActivity(EdwinCommentsActivity.this, stringArrayList);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PHOTO_CODE:
                if (grantResults.length > 0 && (grantResults[0] | grantResults[1]) == PackageManager.PERMISSION_GRANTED) {
                    mOnClickListener.onClick(mBtnObtainPhoto);
                } else {
                    ToastUtils.getInstance().show(EdwinCommentsActivity.this, "请提供读写权限");
                }
                break;
            default:
                break;
        }
    }
}
