package com.zzlecheng.yjcz.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.base.BaseActivity;

import butterknife.BindView;

/**
 * @类名: VideoPlayActivity
 * @描述: 视频播放页
 * @作者: huangchao
 * @时间: 2018/10/18 上午9:14
 * @版本: 1.0.0
 */
public class VideoPlayActivity extends BaseActivity {

    @BindView(R.id.vv_Audio)
    VideoView vvAudio;
    private String url = "";

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_video_play;
    }

    @Override
    protected void initView() {
        //隐藏系统状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        vvAudio.setMediaController(new MediaController(this));
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");
        //网络视频
//        vvAudio.setVideoPath(url);
        //服务器地址
        vvAudio.setVideoURI(Uri.parse(url));
        vvAudio.requestFocus();
        vvAudio.start();
    }

    @Override
    protected void loadData() {

    }
}
