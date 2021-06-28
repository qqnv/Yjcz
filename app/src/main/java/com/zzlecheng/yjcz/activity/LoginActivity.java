package com.zzlecheng.yjcz.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushManager;
import com.zzlecheng.yjcz.MainActivity;
import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.base.BaseActivity;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.base.Commons;
import com.zzlecheng.yjcz.bean.LoginBean;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.PermissionUtils;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;
import com.zzlecheng.yjcz.utils.SpUtil;
import com.zzlecheng.yjcz.utils.TagAliasOperatorHelper;
import com.zzlecheng.yjcz.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zzlecheng.yjcz.utils.TagAliasOperatorHelper.ACTION_SET;

/**
 * @类名: LoginActivity
 * @描述: 登陆页
 * @作者: huangchao
 * @时间: 2018/8/20 下午2:37
 * @版本: 1.0.0
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_username)
    LinearLayout llUsername;
    @BindView(R.id.ll_password)
    LinearLayout llPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_ip)
    TextView tvIp;

    //用户名
    private String username;
    //用户密码
    private String password;
    //声明一个long类型变量：用于存放上一点击“返回键”的时刻
    private long mExitTime;

    public static String channelIds = "";

    public static int sequence = 1;

    private List<String> list = new ArrayList<>();

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        //检测读写权限
        PermissionUtils.verifyStoragePermissions(this);
        //隐藏密码
        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        //显示密码
//        etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        tvIp.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvIp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,
                ChangeIpActivity.class)));
        //若用户清除缓存，则自动为用户添加url
        if ("".equals(SharedPreferenceUtils.getInstance().getString("url"))) {
            SharedPreferenceUtils.getInstance().putString("url", Commons.BASE_URL);
        }

    }

    @Override
    protected void loadData() {
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        checkedLogin();
    }

    private void checkedLogin() {
        username = etUsername.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.showLongToast(this, "请输入账号!");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showLongToast(this, "请输入密码!");
            return;
        }
        login();
    }

    private void login() {
        if (channelIds.equals("")){
            ToastUtils.showShortToast(this,"请结束app进程后重新登录");
            return;
        }
        LogUtils.e("channelIds"+channelIds);
        HttpMethods.getHttpMethods().login(this, username, password, channelIds, "0",
                new BaseObserver<LoginBean>(this, true) {
                    @Override
                    protected void onHandleSuccess(LoginBean loginBean) {

                        PushManager.resumeWork(getApplicationContext());
                        list.add(loginBean.getId());
                        PushManager.setTags(getApplicationContext(), list);

                        SharedPreferenceUtils.getInstance().putString(username, password);
                        //账号
                        SharedPreferenceUtils.getInstance().putString("username", username);
                        //密码
                        SharedPreferenceUtils.getInstance().putString("password", password);
                        //部门ID
                        SharedPreferenceUtils.getInstance().putString("departmentid", loginBean.getBmid());
                        //人员ID
                        SharedPreferenceUtils.getInstance().putString("userid", loginBean.getId());
                        //人员姓名
                        SharedPreferenceUtils.getInstance().putString("user", loginBean.getName());
                        //组织机构
                        SharedPreferenceUtils.getInstance().putString("zzjgbm", loginBean.getZzjgbm());
                        //职位名称
                        SharedPreferenceUtils.getInstance().putString("zwmc", loginBean.getZwmc());
                        //初始化性别
                        SharedPreferenceUtils.getInstance().putString("sex","保密");

                        //保存关于集群调度的相关设置
                        SharedPreferenceUtils.getInstance().putString("azkjid",loginBean.getNumbers());
                        SharedPreferenceUtils.getInstance().putInt("uResolution",Commons.U_RESOLUTION);

                        SpUtil.getInstance().putBoolean(Commons.HAS_LOGIN, true);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtils.showShortToastSafe(LoginActivity.this, "服务器连接异常");
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

}
