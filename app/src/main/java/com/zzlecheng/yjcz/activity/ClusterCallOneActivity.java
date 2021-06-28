package com.zzlecheng.yjcz.activity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.base.BaseActivity;
import com.zzlecheng.yjcz.utils.LogUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import cn.sword.SDK.MediaEngine;

import static android.view.OrientationEventListener.ORIENTATION_UNKNOWN;
import static cn.sword.SDK.MediaEngine.GetInstance;
import static com.zzlecheng.yjcz.MainActivity.clusterCallId;
import static com.zzlecheng.yjcz.MainActivity.clusterIsVideo;
import static com.zzlecheng.yjcz.MainActivity.isMonitor;
import static com.zzlecheng.yjcz.activity.ClusterSchedulingActivity.isVideo;
import static com.zzlecheng.yjcz.activity.ClusterSchedulingActivity.me;

/**
 * @类名: ClusterCallOneActivity
 * @描述: 视频通话界面
 * @作者: huangchao
 * @时间: 2019/1/21 2:27 PM
 * @版本: 1.0.0
 */
public class ClusterCallOneActivity extends BaseActivity implements Handler.Callback, SurfaceHolder.Callback, View.OnClickListener {


    public static Handler handler_;
    private final Handler handler = new Handler(this);
    @BindView(R.id.sfv_video)
    SurfaceView sfvVideo;
    @BindView(R.id.sfv_preview)
    SurfaceView sfvPreview;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_mute)
    ImageView ivMute;
    @BindView(R.id.iv_handsFree)
    ImageView ivHandsFree;
    @BindView(R.id.iv_isPreview)
    ImageView ivIsPreview;
    @BindView(R.id.ll_isPreview)
    LinearLayout llIsPreview;
    @BindView(R.id.iv_selfAdaption)
    ImageView ivSelfAdaption;
    @BindView(R.id.ll_selfAdaption)
    LinearLayout llSelfAdaption;
    @BindView(R.id.iv_hangUp)
    ImageView ivHangUp;
    @BindView(R.id.iv_answerAudio)
    ImageView ivAnswerAudio;
    @BindView(R.id.iv_answerVideo)
    ImageView ivAnswerVideo;
    @BindView(R.id.ll_dealWith)
    LinearLayout llDealWith;

    private String decoderId = "";
    private String sessionId = "";
    public String remoteNumber = "";
    private int channelId = 0;
    private SensorManager sm;
    private Sensor sensor;
    private int degree = 0;
    private static AudioManager am = null;
    private boolean showPreview = true;
    private int totalCallTime = 0;
    private String name = "";
    private Boolean isComing = true;

    private boolean isMt = false;
    private boolean isJy = false;
    //第一次进入界面时是来电还是拨出
    private boolean isCome = true;


    @Override
    protected int setLayoutRes() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        return R.layout.activity_cluster_call_one;
    }

    @Override
    protected void initView() {
        handler_ = handler;
        me.ME_SetSurfaceView(sfvPreview, sfvVideo);
        setupVideoPreview(sfvPreview);
        updateCallState(ClusterSchedulingActivity.lastInfo_Number,
                ClusterSchedulingActivity.lastInfo_State);
        sm = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        // 获取传感器类型
        sensor = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
//        sm.registerListener(mSensorListener, sensor, SensorManager.SENSOR_DELAY_UI);
        ivMute.setOnClickListener(this);
        ivHandsFree.setOnClickListener(this);
        ivIsPreview.setOnClickListener(this);
        ivSelfAdaption.setOnClickListener(this);
        ivHangUp.setOnClickListener(this);
        ivAnswerVideo.setOnClickListener(this);
        ivAnswerAudio.setOnClickListener(this);
        if (isCome) {
            isVideo = clusterIsVideo;
            isComing = true;
        }
        if (isMonitor) {
            tvName.setVisibility(View.GONE);
            tvTime.setVisibility(View.GONE);
            sfvPreview.setVisibility(View.GONE);
            sfvVideo.setVisibility(View.VISIBLE);
            llDealWith.setVisibility(View.GONE);
            ivAnswerAudio.setVisibility(View.GONE);
            ivAnswerVideo.setVisibility(View.GONE);
            acceptCall(true);
        }
        isShow();
    }

    private void isShow() {
        LogUtils.e("视频界面信息：" + "是来电：" + isComing + "是视频：" + isVideo);
        if (isComing) {//来电
            if (isVideo) {//是视频
                //不显示状态
                tvState.setVisibility(View.GONE);
            } else {//是音频
                sfvPreview.setVisibility(View.GONE);
                sfvVideo.setVisibility(View.GONE);
                llIsPreview.setVisibility(View.GONE);
                llSelfAdaption.setVisibility(View.GONE);
                ivAnswerVideo.setVisibility(View.GONE);
            }
        } else {//本机拨打出去
            ivAnswerAudio.setVisibility(View.GONE);
            ivAnswerVideo.setVisibility(View.GONE);
            if (isVideo) {//是视频
                tvState.setVisibility(View.GONE);
            } else {//是音频
                sfvPreview.setVisibility(View.GONE);
                sfvVideo.setVisibility(View.GONE);
                llIsPreview.setVisibility(View.GONE);
                llSelfAdaption.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        sfvPreview.bringToFront();
    }

    public void setupVideoPreview(SurfaceView surfacePreview) {
        if (showPreview) {
            Log.w("TAG", "显示预览~~~~"); //still not work...
            surfacePreview.setZOrderOnTop(true);
            surfacePreview.setZOrderMediaOverlay(true);
        }
    }

    private SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            int orientation = ORIENTATION_UNKNOWN;
            float X = -values[0];
            float Y = -values[1];
            float Z = -values[2];
            float magnitude = X * X + Y * Y;
            if (magnitude * 4 >= Z * Z) {
                // 屏幕旋转时
                float OneEightyOverPi = 57.29577957855f;
                float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
                orientation = 90 - Math.round(angle);
                while (orientation >= 360) {
                    orientation -= 360;
                }
                while (orientation < 0) {
                    orientation += 360;
                }
            }

            if (orientation > 45 && orientation < 135) {
                degree = 90;
            } else if (orientation > 135 && orientation < 225) {
                degree = 180;
            } else if (orientation > 225 && orientation < 315) {
                degree = 270;
            } else if ((orientation > 315 && orientation < 360) || (orientation > 0 && orientation < 45)) {
                degree = 0;
            }

            degree += 90; //初始竖屏旋转90度
            if (degree == 360) {
                degree = 0;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public boolean handleMessage(Message m) {

        isCome = false;

        LogUtils.e("视频界面信息：" + m.what + "-" + m.arg1 + "-" + m.arg2 + "-" + m.obj);

        if ("".equals(m.obj) || null != m.obj) {
            name = String.valueOf(m.obj);
        }
        tvName.setText(name);
        tvTime.setText(String.format("%02d:%02d", totalCallTime / 60, totalCallTime % 60));
        //what 3-拨打电话 4和5-接通电话 6对方挂断电话 300定时器
        if (m.what == 6) {//对方挂断
            finish();
        } else if (m.what == 4) {//接通电话
            tvState.setText("正在通话");
            setTimer();
            isComing = false;
            isShow();
        } else if (m.what == 3) {//拨打电话中
            tvState.setText("拨号中");
            isComing = false;
        } else if (m.what == MY_MSG_TYPE.UPDATE_DOWN_LOST_RATIO) {//定时器

        }


        if (m.what == MediaEngine.ME_MsgType.CALL_STATE) {
            updateCallState(m.obj.toString(), m.arg2);
        } else if (m.what == MediaEngine.ME_MsgType.CALL_MEDIA_STATE) {
            onConfigurationChanged(getResources().getConfiguration());
        } else if (m.what == MY_MSG_TYPE.GET_DECODERLIST_CALLBACK) {
            //获取解码器列表异步返回
            MediaEngine.ME_DecoderInfo[] infos = (MediaEngine.ME_DecoderInfo[]) m.obj;
            if (null != infos && infos.length > 0) {
                if (decoderId.equals(""))
                    decoderId = infos[0].deviceId;
                //获取物理显示通道
                GetDecoderDisplays_Async(infos[0]);
            }
        } else if (m.what == MY_MSG_TYPE.GET_DECODER_DISPLAYS_CALLBACK) {
            //获取解码器物理显示通道异步返回，获取session，进行解码
            GetMySession_Async();
        } else if (m.what == MY_MSG_TYPE.GET_MY_SESSION_CALLBACK) {
            //获取会话回调，开始异步解码
            StartDec_Async();
        } else if (m.what == -99) {
            hangupCall(null);
        } else {
            return false;
        }
        return true;
    }

    private void updateCallState(String remoteNumber, int state) {

        switch (state) {
            case MediaEngine.ME_CallState.STATE_CALLING:
                break;
            case MediaEngine.ME_CallState.STATE_INCOMING://来电了
                ivAnswerAudio.setVisibility(View.VISIBLE);
                ivAnswerVideo.setVisibility(View.VISIBLE);
                break;
            case MediaEngine.ME_CallState.STATE_CONNECTING:
                break;
            case MediaEngine.ME_CallState.STATE_CONFIRMED:
                break;
            case MediaEngine.ME_CallState.STATE_DISCONNECTED:
                hangupCall(null);
                break;
        }
        this.remoteNumber = remoteNumber;
    }

    //异步解码上墙，获取解码器物理显示通道
    public void GetDecoderDisplays_Async(MediaEngine.ME_DecoderInfo decoder) {
        if (null == decoder) {
            Log.e("TAG", "获取解码器物理显示通道，解码器信息为空！please check....");
            return;
        }
        GetInstance().ME_GetDecoderDisplays_Async(decoder.deviceId, channels -> {
            for (MediaEngine.ME_DecoderDisplayInfo channel : channels) {
                String str = "";
                switch (channel.displayType) {
                    case MediaEngine.ME_DecoderDisplayType.BNC:
                        str = "BNC";
                        break;
                    case MediaEngine.ME_DecoderDisplayType.HDMI:
                        str = "HDMI";
                        break;
                    case MediaEngine.ME_DecoderDisplayType.VGA:
                        str = "VGA";
                        break;
                    case MediaEngine.ME_DecoderDisplayType.DVI:
                        str = "DVI";
                        if (channelId == 0)
                            channelId = channel.decodeChannelIds[0];
                        break;
                    default:
                        str = "unknow";
                        break;
                }
                str += ",解码通道列表:";
                for (int id : channel.decodeChannelIds) {
                    str += id + ",";
                }
                Log.w("TAG", "显示通道ID：" + channel.displayId + "，类型：" + channel.displayType + "," + str);
            }
            if (this.handler_ != null) {
                Message m2 = Message.obtain(this.handler_,
                        MY_MSG_TYPE.GET_DECODER_DISPLAYS_CALLBACK, null);
                m2.sendToTarget();
            }
        });
    }

    //获取会话，异步方式
    public void GetMySession_Async() {
        GetInstance().ME_GetMySessionList_Async(sessionInfos -> {
            for (MediaEngine.ME_SessionInfo info : sessionInfos) {
                Log.w("TAG", "sid:" + info.sessionId + ",type:" + info.sessionType + "" +
                        ",name:" + info.callingName + ",number:" + info.callingNumber + ",isVideo:" + info.isVideo);
            }
            if (sessionInfos.length == 0)
                return;
            sessionId = sessionInfos[0].sessionId;
            if (ClusterCallOneActivity.handler_ != null) {
                Message m2 = Message.obtain(ClusterCallOneActivity.handler_,
                        MY_MSG_TYPE.GET_MY_SESSION_CALLBACK, null);
                m2.sendToTarget();
            }
        });
    }

    //开始解码，异步方式
    public void StartDec_Async() {
        if (null == sessionId || sessionId.equals("")) return;
        Log.d("TAG", "开始异步方式解码，参数：sessionId=" + sessionId + ",remoteNumber=" + remoteNumber +
                ",decoderId=" + decoderId + ",channelId=" + channelId);
        GetInstance().ME_StartDecode_Async(sessionId, remoteNumber, decoderId, channelId,
                ret -> Log.w("TAG", "开始解码：" + ret));
    }

    /**
     * 接听
     */
    public void acceptCall(boolean isVideos) {
        setTimer();
        //接听隐藏掉接听视频和接听音频按钮
        ivAnswerVideo.setVisibility(View.GONE);
        ivAnswerAudio.setVisibility(View.GONE);
        if (isVideos) {//若是视频接听则显示视频和视频处理按钮
            sfvPreview.setVisibility(View.VISIBLE);
            sfvVideo.setVisibility(View.VISIBLE);
            llIsPreview.setVisibility(View.VISIBLE);
            llSelfAdaption.setVisibility(View.VISIBLE);
            tvState.setVisibility(View.GONE);
        } else {
            sfvPreview.setVisibility(View.GONE);
            sfvVideo.setVisibility(View.GONE);
            llIsPreview.setVisibility(View.GONE);
            llSelfAdaption.setVisibility(View.GONE);
            tvState.setVisibility(View.VISIBLE);
        }

        me.ME_Answer(clusterCallId, isVideos);
//        if (view != null)
//            view.setVisibility(View.GONE);
    }

    /**
     * 挂断
     *
     * @param view
     */
    public void hangupCall(View view) {
        handler_ = null;
        me.ME_Hangup(clusterCallId);
        finish();
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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mute://静音
                int yl;
                if (isJy) {//静音
                    yl = 0;
                    ivMute.setImageResource(R.mipmap.mute);
                } else {//不是静音
                    yl = 1;
                    ivMute.setImageResource(R.mipmap.cluster_voice);
                }
                me.ME_SetRxLevel(clusterCallId, yl);
                isJy = !isJy;
                break;
            case R.id.iv_handsFree://免提
                isMt = !isMt;
                if (isMt) {//是免提
                    ivHandsFree.setImageResource(R.mipmap.horn);
                } else {//不是免提
                    ivHandsFree.setImageResource(R.mipmap.denoise);
                }
                SetSpeakerPhone(isMt);
                break;
            case R.id.iv_isPreview://预览
                if (showPreview) {
                    sfvPreview.setVisibility(View.GONE);
                } else {
                    sfvPreview.setVisibility(View.VISIBLE);
                }
                showPreview = !showPreview;
                break;
            case R.id.iv_selfAdaption://自适应

                break;
            case R.id.iv_hangUp://挂断
                hangupCall(v);
                break;
            case R.id.iv_answerAudio://语音接听
                acceptCall(false);
                break;
            case R.id.iv_answerVideo://视频接听
                acceptCall(true);
                break;
        }
    }

    public class MY_MSG_TYPE {
        public final static int UPDATE_DOWN_LOST_RATIO = 300;
        public final static int GET_DECODERLIST_CALLBACK = 400;
        public final static int GET_DECODER_DISPLAYS_CALLBACK = 401;
        public final static int GET_MY_SESSION_CALLBACK = 402;
    }

    @Override
    protected void onDestroy() {
        sm.unregisterListener(mSensorListener);
        SetSpeakerPhone(false);
        super.onDestroy();
        handler_ = null;
    }

    /**
     * 定时器
     */
    private void setTimer() {

        tvTime.setTextColor(Color.RED);
        Timer timer = null;
        TimerTask timerTask = null;
        if (timer == null) {
            timer = new Timer();
        }
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    totalCallTime++;
                    if (ClusterCallOneActivity.handler_ != null) {
                        Message m2 = Message.obtain(ClusterCallOneActivity.handler_,
                                MY_MSG_TYPE.UPDATE_DOWN_LOST_RATIO, null);
                        m2.sendToTarget();
                    }
                }
            };
        }
        timer.schedule(timerTask, 2000, 1000);
    }

}
