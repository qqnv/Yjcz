package com.zzlecheng.yjcz.activity;

import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.base.BaseActivity;

import butterknife.BindView;

/**
 * @类名: EmergencyDrillActivity
 * @描述:
 * @作者: huangchao
 * @时间: 2019/1/8 2:22 PM
 * @版本: 1.0.0
 */
public class EmergencyDrillActivity extends BaseActivity {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_emergency_drill;
    }

    @Override
    protected void initView() {
        ibBack.setOnClickListener(v -> finish());
        tvTitle.setText("应急演练");
    }

    @Override
    protected void loadData() {

    }

}
