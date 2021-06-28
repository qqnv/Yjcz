package com.zzlecheng.yjcz;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushManager;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zzlecheng.yjcz.activity.AboutUsActivity;
import com.zzlecheng.yjcz.activity.ChangeIpActivity;
import com.zzlecheng.yjcz.activity.ChangePassWordActivity;
import com.zzlecheng.yjcz.activity.ClusterCallOneActivity;
import com.zzlecheng.yjcz.activity.ClusterSchedulingActivity;
import com.zzlecheng.yjcz.activity.ClusterSetActivity;
import com.zzlecheng.yjcz.activity.EmergencyActivity;
import com.zzlecheng.yjcz.activity.EmergencyDrillActivity;
import com.zzlecheng.yjcz.activity.LoginActivity;
import com.zzlecheng.yjcz.activity.MessageActivity;
import com.zzlecheng.yjcz.activity.PersonalActivity;
import com.zzlecheng.yjcz.activity.ReportActivity;
import com.zzlecheng.yjcz.activity.ReportControlActivity;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.base.Commons;
import com.zzlecheng.yjcz.bean.CommonBean;
import com.zzlecheng.yjcz.eventbus.EventBusBean;
import com.zzlecheng.yjcz.bean.ReportTodayBean;
import com.zzlecheng.yjcz.bean.ReportTypesBean;
import com.zzlecheng.yjcz.bean.UnReportBean;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.AlertDialogManager;
import com.zzlecheng.yjcz.utils.CommonUtil;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.LogcatFileManager;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;
import com.zzlecheng.yjcz.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sword.SDK.MediaEngine;

import static com.zzlecheng.yjcz.fragment.ClusterTalkBackFragment.currentPttGroup;
import static com.zzlecheng.yjcz.fragment.ClusterTalkBackFragment.isDj;

/**
 * @类名: MainActivity
 * @描述: 主activity
 * @作者: huangchao
 * @时间: 2018/8/20 下午2:30
 * @版本: 1.0.0
 */
public class MainActivity extends RxAppCompatActivity implements View.OnClickListener, Handler.Callback {

    @BindView(R.id.tv_bb)
    TextView tvBb;
    @BindView(R.id.tv_mine)
    TextView tvMine;
    @BindView(R.id.tv_changePwd)
    TextView tvChangePwd;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.tv_aboutUs)
    TextView tvAboutUs;
    @BindView(R.id.tv_switchAccount)
    TextView tvSwitchAccount;
    @BindView(R.id.tv_clusterSet)
    TextView tvClusterSet;
    @BindView(R.id.tv_singOut)
    TextView tvSingOut;

    @BindView(R.id.settings)
    TextView settings;
    @BindView(R.id.sdv_portrait)
    SimpleDraweeView sdvPortrait;
    @BindView(R.id.ll_homeMessages)
    LinearLayout llHomeMessages;
    @BindView(R.id.ll_homeReport)
    LinearLayout llHomeReport;
    @BindView(R.id.ll_homeSignIn)
    LinearLayout llHomeSignIn;
    @BindView(R.id.ll_homeDrill)
    LinearLayout llHomeDrill;
    @BindView(R.id.ll_homeEmergency)
    LinearLayout llHomeEmergency;
    @BindView(R.id.ll_homeLaunchPlan)
    LinearLayout llHomeLaunchPlan;

    private SlidingMenu menu;
    //声明一个long类型变量：用于存放上一点击“返回键”的时刻
    private long mExitTime;
    //报班人员部门ID
    private String departmentid;
    //报班类型
    private String types = "0";
    //报班人员ID
    private String userid;
    //报班dialog
    private AlertDialogManager mDialog;
    //当前系统所有报班类型，若为2则只有白班和夜班，若为3则有白班、夜班、全天班
    private String allTypes = "";
    //url
    private String url = "";

    //视频编码高度和宽度
    private int w, h;
    //编码旋转角度
    private int encRotate = 90;
    //手动配置的一系列参数
    private int uResolution;
    private String uFps;
    private String uBitrate;
    //服务器IP
    private String uIp = "";
    //服务器端口
    private int uDkh;
    //调度端口
    private int idsPort = 10001;
    //用户名
    private String uName = "";
    //用户密码
    private String uPwd = "";
    //定义全局
    public static MediaEngine me = MediaEngine.GetInstance();
    //本地IP
    private String localIp = "";
    //本地端口
    private int localPort;
    //当前对讲组ID
    private List<String> talkGroupId = new ArrayList<>();
    //当前对讲组名称
    private List<String> talkGroupName = new ArrayList<>();
    //当前对讲组所有成员
    private List<List<String>> talkGroupMessage = new ArrayList<>();

    //新呼叫ID
    public static int clusterCallId;
    //来电号码
    public static String clusterNumber = "暂无";
    //状态
    public static int clusterState;
    //是否视频来电
    public static boolean clusterIsVideo;
    //ids参数
    public static MediaEngine.ME_IdsPara clusterIdsPara;
    //设置默认摄像头为前置
    public static int currentCameraIdx = 1;
    //是监控
    public static boolean isMonitor = false;

    public static Handler handler = null;
    private static final int MSG_SHOW_TOAST = 101;
    private static AudioManager am = null;

    public static int AZKJ_CODE = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_PHONE_STATE
            }, 1);

        }

        SharedPreferenceUtils.getInstance().putBoolean("changed", false);
        //若用户清除缓存，则自动为用户添加URL（此处实则无用）
        if ("".equals(SharedPreferenceUtils.getInstance().getString("url"))) {
            SharedPreferenceUtils.getInstance().putString("url", Commons.BASE_URL);
        }

        url = SharedPreferenceUtils.getInstance().getString("url");
        //为侧滑菜单设置布局
        //new出SlidingMenu对象
        menu = new SlidingMenu(this);
        //设置侧滑的方向.左侧
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // 设置滑动完剩余的宽度
        menu.setBehindOffset(210);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        //绑定
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu.setMenu(R.layout.layout_sliding_left);
        ButterKnife.bind(this);
        initView();
        initData();
        initCluster();
        //权限申请
        if (Build.VERSION.SDK_INT > 22) {
            NotificationManagerCompat.from(this).areNotificationsEnabled();
            requestPermissions(new String[]{android.Manifest.permission.CAMERA
                    , android.Manifest.permission.RECORD_AUDIO
                    , android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , android.Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE
                    , Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    /**
     * 集群调度初始化
     */
    private void initCluster() {
        //初始化
        boolean ret = me.ME_Init(localIp, localPort, 5,
                null, false, false);
        if (!ret)
            LogUtils.e("初始化失败");
        else
            LogUtils.e("初始化成功");
        handler = new Handler(this);
        uIp = Commons.U_IP;
        uDkh = Commons.U_DKH;
        uName = SharedPreferenceUtils.getInstance().getString("azkjid");
//        uName = "10000";
        LogUtils.e("安正科技ID" + uName);
        uPwd = Commons.U_PWD;
        MediaEngine.ME_UserType type = MediaEngine.ME_UserType.Moblie3g;
        //设置回调
        setCallBack();
        //设置参数
        setParams();
        SetSpeakerPhone(true);
        //注册账户
        LogUtils.e(uIp + "-" + uDkh + "-" + idsPort + "-" + uName + "-" + uPwd);
        boolean register = me.ME_Regist(uIp, uDkh, idsPort, uName, uPwd,
                type, 30, this);
        if (!register)
            LogUtils.e("注册失败");
        else {
            LogUtils.e("注册成功");
            SetDefaultVideoCaptureDevice(currentCameraIdx);
        }
        //获取对讲组
        me.ME_SetOnConnectBusinessServerListener(() -> getGroupInfo());
    }

    /**
     * 设置回调
     */
    private void setCallBack() {
        me.ME_SetCallBack(new MediaEngine.ME_CallBack() {
            /**
             * 注册状态通知
             * @param state 是否注册成功
             * @param code 结果码（10000=授权数量不足，10001=重复登录，200=注册成功，其他值=错误码）
             * @param content 描述
             */
            @Override
            public void onRegState(boolean state, int code, String content) {
//                LogUtils.e("注册状态通知" + state + code + content);
            }

            /**
             *  来电
             * @param callId 新呼叫ID
             * @param number 来电号码
             * @param state 状态
             * @param isVideo 是否视频来电
             * @param idsPara IDS 参数，类型为 ME_IdsPara
             */
            @Override
            public void onIncomingCall(int callId, String number, int state, boolean isVideo, MediaEngine.ME_IdsPara idsPara) {
                clusterCallId = callId;
                clusterNumber = number;
                clusterState = state;
                clusterIsVideo = isVideo;
                clusterIdsPara = idsPara;

                LogUtils.e("*****来电状态：对讲信息" + callId + "-" + number + "-" + state + "-" + isVideo);
                if (number.startsWith("*7*")) {
                    //监控呼入 ，格式：*7*xxx，
                    if (MainActivity.handler != null) {
                        isMonitor = true;
                        Message m = Message.obtain(MainActivity.handler, MediaEngine.ME_MsgType.INCOMING_CALL, clusterIsVideo);
                        m.arg1 = callId;
                        m.arg2 = state;
                        m.sendToTarget();
                    }
                } else if (number.startsWith("*5*")) {//普通单呼
                    isMonitor = false;
                    if (MainActivity.handler != null) {
                        Message m = Message.obtain(MainActivity.handler, MediaEngine.ME_MsgType.CONF_INCOMING, clusterIsVideo);
                        m.arg1 = clusterCallId;
                        m.arg2 = state;
                        m.sendToTarget();
                    }
                } else if (number.contains("~")) {
                    isMonitor = false;
                    //会议呼叫或对讲呼叫（作为被叫） ，格式：对讲组号/会议号+~+创建者号码，对讲则自动接听，会议则作为单呼处理
                    String[] str = number.split("~");
                    if (str.length > 1) {
                        if (talkGroupId.contains(str[0])) {
                            //匹配对讲组成功，执行对讲呼入逻辑
                            if (MainActivity.handler != null) {
                                Message m = Message.obtain(MainActivity.handler, MediaEngine.ME_MsgType.PTT_INCOMING, str[0]);
                                m.arg1 = clusterCallId;
                                m.sendToTarget();
                            }
                        } else {
                            //会议呼入，作为普通单呼
                            if (MainActivity.handler != null) {
                                Message m = Message.obtain(MainActivity.handler, MediaEngine.ME_MsgType.INCOMING_CALL, clusterIsVideo);
                                m.arg1 = clusterCallId;
                                m.arg2 = clusterState;
                                m.sendToTarget();
                            }
                        }
                    } else {
                        //其他，当成单呼处理，防止漏接
                        //普通单呼
                        if (MainActivity.handler != null) {
                            Message m = Message.obtain(MainActivity.handler, MediaEngine.ME_MsgType.INCOMING_CALL, clusterIsVideo);
                            m.arg1 = clusterCallId;
                            m.arg2 = clusterState;
                            m.sendToTarget();
                        }
                    }
                } else {
                    isMonitor = false;
                    //普通单呼
                    if (MainActivity.handler != null) {
                        Message m = Message.obtain(MainActivity.handler, MediaEngine.ME_MsgType.INCOMING_CALL, clusterIsVideo);
                        m.arg1 = clusterCallId;
                        m.arg2 = clusterState;
                        m.sendToTarget();
                    }
                }
            }

            /**
             * 呼叫状态回调
             * @param callId 呼叫ID
             * @param number 对端号码
             * @param state 呼叫状态
             */
            @Override
            public void onCallState(int callId, String number, int state) {
                clusterCallId = callId;
                clusterNumber = number;
                LogUtils.e("呼叫状态回调" + callId + "-" + number + "-" + state);
                Message m = Message.obtain(ClusterCallOneActivity.handler_, state, callId, 0, number);
                m.sendToTarget();
            }

            /**
             * 短信发送结果
             * @param state 短消息状态，200或203则成功
             * @param reason 失败原因描述
             */
            @Override
            public void onSendMsgState(int state, String reason) {
                LogUtils.e("短信发送结果" + state + reason);
            }

            /**
             * 收短消息
             * @param from 短消息来源
             * @param msgType 消息类型（MediaEngine.ME_MsgType.MESSAGE_TYPE_TEXT=文字消息
             *                MediaEngine.ME_MsgType.MESSAGE_TYPE_APPLICATION=业务消息）
             * @param msg 消息内容
             */
            @Override
            public void onReceiveMsg(String from, String msgType, String msg) {
//                LogUtils.e("收短消息" + from + msgType + msg);

            }

            /**
             * 日志回调
             * @param level 日志等级
             * @param log 日志内容
             */
            @Override
            public void onLogWriter(int level, String log) {
                LogUtils.e("日志" + level + log);
            }

            /**
             * 对讲组状态推送
             * @param groupNumber 对讲组号
             * @param speaker 当前讲话者
             */
            @Override
            public void onPttStatus(String groupNumber, String speaker) {
                LogUtils.e("对讲组状态推送" + groupNumber + speaker);
                String str = groupNumber + "  讲话：" + speaker;
                if (MainActivity.handler != null) {
                    Message m = Message.obtain(MainActivity.handler, MediaEngine.ME_MsgType.PTT_STATUS, str);
                    m.sendToTarget();
                }
            }

            /**
             * 对讲组申请话权/释放话权结果
             * @param state 对讲组状态
             * @param reason 原因描述
             */
            @Override
            public void onPttReqState(int state, String reason) {
                LogUtils.e("对讲组申请话权/释放话权结果" + state + reason);
                if (MainActivity.handler != null) {
                    Message m = Message.obtain(MainActivity.handler, MediaEngine.ME_MsgType.PTT_REQ_STATE, reason);
                    m.arg1 = state;
                    m.sendToTarget();
                }
            }

            /**
             * 对讲申请话权排队回调
             * @param nspeaker 申请人
             * @param queue 排队顺序
             */
            @Override
            public void onPttWaiting(String nspeaker, String queue) {
                LogUtils.e("对讲申请话权排队回调" + nspeaker + queue);
            }

            /**
             * 对讲申请话权拒绝回调
             * @param error 错误码
             */
            @Override
            public void onPttReject(String error) {
                LogUtils.e("对讲申请话权拒绝回调" + error);
            }

            /**
             * 对讲组注册成员状态变更
             * @param member 成员号码
             * @param state 注册状态（1：在线，2：离线）
             */
            @Override
            public void onMemberRegStateChange(String member, int state) {
                LogUtils.e("对讲组注册成员状态变更" + member + state);
            }

            /**
             * 异步推送事件
             * @param header 消息头 Session.State.Change  Session.Member.Change
             * @param content 消息内容
             */
            @Override
            public void onReceivePublishMsg(String header, String content) {
//                LogUtils.e("异步推送事件" + header + "-" + content);
            }

            @Override
            public void onError(String s, String s1, String s2) {
                LogUtils.e("onError" + s + s1 + s2);
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        LogUtils.e("返回" + msg.what + "-" + msg.arg1 + "-" + msg.arg2 + "-" + msg.obj + "-" + currentPttGroup);
        if (msg.obj.toString().contains(currentPttGroup)) {//如过来电不是当前对讲组则不接受对讲
            switch (msg.what) {
                case 0:
                    //退出系统
                    me.ME_Destroy();
                    finish();
                    Runtime.getRuntime().gc();
                    Process.killProcess(Process.myPid());
                    break;
                case MediaEngine.ME_MsgType.REG_STATE:
                    //注册消息通知
                    String txt = (String) msg.obj;
                    if (msg.arg2 == MediaEngine.ME_StatusCode.LICENSE_NOT_ENOUGH) {
                        LogUtils.e("账户：" + uName + "  注册失败，license数量不足！");
                    } else if (msg.arg2 == MediaEngine.ME_StatusCode.REPEAT_LOGIN) {
                        LogUtils.e("账户：" + uName + "  注册失败，重复登录！");
                    } else {
                        LogUtils.e("账户：" + uName + "  " + txt);
                    }
                    break;
                case MediaEngine.ME_MsgType.INCOMING_CALL:
                    //来电
                    showCallActivity();
                    break;
                case MediaEngine.ME_MsgType.PTT_INCOMING:
                    //来对讲，自动接听
                    //设置语音接收增益
                    me.ME_SetRxLevel(clusterCallId, 2.0f);
                    //发送事件
                    MediaEngine.GetInstance().ME_Answer(MainActivity.clusterCallId, false);
                    break;
                case MediaEngine.ME_MsgType.CONF_INCOMING:
                    //会议呼叫，接听
                    break;
                case MediaEngine.ME_MsgType.CALL_STATE://呼叫状态消息

                    break;
                case MediaEngine.ME_MsgType.CALL_MEDIA_STATE:
                    //媒体状态
                    break;
                case MediaEngine.ME_MsgType.SEND_MSGSTATE:
                    //文字短信发送状态
                    if (msg.arg1 >= MediaEngine.ME_StatusCode.OK200 && msg.arg1 <= MediaEngine.ME_StatusCode.ACCEPTED)
                        Toast.makeText(this, "短消息发送成功!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, "短消息发送失败:" + msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
                case MediaEngine.ME_MsgType.RECEIVE_MSG:
                    //收到文字短信
                    Toast.makeText(this, msg.obj.toString(), Toast.LENGTH_LONG).show();

                    break;
                case MediaEngine.ME_MsgType.RECEIVE_MSG_FILE:
                    //收到文件消息-新文件通知
                    break;
                case MediaEngine.ME_MsgType.RECEIVE_MSG_FILE_STANDBY:
                    //收到文件消息-下载通知
                    break;
                case MediaEngine.ME_MsgType.RECEIVE_MSG_OFFLINE:
                    MediaEngine.ME_Message[] msgOffline = (MediaEngine.ME_Message[]) msg.obj;
                    String txt3 = "收到离线消息：\r\n";
                    for (MediaEngine.ME_Message me_message : msgOffline) {
                        txt3 += me_message.body + "，发送者：" + me_message.sender + "\r\n";
                    }
                    Toast.makeText(this, txt3, Toast.LENGTH_LONG).show();
                    //收到离线文本消息列表
                    break;
                case MediaEngine.ME_MsgType.RECEIVE_MSG_OFFLINE_FILE:
                    //收到离线文件消息列表
                    break;
                case MediaEngine.ME_MsgType.PTT_STATUS:
                    LogUtils.e("lalalla" + CommonUtil.getWordCount(msg.obj.toString()) + "-" + clusterNumber);
                    if (CommonUtil.getWordCount(msg.obj.toString()) > 12) {
                        EventBus.getDefault().postSticky(new EventBusBean(talkGroupId, talkGroupName, talkGroupMessage, clusterNumber));
                    } else {
                        EventBus.getDefault().postSticky(new EventBusBean(talkGroupId, talkGroupName, talkGroupMessage, ""));
                    }
                    break;
                case MediaEngine.ME_MsgType.PTT_REQ_STATE:
                    //申请/释放话权结果回调
                    String str = "";
                    if (msg.arg1 == MediaEngine.ME_MsgType.PTT_REQ_ACCEPT)
                        str = "成功：";
                    else if (msg.arg1 == MediaEngine.ME_MsgType.PTT_REQ_WAITING)
                        str = "排队：";
                    else if (msg.arg1 == MediaEngine.ME_MsgType.PTT_REQ_REJECT)
                        str = "拒绝：";
                    Toast.makeText(this, str + msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
                case MediaEngine.ME_MsgType.PTT_GETGROUPINFO_CB:
                    //获取对讲组信息回调
                    MediaEngine.ME_PttGroupInfo[] g = (MediaEngine.ME_PttGroupInfo[]) msg.obj;
                    if (g != null) {
                        talkGroupId.clear();
                        if (g.length > 0) {
                            for (int i = 0; i < g.length; i++) {
                                talkGroupId.add(g[i].groupNumber);
                                talkGroupName.add(g[i].groupName);
                                MediaEngine.ME_UserInfo[] info = me.ME_GetGroupMember(g[i].groupNumber);
                                List<String> member = new ArrayList<>();
                                if (info.length > 0) {
                                    for (int j = 0; j < info.length; j++) {
                                        member.add(info[j].userId);
                                    }
                                }
                                talkGroupMessage.add(member);
                                EventBus.getDefault().postSticky(new EventBusBean(talkGroupId, talkGroupName, talkGroupMessage, clusterNumber));
                            }
                        }
                    }
                    break;
                case MediaEngine.ME_MsgType.PTT_GETGROUPMEMBER_CB:
                    break;
                case MSG_SHOW_TOAST:
                    Toast.makeText(this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        return false;
    }

    /**
     * 跳转到接听界面
     */
    private void showCallActivity() {
        //解锁
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
        keyguardLock.disableKeyguard();
        //跳转
        Intent intent = new Intent(this, ClusterCallOneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * 同步获取对讲组
     */
    private void getGroupInfo() {
        MediaEngine.ME_PttGroupInfo[] g = me.ME_GetPttGroup();
        if (g != null) {
            talkGroupId.clear();
            if (g.length > 0) {
                for (int i = 0; i < g.length; i++) {
                    talkGroupId.add(g[i].groupNumber);
                    talkGroupName.add(g[i].groupName);
                    MediaEngine.ME_UserInfo[] info = me.ME_GetGroupMember(g[i].groupNumber);
                    List<String> member = new ArrayList<>();
                    if (info.length > 0) {
                        for (int j = 0; j < info.length; j++) {
                            member.add(info[j].userId);
                        }
                    }
//                    LogUtils.e("对讲组数据：" + member + g[i].groupName + g[i].groupNumber);
                    talkGroupMessage.add(member);
                    EventBus.getDefault().postSticky(new EventBusBean(talkGroupId, talkGroupName, talkGroupMessage, clusterNumber));
                }
            }
        }
    }

    /**
     * 异步获取对讲组
     */
    private void getGroupInfoAsync() {
        //        //异步方式获取对讲组
        me.ME_GetPttGroup_Async(me_pttGroupInfos -> {
            MediaEngine.ME_PttGroupInfo[] g = me_pttGroupInfos;
            if (g != null) {
                talkGroupId.clear();
                if (g.length > 0) {
                    for (int i = 0; i < g.length; i++) {
                        talkGroupId.add(g[i].groupNumber);
                        talkGroupName.add(g[i].groupName);
                        //异步方式获取对讲组成员
                        MediaEngine.GetInstance().ME_GetGroupMember_Async(g[i].groupNumber, (groupNumber, me_userInfos) -> {
                            List<String> member = new ArrayList<>();
                            if (me_userInfos.length > 0) {
                                for (int j = 0; j < me_userInfos.length; j++) {
                                    member.add(me_userInfos[j].userId);
                                }
                            }
//                            LogUtils.e("对讲组数据：" + member);
                            talkGroupMessage.add(member);
                            EventBus.getDefault().postSticky(new EventBusBean(talkGroupId, talkGroupName, talkGroupMessage, clusterNumber));
                        });
                    }
                }
            }
        });
    }

    /**
     * 设置默认摄像头,1=前置，2=后置
     */
    public void SetDefaultVideoCaptureDevice(int devId) {
        me.ME_SetDefaultVideoCaptureDevice(devId);
    }


    /**
     * 设置免提
     *
     * @param on
     */
    private void SetSpeakerPhone(boolean on) {
        if (am == null)
            am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setMode(AudioManager.MODE_IN_COMMUNICATION);
        if (on) {
            //免提设置最大音量
            am.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                    am.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), 0);
            am.setSpeakerphoneOn(true);
        } else {
            //非免提设置一半音量，FH688设备中，非免提状态下麦克风增益很大
            am.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                    am.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL) * 2 / 3, 0);
            am.setSpeakerphoneOn(false);
        }
    }

    /**
     * 初始化view，给各个view增加监听事件
     */
    private void initView() {

        departmentid = SharedPreferenceUtils.getInstance().getString("departmentid");
        userid = SharedPreferenceUtils.getInstance().getString("userid");
        uResolution = SharedPreferenceUtils.getInstance().getInt("uResolution");
        uFps = Commons.U_FPS;
        uBitrate = Commons.U_BITRATE;

        LogUtils.e("部门ID：" + departmentid + "-用户ID：" + userid);

        setSlidingView();
        tvBb.setOnClickListener(this);
        sdvPortrait.setOnClickListener(this);
        tvMine.setOnClickListener(this);
        tvChangePwd.setOnClickListener(this);
        tvMessage.setOnClickListener(this);
        tvAboutUs.setOnClickListener(this);
        tvSwitchAccount.setOnClickListener(this);
        tvClusterSet.setOnClickListener(this);
        tvSingOut.setOnClickListener(this);
        settings.setOnClickListener(this);

        llHomeMessages.setOnClickListener(this);
        llHomeReport.setOnClickListener(this);
        llHomeSignIn.setOnClickListener(this);
        llHomeDrill.setOnClickListener(this);
        llHomeEmergency.setOnClickListener(this);
        llHomeLaunchPlan.setOnClickListener(this);

    }

    /**
     * 1.查询当前用户报班情况，若已报班，则再次点击时提醒是否选择退班
     * 2.查询当前系统报班类型，如全天班-1、白班-2、夜班-3，根据报班类型弹出不同的提示框
     */
    private void initData() {
        //查询当前用户报班情况，若已报班，则再次点击时提醒是否选择退班
        HttpMethods.getHttpMethods().reportToday(this, userid, "0",
                new BaseObserver<List<ReportTodayBean>>(this, false) {
                    @Override
                    protected void onHandleSuccess(List<ReportTodayBean> reportTodayBeans) {
                        if (reportTodayBeans.toString() == "[]") {//未报班
                            tvBb.setText(R.string.bb);
                        } else {//已报班
                            tvBb.setText(reportTodayBeans.get(0).getTypes());
                        }
                    }
                });
        //查询当前系统报班类型，如全天班-1、白班-2、夜班-3，根据报班类型弹出不同的提示框
        HttpMethods.getHttpMethods().reportTypes(this, "0",
                new BaseObserver<ReportTypesBean>(this, false) {
                    @Override
                    protected void onHandleSuccess(ReportTypesBean reportTypesBean) {
                        if (reportTypesBean.getWorkType().size() == 2) {//只有两种班次可选择
                            allTypes = "2";
                        } else {
                            allTypes = "3";
                        }
                    }
                });
    }

    /**
     * 设置侧滑导航的相关信息
     */
    private void setSlidingView() {

        //设置侧滑页面的头像展示
//        Uri uri = Uri.parse("https://imgsa.baidu.com/forum/pic/item/3bc79f3df8dcd1000ac6c4fa798b4710b8122f96.jpg");
        SimpleDraweeView sdvPortraitLeft = findViewById(R.id.sdv_portraitLeft);
//        sdvPortraitLeft.setImageURI(uri);
        sdvPortraitLeft.setImageResource(R.mipmap.icon);
        TextView tvNmae = findViewById(R.id.tv_name);
        tvNmae.setText(SharedPreferenceUtils.getInstance().getString("user"));
        //侧滑页面底部设置的图标
        Drawable drawable_settings = getResources().getDrawable(R.mipmap.sz);
        drawable_settings.setBounds(0, 0, 70, 70);//40,40为宽高
        settings.setCompoundDrawables(null, drawable_settings, null, null);
        //设置文字右侧的图片
        Drawable drawable_sliding_xiangyoua = getResources().getDrawable(R.drawable.ic_right);
        drawable_sliding_xiangyoua.setBounds(0, 0, 33, 33);//40,40为宽高
        //设置textview的drawableleft大小
//        Drawable drawableMine = getResources().getDrawable(R.drawable.ic_boy);
//        drawableMine.setBounds(0, 0, 70, 40);
//        tvMine.setCompoundDrawables(drawableMine, null, drawable_sliding_xiangyoua, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bb://报班
                newspaperClass();
                break;
            case R.id.ll_homeMessages://消息中心
                startActivity(new Intent(this, MessageActivity.class));
                break;
            case R.id.ll_homeReport://事件上报
                startActivity(new Intent(this, ReportActivity.class));
                break;
            case R.id.ll_homeSignIn://值班管理
                startActivity(new Intent(this, ReportControlActivity.class));
                break;
            case R.id.ll_homeDrill://应急演练
                Intent intentEmergencyDrill = new Intent(this, EmergencyDrillActivity.class);
                Bundle bundleEmergencyDrill = new Bundle();
                bundleEmergencyDrill.putBoolean("isYl", true);
                intentEmergencyDrill.putExtras(bundleEmergencyDrill);
                startActivity(intentEmergencyDrill);
                break;
            case R.id.ll_homeEmergency://应急处置
                Intent intentEmergency = new Intent(this, EmergencyActivity.class);
                Bundle bundleEmergency = new Bundle();
                bundleEmergency.putBoolean("isYl", false);
                intentEmergency.putExtras(bundleEmergency);
                startActivity(intentEmergency);
                break;
            case R.id.ll_homeLaunchPlan://集群调度
                startActivity(new Intent(this, ClusterSchedulingActivity.class));
                break;
            case R.id.sdv_portrait://左上角头像
                menu.showMenu();
                break;
            case R.id.tv_mine://个人信息
                startActivity(new Intent(this, PersonalActivity.class));
                break;
            case R.id.tv_changePwd://修改密码
                startActivity(new Intent(this, ChangePassWordActivity.class));
                break;
            case R.id.tv_message://消息中心
                startActivity(new Intent(this, MessageActivity.class));
                break;
            case R.id.tv_aboutUs://关于我们
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.tv_switchAccount://更换IP
                startActivity(new Intent(this, ChangeIpActivity.class));
                break;
            case R.id.tv_clusterSet://调度配置
                Intent intent = new Intent(this, ClusterSetActivity.class);
                startActivityForResult(intent, AZKJ_CODE);
                break;
            case R.id.tv_singOut://退出登录
                SharedPreferenceUtils.getInstance().clear();
                //停止推送
                PushManager.stopWork(this);
                SharedPreferenceUtils.getInstance().putString("url", url);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.settings://设置主题
                break;
        }
    }

    /**
     * 点击报班
     */
    private void newspaperClass() {
        if (!tvBb.getText().toString().trim().equals("加载中")) {
            if (!tvBb.getText().toString().trim().equals("报班")) {//未报班
                AlertDialogManager.Builder builder = new AlertDialogManager.Builder(this);
                builder.setTitle("退班")
                        .setMessage("是否确定退班")
                        .setPositiveButton("确定", (dialog, which) -> {
                            unReport();
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                            mDialog.dismiss();
                        });
                mDialog = builder.create();
                mDialog.show();
            } else {//已报班
                CustomPopupWindow popupWindow = new CustomPopupWindow(this);
                popupWindow.showAtLocation(this.findViewById(R.id.tableRow),
                        Gravity.CENTER, 0, 0);
                popupWindow.setOnDismissListener(new popupDismissListener());
            }
        }
    }

    /**
     * 报班弹框
     */
    class CustomPopupWindow extends PopupWindow implements View.OnClickListener {

        Context mContext;
        private LayoutInflater mInflater;
        private View mContentView;

        public CustomPopupWindow(Context context) {
            super(context);
            this.mContext = context;
            //打气筒
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //打气
            mContentView = mInflater.inflate(R.layout.layout_popup_bb, null);
            TextView tv_timeAllDay = mContentView.findViewById(R.id.tv_timeAllDay);
            TextView tv_timeDay = mContentView.findViewById(R.id.tv_timeDay);
            TextView tv_timeNight = mContentView.findViewById(R.id.tv_timeNight);
            String currentTime = CommonUtil.getDate();
            if (allTypes == "3") {//全部有
                mContentView.findViewById(R.id.rb_allDay).setOnClickListener(this);
                tv_timeAllDay.setText(currentTime);
            } else {//没有全天班
                mContentView.findViewById(R.id.tv_timeAllDay).setVisibility(View.GONE);
                mContentView.findViewById(R.id.v_allDay).setVisibility(View.GONE);
                mContentView.findViewById(R.id.rb_allDay).setVisibility(View.GONE);
                mContentView.findViewById(R.id.tv_vAllDay).setVisibility(View.GONE);
            }
            mContentView.findViewById(R.id.rb_day).setOnClickListener(this);
            mContentView.findViewById(R.id.rb_night).setOnClickListener(this);
            mContentView.findViewById(R.id.tv_cancel).setOnClickListener(this);
            mContentView.findViewById(R.id.tv_sure).setOnClickListener(this);
            tv_timeDay.setText(currentTime);
            tv_timeNight.setText(currentTime);


            //设置View
            setContentView(mContentView);
            //设置宽与高
            setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            //设置进出动画
            setAnimationStyle(R.style.PopupWindowStyle);
            //设置背景只有设置了这个才可以点击外边和BACK消失
            setBackgroundDrawable(new ColorDrawable());
            //设置可以获取集点
            setFocusable(true);
            //设置可以触摸
            setTouchable(true);
            //设置点击外边可以消失
            setOutsideTouchable(true);
            backgroundAlpha(0.4f);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rb_allDay:
                    types = "1";
                    return;
                case R.id.rb_day:
                    types = "2";
                    return;
                case R.id.rb_night:
                    types = "3";
                    return;
                case R.id.tv_cancel:
                    types = "0";
                    dismiss();
                    return;
                case R.id.tv_sure:
                    if (types == "0") {
                        ToastUtils.showShortToast(MainActivity.this, "请选择报班类型");
                    } else {
                        report();
                        dismiss();
                    }
                    return;
            }
        }
    }

    /**
     * 报班弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    /**
     * 设置遮罩层
     *
     * @param f
     */
    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }

    /**
     * 报班
     */
    private void report() {
        LogUtils.e(userid);
        HttpMethods.getHttpMethods().report(this, departmentid, types, userid, "0",
                new BaseObserver<CommonBean>(this, false) {
                    @Override
                    protected void onHandleSuccess(CommonBean reportBean) {
                        ToastUtils.showShortToast(MainActivity.this, "报班成功");
                        initData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.toString());
                    }

                    @Override
                    protected void onHandleError(String msg) {
                        LogUtils.e(msg);
                    }
                });
    }

    /**
     * 退班
     */
    private void unReport() {
        HttpMethods.getHttpMethods().unReport(this, departmentid, userid, "0",
                new BaseObserver<UnReportBean>(this, false) {
                    @Override
                    protected void onHandleSuccess(UnReportBean unReportBean) {
                        ToastUtils.showShortToast(MainActivity.this, "退班成功");
                        initData();
                    }
                });
    }

    /**
     * 再按一次退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 全局设置刷新加载头尾
     */
    static {//static 代码段可以防止内存泄露
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater((context, layout) -> {

            layout.setPrimaryColorsId(R.color.main, android.R.color.white);//全局设置主题颜色

            return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogcatFileManager.getInstance().stop();
    }

    /**
     * 设置默认参数
     */
    private void setParams() {

        //三个参数：打开或关闭Trace,是否将发送方向的视频录制为裸文件,是否将接收方向的视频录制为裸文件
        me.ME_Trace(true, false, false);
        me.ME_TraceAudio(true);
        //启用或关闭硬编解码，默认开启，h265开启情况下会优先选择
        //四个参数：开启硬解码,开启硬编码,采用h264硬编解码(需硬件支持),采用h265硬编解码（需硬件支持）
        me.ME_UseHardwareCodec(true, true, true, false);
        //设置视频相关参数
        //八个参数：视频编码宽度，视频编码高度，帧率，码率
        //         编码旋转角度，解码旋转角度，是否快速解码渲染，屏幕竖横比（4：3或16：9）
        getVedioParam();
        me.ME_SetVideoCodecParam(w, h, Integer.parseInt(uFps), Integer.parseInt(uBitrate),
                encRotate, 0, true, 1);
        //开启或关闭FEC前向纠错,主要用于抗网络丢包
        me.ME_EnableFec(true);
        //设置FEC参数
        //五个参数：语音丢包时延，视频丢包时延，冗余模式，冗余率，分组大小（和分辨率有关）
        me.ME_SetFecParams(20, 20, 1, 20, 16);
        //设置RTP载荷大小
        me.ME_SetRtpPayloadSize(1000);
        //设置接收缓冲大小(默认128k，丢包情况下可增大至1024K)
        me.ME_SetSocketRcvBufferSize(1024);
        //设置发送缓冲大小
        me.ME_SetSocketSndBufferSize(128);
        //启用或关闭信令简写
        me.ME_UseShortHeader(false);
        //开启或关闭发送速率控制（默认关闭），建议在公网无线环境中开启（如4G）
        me.ME_SendRtpSpeedControl(false);
        me.ME_SetVidUseJBuffer(false);

    }

    /**
     * 换算视频编码宽度
     */
    private void getVedioParam() {
        String[] resolutionArray = getResources().getStringArray(R.array.resolution);
        String resolution = resolutionArray[uResolution];
        w = Integer.parseInt(resolution.split("\\*")[0]);
        h = Integer.parseInt(resolution.split("\\*")[1]);
        if (encRotate % 180 == 90) {
            //手机通常是竖着拿，旋转90度后，宽高交换
            w ^= h;
            h ^= w;
            w ^= h;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == AZKJ_CODE) {
            //若调度配置修改则返回重新注册
            initCluster();
        }
    }
}
