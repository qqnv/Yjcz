package com.zzlecheng.yjcz.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.baidu.android.pushservice.PushMessageReceiver;
import com.zzlecheng.yjcz.activity.ProcedureActivity;
import com.zzlecheng.yjcz.eventbus.PushEventBean;
import com.zzlecheng.yjcz.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.zzlecheng.yjcz.activity.LoginActivity.channelIds;

/**
 * @类名: MyPushServiceReceiver
 * @描述:
 * @作者: huangchao
 * @时间: 2018/12/20 9:48 AM
 * @版本: 1.0.0
 */
public class MyPushServiceReceiver extends PushMessageReceiver {
    @Override
    public void onBind(Context context, int errorCode, String appId, String userId, String channelId, String requestId) {
        channelIds = channelId;
    }

    @Override
    public void onUnbind(Context context, int i, String s) {

    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {

    }

    @Override
    public void onMessage(Context context, String s, String s1) {
        LogUtils.e("透传消息"+context+"-"+s+"-"+s1);
        //发送粘性事件
        EventBus.getDefault().postSticky(new PushEventBean(s));
    }

    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {
        LogUtils.e(s + "-" + s1 + "-" + s2);
        String mod = JSONObject.parseObject(s2).getString("mod");
        if ("yjya".equals(mod)) {
            String extra = JSONObject.parseObject(s2).getString("extra");
            String lcid = JSONObject.parseObject(extra).getString("lcid");
            String lclsid = JSONObject.parseObject(extra).getString("lclsid");
            String lcmc = JSONObject.parseObject(extra).getString("lcmc");
            Intent intent = new Intent(context.getApplicationContext(), ProcedureActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("lclsId", lclsid);
            bundle.putString("lcId", lcid);
            bundle.putString("lcmc", lcmc);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            LogUtils.e(intent.toUri(0) + "-------");
            LogUtils.e(extra);
            context.getApplicationContext().startActivity(intent);
        }
    }

    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {
        LogUtils.e(s + "-" + s1 + "-" + s2);

    }

}
