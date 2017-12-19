package com.zhoukp.inform.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhoukp.inform.bean.InformType;

import java.util.ArrayList;

/**
 * Created by 周开平 on 2017/3/25 16:11.
 * qq 275557625@qq.com
 * 作用：缓存数据
 */

public class CacheUtils {
    /**
     * 得到缓存值
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("HQUZkP", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("HQUZkP", Context.MODE_PRIVATE);
        LogUtil.e("key==" + value);
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 缓存文本数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("HQUZkP", Context.MODE_PRIVATE);
        LogUtil.e("key==" + value);
        sp.edit().putString(key, value).commit();
    }

    /**
     * 获取缓存的文本信息
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("HQUZkP", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    /**
     * 缓存链表数据
     *
     * @param context
     * @param key     键值
     * @param datas   数据
     */
    public static void putArrayList(Context context, String key, ArrayList<InformType> datas) {
        SharedPreferences sp = context.getSharedPreferences("HQUZkP", Context.MODE_PRIVATE);
        String value = "";
        for (int i = 0; i < datas.size(); i++) {
            value += datas.get(i).toString();
        }
        LogUtil.e("key==" + value);
        sp.edit().putString("datas", value).commit();
    }

    /**
     * 获取缓存的链表数据
     *
     * @param context
     * @param key     键值
     * @return
     */
    public static ArrayList<InformType> getArrayList(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("HQUZkP", Context.MODE_PRIVATE);
        String value = sp.getString(key, "");
        LogUtil.e("value==" + value);

        ArrayList<InformType> datas = new ArrayList<>();

        String[] strings = value.split("@");
        for (int i = 0; i < strings.length; i++) {
            String[] info = strings[i].split(",");
            InformType informType = new InformType();
            informType.setType(Integer.parseInt(info[0]));
            informType.setTitle(info[1]);
            informType.setUrl(info[2]);
            informType.setImg_url(info[3]);
            datas.add(informType);
        }
        LogUtil.e("datas==" + datas.toString());
        return datas;
    }

    /**
     * 根据key删除缓存的SharedPreferences数据
     *
     * @param context
     * @param key
     */
    public static void clearSp(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("HQUZkP", Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    /**
     * 清除所有的SharedPreferences缓存的数据
     *
     * @param context
     */
    public static void clearAllSp(Context context) {
        SharedPreferences sp = context.getSharedPreferences("HQUZkP", Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }
}
