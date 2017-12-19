package com.zhoukp.inform.activity;

import android.app.Activity;
import android.app.Dialog;
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
import com.zhoukp.inform.view.CommomDialog;

/**
 * 作者： zhoukp
 * 时间：2017/12/11 16:59
 * 邮箱：zhoukaiping@szy.cn
 * 作用：
 */

public class AddLinkActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_title, tv_next;
    private EditText et_link_title, et_link_address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlink);

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
        String title = intent.getStringExtra("title");
        String link = intent.getStringExtra("link");

        if (!title.equals("") && title != null) {
            et_link_title.setText(title);
        }

        if (!link.equals("") && link != null) {
            et_link_address.setText(link);
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
        tv_title.setText("链接设置");
        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_next.setText("确定");

        et_link_title = (EditText) findViewById(R.id.et_link_title);
        et_link_address = (EditText) findViewById(R.id.et_link_address);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                //关闭当前页面
                new CommomDialog(AddLinkActivity.this, R.style.dialog, "放弃编辑？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
                            AddLinkActivity.this.finish();
                        } else if (!confirm) {
                            dialog.dismiss();
                        }
                    }
                }).setPositiveButton("放弃").show();
                break;
            case R.id.tv_next:
                //点击确定，关闭当前页面并带回结果
                Intent intent = new Intent();
                intent.putExtra("title", et_link_title.getText().toString());
                intent.putExtra("link", et_link_address.getText().toString());
                LogUtil.e(et_link_title.getText().toString() + ":" + et_link_address.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }
}
