package com.zzlecheng.yjcz.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.adapter.ImageAdapter;
import com.zzlecheng.yjcz.adapter.ImageAdapter1;
import com.zzlecheng.yjcz.base.BaseActivity;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.bean.CommonBean;
import com.zzlecheng.yjcz.bean.LocationBean;
import com.zzlecheng.yjcz.bean.ReportPictureBean;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.BitmapOption;
import com.zzlecheng.yjcz.utils.CommonUtil;
import com.zzlecheng.yjcz.utils.EditTextWithScrollView;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.MyImageViewUtil;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;
import com.zzlecheng.yjcz.utils.SpacesItemDecoration;
import com.zzlecheng.yjcz.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @类名: ReportActivity
 * @描述: 事件上报
 * @作者: huangchao
 * @时间: 2018/9/4 下午7:11
 * @版本: 1.0.0
 */
public class ReportActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
    @BindView(R.id.tv_fssj)
    TextView tvFssj;
    @BindView(R.id.tv_sbsj)
    TextView tvSbsj;
    @BindView(R.id.tv_sbwz)
    TextView tvSbwz;
    @BindView(R.id.edt_jtwz)
    EditText edtJtwz;
    @BindView(R.id.rv_src)
    RecyclerView rvSrc;
    @BindView(R.id.iv_tjzp)
    ImageView ivTjzp;
    @BindView(R.id.rv_src1)
    RecyclerView rvSrc1;
    @BindView(R.id.iv_tjzp1)
    ImageView ivTjzp1;
    @BindView(R.id.ll_zp)
    LinearLayout llZp;
    @BindView(R.id.edt_ms)
    EditTextWithScrollView edtMs;
    @BindView(R.id.btn_sb)
    Button btnSb;
    @BindView(R.id.btn_qx)
    Button btnQx;
    @BindView(R.id.sv_home)
    ScrollView svHome;
    @BindView(R.id.ll_sbsj)
    LinearLayout llSbsj;
    private String sjdm = "lc_yaqy";

    private TimePickerView pvCustomTime;
    private OptionsPickerView pvOptions;

    //该事件的唯一ID,随机生成
    private String id;
    //上报人员部门
    private String reportdepart;
    //上报人员ID
    private String reportorid;
    //发生时间
    private String occurtime;
    //上报时间
    private String reporttime = "";
    //上报位置
    private String eventareaName;
    //上报位置ID
    private String eventareaId;
    //具体位置
    private String position;
    //描述
    private String description;
    //照片
    private ImageAdapter mImageAdapter;
    private ImageAdapter1 mImageAdapter1;
    private List<ReportPictureBean> beanList = new ArrayList<>();
    private String paths = "";
    //最多上传几张
    private int maxSrc = 8;
    //activity返回
    private final static int sendPicture = 1;
    private final static int sendUser = 2;


    protected int setLayoutRes() {
        return R.layout.activity_report;
    }

    @Override
    protected void initView() {
        tvTitle.setText(R.string.report);
        Date date = new Date();
        tvFssj.setText(getTime(date));
        llSbsj.setVisibility(View.GONE);
        tvFssj.setHintTextColor(getResources().getColor(R.color.white));
        tvSbwz.setHintTextColor(getResources().getColor(R.color.white));
        edtJtwz.setHintTextColor(getResources().getColor(R.color.white));
        edtMs.setHintTextColor(getResources().getColor(R.color.white));
        ibBack.setOnClickListener(this);
        tvFssj.setOnClickListener(this);
        tvSbsj.setOnClickListener(this);
        tvSbwz.setOnClickListener(this);
        ivTjzp.setOnClickListener(this);
        ivTjzp1.setOnClickListener(this);
        btnSb.setOnClickListener(this);
        btnQx.setOnClickListener(this);

        id = CommonUtil.getItemID(10);
        reportdepart = SharedPreferenceUtils.getInstance().getString("departmentid");
        reportorid = SharedPreferenceUtils.getInstance().getString("userid");

        rvSrc.setHasFixedSize(true);
        rvSrc.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        //设置边距
        rvSrc.addItemDecoration(new SpacesItemDecoration(0, 14));
        mImageAdapter = new ImageAdapter(this, beanList);
        rvSrc.setAdapter(mImageAdapter);
        mImageAdapter.buttonSetOnclick(new ImageAdapter.ButtonInterface() {
            @Override
            public void onClick(View view, String path) {
                paths = path;
                CustomPopupWindow popupWindow = new CustomPopupWindow(ReportActivity.this);
                popupWindow.showAtLocation(findViewById(R.id.tv_sbwz),
                        Gravity.CENTER, 0, 0);
                popupWindow.setOnDismissListener(new popupDismissListener());
            }

            @Override
            public void onDeleteClick(View view, String path) {
                deletePicture(path);
            }
        });

        rvSrc1.setHasFixedSize(true);
        rvSrc1.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        //设置边距
        rvSrc1.addItemDecoration(new SpacesItemDecoration(0, 14));
        mImageAdapter1 = new ImageAdapter1(this, beanList);
        rvSrc1.setAdapter(mImageAdapter1);
        mImageAdapter1.buttonSetOnclick(new ImageAdapter1.ButtonInterface() {
            @Override
            public void onClick(View view, String path) {
                paths = path;
                CustomPopupWindow popupWindow = new CustomPopupWindow(ReportActivity.this);
                popupWindow.showAtLocation(findViewById(R.id.tv_sbwz),
                        Gravity.CENTER, 0, 0);
                popupWindow.setOnDismissListener(new popupDismissListener());
            }

            @Override
            public void onDeleteClick(View view, String path) {
                deletePicture(path);
            }
        });
    }

    @Override
    protected void loadData() {
        HttpMethods.getHttpMethods().location(this, sjdm, "0",
                new BaseObserver<List<LocationBean>>(this, false) {
                    @Override
                    protected void onHandleSuccess(List<LocationBean> locationBeans) {
                        pvOptions = new OptionsPickerBuilder(ReportActivity.this, (options1, options2, options3, v) -> {
                            //返回的分别是三个级别的选中位置
                            String tx = locationBeans.get(options1).getPickerViewText();
                            tvSbwz.setText(tx);
                        })
                                .setTitleText("位置选择")
                                .setContentTextSize(18)//设置滚轮文字大小
                                .setDividerColor(R.color.select_line)//设置分割线的颜色
                                .setSelectOptions(0, 1)//默认选中项
                                .setBgColor(getResources().getColor(R.color.white))
                                .setTitleBgColor(getResources().getColor(R.color.select_title_bg))
                                .setTitleColor(getResources().getColor(R.color.select_sure))
                                .setCancelColor(getResources().getColor(R.color.select_sure))
                                .setSubmitColor(getResources().getColor(R.color.select_sure))
                                .setDividerColor(getResources().getColor(R.color.select_sure))
                                .setTextColorCenter(Color.BLACK)
                                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                                .setLabels("", "", "")
                                .setBackgroundId(0x00000000) //设置外部遮罩颜色
                                .setOptionsSelectChangeListener((options1, options2, options3) -> {
                                })
                                .build();
                        pvOptions.setPicker(locationBeans);//一级选择器
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        pvOptions = new OptionsPickerBuilder(ReportActivity.this, (options1, options2, options3, v) -> {
                        })
                                .setTitleText("位置选择")
                                .setContentTextSize(18)//设置滚轮文字大小
                                .setDividerColor(R.color.select_line)//设置分割线的颜色
                                .setSelectOptions(0, 1)//默认选中项
                                .setBgColor(getResources().getColor(R.color.white))
                                .setTitleBgColor(getResources().getColor(R.color.select_title_bg))
                                .setTitleColor(getResources().getColor(R.color.select_sure))
                                .setCancelColor(getResources().getColor(R.color.select_sure))
                                .setSubmitColor(getResources().getColor(R.color.select_sure))
                                .setDividerColor(getResources().getColor(R.color.select_sure))
                                .setTextColorCenter(Color.BLACK)
                                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                                .setLabels("", "", "")
                                .setBackgroundId(0x00000000) //设置外部遮罩颜色
                                .setOptionsSelectChangeListener((options1, options2, options3) -> {
                                })
                                .build();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back://返回上一页
                finish();
                break;
            case R.id.tv_fssj://发生时间
                initCustomTimePicker(0);
                pvCustomTime.show(v);
                break;
            case R.id.tv_sbsj://上报时间
                initCustomTimePicker(1);
                pvCustomTime.show(v);
                break;
            case R.id.tv_sbwz://上报位置
//                pvOptions.show();
                Intent intentLocation = new Intent(this,LocationTreeActivity.class);
                startActivityForResult(intentLocation,sendUser);
                break;
            case R.id.iv_tjzp:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
                startActivityForResult(intent, sendPicture);
                break;
            case R.id.iv_tjzp1:
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
                startActivityForResult(intent1, sendPicture);
                break;
            case R.id.btn_sb://上报事件
                updateReport();
                break;
            case R.id.btn_qx://取消重置
                resetReport();
                break;
        }
    }

    /**
     * 上报事件
     */
    private void updateReport() {

        occurtime = tvFssj.getText().toString().trim();
        position = edtJtwz.getText().toString().trim();
        description = edtMs.getText().toString().trim();
        if (occurtime == "" || occurtime.equals("")) {
            ToastUtils.showShortToast(this, R.string.fssj_);
            return;
        }
        if (eventareaId == "" || eventareaId.equals("")) {
            ToastUtils.showShortToast(this, R.string.sbwz_);
            return;
        }
        if (position == "" || position.equals("")) {
            ToastUtils.showShortToast(this, R.string.jtwz_);
            return;
        }
        if (description == "" || description.equals("")) {
            ToastUtils.showShortToast(this, R.string.ms_);
            return;
        }
        LogUtils.e(eventareaId+"-----");
        HttpMethods.getHttpMethods().reportEvent(this, id, reportdepart, reportorid, occurtime,
                reporttime, eventareaId, position, description, "0", new BaseObserver<CommonBean>(this, false) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        ToastUtils.showShortToast(ReportActivity.this, "上报成功");
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LogUtils.e(e.toString());
                    }
                });
    }

    /**
     * 取消上报事件，重置信息
     */
    private void resetReport() {
        tvFssj.setText("");
        tvFssj.setHint(R.string.fssj_);
        tvSbsj.setText("");
        tvSbwz.setHint(R.string.sbwz_);
        edtJtwz.setText("");
        edtJtwz.setHint(R.string.jtwz_);
        edtMs.setText("");
        edtMs.setHint(R.string.ms_);
    }

    private void initCustomTimePicker(int i) {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerBuilder(this, (date, v) -> {//选中事件回调
            if (i == 0) {
                tvFssj.setText(getTime(date));
            }
        })
//                .setType(TimePickerView.Type.ALL)//default is all
//                .setCancelText("取消")
//                .setSubmitText("确认")
//                .setContentTextSize(18)
//                .setTitleSize(20)
//                .setTitleText("请选择时间")
//                .setTitleColor(Color.BLACK)
//                .setDividerColor(Color.WHITE)//设置分割线的颜色
//                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
//                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
//                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
//                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
//                .setSubmitColor(Color.WHITE)
//                .setCancelColor(Color.WHITE)
//                .animGravity(Gravity.RIGHT)// default is center
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.layout_pickerview_custom, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                        TextView tvCancel = v.findViewById(R.id.tv_cancel);
                        tvSubmit.setOnClickListener(v1 -> {
                            pvCustomTime.returnData();
                            pvCustomTime.dismiss();
                        });
                        tvCancel.setOnClickListener(v12 -> pvCustomTime.dismiss());
                    }
                })
                .setContentTextSize(18)
                .setType(new boolean[]{true, true, true, true, true, true})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();

    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {  // 拍照
                Bundle extras = data.getExtras();
                Bitmap photoBit = (Bitmap) extras.get("data");
                Bitmap option = BitmapOption.bitmapOption(photoBit, 5);
//                ivPz.setImageBitmap(option);
                saveBitmap2file(option, "0002.jpg");
                final File file = new File("/sdcard/" + "0002.jpg");
                //开始联网上传的操作
                uploadFile(file.getAbsolutePath());
            }
            if (requestCode == sendUser){
                eventareaId = data.getStringExtra("id");
                eventareaName = data.getStringExtra("name");
                tvSbwz.setText(eventareaName);
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

    /**
     * 上传照片
     *
     * @param absolutePath
     */
    private void uploadFile(String absolutePath) {
        HttpMethods.getHttpMethods().reportPicture(this, id, "0", "0", new File(absolutePath),
                new BaseObserver<CommonBean>(this, false) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        ToastUtils.showShortToast(ReportActivity.this, "图片上传成功");
                        loadPicture();
                    }
                });
    }

    /**
     * 删除照片
     *
     * @param path
     */
    private void deletePicture(String path) {
        if (path != "") {//地址不为空
            int i = path.indexOf("=");
            String pat = path.substring(i + 1, path.length());
            int m = pat.indexOf("&");
            String pats = pat.substring(0, m);
            HttpMethods.getHttpMethods().deletePicture(this, pats, "0",
                    new BaseObserver<CommonBean>(this, false) {
                        @Override
                        protected void onHandleSuccess(CommonBean commonBean) {
                            ToastUtils.showShortToast(ReportActivity.this, "删除成功");
                            loadPicture();
                        }
                    });
        }
    }


    /**
     * 加载已上传的照片
     */
    private void loadPicture() {
        HttpMethods.getHttpMethods().postReportPicture(this, id, "0", "0",
                new BaseObserver<List<ReportPictureBean>>(this, false) {
                    @Override
                    protected void onHandleSuccess(List<ReportPictureBean> reportPictureBeans) {
                        if (reportPictureBeans.size() >= maxSrc / 2) {//如果照片大于等于4张
                            ivTjzp.setVisibility(View.GONE);
                            llZp.setVisibility(View.VISIBLE);
                            if (reportPictureBeans.size() >= maxSrc) {//如果照片大于等于8张
                                ivTjzp1.setVisibility(View.GONE);
                            } else {
                                ivTjzp1.setVisibility(View.VISIBLE);
                            }
                        } else {
                            ivTjzp.setVisibility(View.VISIBLE);
                            llZp.setVisibility(View.GONE);
                        }

                        beanList.clear();
                        beanList.addAll(reportPictureBeans);
                        mImageAdapter.notifyDataSetChanged();
                        if (beanList.size() >= 4) {//如果照片大于等于4张
                            mImageAdapter1.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LogUtils.e(e + "--");
                    }
                });
    }

    /**
     * 弹框
     */
    class CustomPopupWindow extends PopupWindow {

        Context mContext;
        private LayoutInflater mInflater;
        private View mContentView;

        private MyImageViewUtil ivDt;


        public CustomPopupWindow(Context context) {
            super(context);
            this.mContext = context;
            //打气筒
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //打气
            mContentView = mInflater.inflate(R.layout.layout_popup_src, null);
            ivDt = mContentView.findViewById(R.id.iv_dt);
            ivDt.setImageURL(paths);
            //设置View
            setContentView(mContentView);
            //设置宽与高
            setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            //设置进出动画
            setAnimationStyle(R.style.PopupWindowStyle);
            //设置背景只有设置了这个才可以点击外边和BACK消失
            setBackgroundDrawable(new ColorDrawable());
            //设置可以获取集点
            setFocusable(true);
            //设置可以触摸
            setTouchable(true);
            //设置点击外边可以消失
            setOutsideTouchable(true);
            backgroundAlpha(0.4f);
        }
    }

    /**
     * 报班弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    /**
     * 设置遮罩层
     *
     * @param f
     */
    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }



}
