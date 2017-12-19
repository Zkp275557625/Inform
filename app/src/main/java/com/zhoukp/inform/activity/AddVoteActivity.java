package com.zhoukp.inform.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhoukp.inform.R;
import com.zhoukp.inform.utils.CacheUtils;
import com.zhoukp.inform.utils.LogUtil;
import com.zhoukp.inform.utils.PermissionUtils;
import com.zhoukp.inform.view.CommomDialog;
import com.zhoukp.inform.view.KPEditText;

import java.util.ArrayList;

/**
 * 作者： zhoukp
 * 时间：2017/12/15 15:22
 * 邮箱：zhoukaiping@szy.cn
 * 作用：添加投票页面
 */

public class AddVoteActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_title, tv_next;
    private LinearLayout ll_vote_choices;
    private TextView tv_add_choice, tv_finall_time, tv_vote_type;
    private EditText et_title;

    private ArrayList<KPEditText> editTexts;
    private String[] vote_data;

    //选项索引
    private int choice_index = 1;
    //动画是否播放完毕  默认完毕(不播放)
    private boolean isTransitionFinish = true;

    private WindowManager manager;
    private int screenWidth;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvote);

        //获取屏幕宽高
        manager = this.getWindowManager();
        screenWidth = manager.getDefaultDisplay().getWidth();

        initViews();

        initData();

        initEvent();
    }

    private void initData() {
        String voteData = CacheUtils.getString(AddVoteActivity.this, "vote_data");
        if (!TextUtils.isEmpty(voteData)) {//从缓存数据恢复页面
            //移除之前添加的所有的view
            ll_vote_choices.removeAllViews();
            //清空数据
            editTexts.clear();

            String[] split = voteData.split(",");
            for (int i = 0; i < split.length; i++) {
                if (i == 0) {
                    et_title.setText(split[i]);
                    et_title.setHint("请输入投票主题");
                } else if (i == split.length - 2) {
                    tv_finall_time.setText(split[i]);
                } else if (i == split.length - 1) {
                    tv_vote_type.setText(split[i]);
                } else {
                    LogUtil.e("i==" + i);
                    final KPEditText editText = new KPEditText(AddVoteActivity.this);
                    editText.et_text.setHint("选项" + i);
                    editText.et_text.setText(split[i]);
                    choice_index = i;
                    if (choice_index > 2) {
                        editText.ib_delete.setVisibility(View.VISIBLE);
                        editText.ib_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for (int j = ll_vote_choices.indexOfChild(editText); j < editTexts.size(); j++) {
                                    int new_index = Integer.parseInt(editTexts.get(j).et_text.getHint().toString().substring(2)) - 1;
                                    LogUtil.e("new_index==" + new_index);
                                    editTexts.get(j).et_text.setHint("选项" + new_index);
                                }
                                isTransitionFinish = false;
                                editTexts.remove(editText);
                                ll_vote_choices.removeView(editText);

                                choice_index--;
                            }
                        });
                    }
                    editTexts.add(editText);
                    ll_vote_choices.addView(editText);
                    choice_index++;
                }
            }
        } else {
            //接收上级页面传递过来的数据
            String[] vote_data = getIntent().getStringArrayExtra("vote_data");
            if (vote_data != null && vote_data.length > 3) {
                ll_vote_choices.removeAllViews();
                editTexts.clear();
                for (int i = 0; i < vote_data.length; i++) {
                    if (i == 0) {
                        et_title.setText(vote_data[i]);
                    } else if (i == vote_data.length - 2) {
                        tv_finall_time.setText(vote_data[i]);
                    } else if (i == vote_data.length - 1) {
                        tv_vote_type.setText(vote_data[i]);
                    } else {
                        LogUtil.e("i==" + i);
                        final KPEditText editText = new KPEditText(AddVoteActivity.this);
                        editText.et_text.setHint("选项" + i);
                        editText.et_text.setText(vote_data[i]);
                        choice_index = i;
                        if (choice_index > 2) {
                            editText.ib_delete.setVisibility(View.VISIBLE);
                            editText.ib_delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    for (int j = ll_vote_choices.indexOfChild(editText); j < editTexts.size(); j++) {
                                        int new_index = Integer.parseInt(editTexts.get(j).et_text.getHint().toString().substring(2)) - 1;
                                        LogUtil.e("new_index==" + new_index);
                                        editTexts.get(j).et_text.setHint("选项" + new_index);
                                    }
                                    isTransitionFinish = false;
                                    editTexts.remove(editText);
                                    ll_vote_choices.removeView(editText);

                                    choice_index--;
                                }
                            });
                        }
                        editTexts.add(editText);
                        ll_vote_choices.addView(editText);
                        choice_index++;
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_title.setText("投票设置");
        tv_next.setText("完成");

        ll_vote_choices = (LinearLayout) findViewById(R.id.ll_vote_choices);
        tv_add_choice = (TextView) findViewById(R.id.tv_add_choice);
        tv_finall_time = (TextView) findViewById(R.id.tv_finall_time);
        tv_vote_type = (TextView) findViewById(R.id.tv_vote_type);

        et_title = (EditText) findViewById(R.id.et_title);

        editTexts = new ArrayList<>();

        for (choice_index = 1; choice_index < 3; ) {
            KPEditText editText = new KPEditText(AddVoteActivity.this);
            editText.et_text.setHint("选项" + choice_index);
            editText.ib_delete.setVisibility(View.GONE);
            choice_index++;
            editTexts.add(editText);
            ll_vote_choices.addView(editText);
        }

        LayoutTransition transition = new LayoutTransition();
        transition.setAnimator(LayoutTransition.APPEARING, getAppearingAnimation());
        transition.setDuration(LayoutTransition.APPEARING, 500);
        transition.setStartDelay(LayoutTransition.APPEARING, 0);

        transition.setAnimator(LayoutTransition.DISAPPEARING, getDisappearingAnimation());
        transition.setDuration(LayoutTransition.DISAPPEARING, 500);

        transition.addTransitionListener(new LayoutTransition.TransitionListener() {
            @Override
            public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                LogUtil.e("startTransition");
            }

            @Override
            public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                LogUtil.e("endTransition");
                isTransitionFinish = true;
            }
        });

        ll_vote_choices.setLayoutTransition(transition);
    }

    private Animator getAppearingAnimation() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(null, "translationX", screenWidth, 0.0f, 0.0f));
        return animatorSet;
    }

    private Animator getDisappearingAnimation() {
        AnimatorSet mSet = new AnimatorSet();
        mSet.playTogether(ObjectAnimator.ofFloat(null, "translationX", 0.0f, screenWidth, screenWidth));
        return mSet;
    }

//    /**
//     * 获取动画
//     *
//     * @param isOut
//     * @return
//     */
//    private TranslateAnimation getAnimate(boolean isOut) {
//        TranslateAnimation animation = new TranslateAnimation(
//                Animation.RELATIVE_TO_SELF, isOut ? 1 : 0,
//                Animation.RELATIVE_TO_SELF, isOut ? 0 : 1,
//                Animation.RELATIVE_TO_SELF, 0,
//                Animation.RELATIVE_TO_SELF, 0
//        );
//        animation.setDuration(500);
//        return animation;
//    }

    private void initEvent() {
        iv_back.setOnClickListener(this);
        tv_add_choice.setOnClickListener(this);
        tv_vote_type.setOnClickListener(this);
        tv_finall_time.setOnClickListener(this);
        tv_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                new CommomDialog(AddVoteActivity.this, R.style.dialog, "是否保存编辑的内容？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
                            //保存app数据，使得下次进入app数据还存在，不需要重新编辑
                            if (PermissionUtils.isGrantExternalRW(AddVoteActivity.this, 1)) {
                                saveData();
                            }
                            AddVoteActivity.this.finish();
                        } else if (!confirm) {
                            dialog.dismiss();
                            //删除之前缓存的SharedPreferences数据
                            CacheUtils.clearSp(AddVoteActivity.this, "vote_data");
                            Intent intent = new Intent();
                            intent.putExtra("vote_data", "");
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    }
                }).setPositiveButton("保存").show();
                break;
            case R.id.tv_add_choice:
                //添加选项
                final KPEditText editText = new KPEditText(AddVoteActivity.this);
                editText.et_text.setHint("选项" + choice_index);
                editText.ib_delete.setVisibility(View.VISIBLE);
                editText.ib_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i = ll_vote_choices.indexOfChild(editText); i < editTexts.size(); i++) {
                            int new_index = Integer.parseInt(editTexts.get(i).et_text.getHint().toString().substring(2)) - 1;
                            LogUtil.e("new_index==" + new_index);
                            editTexts.get(i).et_text.setHint("选项" + new_index);
                        }
                        editTexts.remove(editText);
                        ll_vote_choices.removeView(editText);
                        choice_index--;
                        isTransitionFinish = false;
                    }
                });
                isTransitionFinish = false;
                editTexts.add(editText);
                ll_vote_choices.addView(editText);
                choice_index++;
                break;
            case R.id.tv_vote_type:
                intent = new Intent(AddVoteActivity.this, SelectVoteTypeActivity.class);
                intent.putExtra("choices", editTexts.size() + "");
                startActivityForResult(intent, 1);
                break;
            case R.id.tv_finall_time:
                intent = new Intent(AddVoteActivity.this, SelectTimeActivity.class);
                startActivityForResult(intent, 2);
                break;
            case R.id.tv_next:
                //关闭当前页面并带回结果

                //判断用户当前有没有填写数据
                if (!TextUtils.isEmpty(et_title.getText().toString())
                        && !TextUtils.isEmpty(editTexts.get(0).et_text.getText().toString())
                        && !TextUtils.isEmpty(editTexts.get(1).et_text.getText().toString())) {

                    backToInform();

                } else {
                    //弹出对话框询问是否真的保存当前数据
                    new CommomDialog(AddVoteActivity.this, R.style.dialog, "确定不再编辑了？", new CommomDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if (confirm) {
                                dialog.dismiss();
                                backToInform();
                            } else if (!confirm) {
                                dialog.dismiss();
                            }
                        }
                    }).setPositiveButton("确定").show();
                }
                break;
        }
    }

    private void backToInform() {
        Intent intent;
        intent = new Intent();
        //生成投票结果
        vote_data = new String[editTexts.size() + 3];
        String vote_data_string = "";
        for (int i = 0; i < vote_data.length; i++) {
            if (i == 0) {
                vote_data[i] = et_title.getText().toString();
            } else if (i == vote_data.length - 2) {
                vote_data[i] = tv_finall_time.getText().toString();
            } else if (i == vote_data.length - 1) {
                vote_data[i] = tv_vote_type.getText().toString();
            } else {
                vote_data[i] = editTexts.get(i - 1).et_text.getText().toString();
            }
            vote_data_string += vote_data[i] + ",";
        }

        //保存app数据，使得下次进入app数据还存在，不需要重新编辑
        if (PermissionUtils.isGrantExternalRW(AddVoteActivity.this, 1)) {
            saveData();
        }
        LogUtil.e("vote_data==" + vote_data_string);
        intent.putExtra("vote_data", vote_data);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    //保存数据
    private void saveData() {
        vote_data = new String[editTexts.size() + 3];
        String vote_data_string = "";
        for (int i = 0; i < vote_data.length; i++) {
            if (i == 0) {
                vote_data[i] = et_title.getText().toString();
            } else if (i == vote_data.length - 2) {
                vote_data[i] = tv_finall_time.getText().toString();
            } else if (i == vote_data.length - 1) {
                vote_data[i] = tv_vote_type.getText().toString();
            } else {
                vote_data[i] = editTexts.get(i - 1).et_text.getText().toString();
            }
            vote_data_string += vote_data[i] + ",";
        }
        LogUtil.e("vote_data==" + vote_data_string);
        CacheUtils.putString(AddVoteActivity.this, "vote_data", vote_data_string);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveData();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddVoteActivity.this, "您的手机暂不适配哦~", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 1:
                    //选择投票类型回调
                    String choicestype = data.getStringExtra("choicestype");
                    tv_vote_type.setText(choicestype);
                    break;
                case 2:
                    String date_time = data.getStringExtra("date_time");
                    tv_finall_time.setText(date_time);
                    break;
            }
        }
    }
}