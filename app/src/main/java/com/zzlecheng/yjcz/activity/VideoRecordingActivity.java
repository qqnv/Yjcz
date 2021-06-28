package com.zzlecheng.yjcz.activity;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.base.BaseActivity;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.bean.CommonBean;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.CameraUtil;
import com.zzlecheng.yjcz.utils.FileUtil;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.RecorderStatus;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;
import com.zzlecheng.yjcz.utils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @类名: VideoRecordingActivity
 * @描述: 录制视频
 * @作者: huangchao
 * @时间: 2018/9/28 上午9:54
 * @版本: 1.0.0
 */
public class VideoRecordingActivity extends BaseActivity implements TextureView.SurfaceTextureListener {
    //视图控件
    private TextureView mTextureV;
    private SurfaceTexture mSurfaceTexture;
    private Chronometer chronometer;
    private TextView tvCommit;
    private TextView tvRecordControl;

    //存储文件
    private Camera mCamera;
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private int mRotationDegree;

    //录制相关参数
    private MediaRecorder mMediaRecorder;
    private int mFps;//帧率
    private RecorderStatus mStatus = RecorderStatus.RELEASED;//录制状态

    private String pid = "";

    //录制出错的回调
    private MediaRecorder.OnErrorListener onErrorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            try {
                if (mMediaRecorder != null) {
                    mMediaRecorder.reset();
                }
            } catch (Exception e) {
                Toast.makeText(VideoRecordingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected int setLayoutRes() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return R.layout.activity_video_recording;
    }

    @Override
    protected void initView() {
        hideStatusBarAndNavBar(this);
        FileUtil.init(this);
        mTextureV = findViewById(R.id.surface);
        mTextureV.setSurfaceTextureListener(this);
        chronometer = findViewById(R.id.record_time);
        chronometer.setFormat("%s");
        Bundle bundle = getIntent().getExtras();
        pid = bundle.getString("pid");
        tvRecordControl = findViewById(R.id.record_control);
        tvCommit = findViewById(R.id.tv_updates);
        tvCommit.setOnClickListener(v -> commitVideo());
    }

    /**
     * 隐藏导航栏和状态栏
     */
    public static void hideStatusBarAndNavBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void loadData() {

    }


    /**
     * 开始录制和停止录制
     *
     * @param v
     */
    public void control(View v) {
        if (mStatus == RecorderStatus.RECORDING) {
            stopRecord();
            tvRecordControl.setText("录制完成");
            tvCommit.setVisibility(View.VISIBLE);
        } else {
            startRecord();
            tvRecordControl.setText("点击停止录制");
            tvCommit.setVisibility(View.GONE);
        }
    }

    /**
     * 开始录制
     */
    private void startRecord() {
        initCamera();
        mCamera.unlock();
        initMediaRecorder();
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        mStatus = RecorderStatus.RECORDING;
    }

    /**
     * 停止录制
     */
    private void stopRecord() {
        releaseMediaRecorder();
        releaseCamera();
    }

    private void commitVideo() {
        String absolutePath = "/storage/emulated/0/.a_media/yjcz.mp4";
        HttpMethods.getHttpMethods().reportPicture(this, pid, "1","0", new File(absolutePath),
                new BaseObserver<CommonBean>(this, false) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        ToastUtils.showShortToast(VideoRecordingActivity.this, "视频上传成功");
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LogUtils.e(e.toString());
                    }
                });
    }

    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {
        mSurfaceTexture = surface;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        //缓冲区大小变化时回调，该方法不需要用户自己处理
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        stopRecord();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        //当每一帧数据可用时回调，不做处理
    }

    /**
     * 初始化相机
     */
    private void initCamera() {
        if (mSurfaceTexture == null) return;
        if (mCamera != null) {
            releaseCamera();
        }

        mCamera = Camera.open(mCameraId);
        if (mCamera == null) {
            Toast.makeText(this, "没有可用相机", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            mCamera.setPreviewTexture(mSurfaceTexture);
            mRotationDegree = CameraUtil.getCameraDisplayOrientation(this, mCameraId);
            mCamera.setDisplayOrientation(mRotationDegree);
            setCameraParameter(mCamera);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置相机的参数
     *
     * @param camera
     */
    private void setCameraParameter(Camera camera) {
        if (camera == null) return;
        Camera.Parameters parameters = camera.getParameters();
        //获取相机支持的>=20fps的帧率，用于设置给MediaRecorder
        //因为获取的数值是*1000的，所以要除以1000
        List<int[]> previewFpsRange = parameters.getSupportedPreviewFpsRange();
        for (int[] ints : previewFpsRange) {
            if (ints[0] >= 20000) {
                mFps = ints[0] / 1000;
                break;
            }
        }
        //设置聚焦模式
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }


        //设置预览尺寸,因为预览的尺寸和最终是录制视频的尺寸无关，所以我们选取最大的数值
        //通常最大的是手机的分辨率，这样可以让预览画面尽可能清晰并且尺寸不变形，前提是TextureView的尺寸是全屏或者接近全屏
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        parameters.setPreviewSize(supportedPreviewSizes.get(0).width, supportedPreviewSizes.get(0).height);
        //缩短Recording启动时间
        parameters.setRecordingHint(true);
        //是否支持影像稳定能力，支持则开启
        if (parameters.isVideoStabilizationSupported())
            parameters.setVideoStabilization(true);
        camera.setParameters(parameters);
    }


    /**
     * 释放相机
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    /**
     * 初始化MediaRecorder
     */
    private void initMediaRecorder() {
        //如果是处于release状态，那么只有重新new一个进入initial状态
        //否则其他状态都可以通过reset()方法回到initial状态
        if (mStatus == RecorderStatus.RELEASED) {
            mMediaRecorder = new MediaRecorder();
        } else {
            mMediaRecorder.reset();
        }
        //设置选择角度，顺时针方向，因为默认是逆向90度的，这样图像就是正常显示了,这里设置的是观看保存后的视频的角度
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setOrientationHint(mRotationDegree);
        mMediaRecorder.setOnErrorListener(onErrorListener);
        //采集声音来源、mic是麦克风
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //采集图像来源、
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //设置编码参数
//        setProfile();
        setConfig();

        mMediaRecorder.setPreviewDisplay(new Surface(mSurfaceTexture));
        //设置输出的文件路径
        mMediaRecorder.setOutputFile(FileUtil.newMp4File().getAbsolutePath());
    }


    /**
     * 释放MediaRecorder
     */
    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            if (mStatus == RecorderStatus.RELEASED) return;
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mStatus = RecorderStatus.RELEASED;
            //停止计时
            chronometer.stop();
        }
    }


    /**
     * 通过系统的CamcorderProfile设置MediaRecorder的录制参数
     */
    private void setProfile() {
        CamcorderProfile profile = null;
        if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_1080P)) {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_1080P);
        } else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_720P)) {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
        } else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_480P)) {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);

        }
        if (profile != null) {
            mMediaRecorder.setProfile(profile);
        }
    }


    /**
     * 自定义MediaRecorder的录制参数
     */
    private void setConfig() {
        //设置封装格式 默认是MP4
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        //音频编码
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //图像编码
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //声道
        mMediaRecorder.setAudioChannels(1);
        //设置最大录像时间 单位：毫秒
        mMediaRecorder.setMaxDuration(60 * 1000);
        //设置最大录制的大小60M 单位，字节
        mMediaRecorder.setMaxFileSize(60 * 1024 * 1024);
        //再用44.1Hz采样率
        mMediaRecorder.setAudioEncodingBitRate(22050);
        //设置帧率，该帧率必须是硬件支持的，可以通过Camera.CameraParameter.getSupportedPreviewFpsRange()方法获取相机支持的帧率
        mMediaRecorder.setVideoFrameRate(mFps);
        //设置码率
        mMediaRecorder.setVideoEncodingBitRate(500 * 1024 * 8);
        //设置视频尺寸，通常搭配码率一起使用，可调整视频清晰度
        mMediaRecorder.setVideoSize(1280, 720);
    }

}
