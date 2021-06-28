package com.zzlecheng.yjcz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.adapter.TestArrayAdapter;
import com.zzlecheng.yjcz.base.BaseActivity;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zzlecheng.yjcz.MainActivity.AZKJ_CODE;

/**
 * @类名: ClusterSetActivity
 * @描述: 设置配置界面
 * @作者: huangchao
 * @时间: 2019/1/17 10:46 AM
 * @版本: 1.0.0
 */
public class ClusterSetActivity extends BaseActivity {


    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_setUp)
    ImageButton ibSetUp;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
    @BindView(R.id.tv_ip)
    TextView tvIp;
    @BindView(R.id.et_ip)
    EditText etIp;
    @BindView(R.id.tv_dkh)
    TextView tvDkh;
    @BindView(R.id.et_dkh)
    EditText etDkh;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_pwd)
    TextView tvPwd;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_bitrate)
    TextView tvBitrate;
    @BindView(R.id.et_bitrate)
    EditText etBitrate;
    @BindView(R.id.et_fps)
    EditText etFps;
    @BindView(R.id.tv_resolution)
    TextView tvResolution;
    @BindView(R.id.sp_resolution)
    Spinner spResolution;
    @BindView(R.id.cb_autoAnswer)
    CheckBox cbAutoAnswer;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_sure)
    Button btnSure;
    //分辨率设置
    private int uResolution;

    private ArrayAdapter<String> mAdapter;
    private String[] mStringArray;

    @Override

    protected int setLayoutRes() {
        return R.layout.activity_cluster_set;
    }

    @Override
    protected void initView() {

        ibBack.setOnClickListener(v -> finish());
        tvTitle.setText(R.string.cluster_set);

        mStringArray = getResources().getStringArray(R.array.resolution);
        //使用自定义的ArrayAdapter
        mAdapter = new TestArrayAdapter(this, mStringArray);
        spResolution.setAdapter(mAdapter);
        reSet();
        //点击重置
        btnCancel.setOnClickListener(v -> {
            reSet();
        });
        spResolution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String[] resolution = getResources().getStringArray(R.array.resolution);
                uResolution = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSure.setOnClickListener(v -> {
            //设置修改后的值
            SharedPreferenceUtils.getInstance().putInt("uResolution", uResolution);
            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
            finish();
        });
    }

    @Override
    protected void loadData() {

    }

    /**
     * 获取登录时设置的默认值
     */
    private void reSet() {
        spResolution.setSelection(SharedPreferenceUtils.getInstance().getInt("uResolution"), true);

    }

}
