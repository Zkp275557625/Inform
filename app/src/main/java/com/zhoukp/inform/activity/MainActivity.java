package com.zhoukp.inform.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zhoukp.inform.R;

/**
 * 作者： zhoukp
 * 时间：2017/12/11 9:57
 * 邮箱：zhoukaiping@szy.cn
 * 作用：主页面
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_start1;
    private Button btn_start2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化视图
        initViews();
        //初始化事件
        initEvent();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        btn_start1.setOnClickListener(this);
        btn_start2.setOnClickListener(this);
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        btn_start1 = (Button) findViewById(R.id.btn_start1);
        btn_start2 = (Button) findViewById(R.id.btn_start2);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_start1:
                intent = new Intent(MainActivity.this, InformActivity.class);
                intent.putExtra("cache", "no");
                startActivity(intent);
                break;
            case R.id.btn_start2:
//                Toast.makeText(MainActivity.this, btn_start2.getText().toString(), Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this, InformActivity.class);
                intent.putExtra("cache", "yes");
                startActivity(intent);
                break;
        }
    }
}
