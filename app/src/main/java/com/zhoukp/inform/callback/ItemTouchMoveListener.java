package com.zhoukp.inform.callback;

import android.support.v7.widget.RecyclerView;

/**
 * 作者： zhoukp
 * 时间：2017/12/12 11:21
 * 邮箱：zhoukaiping@szy.cn
 * 作用：recyclerview的item拖动监听接口
 */

public interface ItemTouchMoveListener {

    /**
     * move监听
     *
     * @param fromPosition 本来的位置
     * @param toPosition   移动到的位置
     * @return
     */
    boolean onItemMove(int fromPosition, int toPosition);

    /**
     * 移除监听
     *
     * @param position 位置
     * @return
     */
//    boolean onItemRemove(int position);
}
