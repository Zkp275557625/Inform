package com.zhoukp.inform.callback;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.zhoukp.inform.adapter.KPRecyclerViewAdapter;

/**
 * 作者： zhoukp
 * 时间：2017/12/12 11:16
 * 邮箱：zhoukaiping@szy.cn
 * 作用：recyclerview拖拽回调
 */

public class KPItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchMoveListener itemTouchMoveListener;

    public KPItemTouchHelperCallback(ItemTouchMoveListener itemTouchMoveListener) {
        this.itemTouchMoveListener = itemTouchMoveListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;//不同条目类型不能移动
        } else {
            return itemTouchMoveListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setAlpha(0.5f);
        } else if (actionState == ItemTouchHelper.ANIMATION_TYPE_SWIPE_SUCCESS) {
            ((KPRecyclerViewAdapter.KPViewHolder) viewHolder).ll_add_item.setVisibility(View.VISIBLE);
        } else if (actionState == ItemTouchHelper.ANIMATION_TYPE_DRAG) {
            ((KPRecyclerViewAdapter.KPViewHolder) viewHolder).ll_add_item.setVisibility(View.GONE);
        }
    }


    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder
            viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            float percentage = 1 - Math.abs(dX) / viewHolder.itemView.getWidth();
            viewHolder.itemView.setScaleX(percentage);
            viewHolder.itemView.setScaleY(percentage);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setAlpha(1.0f);
        viewHolder.itemView.setScaleX(1.0f);
        viewHolder.itemView.setScaleY(1.0f);
        super.clearView(recyclerView, viewHolder);
    }

    /* 是否开启长按拖动 */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
