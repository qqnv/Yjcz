package com.zzlecheng.yjcz.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.adapter.ImageAdapter;
import com.zzlecheng.yjcz.adapter.ImageAdapter1;
import com.zzlecheng.yjcz.adapter.VideoAdapter;
import com.zzlecheng.yjcz.adapter.VideoAdapter1;
import com.zzlecheng.yjcz.base.BaseActivity;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.bean.CommonBean;
import com.zzlecheng.yjcz.bean.ReportPictureBean;
import com.zzlecheng.yjcz.bean.ReportVideoBean;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.BitmapOption;
import com.zzlecheng.yjcz.utils.CommonUtil;
import com.zzlecheng.yjcz.utils.ImageUtils;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.MyImageViewUtil;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;
import com.zzlecheng.yjcz.utils.SpacesItemDecoration;
import com.zzlecheng.yjcz.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zzlecheng.yjcz.view.MyProcedureView.isHz;

/**
 * @类名: ProcedureOperationActivity
 * @描述: 流程节点操作（上传视频照片等）
 * @作者: huangchao
 * @时间: 2018/12/24 4:25 PM
 * @版本: 1.0.0
 */
public class ProcedureOperationActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
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
    @BindView(R.id.rv_video)
    RecyclerView rvVideo;
    @BindView(R.id.iv_video)
    ImageView ivVideo;
    @BindView(R.id.rv_video1)
    RecyclerView rvVideo1;
    @BindView(R.id.iv_video1)
    ImageView ivVideo1;
    @BindView(R.id.ll_video)
    LinearLayout llVideo;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.edt_ms)
    EditText edtMs;
    //关联ID
    private String id = "";
    //照片
    private ImageAdapter mImageAdapter;
    private ImageAdapter1 mImageAdapter1;
    private List<ReportPictureBean> beanList = new ArrayList<>();
    //视频
    private VideoAdapter mVideoAdapter;
    private VideoAdapter1 mVideoAdapter1;
    private List<ReportVideoBean> beanList1 = new ArrayList<>();

    //最多上传几张
    private int maxSrc = 8;
    //大图地址
    private String paths = "";

    //提交
    //流程ID
    private String lcid = "";
    //流程历史ID
    private String lclsid;
    //节点历史ID
    private String jdid = "";
    //节点名称
    private String jdname = "";
    private String userId = "";
    private String lcmc = "";


    @Override
    protected int setLayoutRes() {
        return R.layout.activity_produre_operation;
    }

    @Override
    protected void initView() {

        id = CommonUtil.getItemID(10);
        Bundle bundle = getIntent().getExtras();

        lcid = bundle.getString("lcid");
        lclsid = bundle.getString("lclsId");
        jdid = bundle.getString("jdId");
        jdname = bundle.getString("jdName");
        lcmc = bundle.getString("lcmc");

        tvTitle.setText("节点操作");
        ibBack.setOnClickListener(this);
        ivTjzp.setOnClickListener(this);
        ivTjzp1.setOnClickListener(this);
        ivVideo.setOnClickListener(this);
        ivVideo1.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
        //图片1
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
                CustomPopupWindow popupWindow = new CustomPopupWindow(ProcedureOperationActivity.this);
                popupWindow.showAtLocation(findViewById(R.id.btn_commit),
                        Gravity.CENTER, 0, 0);
                popupWindow.setOnDismissListener(new popupDismissListener());
            }

            @Override
            public void onDeleteClick(View view, String path) {
                deletePicture(path);
            }
        });
        //图片2
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
                CustomPopupWindow popupWindow = new CustomPopupWindow(ProcedureOperationActivity.this);
                popupWindow.showAtLocation(findViewById(R.id.btn_commit),
                        Gravity.CENTER, 0, 0);
                popupWindow.setOnDismissListener(new popupDismissListener());
            }

            @Override
            public void onDeleteClick(View view, String path) {
                deletePicture(path);
            }
        });
        //视频1
        rvVideo.setHasFixedSize(true);
        rvVideo.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        //设置边距
        rvVideo.addItemDecoration(new SpacesItemDecoration(0, 14));
        mVideoAdapter = new VideoAdapter(this, beanList1);
        rvVideo.setAdapter(mVideoAdapter);
        mVideoAdapter.buttonSetOnclick(new VideoAdapter.ButtonInterface() {
            @Override
            public void onClick(View view, String path) {
                Intent intent = new Intent(ProcedureOperationActivity.this, VideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", path);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(View view, String path) {
                deletePicture(path);
            }
        });
        //视频2
        rvVideo1.setHasFixedSize(true);
        rvVideo1.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        //设置边距
        rvVideo1.addItemDecoration(new SpacesItemDecoration(0, 14));
        mVideoAdapter1 = new VideoAdapter1(this, beanList1);
        rvVideo1.setAdapter(mVideoAdapter1);
        mVideoAdapter1.buttonSetOnclick(new VideoAdapter1.ButtonInterface() {
            @Override
            public void onClick(View view, String path) {
                Intent intent = new Intent(ProcedureOperationActivity.this, VideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", path);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(View view, String path) {
                deletePicture(path);
            }
        });


    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.iv_tjzp:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
                startActivityForResult(intent, 1);
                break;
            case R.id.iv_tjzp1:
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
                startActivityForResult(intent1, 1);
                break;
            case R.id.iv_video:
                Intent intent2 = new Intent(this, VideoRecordingActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("pid", id);
                intent2.putExtras(bundle2);
                startActivity(intent2);
                break;
            case R.id.iv_video1:
                startActivity(new Intent(this, VideoRecordingActivity.class));
                break;
            case R.id.btn_commit:
                commitComment();
                break;
        }
    }

    /**
     * 提交
     */
    private void commitComment() {
        userId = SharedPreferenceUtils.getInstance().getString("userid");
        String content = edtMs.getText().toString().trim();
        if ("".equals(content)) {
            ToastUtils.showShortToast(this, "请填写操作内容");
            return;
        }
        HttpMethods.getHttpMethods().commitProcedure(this, lcid, lclsid, jdid, "操作",
                jdname, userId, id, "node_type_make", lcmc, content, "0",
                new BaseObserver<CommonBean>(this, false) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        ToastUtils.showShortToast(ProcedureOperationActivity.this, "提交成功");
                        isHz = true;
                        finish();
                    }

                    @Override
                    protected void onHandleError(String msg) {
                        LogUtils.e(msg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.toString());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {  // 拍照
                Bundle extras = data.getExtras();
                Bitmap photoBit = (Bitmap) extras.get("data");
                Bitmap option = BitmapOption.bitmapOption(photoBit, 5);
//                ivPz.setImageBitmap(option);
                final File file = ImageUtils.compressImage(option);
                LogUtils.e(file.getAbsolutePath());
                //开始联网上传的操作
                uploadFile(file.getAbsolutePath());
            }
        }
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
                        ToastUtils.showShortToast(ProcedureOperationActivity.this, "图片上传成功");
                        loadPicture();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LogUtils.e(e.toString());
                    }
                });
    }


    /**
     * 删除照片和视频
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
                            ToastUtils.showShortToast(ProcedureOperationActivity.this, "删除成功");
                            loadPicture();
                            loadRadio();
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

    @Override
    protected void onResume() {
        super.onResume();
        loadRadio();
        loadPicture();
    }

    /**
     * 加载已上传的视频
     */
    private void loadRadio() {
        HttpMethods.getHttpMethods().postReportVideo(this, id, "1", "0",
                new BaseObserver<List<ReportVideoBean>>(this, false) {
                    @Override
                    protected void onHandleSuccess(List<ReportVideoBean> reportVideoBeans) {
                        if (reportVideoBeans.size() >= maxSrc / 2) {//如果照片大于等于4张
                            ivVideo.setVisibility(View.GONE);
                            llVideo.setVisibility(View.VISIBLE);
                            if (reportVideoBeans.size() >= maxSrc) {//如果照片大于等于8张
                                ivVideo1.setVisibility(View.GONE);
                            } else {
                                ivVideo1.setVisibility(View.VISIBLE);
                            }
                        } else {
                            ivVideo.setVisibility(View.VISIBLE);
                            llVideo.setVisibility(View.GONE);
                        }

                        beanList1.clear();
                        beanList1.addAll(reportVideoBeans);
                        mVideoAdapter.notifyDataSetChanged();
                        if (beanList1.size() >= 4) {//如果照片大于等于4张
                            mVideoAdapter1.notifyDataSetChanged();
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
