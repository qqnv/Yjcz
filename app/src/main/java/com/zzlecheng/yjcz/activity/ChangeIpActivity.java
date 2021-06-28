package com.zzlecheng.yjcz.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.base.BaseActivity;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;
import com.zzlecheng.yjcz.utils.ToastUtils;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

import static com.zzlecheng.yjcz.activity.LoginActivity.sequence;

/**
 * @类名: ChangeIpActivity
 * @描述: 更改IP设置
 * @作者: huangchao
 * @时间: 2018/12/5 10:46 AM
 * @版本: 1.0.0
 */
public class ChangeIpActivity extends BaseActivity {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
    @BindView(R.id.tv_ip)
    TextView tvIp;
    @BindView(R.id.et_ip)
    EditText etIp;
    @BindView(R.id.ll_ip)
    LinearLayout llIp;
    @BindView(R.id.tv_dkh)
    TextView tvDkh;
    @BindView(R.id.et_dkh)
    EditText etDkh;
    @BindView(R.id.ll_dkh)
    LinearLayout llDkh;
    @BindView(R.id.tv_differ)
    TextView tvDiffer;
    @BindView(R.id.btn_sure)
    Button btnSure;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_change_ip;
    }

    @Override
    protected void initView() {
        tvDiffer.setText("当前IP：" + SharedPreferenceUtils.getInstance().getString("url"));
        etIp.setText("192.168.100.");
        etDkh.setText("8080");
        tvTitle.setText("IP及端口号更改");
        ibBack.setOnClickListener(v -> finish());
        btnSure.setOnClickListener(v -> {
            if (etIp.getText().toString() != "" || etDkh.getText().toString() != "") {
                //清除本地缓存
                SharedPreferenceUtils.getInstance().clear();
                //清除别名
                JPushInterface.deleteAlias(this, sequence);
                String url = "http://" + etIp.getText().toString().trim() + ":" +
                        etDkh.getText().toString().trim() + "/yjcz/";
                SharedPreferenceUtils.getInstance().putString("url", url);
                SharedPreferenceUtils.getInstance().putBoolean("changed", true);
                //跳转到登录页
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                ToastUtils.showShortToast(this, "请填写IP和端口号");
            }
        });
    }

    @Override
    protected void loadData() {

    }

}
