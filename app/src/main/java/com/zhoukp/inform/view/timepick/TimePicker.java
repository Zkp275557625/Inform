package com.zhoukp.inform.view.timepick;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.zhoukp.inform.utils.DensityUtil;
import com.zhoukp.inform.view.wheelview.OnWheelChangedListener;
import com.zhoukp.inform.view.wheelview.StringWheelAdapter;
import com.zhoukp.inform.view.wheelview.WheelView;
import com.zhoukp.inform.view.datepick.DateObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 作者： KaiPingZhou
 * 时间：2017/12/17 21:21
 * 邮箱：275557625@qq.com
 * 作用：自定义时间选择器
 */
public class TimePicker extends LinearLayout {

    private Calendar calendar = Calendar.getInstance();
    private WheelView hours, mins; //Wheel picker
    private OnChangeListener onChangeListener; //onChangeListener
    private final int MARGIN_RIGHT = 0;        //调整文字右端距离
    private ArrayList<DateObject> hourList, minuteList;
    private DateObject dateObject;        //时间数据对象

    //Constructors
    public TimePicker(Context context) {
        this(context, null);
        ;
    }

    public TimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        hourList = new ArrayList<>();
        minuteList = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            dateObject = new DateObject(hour + i, -1, true);
            hourList.add(dateObject);
        }

        for (int j = 0; j < 60; j++) {
            dateObject = new DateObject(-1, minute + j, false);
            minuteList.add(dateObject);
        }

        //小时选择器
        hours = new WheelView(context);
        LinearLayout.LayoutParams lparams_hours =
                new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 80), LayoutParams.MATCH_PARENT);

        lparams_hours.setMargins(0, 0, MARGIN_RIGHT, 0);
        hours.setLayoutParams(lparams_hours);
        hours.setAdapter(new StringWheelAdapter(hourList, 24));
        hours.setVisibleItems(5);
        hours.setCyclic(true);
        hours.addChangingListener(onHoursChangedListener);
        addView(hours);

        //分钟选择器
        mins = new WheelView(context);
        mins.setLayoutParams(new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 80), LayoutParams.MATCH_PARENT));
        mins.setAdapter(new StringWheelAdapter(minuteList, 60));
        mins.setVisibleItems(5);
        mins.setCyclic(true);
        mins.addChangingListener(onMinsChangedListener);
        addView(mins);
    }


    //listeners
    private OnWheelChangedListener onHoursChangedListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView hours, int oldValue, int newValue) {
            calendar.set(Calendar.HOUR_OF_DAY, newValue);
            change();
        }
    };
    private OnWheelChangedListener onMinsChangedListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView mins, int oldValue, int newValue) {
            calendar.set(Calendar.MINUTE, newValue);
            change();
        }
    };

    /**
     * 滑动改变监听器回调的接口
     */
    public interface OnChangeListener {
        void onChange(int hour, int munite);
    }

    /**
     * 设置滑动改变监听器
     *
     * @param onChangeListener
     */
    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    /**
     * 滑动最终调用的方法
     */
    private void change() {
        if (onChangeListener != null) {
            onChangeListener.onChange(getHourOfDay(), getMinute());
        }
    }


    /**
     * 获取小时
     *
     * @return
     */
    public int getHourOfDay() {
        return hourList.get(hours.getCurrentItem()).getHour();
    }

    /**
     * 获取分钟
     *
     * @return
     */
    public int getMinute() {
        return minuteList.get(mins.getCurrentItem()).getMinute();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}