package com.zhoukp.inform.adapter;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhoukp.inform.R;
import com.zhoukp.inform.activity.AddLinkActivity;
import com.zhoukp.inform.activity.AddTextActivity;
import com.zhoukp.inform.activity.FillTitleActivity;
import com.zhoukp.inform.activity.InformActivity;
import com.zhoukp.inform.bean.InformType;
import com.zhoukp.inform.callback.ItemTouchMoveListener;
import com.zhoukp.inform.callback.StartDragListener;
import com.zhoukp.inform.utils.CacheUtils;
import com.zhoukp.inform.utils.LogUtil;
import com.zhoukp.inform.view.CommomDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.zhoukp.inform.utils.ImageUtils.getImageThumbnail;

/**
 * 作者： zhoukp
 * 时间：2017/12/11 14:43
 * 邮箱：zhoukaiping@szy.cn
 * 作用：
 */

public class KPRecyclerViewAdapter extends RecyclerView.Adapter<KPRecyclerViewAdapter.KPViewHolder> implements ItemTouchMoveListener {

    //头部布局
    public static final int TYPE_HEADER = 0;
    //其他布局
    public static final int TYPE_NORMAL = 1;

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

    private Context context;
    private ArrayList<InformType> datas;
    private StartDragListener dragListener;

    private OnItemClickListener onItemClickListener;

    //头部布局
    private View headerView;

    private List<LocalMedia> selectList = new ArrayList<>();

    /**
     * 设置头部布局
     *
     * @param headerView
     */
    public void setHeaderView(View headerView) {
        this.headerView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return headerView;
    }

    @Override
    public int getItemViewType(int position) {
        if (headerView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public KPRecyclerViewAdapter(Context context, ArrayList<InformType> datas, StartDragListener dragListener) {
        this.context = context;
        this.datas = datas;
        this.dragListener = dragListener;
    }

    @Override
    public KPViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headerView != null && viewType == TYPE_HEADER) {
            return new KPViewHolder(headerView);
        }

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);

        return new KPViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(KPRecyclerViewAdapter.KPViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;
        //设置数据
        InformType informType = datas.get(getRealPosition(position));
        Bitmap bitmap = null;
        ContentResolver contentResolver = null;
        switch (informType.getType()) {
            case 1:
                //文本模块
                if (!informType.getImg_url().equals("1")) {
                    contentResolver = context.getContentResolver();
                    //获取缩略图以获得更好的性能
                    bitmap = getImageThumbnail(context, contentResolver, informType.getImg_url());
                    LogUtil.e("bitmap.getHeight()==" + bitmap.getHeight());
                    holder.iv_img.setImageBitmap(bitmap);
                } else {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.editor_type_text);
                    holder.iv_img.setImageBitmap(bitmap);
                }
                break;
            case 2:
                //照片模块
                contentResolver = context.getContentResolver();
                //获取缩略图以获得更好的性能
                bitmap = getImageThumbnail(context, contentResolver, informType.getImg_url());
                LogUtil.e("bitmap.getHeight()==" + bitmap.getHeight());
                holder.iv_img.setImageBitmap(bitmap);
                break;
            case 3:
                //链接模块
                if (!informType.getImg_url().equals("1")) {
                    contentResolver = context.getContentResolver();
                    //获取缩略图以获得更好的性能
                    bitmap = getImageThumbnail(context, contentResolver, informType.getImg_url());
                    LogUtil.e("bitmap.getHeight()==" + bitmap.getHeight());
                    holder.iv_img.setImageBitmap(bitmap);
                } else {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.editor_type_link);
                    holder.iv_img.setImageBitmap(bitmap);
                }
                holder.tv_link.setVisibility(View.VISIBLE);
                holder.tv_link.setText(informType.getUrl());
                break;
            case 4:
                //投票模块
                if (!informType.getImg_url().equals("1")) {
                    contentResolver = context.getContentResolver();
                    //获取缩略图以获得更好的性能
                    bitmap = getImageThumbnail(context, contentResolver, informType.getImg_url());
                    LogUtil.e("bitmap.getHeight()==" + bitmap.getHeight());
                    holder.iv_img.setImageBitmap(bitmap);
                } else {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.editor_type_vote);
                    holder.iv_img.setImageBitmap(bitmap);
                }
                holder.iv_add_item.setVisibility(View.GONE);
                break;
        }
        String title = informType.getTitle();
        if (title.equals("添加文字描述")) {
            holder.tv_text.setTextColor(context.getResources().getColor(R.color.font_common_1));
        } else {
            holder.tv_text.setTextColor(context.getResources().getColor(R.color.black));
        }
        holder.tv_text.setText(title);
    }

    /**
     * 获取真正的位置
     *
     * @param position
     * @return
     */
    public int getRealPosition(int position) {
        return headerView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return headerView == null ? datas.size() : datas.size() + 1;
    }

    /**
     * 移除数据
     *
     * @param position
     */
    public void removeData(int position) {
        datas.remove(getRealPosition(position));
        //刷新适配器
        notifyItemRemoved(position);
    }

    /**
     * 添加数据
     *
     * @param position 位置
     * @param data     添加的数据
     */
    public void addNewData(int position, InformType data) {
        datas.add(position, data);

        //刷新适配器
        notifyItemInserted(datas.size());
    }

    public void hideAddView(KPViewHolder holder) {
        if (holder != null) {
            holder.ll_add_item.setVisibility(View.GONE);
            holder.iv_add_item.setVisibility(View.VISIBLE);

            /**
             * 设置itemView中的其他控件获取焦点
             * 否则点击的控件隐藏后会失去焦点
             * recyclerview就会自动滚动到首位置
             */
//            holder.iv_add_item.setFocusable(true);
//            holder.iv_add_item.setFocusableInTouchMode(true);
//            holder.iv_add_item.requestFocus();
//            holder.iv_add_item.requestFocusFromTouch();
        }
    }


    public class KPViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_delete_item, iv_up, iv_down;
        public ImageView iv_img, iv_add_item;
        public TextView tv_text, tv_link;
        public LinearLayout ll_add_item;
        private RelativeLayout rl_add_text, rl_add_image, rl_add_link;

        KPViewHolder(View itemView) {
            super(itemView);

            if (itemView == headerView) return;

            iv_delete_item = (ImageView) itemView.findViewById(R.id.iv_delete_item);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            iv_up = (ImageView) itemView.findViewById(R.id.iv_up);
            iv_down = (ImageView) itemView.findViewById(R.id.iv_down);
            iv_add_item = (ImageView) itemView.findViewById(R.id.iv_add_item);
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            tv_link = (TextView) itemView.findViewById(R.id.tv_link);

            ll_add_item = (LinearLayout) itemView.findViewById(R.id.ll_add_item);
            rl_add_text = (RelativeLayout) itemView.findViewById(R.id.rl_add_text);
            rl_add_image = (RelativeLayout) itemView.findViewById(R.id.rl_add_image);
            rl_add_link = (RelativeLayout) itemView.findViewById(R.id.rl_add_link);


            tv_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FillTitleActivity.class);
                    intent.putExtra("title", tv_text.getText().toString());
                    intent.putExtra("position", getLayoutPosition() + "");
                    LogUtil.e("tv_info_title.text==" + tv_text.getText().toString());
                    LogUtil.e("position==" + getLayoutPosition());
                    ((InformActivity) context).startActivityForResult(intent, EDITTEXT);
                }
            });

            tv_link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FillTitleActivity.class);
                    intent.putExtra("title", tv_link.getText().toString());
                    intent.putExtra("position", getLayoutPosition() + "");
                    LogUtil.e("title==" + tv_link.getText().toString());
                    LogUtil.e("position==" + getLayoutPosition());
                    ((InformActivity) context).startActivityForResult(intent, EDITLINK);
                }
            });

            //设置点击事件
            //删除item逻辑处理
            iv_delete_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //判断是不是只有一条数据，如果只有一条数据就不能删除，否则可以删除当前item
                    if (getItemCount() == 2) {
                        //弹窗提示
                        Toast.makeText(context, "至少保留一个内容", Toast.LENGTH_SHORT).show();
                    } else if (getItemCount() > 2) {
                        new CommomDialog(context, R.style.dialog, "确定删除该模块内容？", new CommomDialog.OnCloseListener() {
                            @Override
                            public void onClick(Dialog dialog, boolean confirm) {
                                if (confirm) {
                                    if (datas.get(getRealPosition(getLayoutPosition())).getType() == 4) {
                                        //投票模块被删除了 显示添加投票按钮
                                        //删除之前缓存的SharedPreferences数据
                                        CacheUtils.clearSp(context, "vote_data");
                                        ((InformActivity) context).ll_add_vote.setVisibility(View.VISIBLE);
                                    }
                                    removeData(getLayoutPosition());
                                    dialog.dismiss();
                                } else if (!confirm) {
                                    dialog.dismiss();
                                }
                            }
                        }).setPositiveButton("确定").show();
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.OnItemClick(view, datas.get(getRealPosition(getLayoutPosition())), getLayoutPosition());
                    }
                }
            });

            //增加item逻辑处理
            iv_add_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //设置添加模块布局可见
                    ll_add_item.setVisibility(View.VISIBLE);
                    iv_add_item.setVisibility(View.GONE);

                    /**
                     * 设置itemView中的其他控件获取焦点
                     * 否则点击的控件隐藏后会失去焦点
                     * recyclerview就会自动滚动到首位置
                     */
                    ll_add_item.setFocusable(true);
                    ll_add_item.setFocusableInTouchMode(true);
                    ll_add_item.requestFocus();
                    ll_add_item.requestFocusFromTouch();
                }
            });

            //添加文本模块的逻辑处理
            rl_add_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddTextActivity.class);
                    intent.putExtra("text", "");
                    ((InformActivity) context).startActivityForResult(intent, ADDTEXT);
                    ll_add_item.setVisibility(View.GONE);
                    iv_add_item.setVisibility(View.VISIBLE);

                }
            });

            //添加图片模块的逻辑处理
            rl_add_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 进入相册 以下是例子：不需要的api可以不写
                    PictureSelector.create((InformActivity) context)
                            .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                            .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                            .maxSelectNum(3)// 最大图片选择数量
                            .minSelectNum(1)// 最小选择数量
                            .imageSpanCount(4)// 每行显示个数
                            .selectionMode(PictureConfig.MULTIPLE)//多选 or单选
                            .previewImage(true)// 是否可预览图片
                            .isCamera(true)// 是否显示拍照按钮
                            .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                            .enableCrop(false)// 是否裁剪
                            .compress(true)// 是否压缩
                            .synOrAsy(true)//同步true或异步false 压缩 默认同步
                            .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                            .withAspectRatio(16, 9)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                            .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                            .isGif(true)// 是否显示gif图片
                            .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                            .circleDimmedLayer(false)// 是否圆形裁剪
                            .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                            .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                            .openClickSound(true)// 是否开启点击声音
                            .selectionMedia(selectList)// 是否传入已选图片
                            .minimumCompressSize(100)// 小于100kb的图片不压缩
                            .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

                    ll_add_item.setVisibility(View.GONE);
                    iv_add_item.setVisibility(View.VISIBLE);

                }
            });

            iv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //调用系统相册并选择一张图片显示到界面上
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    CacheUtils.putString(context, "iv_img_position", getLayoutPosition() + "");
                    ((InformActivity) context).startActivityForResult(intent, ADDIMAGE);
                }
            });

            //添加链接模块的逻辑处理
            rl_add_link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddLinkActivity.class);
                    intent.putExtra("title", "");
                    intent.putExtra("link", "");
                    ((InformActivity) context).startActivityForResult(intent, ADDLINK);

                    ll_add_item.setVisibility(View.GONE);
                    iv_add_item.setVisibility(View.VISIBLE);

                }
            });

            //上移一个位置逻辑处理
            iv_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    if (position <= 1) {
                        Toast.makeText(context, "已经在第一行了", Toast.LENGTH_SHORT).show();
                    } else {
                        onItemMove(position, position - 1);
                    }
                    iv_up.setFocusable(true);
                    iv_up.setFocusableInTouchMode(true);
                    iv_up.requestFocus();
                    iv_up.requestFocusFromTouch();
                }
            });

            //下移一个位置逻辑处理
            iv_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    if (position >= datas.size()) {
                        Toast.makeText(context, "已经在最后一行了", Toast.LENGTH_SHORT).show();
                    } else {
                        onItemMove(position, position + 1);
                    }
                    iv_down.setFocusable(true);
                    iv_down.setFocusableInTouchMode(true);
                    iv_down.requestFocus();
                    iv_down.requestFocusFromTouch();
                }
            });
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(datas, fromPosition - 1, toPosition - 1);//交换数据
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
}
