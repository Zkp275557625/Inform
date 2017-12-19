package com.zhoukp.inform.adapter;

import android.view.View;

import com.zhoukp.inform.bean.InformType;

/**
 * 作者： zhoukp
 * 时间：2017/12/11 17:50
 * 邮箱：zhoukaiping@szy.cn
 * 作用：RecyclerView的item点击事件接口
 */

public interface OnItemClickListener {
    /**
     * 某个item被点击的回调
     *
     * @param view
     * @param data
     */
    void OnItemClick(View view, InformType data, int position);
}
