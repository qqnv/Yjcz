package com.zzlecheng.yjcz.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.base.BaseActivity;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.bean.CommonBean;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.ActivityManager;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;
import com.zzlecheng.yjcz.utils.ToastUtils;

import butterknife.BindView;

/**
 * @类名: ChangePassWordActivity
 * @描述: 更改密码
 * @作者: huangchao
 * @时间: 2018/7/25 下午4:48
 * @版本: 1.0.0
 */
public class ChangePassWordActivity extends BaseActivity {


    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
    @BindView(R.id.tv_oldPwd)
    TextView tvOldPwd;
    @BindView(R.id.et_oldPwd)
    EditText etOldPwd;
    @BindView(R.id.ll_oldPwd)
    LinearLayout llOldPwd;
    @BindView(R.id.tv_newPwd)
    TextView tvNewPwd;
    @BindView(R.id.et_newPwd)
    EditText etNewPwd;
    @BindView(R.id.ll_newPwd)
    LinearLayout llNewPwd;
    @BindView(R.id.tv_newPwdSure)
    TextView tvNewPwdSure;
    @BindView(R.id.et_newPwdSure)
    EditText etNewPwdSure;
    @BindView(R.id.ll_newPwdSure)
    LinearLayout llNewPwdSure;
    @BindView(R.id.tv_differ)
    TextView tvDiffer;
    @BindView(R.id.btn_sure)
    Button btnSure;

    //两次输入的密码是否一致
    private boolean isNewAgain = false;
    //用户ID
    private String userId;
    //用户新密码
    private String mm;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void initView() {
        tvTitle.setText(R.string.change_pwd);
        ibBack.setOnClickListener(view -> finish());
        userId = SharedPreferenceUtils.getInstance().getString("userid");
    }

    @Override
    protected void loadData() {

        etNewPwdSure.addTextChangedListener(new MyEditTextChangeListener());

        etOldPwd.setOnFocusChangeListener((view, b) -> {
            if (!b) {//失去焦点处理
                if (!isRightOld()) {
                    ToastUtils.showShortToast(ChangePassWordActivity.this, "您输入的原密码不正确，请重新输入");
                }
            }
        });

        btnSure.setOnClickListener(v -> updateWord());

    }

    /**
     * 确认提交密码
     */
    private void updateWord() {
        if (etOldPwd.getText().toString().equals("")) {
            ToastUtils.showShortToast(this, "请输入旧密码");
            return;
        }
        if (!isRightOld()) {
            ToastUtils.showShortToast(this, "请输入正确的旧密码");
            return;
        }
        if (etNewPwd.getText().toString().equals("")) {
            ToastUtils.showShortToast(this, "请输入新密码");
            return;
        }
        if (!isNewAgain) {
            ToastUtils.showShortToast(this, "两次输入的密码不一致");
            return;
        }
        mm = etNewPwdSure.getText().toString();
        HttpMethods.getHttpMethods().changeWord(this, userId, mm, "0",
                new BaseObserver<CommonBean>(this, false) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        ToastUtils.showShortToast(ChangePassWordActivity.this, "密码修改成功请重新登录");
                        SharedPreferenceUtils.getInstance().clear();
                        startActivity(new Intent(ChangePassWordActivity.this, LoginActivity.class));
                        ActivityManager.finishAll();
                    }
                });
    }

    class MyEditTextChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().equals(etNewPwd.getText().toString())) {
                tvDiffer.setText("");
                isNewAgain = true;
            } else {
                tvDiffer.setText(R.string.differ);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    /**
     * true则密码正确，false则密码错误
     *
     * @return
     */
    private boolean isRightOld() {
        String oldPassword = SharedPreferenceUtils.getInstance().getString("password");
        if (!etOldPwd.getText().toString().equals("")) {
            if (oldPassword.equals(etOldPwd.getText().toString())) {
                return true;
            }
        }
        return false;
    }

}
