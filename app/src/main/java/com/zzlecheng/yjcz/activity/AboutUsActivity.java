package com.zzlecheng.yjcz.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.utils.AppUpdateUtils;
import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.base.BaseActivity;
import com.zzlecheng.yjcz.base.Commons;
import com.zzlecheng.yjcz.utils.UpdateAppHttpUtil;

import butterknife.BindView;

/**
 * @类名: AboutUsActivity
 * @描述: 关于我们
 * @作者: huangchao
 * @时间: 2018/7/25 下午4:58
 * @版本: 1.0.0
 */
public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
    @BindView(R.id.iv_qr)
    ImageView ivQr;
    @BindView(R.id.currentVersion)
    TextView currentVersion;
    @BindView(R.id.tv_checkVersion)
    TextView tvCheckVersion;

    private String appVersion;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_aboutus;
    }

    @Override
    protected void initView() {
        appVersion = AppUpdateUtils.getVersionName(this);
        //设置标题头
        tvTitle.setText(R.string.about_us);
        //设置当前版本号
        currentVersion.setText(appVersion);
        //设置当前apk下载二维码
        Bitmap qr = CodeUtils.createImage(Commons.APP_DOWN, 600, 600,
                BitmapFactory.decodeResource(getResources(), R.mipmap.icon));
        ivQr.setImageBitmap(qr);
        ibBack.setOnClickListener(view -> finish());
        tvCheckVersion.setOnClickListener(v -> updateApp());
    }

    /**
     * app更新
     */
    public void updateApp() {
        new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(this)
                //更新地址
                .setUpdateUrl(Commons.APP_DOWN + "?app=0&numbers=" + appVersion)
                .handleException(e -> e.printStackTrace())
                //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                .build()
                .update();
    }

    @Override
    protected void loadData() {

    }
}
