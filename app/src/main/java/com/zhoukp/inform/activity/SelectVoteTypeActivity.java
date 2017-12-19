package com.zhoukp.inform.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zhoukp.inform.R;
import com.zhoukp.inform.utils.LogUtil;
import com.zhoukp.inform.view.CommomDialog;
import com.zhoukp.inform.view.PickerScrollView;
import com.zhoukp.inform.view.Pickers;

import java.util.ArrayList;

/**
 * 作者： KaiPingZhou
 * 时间：2017/12/16 22:20
 * 邮箱：275557625@qq.com
 * 作用：选择投票类型页面
 */
public class SelectVoteTypeActivity extends Activity implements View.OnClickListener {

    private PickerScrollView pickerscrollview;
    private TextView tv_cancel, tv_submit;

    private int choices;
    private ArrayList<Pickers> datas; //滚动选择器数据
    private String[] id;
    private String[] type;

    private String choicesType = "单选";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_votetype);

        initViews();

        initData();

        initEvent();
    }

    private void initData() {
        //获取投票选项数量
        choices = Integer.parseInt(getIntent().getStringExtra("choices"));
        datas = new ArrayList<>();
        id = new String[choices];
        type = new String[choices];
        for (int i = 0; i < choices; i++) {
            if (i == 0) {
                type[i] = "单选";
            } else if (i == 1) {
                type[i] = "多选（无限制）";
            } else if (i >= 2) {
                type[i] = "多选，最多" + i + "项";
            }
            id[i] = i + 1 + "";
            datas.add(new Pickers(type[i], id[i]));
        }
        //设置数据，默认选择第一条
        pickerscrollview.setData(datas);
        pickerscrollview.setSelected(0);

        pickerscrollview.setOnSelectListener(new PickerScrollView.onSelectListener() {
            @Override
            public void onSelect(Pickers pickers) {
                LogUtil.e("选择：" + pickers.getShowId() + "--" + pickers.getShowConetnt());
                choicesType = pickers.getShowConetnt();
            }
        });
    }

    private void initViews() {
        pickerscrollview = (PickerScrollView) findViewById(R.id.pickerscrollview);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
    }

    private void initEvent() {
        tv_cancel.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                new CommomDialog(SelectVoteTypeActivity.this, R.style.dialog, "放弃编辑？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
                            SelectVoteTypeActivity.this.finish();
                        } else if (!confirm) {
                            dialog.dismiss();
                        }
                    }
                }).setPositiveButton("放弃").show();
                break;
            case R.id.tv_submit:
                //点击确定，关闭当前页面并带回结果
                Intent intent = new Intent();
                intent.putExtra("choicestype", choicesType);
                LogUtil.e("choicestype==" + choicesType);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }
}
