package com.zzlecheng.yjcz.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.othershe.library.NiceImageView;
import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.base.BaseActivity;
import com.zzlecheng.yjcz.utils.BitmapOption;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @类名: PersonalActivity
 * @描述: 个人信息页
 * @作者: huangchao
 * @时间: 2018/7/25 下午4:47
 * @版本: 1.0.0
 */
public class PersonalActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ib_back)
    ImageButton ibBack;//返回
    @BindView(R.id.tv_title)
    TextView tvTitle;//标题
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;//标题头
    @BindView(R.id.nv_portrait)
    NiceImageView nvPortrait;//用户头像
    @BindView(R.id.rl_portrait)
    RelativeLayout rlPortrait;
    @BindView(R.id.tv_username)
    TextView tvUsername;//用户名
    @BindView(R.id.rl_username)
    RelativeLayout rlUsername;
    @BindView(R.id.tv_sex)
    TextView tvSex;//性别
    @BindView(R.id.rl_sex)
    RelativeLayout rlSex;
    @BindView(R.id.tv_company)
    TextView tvCompany;//单位
    @BindView(R.id.rl_company)
    RelativeLayout rlCompany;
    @BindView(R.id.tv_department)
    TextView tvDepartment;//部门
    @BindView(R.id.rl_department)
    RelativeLayout rlDepartment;
    @BindView(R.id.tv_position)
    TextView tvPosition;//职位
    @BindView(R.id.rl_position)
    RelativeLayout rlPosition;
    @BindView(R.id.btn_signOut)
    Button btnSignOut;//退出
    @BindView(R.id.v_company)
    TextView vCompany;
    @BindView(R.id.v_department)
    TextView vDepartment;

    //性别选择
    private String[] sexArray = new String[]{"男", "女", "保密"};
    private int sexNumber;

    //头像上传加载
    private PopupWindow pop = null;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_personal;
    }

    @Override
    protected void initView() {
        tvTitle.setText(R.string.mine);
        ibBack = findViewById(R.id.ib_back);

//        rlPortrait.setOnClickListener(this);
        rlUsername.setOnClickListener(this);
        rlSex.setOnClickListener(this);
        rlCompany.setOnClickListener(this);
        rlDepartment.setOnClickListener(this);
        rlPosition.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        ibBack.setOnClickListener(this);
        tvUsername.setText(SharedPreferenceUtils.getInstance().getString("user"));
        tvSex.setText(SharedPreferenceUtils.getInstance().getString("sex"));
        tvPosition.setText(SharedPreferenceUtils.getInstance().getString("zwmc"));
        //暂时没有单位和部门只有职位
        rlCompany.setVisibility(View.GONE);
        vCompany.setVisibility(View.GONE);
        rlDepartment.setVisibility(View.GONE);
        vDepartment.setVisibility(View.GONE);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_portrait:
                showPopupWindow();
                return;
            case R.id.rl_username:
                return;
            case R.id.rl_sex:
                setSex();
                return;
            case R.id.rl_company:
                return;
            case R.id.rl_department:
                return;
            case R.id.rl_position:
                return;
            case R.id.btn_signOut:
                SharedPreferenceUtils.getInstance().clear();
                startActivity(new Intent(PersonalActivity.this, LoginActivity.class));
                finish();
                return;
            case R.id.ib_back:
                finish();
                return;
        }
    }

    /**
     * 设置性别
     */
    private void setSex() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);// 自定义对话框
        if (tvSex.getText().toString() == "男") {
            sexNumber = 0;
        } else if (tvSex.getText().toString() == "女") {
            sexNumber = 1;
        } else {
            sexNumber = 2;
        }
        // 2默认的选中
        builder.setSingleChoiceItems(sexArray, sexNumber, (dialog, which) -> {// which是被选中的位置
            // showToast(which+"");
            tvSex.setText(sexArray[which]);
            SharedPreferenceUtils.getInstance().putString("sex", sexArray[which]);
            dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
        });
        builder.show();// 让弹出框显示
    }

    /**
     * 显示弹框
     */
    private void showPopupWindow() {
        pop = new PopupWindow(PersonalActivity.this);
        View view = getLayoutInflater().inflate(R.layout.layout_popup, null);//加载布局

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);

        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);   //设置PopupWindow 一些参数
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        RelativeLayout parent = view.findViewById(R.id.parent1);  //最外层布局
        Button bt1 = view.findViewById(R.id.btn_camera);
        Button bt2 = view.findViewById(R.id.btn_photo);
        Button bt3 = view.findViewById(R.id.btn_cancel);
        parent.setOnClickListener(new View.OnClickListener() { // 设置点击最外层布局关闭PopupWindow
            @Override
            public void onClick(View v) {
                pop.dismiss();

            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//相机
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
                startActivityForResult(intent, 1);
                pop.dismiss();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//相册
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
                startActivityForResult(i, 2);
                pop.dismiss();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
            }
        });

        //设置popupWindow消失时的监听
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {  // 拍照
                Bundle extras = data.getExtras();
                Bitmap photoBit = (Bitmap) extras.get("data");
                Bitmap option = BitmapOption.bitmapOption(photoBit, 5);
                nvPortrait.setImageBitmap(option);
                saveBitmap2file(option, "0001.jpg");
                final File file = new File("/sdcard/" + "0001.jpg");
                Log.e("TAG", "file333333333333333   " + file.getAbsolutePath());
                //开始联网上传的操作
                uploadFile(file.getAbsolutePath());
            } else if (requestCode == 2) { // 相册
                try {
                    Uri uri = data.getData();
                    String[] pojo = {MediaStore.Images.Media.DATA};
                    Cursor cursor = managedQuery(uri, pojo, null, null, null);
                    if (cursor != null) {
                        ContentResolver cr = this.getContentResolver();
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        String path = cursor.getString(column_index);
                        final File file = new File(path);
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        Bitmap option = BitmapOption.bitmapOption(bitmap, 5);
                        nvPortrait.setImageBitmap(option);//设置为头像的背景
                        //开始联网上传的操作
                        uploadFile(file.getAbsolutePath());
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    static boolean saveBitmap2file(Bitmap bmp, String filename) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream("/sdcard/" + filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmp.compress(format, quality, stream);
    }

    /* 上传文件至Server的方法 */
    public void uploadFile(String filePath) {
        LogUtils.e(filePath);
    }

}
