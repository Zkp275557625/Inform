package com.zhoukp.inform.callback;

import android.support.v7.widget.RecyclerView;

/**
 * 作者： zhoukp
 * 时间：2017/12/12 11:25
 * 邮箱：zhoukaiping@szy.cn
 * 作用：拖拽监听接口
 */

public interface StartDragListener {
    /**
     * 开始拖拽
     *
     * @param viewHolder
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
