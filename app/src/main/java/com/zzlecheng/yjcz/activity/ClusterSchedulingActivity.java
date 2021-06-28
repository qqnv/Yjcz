package com.zzlecheng.yjcz.activity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.base.BaseActivity;
import com.zzlecheng.yjcz.eventbus.EventBusBean;
import com.zzlecheng.yjcz.fragment.ClusterMailListFragment;
import com.zzlecheng.yjcz.fragment.ClusterMessageFragment;
import com.zzlecheng.yjcz.fragment.ClusterTalkBackFragment;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.sword.SDK.MediaEngine;
import sakura.bottomtabbar.BottomTabBar;

/**
 * @类名: ClusterSchedulingActivity
 * @描述: 集群调度
 * @作者: huangchao
 * @时间: 2018/9/4 下午7:14
 * @版本: 1.0.0
 */
public class ClusterSchedulingActivity extends BaseActivity implements Handler.Callback {

    //本地IP
    private String localIp = "";
    //本地端口
    private int localPort;
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
    //设置默认摄像头为前置
    private int currentCameraIdx = 1;
    private static final int MSG_SHOW_TOAST = 101;

    //当前对讲组ID
    private List<String> grps = new ArrayList<>();
    //当前对讲组所有成员
    private List<String> mem = new ArrayList<>();
    //当前是谁在讲话
    private String currentSpeaker = "暂无";

    //视频编码高度和宽度
    private int w, h;
    //编码旋转角度
    private int encRotate = 90;

    //手动配置的一系列参数
    private int uResolution;
    private String uFps;
    private String uBitrate;

    //语音呼叫
    public static MediaEngine.ME_IdsPara S_idsPara;
    public static String lastInfo_Number;
    public static int lastInfo_State;
    public static int currentCallId = -1;
    private int RceiveTimes = 0;
    private int ReqPttTimes = 0;
    private boolean ISReqPtt = false;
    public static boolean isVideo;


    public static Handler handler = null;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_cluster_scheduling;
    }

    @Override
    protected void initView() {

        ((BottomTabBar) findViewById(R.id.BottomTabBar))
                .initFragmentorViewPager(getSupportFragmentManager())
                .addReplaceLayout(R.id.fl_content)
                .setImgSize(50, 50)
                .setTabPadding(15, 0, 0)
                .setFontSize(11)
                .setChangeColor(getResources().getColor(R.color.orange), getResources().getColor(R.color.white))
                .setTabBarBackgroundResource(R.color.main)
                .addTabItem("对讲", getResources().getDrawable(R.mipmap.intercom),
                        getResources().getDrawable(R.mipmap.intercom_1), ClusterTalkBackFragment.class)
                .addTabItem("通讯录", getResources().getDrawable(R.mipmap.book),
                        getResources().getDrawable(R.mipmap.book_1), ClusterMailListFragment.class)
//                .addTabItem("信息", getResources().getDrawable(R.mipmap.information),
//                        getResources().getDrawable(R.mipmap.information_1), ClusterMessageFragment.class)
                .setOnTabChangeListener((position, V) -> LogUtils.e(""))
                .commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        initData();
    }

    /**
     * 集群调度初始化
     */
    private void initData() {

        uResolution = SharedPreferenceUtils.getInstance().getInt("uResolution");
        uFps = SharedPreferenceUtils.getInstance().getString("uFps");
        uBitrate = SharedPreferenceUtils.getInstance().getString("uBitrate");

        //初始化
        boolean ret = me.ME_Init(localIp, localPort, 5,
                null, false, false);
        if (!ret)
            LogUtils.e("初始化失败");
        else
            LogUtils.e("初始化成功");
        handler = new Handler(this);
        uIp = SharedPreferenceUtils.getInstance().getString("uIp");
        uDkh = SharedPreferenceUtils.getInstance().getInt("uDkh");
        uName = SharedPreferenceUtils.getInstance().getString("uName");
        uPwd = SharedPreferenceUtils.getInstance().getString("uPwd");
        MediaEngine.ME_UserType type = MediaEngine.ME_UserType.Moblie3g;
        //设置回调
        setCallBack();
        //设置参数
        setParams();
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
        //获取离线消息
        getOfflineMessage();
    }

    /**
     * 设置回调
     */
    private void setCallBack() {
        me.ME_SetCallBack(new MediaEngine.ME_CallBack() {
            //注销状态
            @Override
            public void onRegState(boolean b, int i, String s) {
                LogUtils.e("onRegState" + b + i + s);
            }

            //来对讲
            @Override
            public void onIncomingCall(int i, String s, int i1, boolean b, MediaEngine.ME_IdsPara me_idsPara) {
                S_idsPara = me_idsPara;
                lastInfo_Number = s;
                lastInfo_State = i1;
                currentCallId = i; //新来电
                isVideo = b;
                LogUtils.e("*****来电状态：对讲信息" + i + "-" + s + "-" + i1 + "-" + b);
//                LogUtils.e("对讲详情"+me_idsPara.getNumber()+"-"+me_idsPara.getSession()+"-"+me_idsPara.getType());
                if (s.startsWith("*5*")) {//普通单呼
                    if (ClusterSchedulingActivity.handler != null) {
                        Message m = Message.obtain(ClusterSchedulingActivity.handler, MediaEngine.ME_MsgType.CONF_INCOMING, b);
                        m.arg1 = i;
                        m.arg2 = i1;
                        m.sendToTarget();
                    }
                } else if (s.startsWith("*7*")) {//监控呼入
                    if (ClusterSchedulingActivity.handler != null) {
                        Message m = Message.obtain(ClusterSchedulingActivity.handler, MediaEngine.ME_MsgType.INCOMING_CALL, b);
                        m.arg1 = i;
                        m.arg2 = i1;
                        m.sendToTarget();
                    }
                } else if (s.startsWith("*9*")) {//临时对讲
                } else if (s.contains("~")) {
                    //会议呼叫或对讲呼叫（作为被叫） ，格式：对讲组号/会议号+~+创建者号码，对讲则自动接听，会议则作为单呼处理
                    String[] str = s.split("~");
                    if (str.length > 1) {
                        if (grps.contains(str[0])) {
                            //匹配对讲组成功，执行对讲呼入逻辑
                            if (ClusterSchedulingActivity.handler != null) {
                                Message m = Message.obtain(ClusterSchedulingActivity.handler, MediaEngine.ME_MsgType.PTT_INCOMING, str[0]);
                                m.arg1 = i;
                                LogUtils.e("返回的数据啊嗷嗷：" + m.getTarget().toString());
                                m.sendToTarget();
                            }
                        } else {
                            //会议呼入，作为普通单呼
                            if (ClusterSchedulingActivity.handler != null) {
                                Message m = Message.obtain(ClusterSchedulingActivity.handler, MediaEngine.ME_MsgType.INCOMING_CALL, b);
                                m.arg1 = i;
                                m.arg2 = i1;
                                m.sendToTarget();
                            }
                        }
                    } else {
                        //其他，当成单呼处理，防止漏接
                        //普通单呼
                        if (ClusterSchedulingActivity.handler != null) {
                            Message m = Message.obtain(ClusterSchedulingActivity.handler, MediaEngine.ME_MsgType.INCOMING_CALL, b);
                            m.arg1 = i;
                            m.arg2 = i1;
                            m.sendToTarget();
                        }
                    }
                } else {
                    //普通单呼
                    if (ClusterSchedulingActivity.handler != null) {
                        Message m = Message.obtain(ClusterSchedulingActivity.handler, MediaEngine.ME_MsgType.INCOMING_CALL,b);
                        m.arg1 = i;
                        m.arg2 = i1;
                        m.sendToTarget();
                    }
                }

            }

            //呼叫状态
            @Override
            public void onCallState(int i, String s, int i1) {
                LogUtils.e("呼叫状态" + i + "-" + s + "-" + i1);
                Message m = Message.obtain(ClusterCallOneActivity.handler_, i1, i, 0, s);
                m.sendToTarget();
            }

            //短信发送结果
            @Override
            public void onSendMsgState(int i, String s) {
                LogUtils.e("onSendMsgState" + i + s);
            }

            //收到短信
            @Override
            public void onReceiveMsg(String from, String msgType, String msg) {
                LogUtils.e("onReceiveMsg" + from + msgType + msg);
                if (msgType.equals(MediaEngine.ME_MsgType.MESSAGE_TYPE_TEXT)) {
                    String str = "收到短消息:" + msg + " 来自:" + from;
                    if (ClusterSchedulingActivity.handler != null) {
                        Message m = Message.obtain(ClusterSchedulingActivity.handler, MediaEngine.ME_MsgType.RECEIVE_MSG, str);
                        m.sendToTarget();
                    }
                } else if (msgType.equals(MediaEngine.ME_MsgType.MESSAGE_TYPE_APPLICATION)) {
                    //业务消息
                    String list[] = msg.split("\r\n");
                    if (list.length >= 4) {
                        if (list[0].equals(MediaEngine.ME_MsgType.MESSAGE_TYPE_APPLICATION_INDMSG)) {
                            MediaEngine.ME_Message txtMessage = new MediaEngine.ME_Message();
                            txtMessage.msgid = list[1].replace("msgid:", "");
                            txtMessage.body = list[2].replace("body:", "");
                            txtMessage.sender = list[3].replace("sendid:", "");
                            txtMessage.time = list[4].replace("time:", "");
                            txtMessage.receivers = list[5].replace("receiver:", "").split(",");

                            String str = "收到短消息:" + txtMessage.body + " 来自:" + txtMessage.sender;
                            if (ClusterSchedulingActivity.handler != null) {
                                Message m = Message.obtain(ClusterSchedulingActivity.handler, MediaEngine.ME_MsgType.RECEIVE_MSG, str);
                                m.sendToTarget();
                            }
                        } else if (list[0].equals(MediaEngine.ME_MsgType.MESSAGE_TYPE_APPLICATION_INDD_UPLOAD)) {
                            MediaEngine.ME_MessageFile messageFile = new MediaEngine.ME_MessageFile();
                            messageFile.sender = list[1].replace("srcnum:", "");
                            messageFile.fileName = list[2].replace("filename:", "");
                            messageFile.filePath = list[3].replace("filepath:", "");
                            messageFile.fileid = list[4].replace("fileid:", "");
                            int type = Integer.parseInt(list[5].replace("type:", ""));   //1=新文件通知 2=下载通知，仅当收到2时才进行实际的下载操作
                            messageFile.fileType = Integer.parseInt(list[6].replace("filetype:", ""));
                            messageFile.time = list[7].replace("time:", "");
                            //String str = "收到文件消息:" + list[2].replace("body:","") + " 来自:" + list[3].replace("sendid:","");
                            if (type == 1) {
                                //新文件通知
                                if (ClusterSchedulingActivity.handler != null) {
                                    Message m = Message.obtain(ClusterSchedulingActivity.handler, MediaEngine.ME_MsgType.RECEIVE_MSG_FILE, messageFile);
                                    m.sendToTarget();
                                }
                            } else if (type == 2) {
                                //文件下载通知，执行下载文件短信操作
                                if (ClusterSchedulingActivity.handler != null) {
                                    Message m = Message.obtain(ClusterSchedulingActivity.handler, MediaEngine.ME_MsgType.RECEIVE_MSG_FILE_STANDBY, messageFile);
                                    m.sendToTarget();
                                }
                            }
                        } else {
                            //其他业务消息
                        }
                    }
                }
            }

            //日志
            @Override
            public void onLogWriter(int i, String s) {
                LogUtils.e("onLogWriter" + i + s);
            }

            //对讲组状态
            @Override
            public void onPttStatus(String s, String s1) {
                LogUtils.e("onPttStatus" + s + s1);
                String str = s + "  讲话：" + s1;
                if (ClusterSchedulingActivity.handler != null) {
                    Message m = Message.obtain(ClusterSchedulingActivity.handler, MediaEngine.ME_MsgType.PTT_STATUS, str);
                    m.sendToTarget();
                }
            }

            //对讲申请/释放话权结果
            @Override
            public void onPttReqState(int i, String s) {
                LogUtils.e("onPttReqState" + i + s);
                if (ClusterSchedulingActivity.handler != null) {
                    Message m = Message.obtain(ClusterSchedulingActivity.handler, MediaEngine.ME_MsgType.PTT_REQ_STATE, s);
                    m.arg1 = i;
                    m.sendToTarget();
                }
            }

            @Override
            public void onPttWaiting(String s, String s1) {
                LogUtils.e("onPttWaiting" + s + s1);
            }

            @Override
            public void onPttReject(String s) {
                LogUtils.e("onPttReject" + s);
            }

            @Override
            public void onMemberRegStateChange(String s, int i) {
                LogUtils.e("onMemberRegStateChange" + s + i);
            }

            @Override
            public void onError(String s, String s1, String s2) {
                LogUtils.e("onError" + s + s1 + s2);
            }

            @Override
            public void onReceivePublishMsg(String s, String s1) {
                LogUtils.e("onReceivePublishMsg" + s + "-" + s1);
            }
        });
    }

    @Override
    protected void loadData() {

    }

    /**
     * 获取对讲组
     */
    private void getGroupInfo() {
        //异步方式获取对讲组
        me.ME_GetPttGroup_Async(me_pttGroupInfos -> {
            if (ClusterSchedulingActivity.handler != null) {
                Message m = Message.obtain(ClusterSchedulingActivity.handler, MediaEngine.ME_MsgType.PTT_GETGROUPINFO_CB, me_pttGroupInfos);
                m.sendToTarget();
            }

        });
    }

    /**
     * 通常在登录成功后获取一次，内含离线文字短信息或文件短信
     */
    private void getOfflineMessage() {
        //异步方式获取离线消息
        me.ME_GetOfflineMessage_Async((me_messages, me_messageFiles) -> {
            if (ClusterSchedulingActivity.handler != null) {
                Message m = Message.obtain(ClusterSchedulingActivity.handler, MediaEngine.ME_MsgType.RECEIVE_MSG_OFFLINE, me_messages);
                m.sendToTarget();
            }
            if (ClusterSchedulingActivity.handler != null) {
                Message m = Message.obtain(ClusterSchedulingActivity.handler, MediaEngine.ME_MsgType.RECEIVE_MSG_OFFLINE_FILE, me_messageFiles);
                m.sendToTarget();
            }
        }, 300);
    }

    /**
     * 设置默认摄像头,1=前置，2=后置
     */
    public void SetDefaultVideoCaptureDevice(int devId) {
        me.ME_SetDefaultVideoCaptureDevice(devId);
    }

    @Override
    public boolean handleMessage(Message msg) {
        LogUtils.e("返回" + msg.toString());
        switch (msg.what) {
            case 0:
                //退出系统
                me.ME_Destroy();
                finish();
                Runtime.getRuntime().gc();
                Process.killProcess(Process.myPid());
                break;
            case 1111:
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
//                Message m = Message.obtain(ClusterCallOneActivity.handler_, msg.arg1);
//                m.sendToTarget();
                showCallActivity();
                break;
            case MediaEngine.ME_MsgType.PTT_INCOMING:
                //来对讲，自动接听
                //设置语音接收增益
                me.ME_SetRxLevel(currentCallId, 2.0f);
                //发送事件
                MediaEngine.GetInstance().ME_Answer(ClusterSchedulingActivity.currentCallId, false);
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
                MediaEngine.ME_MessageFile fileMsg = (MediaEngine.ME_MessageFile) msg.obj;
                String txt1 = "收到新文件通知：" + fileMsg.fileName + ",路径：" + fileMsg.filePath + "，发送者：" + fileMsg.sender;
                Toast.makeText(this, txt1, Toast.LENGTH_LONG).show();
                break;
            case MediaEngine.ME_MsgType.RECEIVE_MSG_FILE_STANDBY:
                //收到文件消息-下载通知
                MediaEngine.ME_MessageFile fileMsg1 = (MediaEngine.ME_MessageFile) msg.obj;
                String txt2 = "收到文件下载通知：" + fileMsg1.fileName + ",路径：" + fileMsg1.filePath + "，发送者：" + fileMsg1.sender;

                Toast.makeText(this, txt2, Toast.LENGTH_LONG).show();
                //可以采用第三方插件通过HTTP协议下载文件，比如OKHTTP，下载路径为:serverIP:serverPort/fileMsg1.filePath，服务器端口默认是80。
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
                MediaEngine.ME_MessageFile[] fileMsgOffline = (MediaEngine.ME_MessageFile[]) msg.obj;
                String txt4 = "收到离线文件：\r\n";
                for (MediaEngine.ME_MessageFile me_messageFile : fileMsgOffline) {
                    txt4 += me_messageFile.fileName + ",路径：" + me_messageFile.filePath + "，发送者：" + me_messageFile.sender + "\r\n";
                }
                Toast.makeText(this, txt4, Toast.LENGTH_LONG).show();
                break;
            case MediaEngine.ME_MsgType.PTT_STATUS:
//                EventBus.getDefault().post(new EventBusBean(grps, mem, msg.obj.toString()));
                if (msg.obj.toString().contains(uName) && ISReqPtt) {
                    RceiveTimes += 1;
                    LogUtils.e("申请话权：" + ReqPttTimes + "收到信息：" + RceiveTimes);
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
                //获取对讲组信息异步回调
                MediaEngine.ME_PttGroupInfo[] g = (MediaEngine.ME_PttGroupInfo[]) msg.obj;
                if (g != null) {
                    grps.clear();
                    if (g.length > 0) {
                        for (int i = 0; i < g.length; i++) {
                            grps.add(g[i].groupNumber);
                            //异步方式获取对讲组成员
                            MediaEngine.GetInstance().ME_GetGroupMember_Async(g[i].groupNumber, (groupNumber, me_userInfos) -> {
                                List<String> member = new ArrayList<>();
                                if (me_userInfos.length > 0) {
                                    for (int j = 0; j < me_userInfos.length; j++) {
                                        member.add(me_userInfos[j].userId);
                                    }
                                }
                                mem.addAll(member);
                                //发送事件
//                                EventBus.getDefault().post(new EventBusBean(grps, member, currentSpeaker));
                            });
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

}
