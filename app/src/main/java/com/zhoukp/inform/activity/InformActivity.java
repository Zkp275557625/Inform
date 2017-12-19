package com.zhoukp.inform.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhoukp.inform.R;
import com.zhoukp.inform.adapter.KPRecyclerViewAdapter;
import com.zhoukp.inform.adapter.OnItemClickListener;
import com.zhoukp.inform.bean.InformType;
import com.zhoukp.inform.callback.KPItemTouchHelperCallback;
import com.zhoukp.inform.callback.StartDragListener;
import com.zhoukp.inform.utils.CacheUtils;
import com.zhoukp.inform.utils.ImageUtils;
import com.zhoukp.inform.utils.LogUtil;
import com.zhoukp.inform.utils.PermissionUtils;
import com.zhoukp.inform.view.CommomDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： zhoukp
 * 时间：2017/12/11 9:57
 * 邮箱：zhoukaiping@szy.cn
 * 作用：通知页面
 */

public class InformActivity extends AppCompatActivity implements View.OnClickListener, StartDragListener {

    //调用系统相册-选择图片
    private static final int IMAGE = 1;
    //填写标题
    private static final int TITLE = 2;
    //添加文本模块
    private static final int ADDTEXT = 3;
    //添加图片模块
    private static final int ADDIMAGE = 4;
    //添加链接模块
    private static final int ADDLINK = 5;
    //编辑模块描述
    private static final int EDITTEXT = 6;
    //编辑链接模块的链接
    private static final int EDITLINK = 7;
    //添加投票模块
    private static final int ADDVOTE = 8;
    //编辑投票模块
    private static final int EDITVOTE = 9;

    //添加音乐的索引
    private static int MUSIC_INDEX = 1;

    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_next;
    private Button btn_add_music;
    private Button btn_add_cover;
    private ImageView iv_img;
    private TextView tv_info_title;
    private RecyclerView recyclerview;
    private ScrollView scrollview;
    public LinearLayout ll_add_vote;


    //recyclerview数据适配器
    private KPRecyclerViewAdapter adapter;
    //数据
    private ArrayList<InformType> datas;

    private View topView;

    private ItemTouchHelper itemTouchHelper;

    //封面图片的链接
    private String coverImagePath = "";
    //添加的音乐
    private String music = "";
    //添加的标题
    private String title = "";

    private List<LocalMedia> selectList = new ArrayList<>();

    String[] vote_data;
    private String voteData;
    private String voteImg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform);

        //初始化视图
        initViews();

        //初始化数据
        initData();

        //初始化事件
        initEvents();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        //设置recyclerview为垂直显示方式并禁止其滑动
        recyclerview.setLayoutManager(new LinearLayoutManager(InformActivity.this, LinearLayoutManager.VERTICAL, false));
        datas = new ArrayList<>();
        MUSIC_INDEX = 1;

        String cache = getIntent().getStringExtra("cache");

        if (cache.equals("yes")) {//缓存启动
            title = CacheUtils.getString(InformActivity.this, "title");
            if (title.equals("")) {
                InformType informType = new InformType();
                informType.setType(1);
                informType.setTitle("下班啦");
                datas.add(informType);
            } else {
                music = CacheUtils.getString(InformActivity.this, "music");
                coverImagePath = CacheUtils.getString(InformActivity.this, "coverimagepath");
                datas = CacheUtils.getArrayList(InformActivity.this, "datas");
                voteData = CacheUtils.getString(InformActivity.this, "vote_data");

                voteImg = CacheUtils.getString(InformActivity.this, "vote_img");
                String[] split = voteData.split(",");
                ArrayList<String> vote_data_arraylist = new ArrayList<>();
                for (int i = 1; i < split.length; i++) {
                    vote_data_arraylist.add(split[i]);
                }

                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).getType() == 4) {
                        datas.get(i).setTitle(split[0]);
                        datas.get(i).setImg_url(voteImg);
                        datas.get(i).setDatas(vote_data_arraylist);
                    }
                }
            }
        } else {//正常启动
            InformType informType = new InformType();
            informType.setType(1);
            informType.setTitle("下班啦");
            datas.add(informType);
        }

        adapter = new KPRecyclerViewAdapter(InformActivity.this, datas, InformActivity.this);

        recyclerview.setAdapter(adapter);

        setHeader(recyclerview);

        if (cache.equals("yes") && !title.equals("")) {
            Bitmap bitmap = BitmapFactory.decodeFile(coverImagePath);
            iv_img.setImageBitmap(bitmap);
            tv_info_title.setText(title);
            btn_add_music.setText(music);
            if (music.length() > 4) {
                MUSIC_INDEX = Integer.parseInt(music.substring(7)) + 1;
                LogUtil.e("MUSIC_INDEX==" + MUSIC_INDEX);
            } else {
                MUSIC_INDEX = 1;
                LogUtil.e("MUSIC_INDEX==" + MUSIC_INDEX);
            }
        }

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, InformType data, int position) {
                hideAddView();
                if (data.getType() == 4) {
                    //投票模块
                    Intent intent = new Intent(InformActivity.this, AddVoteActivity.class);
                    String[] vote_data = new String[data.getDatas().size() + 1];
                    for (int i = 0; i < vote_data.length; i++) {
                        if (i == 0) {
                            vote_data[i] = data.getTitle();
                        } else {
                            vote_data[i] = data.getDatas().get(i - 1);
                        }
                    }
                    intent.putExtra("vote_data", vote_data);
                    startActivityForResult(intent, EDITVOTE);
                }
            }
        });

        KPItemTouchHelperCallback callback = new KPItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerview);

        //设置recyclerview动画   默认动画
        recyclerview.setItemAnimator(new DefaultItemAnimator());
    }

    public void hideAddView() {
        for (int i = 0; i < datas.size(); i++) {
            RecyclerView.ViewHolder viewHolder = recyclerview.findViewHolderForLayoutPosition(i + 1);
            adapter.hideAddView((KPRecyclerViewAdapter.KPViewHolder) viewHolder);
        }
    }

    /**
     * 判断点击的坐标点在不在view的范围中
     * 返回true-->在view的范围中，返回false-->不在view的范围中
     *
     * @param view 当前view
     * @param ev   点击事件（MotionEvent）
     * @return
     */
    private boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                //不在recyclerview的范围中点击的时候隐藏添加模块的布局
                if (!inRangeOfView(recyclerview, event)) {
                    hideAddView();
                }
                //在topView的范围中点击的时候隐藏添加模块的布局
                if (inRangeOfView(topView, event)) {
                    hideAddView();
                    /**
                     * topView获取焦点
                     */
                    topView.setFocusable(true);
                    topView.setFocusableInTouchMode(true);
                    topView.requestFocus();
                    topView.requestFocusFromTouch();
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 给RecyclerView添加headerView
     *
     * @param view
     */
    private void setHeader(RecyclerView view) {
        topView = LayoutInflater.from(this).inflate(R.layout.topview, view, false);
        btn_add_music = (Button) topView.findViewById(R.id.btn_add_music);
        btn_add_cover = (Button) topView.findViewById(R.id.btn_add_cover);
        iv_img = (ImageView) topView.findViewById(R.id.iv_img);
        tv_info_title = (TextView) topView.findViewById(R.id.tv_info_title);

        btn_add_music.setOnClickListener(this);
        btn_add_cover.setOnClickListener(this);
        tv_info_title.setOnClickListener(this);

        adapter.setHeaderView(topView);
    }

    /**
     * 初始化事件
     */
    private void initEvents() {
        iv_back.setOnClickListener(this);
        ll_add_vote.setOnClickListener(this);

        /**
         * recyclerview滚动过程中清除焦点
         */
        recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                View currentFocus = getCurrentFocus();
                if (currentFocus != null && currentFocus != recyclerview) {
                    LogUtil.e("currentFocus==" + currentFocus);
                    currentFocus.clearFocus();
                }
            }
        });
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_next = (TextView) findViewById(R.id.tv_next);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        ll_add_vote = (LinearLayout) findViewById(R.id.ll_add_vote);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.iv_back:
                //点击返回按钮  对话框提示用户是否要真的退出本页面
                new CommomDialog(InformActivity.this, R.style.dialog, "是否将编辑的内容保存到附件箱？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
                            //保存app数据，使得下次进入app数据还存在，不需要重新编辑
                            if (PermissionUtils.isGrantExternalRW(InformActivity.this, 1)) {
                                saveData();
                            }
                        } else if (!confirm) {
                            dialog.dismiss();
                            if (PermissionUtils.isGrantExternalRW(InformActivity.this, 2)) {
                                //清除所有的缓存数据
                                CacheUtils.clearAllSp(InformActivity.this);
                            }
                            InformActivity.this.finish();
                        }
                    }
                }).show();
                break;
            case R.id.btn_add_music:
                //添加音乐
                LogUtil.e("MUSIC_INDEX==" + MUSIC_INDEX);
                btn_add_music.setText("让我们荡起双桨" + MUSIC_INDEX++);
                break;
            case R.id.btn_add_cover:
                //添加封面
                //调用系统相册并选择一张图片显示到界面上
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
                break;
            case R.id.tv_info_title:
                //设置通知的标题
                intent = new Intent(InformActivity.this, FillTitleActivity.class);
                intent.putExtra("title", tv_info_title.getText().toString());
                LogUtil.e("tv_info_title.text==" + tv_info_title.getText().toString());
                startActivityForResult(intent, TITLE);
                break;
            case R.id.ll_add_vote:
                intent = new Intent(InformActivity.this, AddVoteActivity.class);
                ll_add_vote.setVisibility(View.GONE);
                startActivityForResult(intent, ADDVOTE);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveData();
                }
                break;
            case 2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //清除所有的缓存数据
                    CacheUtils.clearAllSp(InformActivity.this);
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果用户进了添加投票页面之后一路狂按返回，会导致添加投票按钮隐藏不能添加投票
        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                if (datas.get(i).getType() == 4) {
                    ll_add_vote.setVisibility(View.GONE);
//                    //隐藏添加模块按钮
//                    KPRecyclerViewAdapter.KPViewHolder holder = (KPRecyclerViewAdapter.KPViewHolder) recyclerview.findViewHolderForLayoutPosition(i + 1);
//                    holder.iv_add_item.setVisibility(View.GONE);
                    break;
                }
            }
        }
    }

    /**
     * 缓存应用数据
     */
    private void saveData() {
        LogUtil.e("saveData");
        CacheUtils.putString(InformActivity.this, "title", tv_info_title.getText().toString());
        CacheUtils.putString(InformActivity.this, "music", btn_add_music.getText().toString());
        CacheUtils.putString(InformActivity.this, "coverimagepath", coverImagePath);
        CacheUtils.putArrayList(InformActivity.this, "datas", datas);

        String vote_data_string = "";
        if (vote_data != null) {
            for (int i = 0; i < vote_data.length; i++) {
                vote_data_string += vote_data[i] + ",";
            }
        }

        CacheUtils.putString(InformActivity.this, "vote_data", vote_data_string);
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getType() == 4) {
                CacheUtils.putString(InformActivity.this, "vote_img", datas.get(i).getImg_url());
                break;
            }
        }
        InformActivity.this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage = null;
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            int columnIndex = 0;
            InformType informType = null;
            KPRecyclerViewAdapter.KPViewHolder viewHolder = null;
            int position = 0;
            switch (requestCode) {
                case IMAGE:
                    //headerView设置图片回调
                    selectedImage = data.getData();
                    cursor = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                    cursor.moveToFirst();
                    columnIndex = cursor.getColumnIndex(filePathColumns[0]);
                    coverImagePath = cursor.getString(columnIndex);
                    Bitmap bitmap = BitmapFactory.decodeFile(coverImagePath);
                    iv_img.setImageBitmap(bitmap);
                    cursor.close();
                    break;
                case TITLE:
                    //headerView设置标题回调
                    LogUtil.e("title==" + data.getStringExtra("title"));
                    tv_info_title.setText(data.getStringExtra("title"));
                    break;
                case ADDTEXT:
                    //recyclerview添加文本模块回调
                    LogUtil.e("text==" + data.getStringExtra("text"));
                    informType = new InformType();
                    informType.setType(1);
                    informType.setTitle(data.getStringExtra("text"));
                    adapter.addNewData(datas.size(), informType);
                    recyclerview.scrollToPosition(datas.size() + 1);
                    break;
                case ADDIMAGE:
                    //recyclerview添加图片模块回调
                    selectedImage = data.getData();
                    cursor = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                    cursor.moveToFirst();
                    columnIndex = cursor.getColumnIndex(filePathColumns[0]);
                    String imagePath = cursor.getString(columnIndex);
                    cursor.close();
                    position = Integer.parseInt(CacheUtils.getString(InformActivity.this, "iv_img_position"));
                    LogUtil.e("position==" + position);
                    viewHolder = (KPRecyclerViewAdapter.KPViewHolder) recyclerview.findViewHolderForLayoutPosition(position);
                    bitmap = ImageUtils.getImageThumbnail(InformActivity.this, getContentResolver(), imagePath);
                    viewHolder.iv_img.setImageBitmap(bitmap);
                    datas.get(position - 1).setImg_url(imagePath);
                    LogUtil.e("Img_url==" + datas.get(position - 1).getImg_url());
                    recyclerview.scrollToPosition(position);
                    break;
                case ADDLINK:
                    //recyclerview添加链接模块回调
                    LogUtil.e(data.getStringExtra("title") + ":" + data.getStringExtra("link"));
                    informType = new InformType();
                    informType.setType(3);
                    informType.setTitle(data.getStringExtra("title"));
                    informType.setUrl(data.getStringExtra("link"));
                    adapter.addNewData(datas.size(), informType);
                    recyclerview.scrollToPosition(datas.size() + 1);
                    break;
                case PictureConfig.CHOOSE_REQUEST:
                    //九宫格选择图片回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectList) {
                        LogUtil.e("imgPath==" + media.getPath());
                        informType = new InformType();
                        informType.setType(2);
                        informType.setImg_url(media.getPath());
                        informType.setTitle("添加文字描述");
                        adapter.addNewData(datas.size(), informType);
                    }
                    recyclerview.scrollToPosition(datas.size() + 1);
                    break;
                case EDITTEXT:
                    //编辑模块标题回调
                    String des = data.getStringExtra("title");
                    position = Integer.parseInt(data.getStringExtra("position"));
                    LogUtil.e(position + "," + des);
                    viewHolder = (KPRecyclerViewAdapter.KPViewHolder) recyclerview.findViewHolderForLayoutPosition(position);
                    viewHolder.tv_text.setText(des);
                    datas.get(position - 1).setTitle(des);
                    recyclerview.scrollToPosition(position);
                    break;
                case EDITLINK:
                    //编辑链接模块链接回调
                    String link = data.getStringExtra("title");
                    position = Integer.parseInt(data.getStringExtra("position"));
                    LogUtil.e(position + "," + link);
                    viewHolder = (KPRecyclerViewAdapter.KPViewHolder) recyclerview.findViewHolderForLayoutPosition(position);
                    viewHolder.tv_link.setText(link);
                    datas.get(position - 1).setTitle(link);
                    recyclerview.scrollToPosition(position);
                    break;
                case ADDVOTE:
                    //编辑投票模块回调
                    vote_data = data.getStringArrayExtra("vote_data");
                    if (vote_data != null && vote_data.length > 3) {
                        informType = new InformType();
                        informType.setType(4);
                        ArrayList<String> vote_data_arraylist = new ArrayList<>();
                        for (int i = 1; i < vote_data.length; i++) {
                            vote_data_arraylist.add(vote_data[i]);
                        }
                        informType.setDatas(vote_data_arraylist);
                        informType.setTitle(vote_data[0]);
                        adapter.addNewData(datas.size(), informType);
                    } else {
                        //进入添加投票页面直接点击返回键就不费带回返回值
                        ll_add_vote.setVisibility(View.VISIBLE);
                    }
                    break;
                case EDITVOTE:
                    vote_data = data.getStringArrayExtra("vote_data");
                    if (vote_data != null && vote_data.length > 3) {
                        for (int i = 0; i < datas.size(); i++) {
                            if (datas.get(i).getType() == 4) {
                                ArrayList<String> arraylist = new ArrayList<>();
                                for (int j = 1; j < vote_data.length; j++) {
                                    arraylist.add(vote_data[j]);
                                }
                                datas.get(i).setDatas(arraylist);
                                datas.get(i).setTitle(vote_data[0]);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}