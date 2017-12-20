package com.edwin.edwincase.recycler.drag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.edwin.edwincase.EdwinApplication;
import com.edwin.edwincase.EdwinBaseActivity;
import com.edwin.edwincase.R;
import com.hys.utils.DensityUtils;
import com.hys.utils.ImageUtils;
import com.hys.utils.InitCacheFileUtils;
import com.hys.utils.SdcardUtils;
import com.hys.utils.ToastUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

import static com.edwin.edwincase.recycler.drag.CommonConstants.COMPRESS_END_MSG;
import static com.edwin.edwincase.recycler.drag.CommonConstants.FILE_DIR_NAME;
import static com.edwin.edwincase.recycler.drag.CommonConstants.FILE_IMG_NAME;
import static com.edwin.edwincase.recycler.drag.CommonConstants.MAX_IMAGE_SUM;

/**
 * Created by Edwin on 2017/12/17.
 *
 * @author Edwin
 */

public class EdwinPostCommentsImagesActivity extends EdwinBaseActivity {

    private RecyclerView mRecyclerView;
    private PostCommentsImageAdapter mPostCommentsImageAdapter;
    private CompressHandler mCompressHandler;
    private ItemTouchHelper mItemTouchHelper;

    private final static int REQUEST_PHOTO_CODE = 1002;

    private ArrayList<String> originImgs; //原始图片
    private ArrayList<String> draggedImgs; //压缩后的图片

    private TextView mDelTextView;//删除区域提示

    public static void startActivity(Context context, ArrayList<String> images) {
        Intent intent = new Intent(context, EdwinPostCommentsImagesActivity.class);
        intent.putStringArrayListExtra("images", images);
        context.startActivity(intent);
    }
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_edwin_post_comments_images);
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recyclerview_post_comments);
        mDelTextView = findViewById(R.id.text_delete_region);
    }

    @Override
    protected void initData() {
        originImgs = getIntent().getStringArrayListExtra("images");
//        CommentUtils.getInstance().deleteCache(EdwinApplication.getContext());
        InitCacheFileUtils.initImgDir(FILE_DIR_NAME, FILE_IMG_NAME);//清除图片缓存
        // 添加按钮图片资源
        String addPath = getString(R.string.glide_plus_icon_string) + FILE_DIR_NAME + "/mipmap/" + R.mipmap.mine_btn_add;
        draggedImgs = new ArrayList<>();
        originImgs.add(addPath);//添加按键，超过9张时在adapter中隐藏
        draggedImgs.addAll(originImgs);
        mCompressHandler = new CompressHandler(this);
        //开启线程，在新线程中去压缩图片
        new Thread(new CompressionImgRunnable(draggedImgs, originImgs, draggedImgs, mCompressHandler, false)).start();
        mPostCommentsImageAdapter = new PostCommentsImageAdapter(EdwinApplication.getContext(), draggedImgs);
    }

    @Override
    protected void loadData() {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(mPostCommentsImageAdapter);
        CommentsCallback commentsCallback = new CommentsCallback(mPostCommentsImageAdapter, draggedImgs, originImgs);
        mItemTouchHelper = new ItemTouchHelper(commentsCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        //点击监听
        mRecyclerView.addOnItemTouchListener(new OnRecyclerViewItemListener(mRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {
                if (originImgs.get(viewHolder.getAdapterPosition()).contains(getString(R.string.glide_plus_icon_string))) {
                    //打开相册
                    Matisse.from(EdwinPostCommentsImagesActivity.this)
                            .choose(MimeType.allOf())
                            .countable(true)
                            .theme(R.style.Matisse_Dracula)
                            .maxSelectable(MAX_IMAGE_SUM)
//                            .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                            .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.moment_img_grid_size))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine())
                            .forResult(REQUEST_PHOTO_CODE);
                } else {
                    ToastUtils.getInstance().show(EdwinApplication.getContext(), "预览图片");
                }
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder viewHolder) {
                //如果item不是最后一个，则执行拖拽
                if (viewHolder.getLayoutPosition() != draggedImgs.size() - 1) {
                    mItemTouchHelper.startDrag(viewHolder);
                }
            }
        });
        commentsCallback.setDragListener(new CommentsCallback.DragListener() {
            @Override
            public void setDeleteState(boolean delete) {
                if (delete) {
                    mDelTextView.setBackgroundResource(R.color.holo_red_dark);
                    mDelTextView.setText(getResources().getString(R.string.post_delete_tv_s));
                } else {
                    mDelTextView.setText(getResources().getString(R.string.post_delete_tips));
                    mDelTextView.setBackgroundResource(R.color.holo_red_light);
                }
            }

            @Override
            public void setDragState(boolean start) {
                if (start) {
                    mDelTextView.setVisibility(View.VISIBLE);
                } else {
                    mDelTextView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHOTO_CODE && resultCode == RESULT_OK) {
            //从相册选择完图片后，另开线程压缩图片
            ArrayList<Uri> result = (ArrayList<Uri>) Matisse.obtainResult(data);
            ArrayList<String> stringArrayList = new ArrayList<>(9);
            for (Uri uri : result) {
                stringArrayList.add(CommentUtils.getInstance().getImagePath(uri,null));
            }
            new Thread(new CompressionImgRunnable(stringArrayList,
                    originImgs, draggedImgs, mCompressHandler, true)).start();
        }
    }

    private class CompressionImgRunnable implements Runnable {

        ArrayList<String> images;
        ArrayList<String> originImages;
        ArrayList<String> draggedImages;
        Handler handler;
        boolean add;//是否为添加图片

        public CompressionImgRunnable(
                ArrayList<String> images, ArrayList<String> originImages, ArrayList<String> draggedImages,
                Handler handler, boolean add) {
            this.images = images;
            this.originImages = originImages;
            this.draggedImages = draggedImages;
            this.handler = handler;
            this.add = add;
        }

        @Override
        public void run() {
            SdcardUtils sdcardUtils = new SdcardUtils();
            String filePath;
            Bitmap newBitmap;
            int addIndex = originImages.size() - 1;
            for (int i=0; i<images.size(); i++) {
                if (images.get(i).contains(getString(R.string.glide_plus_icon_string))) {
                    continue;
                }
                //压缩
                newBitmap = ImageUtils.compressScaleByWH(
                        images.get(i),
                        DensityUtils.dp2px(EdwinPostCommentsImagesActivity.this, 100),
                        DensityUtils.dp2px(EdwinPostCommentsImagesActivity.this, 100)
                );
                //文件地址
                filePath = sdcardUtils.getSDPATH() + FILE_DIR_NAME + "/"
                        + FILE_IMG_NAME + "/" + String.format(Locale.getDefault(),"img_%d.jpg", System.currentTimeMillis()).toLowerCase();
                //保存图片
                ImageUtils.save(newBitmap, filePath, Bitmap.CompressFormat.JPEG, true);
                //设置值
                if (!add) {
                    images.set(i, filePath);
                } else {
                    draggedImages.add(addIndex, filePath);
                    originImages.add(addIndex++, filePath);
                }
            }
            Message message = new Message();
            message.what = COMPRESS_END_MSG;
            handler.sendMessage(message);
        }
    }

    private static class CompressHandler extends Handler {
        private WeakReference<Activity> mReference;

        public CompressHandler(Activity activity) {
            mReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            EdwinPostCommentsImagesActivity activity = (EdwinPostCommentsImagesActivity) mReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case COMPRESS_END_MSG:
                        activity.mPostCommentsImageAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompressHandler.removeCallbacksAndMessages(null);
    }
}
