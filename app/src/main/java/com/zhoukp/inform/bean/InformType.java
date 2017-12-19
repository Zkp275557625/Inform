package com.zhoukp.inform.bean;

import java.util.ArrayList;

/**
 * 作者： zhoukp
 * 时间：2017/12/11 14:50
 * 邮箱：zhoukaiping@szy.cn
 * 作用：通知类型实体类 1--文本  2--图片  3--链接  4--投票
 */

public class InformType {
    //通知类型
    private int type;
    //通知标题
    private String title;
    //通知链接地址
    private String url = "1";
    //图片链接地址
    private String img_url = "1";
    //投票数标题0、选项、截止时间size()-2、投票类型size()-1
    private ArrayList<String> datas;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public ArrayList<String> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<String> datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return type + "," + title + "," + url + "," + img_url + "@";
    }
}
