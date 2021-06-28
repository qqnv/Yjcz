package com.zzlecheng.yjcz.activity;

import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.base.BaseActivity;

import butterknife.BindView;

/**
 * @类名: SwitchAccountActivity
 * @描述: 切换账号
 * @作者: huangchao
 * @时间: 2018/9/4 下午12:06
 * @版本: 1.0.0
 */
public class SwitchAccountActivity extends BaseActivity {


    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_switch_account;
    }

    @Override
    protected void initView() {
        tvTitle.setText(R.string.switch_account);
        ibBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void loadData() {

    }

}
