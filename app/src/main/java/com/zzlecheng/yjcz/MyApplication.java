package com.zzlecheng.yjcz;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;
import com.zzlecheng.yjcz.utils.SpUtil;
import com.zzlecheng.yjcz.utils.ToastUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * @类名: MyApplication
 * @描述:
 * @作者: huangchao
 * @时间: 2018/8/20 下午2:26
 * @版本: 1.0.0
 */
public class MyApplication extends Application {

    private static MyApplication instance;

    public static int sequence = 1;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化侧滑导航
        Fresco.initialize(this);
        //初始化工具
        LogUtils.isDebug = true;
        ToastUtils.init(true);
        SpUtil.getInstance().init(this);
        SharedPreferenceUtils.getInstance().init(this);
        //初始化极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
