package com.zhoukp.inform.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhoukp.inform.R;
import com.zhoukp.inform.bean.Time;
import com.zhoukp.inform.utils.LogUtil;
import com.zhoukp.inform.view.CommomDialog;
import com.zhoukp.inform.view.datepick.DatePicker;
import com.zhoukp.inform.view.timepick.TimePicker;

import java.util.Calendar;

/**
 * 作者： KaiPingZhou
 * 时间：2017/12/17 21:20
 * 邮箱：275557625@qq.com
 * 作用：选择日期时间页面
 */
public class SelectTimeActivity extends Activity implements View.OnClickListener {

    private TextView tv_cancel, tv_submit;
    private DatePicker date_picker;
    private TimePicker time_picker;

    private Calendar calendar;

    private Time time;

    //日期选择器监听
    DatePicker.OnChangeListener dp_onchanghelistener = new DatePicker.OnChangeListener() {
        @Override
        public void onChange(int year, int month, int day, int day_of_week) {
            time.setYear(year);
            time.setMonth(month);
            time.setDay(day);
        }
    };

    //时间选择器监听
    TimePicker.OnChangeListener tp_onchanghelistener = new TimePicker.OnChangeListener() {
        @Override
        public void onChange(int hour, int minute) {
            time.setHour(hour);
            time.setMinute(minute);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        initViews();

        initData();

        initEvent();
    }

    private void initViews() {
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        date_picker = (DatePicker) findViewById(R.id.date_picker);
        time_picker = (TimePicker) findViewById(R.id.time_picker);

    }

    private void initData() {
        calendar = Calendar.getInstance();

        time = new Time();
        time.setYear(calendar.get(Calendar.YEAR));
        time.setMonth(calendar.get(Calendar.MONTH) + 1);
        time.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        time.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        time.setMinute(calendar.get(Calendar.MINUTE));
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    private Time getCurrentTime() {
        Time time = new Time();
        time.setYear(calendar.get(Calendar.YEAR));
        time.setMonth(calendar.get(Calendar.MONTH) + 1);
        time.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        time.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        time.setMinute(calendar.get(Calendar.MINUTE));
        return time;
    }

    /**
     * 判断t1是否迟于t2  是 返回true   否 返回false
     *
     * @param time1
     * @param time2
     * @return
     */
    private boolean laterTime(Time time1, Time time2) {
        if (time1.getHour() > time2.getHour()) {
            return true;
        } else if (time1.getHour() == time2.getHour()) {
            if (time1.getMinute() > time2.getMinute()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    private void initEvent() {
        tv_cancel.setOnClickListener(this);
        tv_submit.setOnClickListener(this);

        //设置滑动改变监听器
        date_picker.setOnChangeListener(dp_onchanghelistener);
        time_picker.setOnChangeListener(tp_onchanghelistener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                new CommomDialog(SelectTimeActivity.this, R.style.dialog, "放弃编辑？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
                            SelectTimeActivity.this.finish();
                        } else if (!confirm) {
                            dialog.dismiss();
                        }
                    }
                }).setPositiveButton("放弃").show();
                break;
            case R.id.tv_submit:
                Time currentTime = getCurrentTime();
                LogUtil.e("你选择的时间是:" + time.toString());
                LogUtil.e("当前时间是:" + currentTime.toString());
                if (!laterTime(time, currentTime)) {
                    Toast.makeText(SelectTimeActivity.this, "您选择的时间不能早于当前时间", Toast.LENGTH_SHORT).show();
                } else {
                    //点击确定，关闭当前页面并带回结果
                    Intent intent = new Intent();
                    intent.putExtra("date_time", time.toString());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }
}
