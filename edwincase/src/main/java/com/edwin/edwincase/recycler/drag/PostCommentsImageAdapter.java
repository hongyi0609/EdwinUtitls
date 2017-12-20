package com.edwin.edwincase.recycler.drag;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.edwin.edwincase.R;

import java.util.List;

/**
 * Created by Edwin on 2017/12/17.
 *
 * @author Edwin
 */

public class PostCommentsImageAdapter extends RecyclerView.Adapter<PostCommentsImageAdapter.PostImageViewHolder> {

    private List<String> mData;
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    public PostCommentsImageAdapter(Context context, List<String> data) {
        mContext = context;
        mData = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public PostImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostImageViewHolder(mLayoutInflater.inflate(R.layout.item_post_comments_layout,parent,false));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public void onBindViewHolder(PostImageViewHolder holder, int position) {
        if (position > CommonConstants.MAX_IMAGE_SUM) {
            //IMAGE_SIZE允许添加的图片数, position每次添加都会+1
            holder.imageView.setVisibility(View.GONE);
        } else {
            holder.imageView.setVisibility(View.VISIBLE);
        }
        Glide.with(mContext)
             .load(mData.get(position))
             .diskCacheStrategy(DiskCacheStrategy.ALL)
             .into(holder.imageView);
    }

    public class PostImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PostImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_add);
        }
    }

}
