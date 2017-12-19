package com.zhoukp.inform.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhoukp.inform.R;
import com.zhoukp.inform.utils.LogUtil;
import com.zhoukp.inform.view.CommomDialog;

/**
 * 作者： zhoukp
 * 时间：2017/12/11 16:45
 * 邮箱：zhoukaiping@szy.cn
 * 作用：
 */

public class AddTextActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_title, tv_next;
    private EditText et_info_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtext);

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
        String text = intent.getStringExtra("text");
        //防止键值为空
        if (TextUtils.isEmpty(text)) {
            et_info_title.setText(text);
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
        tv_title.setText("文本");
        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_next.setText("下一步");

        et_info_title = (EditText) findViewById(R.id.et_info_title);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                //关闭当前页面
                new CommomDialog(AddTextActivity.this, R.style.dialog, "放弃编辑？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            // Toast.makeText(AddTextActivity.this, "放弃", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            AddTextActivity.this.finish();
                        } else if (!confirm) {
                            // Toast.makeText(AddTextActivity.this, "取消", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                }).setPositiveButton("放弃").show();
                break;
            case R.id.tv_next:
                //点击确定，关闭当前页面并带回结果
                Intent intent = new Intent();
                intent.putExtra("text", et_info_title.getText().toString());
                LogUtil.e(et_info_title.getText().toString());
//                finish();
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }
}
