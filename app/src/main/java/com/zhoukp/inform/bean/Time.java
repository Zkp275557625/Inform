package com.zhoukp.inform.bean;

/**
 * 作者： zhoukp
 * 时间：2017/12/18 17:05
 * 邮箱：zhoukaiping@szy.cn
 * 作用：时间类
 */

public class Time {
    //年份
    private int year;
    //月份
    private int month;
    //时间
    private int day;

    //小时
    private int hour;
    //分钟
    private int minute;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        return year + "-" + month + "-" + day + " " + hour + ":" + minute;
    }
}
