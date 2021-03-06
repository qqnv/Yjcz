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
 * @??????: ReportActivity
 * @??????: ????????????
 * @??????: huangchao
 * @??????: 2018/9/4 ??????7:11
 * @??????: 1.0.0
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

    //??????????????????ID,????????????
    private String id;
    //??????????????????
    private String reportdepart;
    //????????????ID
    private String reportorid;
    //????????????
    private String occurtime;
    //????????????
    private String reporttime = "";
    //????????????
    private String eventareaName;
    //????????????ID
    private String eventareaId;
    //????????????
    private String position;
    //??????
    private String description;
    //??????
    private ImageAdapter mImageAdapter;
    private ImageAdapter1 mImageAdapter1;
    private List<ReportPictureBean> beanList = new ArrayList<>();
    private String paths = "";
    //??????????????????
    private int maxSrc = 8;
    //activity??????
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
        //????????????
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
        //????????????
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
                            //?????????????????????????????????????????????
                            String tx = locationBeans.get(options1).getPickerViewText();
                            tvSbwz.setText(tx);
                        })
                                .setTitleText("????????????")
                                .setContentTextSize(18)//????????????????????????
                                .setDividerColor(R.color.select_line)//????????????????????????
                                .setSelectOptions(0, 1)//???????????????
                                .setBgColor(getResources().getColor(R.color.white))
                                .setTitleBgColor(getResources().getColor(R.color.select_title_bg))
                                .setTitleColor(getResources().getColor(R.color.select_sure))
                                .setCancelColor(getResources().getColor(R.color.select_sure))
                                .setSubmitColor(getResources().getColor(R.color.select_sure))
                                .setDividerColor(getResources().getColor(R.color.select_sure))
                                .setTextColorCenter(Color.BLACK)
                                .isRestoreItem(true)//??????????????????????????????????????????????????????
                                .isCenterLabel(false) //?????????????????????????????????label?????????false?????????item???????????????label???
                                .setLabels("", "", "")
                                .setBackgroundId(0x00000000) //????????????????????????
                                .setOptionsSelectChangeListener((options1, options2, options3) -> {
                                })
                                .build();
                        pvOptions.setPicker(locationBeans);//???????????????
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        pvOptions = new OptionsPickerBuilder(ReportActivity.this, (options1, options2, options3, v) -> {
                        })
                                .setTitleText("????????????")
                                .setContentTextSize(18)//????????????????????????
                                .setDividerColor(R.color.select_line)//????????????????????????
                                .setSelectOptions(0, 1)//???????????????
                                .setBgColor(getResources().getColor(R.color.white))
                                .setTitleBgColor(getResources().getColor(R.color.select_title_bg))
                                .setTitleColor(getResources().getColor(R.color.select_sure))
                                .setCancelColor(getResources().getColor(R.color.select_sure))
                                .setSubmitColor(getResources().getColor(R.color.select_sure))
                                .setDividerColor(getResources().getColor(R.color.select_sure))
                                .setTextColorCenter(Color.BLACK)
                                .isRestoreItem(true)//??????????????????????????????????????????????????????
                                .isCenterLabel(false) //?????????????????????????????????label?????????false?????????item???????????????label???
                                .setLabels("", "", "")
                                .setBackgroundId(0x00000000) //????????????????????????
                                .setOptionsSelectChangeListener((options1, options2, options3) -> {
                                })
                                .build();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back://???????????????
                finish();
                break;
            case R.id.tv_fssj://????????????
                initCustomTimePicker(0);
                pvCustomTime.show(v);
                break;
            case R.id.tv_sbsj://????????????
                initCustomTimePicker(1);
                pvCustomTime.show(v);
                break;
            case R.id.tv_sbwz://????????????
//                pvOptions.show();
                Intent intentLocation = new Intent(this,LocationTreeActivity.class);
                startActivityForResult(intentLocation,sendUser);
                break;
            case R.id.iv_tjzp:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//??????android??????????????????
                startActivityForResult(intent, sendPicture);
                break;
            case R.id.iv_tjzp1:
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//??????android??????????????????
                startActivityForResult(intent1, sendPicture);
                break;
            case R.id.btn_sb://????????????
                updateReport();
                break;
            case R.id.btn_qx://????????????
                resetReport();
                break;
        }
    }

    /**
     * ????????????
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
                        ToastUtils.showShortToast(ReportActivity.this, "????????????");
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
     * ?????????????????????????????????
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
         * ???????????????
         * 1.?????????????????????id??? optionspicker ?????? timepicker ???????????????????????????????????????????????????????????????.
         * ???????????????demo ????????????????????????layout?????????
         * 2.????????????Calendar???????????????0-11???,?????????????????????Calendar???set?????????????????????,???????????????????????????0-11
         * setRangDate??????????????????????????????(?????????????????????????????????????????????1900-2100???????????????????????????)
         */
        Calendar selectedDate = Calendar.getInstance();//??????????????????
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);
        //??????????????? ??????????????????
        pvCustomTime = new TimePickerBuilder(this, (date, v) -> {//??????????????????
            if (i == 0) {
                tvFssj.setText(getTime(date));
            }
        })
//                .setType(TimePickerView.Type.ALL)//default is all
//                .setCancelText("??????")
//                .setSubmitText("??????")
//                .setContentTextSize(18)
//                .setTitleSize(20)
//                .setTitleText("???????????????")
//                .setTitleColor(Color.BLACK)
//                .setDividerColor(Color.WHITE)//????????????????????????
//                .setTextColorCenter(Color.LTGRAY)//????????????????????????
//                .setLineSpacingMultiplier(1.6f)//????????????????????????????????????
//                .setTitleBgColor(Color.DKGRAY)//?????????????????? Night mode
//                .setBgColor(Color.BLACK)//?????????????????? Night mode
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
                .setLabel("???", "???", "???", "???", "???", "???")
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //?????????????????????????????????label?????????false?????????item???????????????label???
                .setDividerColor(0xFF24AD9D)
                .build();

    }

    private String getTime(Date date) {//???????????????????????????????????????
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {  // ??????
                Bundle extras = data.getExtras();
                Bitmap photoBit = (Bitmap) extras.get("data");
                Bitmap option = BitmapOption.bitmapOption(photoBit, 5);
//                ivPz.setImageBitmap(option);
                saveBitmap2file(option, "0002.jpg");
                final File file = new File("/sdcard/" + "0002.jpg");
                //???????????????????????????
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
     * ????????????
     *
     * @param absolutePath
     */
    private void uploadFile(String absolutePath) {
        HttpMethods.getHttpMethods().reportPicture(this, id, "0", "0", new File(absolutePath),
                new BaseObserver<CommonBean>(this, false) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        ToastUtils.showShortToast(ReportActivity.this, "??????????????????");
                        loadPicture();
                    }
                });
    }

    /**
     * ????????????
     *
     * @param path
     */
    private void deletePicture(String path) {
        if (path != "") {//???????????????
            int i = path.indexOf("=");
            String pat = path.substring(i + 1, path.length());
            int m = pat.indexOf("&");
            String pats = pat.substring(0, m);
            HttpMethods.getHttpMethods().deletePicture(this, pats, "0",
                    new BaseObserver<CommonBean>(this, false) {
                        @Override
                        protected void onHandleSuccess(CommonBean commonBean) {
                            ToastUtils.showShortToast(ReportActivity.this, "????????????");
                            loadPicture();
                        }
                    });
        }
    }


    /**
     * ????????????????????????
     */
    private void loadPicture() {
        HttpMethods.getHttpMethods().postReportPicture(this, id, "0", "0",
                new BaseObserver<List<ReportPictureBean>>(this, false) {
                    @Override
                    protected void onHandleSuccess(List<ReportPictureBean> reportPictureBeans) {
                        if (reportPictureBeans.size() >= maxSrc / 2) {//????????????????????????4???
                            ivTjzp.setVisibility(View.GONE);
                            llZp.setVisibility(View.VISIBLE);
                            if (reportPictureBeans.size() >= maxSrc) {//????????????????????????8???
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
                        if (beanList.size() >= 4) {//????????????????????????4???
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
     * ??????
     */
    class CustomPopupWindow extends PopupWindow {

        Context mContext;
        private LayoutInflater mInflater;
        private View mContentView;

        private MyImageViewUtil ivDt;


        public CustomPopupWindow(Context context) {
            super(context);
            this.mContext = context;
            //?????????
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //??????
            mContentView = mInflater.inflate(R.layout.layout_popup_src, null);
            ivDt = mContentView.findViewById(R.id.iv_dt);
            ivDt.setImageURL(paths);
            //??????View
            setContentView(mContentView);
            //???????????????
            setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            //??????????????????
            setAnimationStyle(R.style.PopupWindowStyle);
            //?????????????????????????????????????????????????????????BACK??????
            setBackgroundDrawable(new ColorDrawable());
            //????????????????????????
            setFocusable(true);
            //??????????????????
            setTouchable(true);
            //??????????????????????????????
            setOutsideTouchable(true);
            backgroundAlpha(0.4f);
        }
    }

    /**
     * ???????????????popWin????????????????????????????????????????????????????????????
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
     * ???????????????
     *
     * @param f
     */
    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }



}
