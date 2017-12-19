package com.zhoukp.inform.view;

import java.io.Serializable;

/**
 * 作者： KaiPingZhou
 * 时间：2017/12/16 21:29
 * 邮箱：275557625@qq.com
 * 作用：投票类型选择实体类:单选 多选（不限选）...
 */
public class Pickers implements Serializable {

    private static final long serialVersionUID = 1L;

    private String showConetnt;
    private String showId;

    public String getShowConetnt() {
        return showConetnt;
    }

    public String getShowId() {
        return showId;
    }

    public Pickers(String showConetnt, String showId) {
        super();
        this.showConetnt = showConetnt;
        this.showId = showId;
    }

    public Pickers() {
        super();
    }

}
