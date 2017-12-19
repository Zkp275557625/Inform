package com.zhoukp.inform.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhoukp.inform.R;
import com.zhoukp.inform.utils.LogUtil;

/**
 * 作者： zhoukp
 * 时间：2017/12/11 13:51
 * 邮箱：zhoukaiping@szy.cn
 * 作用：填写标题页面
 */

public class FillTitleActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_title, tv_next;
    private EditText et_info_title;

    private String title;
    private String position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filltitle);

        //初始化视图
        initViews();

        //初始化数据
        initData();

        //初始化事件
        initEvent();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        position = intent.getStringExtra("position");
        LogUtil.e("position==" + position);
        //防止键值为空
        if (!title.equals("") && title != null) {
            et_info_title.setText(title);
        }
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        iv_back.setOnClickListener(this);
        tv_next.setOnClickListener(this);
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("编辑标题");
        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_next.setText("确定");

        et_info_title = (EditText) findViewById(R.id.et_info_title);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                //关闭当前页面
                finish();
                break;
            case R.id.tv_next:
                //点击确定，关闭当前页面并带回结果
                Intent intent = new Intent();
                intent.putExtra("title", et_info_title.getText().toString());
                if (position != null && !position.equals("")) {
                    intent.putExtra("position", position);
                }
                LogUtil.e(et_info_title.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }
}
