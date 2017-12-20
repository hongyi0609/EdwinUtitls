package com.edwin.edwincase.recycler.drag;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.edwin.edwincase.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Edwin on 2017/12/17.
 *
 * @author Edwin
 */

public class CommentsCallback extends ItemTouchHelper.Callback {

    private int mDragFlags;
    private int mSwipeFlags; //滑动标记
    private boolean isActionUp; //手指抬起标记位

    private PostCommentsImageAdapter mAdapter;
    private List<String> mImages; //经过压缩处理的图片
    private List<String> mOriginImages; //未经处理的图片，为了拖拽后顺序保持一致

    public CommentsCallback(PostCommentsImageAdapter adapter, List<String> images, List<String> originIamges) {
        mAdapter = adapter;
        mImages = images;
        mOriginImages = originIamges;
    }

    /**
     * 设置item是否响应拖拽事件和滑动事件，以及设置拖拽和滑动操作的方向
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //根据recyclerView的布局管理器判断是否可以拖拽
        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            //设置拖拽方向
            mDragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            mSwipeFlags = 0;//不可滑动
        }
        return makeMovementFlags(mDragFlags, mSwipeFlags);
    }

    /**
     * 当用户从item原来的位置拖动可以拖动的item到新位置的过程中调用
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition == mImages.size() - 1 || toPosition == mImages.size() - 1) {
            // add 位置，拖放失败
            return true;
        } else if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mImages, i, i + 1);
                Collections.swap(mOriginImages, i, i + 1);
            }
        } else if (fromPosition > toPosition) {
            for (int i = fromPosition; i > toPosition; i-- ) {
                Collections.swap(mImages, i, i - 1);
                Collections.swap(mOriginImages, i, i - 1);
            }
        }
        mAdapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    /**
     * 设置是否支持长按拖拽
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /**
     *
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        mAdapter.notifyDataSetChanged();
        resetAdapter();
    }

    private void resetAdapter() {
        if (mDragListener != null) {
            mDragListener.setDeleteState(false);
            mDragListener.setDragState(false);
        }
        isActionUp = false;
    }

    /**
     * 自定义拖动与滑动交互
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (null == mDragListener) {
            return;
        }
        if (dY > (recyclerView.getHeight()
                - viewHolder.itemView.getBottom() // item底部距离recyclerView顶部高度
                - CommentUtils.getInstance().getPixelById(R.dimen.comments_post_delete))) { //拖到删除处
            mDragListener.setDeleteState(true);
            if (isActionUp) { //在删除处放手，则删除item
                //先设置不可见，如果不设置的话，会看到viewHolder返回到原位置时才消失，因为remove会在viewHolder动画执行完成后才将viewHolder删除
                viewHolder.itemView.setVisibility(View.INVISIBLE);
                mOriginImages.remove(viewHolder.getAdapterPosition());
                mImages.remove(viewHolder.getAdapterPosition());
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                resetAdapter();
                return;
            }
        } else { //没有到删除处
            if (View.INVISIBLE == viewHolder.itemView.getVisibility()) {
                //如果viewHolder不可见，则表示用户放手，重置删除区域状态
                mDragListener.setDeleteState(false);
            }
            mDragListener.setDeleteState(false);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    /**
     * 当长按选中item的时候（拖拽开始的时候）调用
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (mDragListener != null && ItemTouchHelper.ACTION_STATE_DRAG == actionState) {
            mDragListener.setDragState(true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 设置手指离开后ViewHolder的动画时间，在用户手指离开后调用
     * @param recyclerView
     * @param animationType
     * @param animateDx
     * @param animateDy
     * @return
     */
    @Override
    public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        // 手指放开
        isActionUp = true;
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
    }

    public interface DragListener {
        /**
         * 用户是否将 item拖动到删除处，根据状态改变颜色
         * @param delete
         */
        void setDeleteState(boolean delete);

        /**
         * 是否于拖拽状态
         * @param start
         */
        void setDragState(boolean start);
    }

    public void setDragListener(DragListener dragListener) {
        mDragListener = dragListener;
    }

    private DragListener mDragListener;
}
