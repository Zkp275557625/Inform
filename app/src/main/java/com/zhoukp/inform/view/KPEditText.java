package com.zhoukp.inform.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.zhoukp.inform.R;

/**
 * 作者： zhoukp
 * 时间：2017/12/15 16:15
 * 邮箱：zhoukaiping@szy.cn
 * 作用：
 */

public class KPEditText extends RelativeLayout {

    public ImageButton ib_delete, ib_hide_keyboard;
    public EditText et_text;

    //当输入框状态改变时，会调用相应的方法
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        //在文字改变后调用
        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0) {
                hideBtn();// 隐藏按钮
            } else {
                showBtn();// 显示按钮
            }
        }
    };

    //设置按钮不可见
    public void hideBtn() {
        if (ib_delete.isShown()) {
            ib_delete.setVisibility(View.GONE);
        }
    }

    //设置按钮可见
    public void showBtn() {
        if (!ib_delete.isShown()) {
            ib_delete.setVisibility(View.VISIBLE);
        }
    }

    //隐藏键盘
    public void hideKeyBoard(Context context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public KPEditText(Context context) {
        this(context, null);
    }

    public KPEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KPEditText(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.edit_text, this, true);

        init();

//        et_text.addTextChangedListener(textWatcher);//为输入框绑定一个监听文字变化的监听器

        ib_hide_keyboard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard(context);
            }
        });
    }

    private void init() {
        ib_delete = (ImageButton) findViewById(R.id.ib_delete);
        ib_hide_keyboard = (ImageButton) findViewById(R.id.ib_hide_keyboard);
        et_text = (EditText) findViewById(R.id.et_text);
    }
}
