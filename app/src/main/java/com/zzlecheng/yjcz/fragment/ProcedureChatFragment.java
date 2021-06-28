package com.zzlecheng.yjcz.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.activity.VideoPlayActivity;
import com.zzlecheng.yjcz.activity.VideoRecordChatActivity;
import com.zzlecheng.yjcz.adapter.ChatPopupAdapter;
import com.zzlecheng.yjcz.adapter.ProcedureChatAdapter;
import com.zzlecheng.yjcz.base.BaseFragment;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.bean.CommonBean;
import com.zzlecheng.yjcz.bean.PopBean;
import com.zzlecheng.yjcz.bean.ProcedureChatBean;
import com.zzlecheng.yjcz.eventbus.PushEventBean;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.BitmapOption;
import com.zzlecheng.yjcz.utils.ImageUtils;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.MediaUtils;
import com.zzlecheng.yjcz.utils.MyImageViewUtil;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;
import com.zzlecheng.yjcz.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @类名: ProcedureChatFragment
 * @描述: 聊天界面
 * @作者: huangchao
 * @时间: 2018/12/28 1:52 PM
 * @版本: 1.0.0
 */
public class ProcedureChatFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_pop)
    RelativeLayout rlPop;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
    @BindView(R.id.rlv_recyclerView)
    RecyclerView rlvRecyclerView;
    @BindView(R.id.iv_voice)
    ImageView ivVoice;
    @BindView(R.id.edt_content)
    EditText edtContent;
    @BindView(R.id.iv_video)
    ImageView ivVideo;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.time_display)
    Chronometer chronometer;
    @BindView(R.id.mic_icon)
    ImageView micIcon;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.audio_layout)
    RelativeLayout audioLayout;
    private String id = "";
    private String lcid = "";
    private String lclsid = "";
    private String content = "";
    private String soures = "app";
    private String userid = "";
    private String username = "";

    Handler handler = new Handler();
    private Runnable runnable;

    private List<ProcedureChatBean> beans = new ArrayList<>();
    private ProcedureChatAdapter procedureChatAdapter;

    private String paths1;
    //弹出的人员
    private List<PopBean> popBeans = new ArrayList<>();
    private ChatPopupAdapter chatPopupAdapter;

    //语音录制
    private MediaUtils mediaUtils;
    private boolean isCancel;
    private String duration;
    private boolean isFirst = true;

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_procedure_chat;
    }

    @Override
    protected void initView() {

        //注册订阅者
        EventBus.getDefault().register(this);

        tvTitle.setText("应急会议室");
        ibBack.setOnClickListener(this);
        rlPop.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
        ivVideo.setOnClickListener(this);
        tvCommit.setOnClickListener(this);

        ivVoice.setOnTouchListener(touchListener);
        chronometer.setOnChronometerTickListener(tickListener);

        userid = SharedPreferenceUtils.getInstance().getString("userid");
        username = SharedPreferenceUtils.getInstance().getString("user");
        Bundle bundle = getActivity().getIntent().getExtras();
        lclsid = bundle.getString("lclsId");
        lcid = bundle.getString("lcId");
        LogUtils.e("&lclsid=" + lclsid);
        LogUtils.e("&lcid=" + lcid);

        rlvRecyclerView.setHasFixedSize(true);
        rlvRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        procedureChatAdapter = new ProcedureChatAdapter(getActivity(), beans);
        rlvRecyclerView.setAdapter(procedureChatAdapter);

        procedureChatAdapter.buttonSetOnclick((view, path, types) -> {
            if ("0".equals(types)) {//如果是照片
                //大图全屏展示
                paths1 = path;
                CustomPopupWindow1 popupWindow1 = new CustomPopupWindow1(getContext());
                popupWindow1.showAtLocation(getActivity().findViewById(R.id.ib_back),
                        Gravity.CENTER, 0, 0);
                popupWindow1.setOnDismissListener(new popupDismissListener());
            } else {//如果是视频
                Intent intent1 = new Intent(getContext(), VideoPlayActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("url", path);
                intent1.putExtras(bundle1);
                startActivity(intent1);
            }
        });

        getChatList();

//        //定时请求数据
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                getChatList();
//                handler.postDelayed(this, 3000);
//            }
//        };
//        handler.postDelayed(runnable, 0);

        //语音录制
        mediaUtils = new MediaUtils(getActivity());
        mediaUtils.setRecorderType(MediaUtils.MEDIA_AUDIO);
//        mediaUtils.setTargetDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
        mediaUtils.setTargetDir(Environment.getExternalStorageDirectory());
//        mediaUtils.setTargetName(UUID.randomUUID() + ".mp3");
        mediaUtils.setTargetName("yjcz.mp3");

    }

    //定义处理接收的方法
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void receiveEventBus(PushEventBean eventBean) {
        getChatList();
    }

    /**
     * 应急聊天室列表
     */
    private void getChatList() {
        LogUtils.e("聊天内容刷新");
        HttpMethods.getHttpMethods().getChatList(getContext(), lclsid, "0",
                new BaseObserver<List<ProcedureChatBean>>(getContext(), false) {
                    @Override
                    protected void onHandleSuccess(List<ProcedureChatBean> procedureChatBeans) {
                        beans.clear();
                        beans.addAll(procedureChatBeans);
                        int chatSize = SharedPreferenceUtils.getInstance().getInt("chatSize");
                        //若数据改变则刷新界面
                        if (procedureChatBeans.size() != chatSize || isFirst) {
                            procedureChatAdapter.notifyDataSetChanged();
                            isFirst = false;
                        }
                        SharedPreferenceUtils.getInstance().putInt("chatSize", procedureChatBeans.size());
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
                getActivity().finish();
                break;
            case R.id.rl_pop:
                getPop();
                CustomPopupWindow popupWindow = new CustomPopupWindow(getContext());
                popupWindow.showAtLocation(getActivity().findViewById(R.id.tv_title),
                        Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 180);
                popupWindow.setOnDismissListener(new popupDismissListener());
                break;
            case R.id.iv_video:
                Intent intent = new Intent(getContext(), VideoRecordChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("lclsid", lclsid);
                bundle.putString("userid", userid);
                bundle.putString("username", username);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.iv_photo:
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
                startActivityForResult(intent1, 1);
                break;
            case R.id.tv_commit:
                commitContent();
                break;
        }
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            boolean ret = false;
            float downY = 0;
            int action = event.getAction();
            switch (v.getId()) {
                case R.id.iv_voice:
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            startAnim(true);
                            mediaUtils.record();
                            ret = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            stopAnim();
                            if (isCancel) {
                                isCancel = false;
                                mediaUtils.stopRecordUnSave();
                                Toast.makeText(getContext(), "取消上传", Toast.LENGTH_SHORT).show();
                            } else {
                                int duration = getDuration(chronometer.getText().toString());
                                switch (duration) {
                                    case -1:
                                        break;
                                    case -2:
                                        mediaUtils.stopRecordUnSave();
                                        Toast.makeText(getContext(), "时间太短", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        mediaUtils.stopRecordSave();
                                        String path = mediaUtils.getTargetFilePath();
                                        LogUtils.e(path);
                                        uploadFile(path, "3");
                                        break;
                                }
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float currentY = event.getY();
                            if (downY - currentY > 10) {
                                moveAnim();
                                isCancel = true;
                            } else {
                                isCancel = false;
                                startAnim(false);
                            }
                            break;
                    }
                    break;
            }
            return ret;
        }
    };

    Chronometer.OnChronometerTickListener tickListener = new Chronometer.OnChronometerTickListener() {
        @Override
        public void onChronometerTick(Chronometer chronometer) {
            if (SystemClock.elapsedRealtime() - chronometer.getBase() > 60 * 1000) {
                stopAnim();
                mediaUtils.stopRecordSave();
                Toast.makeText(getContext(), "录音超时", Toast.LENGTH_SHORT).show();
                String path = mediaUtils.getTargetFilePath();
                Toast.makeText(getContext(), "文件已保存至：" + path, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private int getDuration(String str) {
        String a = str.substring(0, 1);
        String b = str.substring(1, 2);
        String c = str.substring(3, 4);
        String d = str.substring(4);
        if (a.equals("0") && b.equals("0")) {
            if (c.equals("0") && Integer.valueOf(d) < 1) {
                return -2;
            } else if (c.equals("0") && Integer.valueOf(d) > 1) {
                duration = d;
                return Integer.valueOf(d);
            } else {
                duration = c + d;
                return Integer.valueOf(c + d);
            }
        } else {
            duration = "60";
            return -1;
        }

    }

    private void startAnim(boolean isStart) {
        audioLayout.setVisibility(View.VISIBLE);
        tvInfo.setText("上滑取消");
        ivVoice.setBackground(getResources().getDrawable(R.drawable.mic_pressed_bg));
        micIcon.setBackground(null);
        micIcon.setBackground(getResources().getDrawable(R.mipmap.ic_mic_white_24dp));
        if (isStart) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.setFormat("%S");
            chronometer.start();
        }
    }

    private void stopAnim() {
        audioLayout.setVisibility(View.GONE);
        ivVoice.setBackground(getResources().getDrawable(R.drawable.mic_bg));
        chronometer.stop();
    }

    private void moveAnim() {
        tvInfo.setText("松手取消");
        micIcon.setBackground(null);
        micIcon.setBackground(getResources().getDrawable(R.mipmap.ic_undo_black_24dp));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {  // 拍照
                Bundle extras = data.getExtras();
                Bitmap photoBit = (Bitmap) extras.get("data");
                Bitmap option = BitmapOption.bitmapOption(photoBit, 5);
                final File file = ImageUtils.compressImage(option);
                //开始联网上传的操作
                uploadFile(file.getAbsolutePath(), "0");
            }
        }
    }

    private void uploadFile(String absolutePath, String types) {
        HttpMethods.getHttpMethods().commitChatFj(getContext(), lclsid, soures, types, userid,
                username, "0", new File(absolutePath),
                new BaseObserver<CommonBean>(getContext(), false) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        if ("0".equals(types)) {
                            ToastUtils.showShortToast(getContext(), "图片发送成功");
                        } else {
                            ToastUtils.showShortToast(getContext(), "语音发送成功");
                        }
                        getChatList();
                    }
                });
    }

    /**
     * 应急聊天参与人员
     */
    public void getPop() {
        HttpMethods.getHttpMethods().getPop(getContext(), lcid, "0",
                new BaseObserver<List<PopBean>>(getContext(), false) {
                    @Override
                    protected void onHandleSuccess(List<PopBean> popBeanList) {
                        popBeans.clear();
                        popBeans.addAll(popBeanList);
                        chatPopupAdapter.notifyDataSetChanged();
                    }
                });
    }


    /**
     * 查看人员弹框
     */
    class CustomPopupWindow extends PopupWindow {

        Context mContext;
        private LayoutInflater mInflater;
        private View mContentView;

        public CustomPopupWindow(Context context) {
            super(context);

            //测量屏幕及模块宽高
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int height = dm.heightPixels;
            int width = dm.widthPixels;
            this.mContext = context;
            //打气筒
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //打气
            mContentView = mInflater.inflate(R.layout.layout_popup_chat, null);

            RecyclerView recyclerView = mContentView.findViewById(R.id.rlv_recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL, false));
            chatPopupAdapter = new ChatPopupAdapter(getActivity(), popBeans);
            recyclerView.setAdapter(chatPopupAdapter);

            //设置View
            setContentView(mContentView);
            //设置宽与高
            setWidth(width / 4 * 3);
            setHeight(height / 4);
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
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = f;
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * 提交评论信息
     */
    private void commitContent() {
        content = edtContent.getText().toString().trim();
        if (!"".equals(content)) {
            HttpMethods.getHttpMethods().commitContent(getContext(), lclsid, content, soures, "4",
                    userid, username, "0",
                    new BaseObserver<CommonBean>(getContext(), false) {
                        @Override
                        protected void onHandleSuccess(CommonBean commonBean) {
                            ToastUtils.showShortToast(getContext(), "消息发送成功");
                            edtContent.setText("");
                            getChatList();
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
        } else {
            ToastUtils.showShortToast(getContext(), "请填写要发送的内容");
        }

    }

    /**
     * 查看图片弹框
     */
    class CustomPopupWindow1 extends PopupWindow {

        Context mContext;
        private LayoutInflater mInflater;
        private View mContentView;

        private MyImageViewUtil ivDt;


        public CustomPopupWindow1(Context context) {
            super(context);
            this.mContext = context;
            //打气筒
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //打气
            mContentView = mInflater.inflate(R.layout.layout_popup_src, null);
            ivDt = mContentView.findViewById(R.id.iv_dt);
            ivDt.setImageURL(paths1);
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
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        EventBus.getDefault().unregister(this);
    }

}
