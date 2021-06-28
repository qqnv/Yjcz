package com.zzlecheng.yjcz.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.othershe.library.NiceImageView;
import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.adapter.ClusterPopupAdapter;
import com.zzlecheng.yjcz.base.BaseFragment;
import com.zzlecheng.yjcz.eventbus.ClusterNameBean;
import com.zzlecheng.yjcz.eventbus.EventBusBean;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.sword.SDK.MediaEngine;

/**
 * @类名: ClusterTalkBackFragment
 * @描述: 对讲功能
 * @作者: huangchao
 * @时间: 2019/1/18 10:09 AM
 * @版本: 1.0.0
 */
public class ClusterTalkBackFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.tv_djz)
    TextView tvDjz;
    @BindView(R.id.tv_jhr)
    TextView tvJhr;
    @BindView(R.id.tv_dqzt)
    TextView tvDqzt;
    @BindView(R.id.iv_voice)
    ImageView ivVoice;
    @BindView(R.id.niv_qz)
    NiceImageView nivQz;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
    @BindView(R.id.sp_djz)
    AppCompatSpinner spDjz;


    public static boolean isDj = true;
    //第一次接受消息
    private boolean isFirst = true;

    //弹出的人员
    private EventBusBean eventBusBeans;
    private ClusterPopupAdapter clusterPopupAdapter;

    private List<String> talkGroupId = new ArrayList<>();
    private List<String> talkGroupName = new ArrayList<>();

    public static String currentPttGroup = "";
    private ArrayAdapter<String> adapter;

    public static int currentGroupIds;

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_cluster_talkback;
    }

    @Override
    protected void initView() {
        //注册订阅者
        EventBus.getDefault().register(this);
        tvTitle.setText("对讲组");
        ibBack.setOnClickListener(this);
        nivQz.setOnClickListener(this);
        tvJhr.setText("讲话人：暂无");
        tvDqzt.setText("当前状态：空闲");
        if (talkGroupId.size()>0){
            currentPttGroup = talkGroupId.get(0);
            currentGroupIds = 0;
        }

        spDjz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentPttGroup = talkGroupId.get(position);
                currentGroupIds = position;
                SharedPreferenceUtils.getInstance().putString("currentGroup",currentPttGroup);
                TextView textView = (TextView) view;
                textView.setTextColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ivVoice.setOnTouchListener((v, event) -> {
            if ("".equals(currentPttGroup)) {
                currentPttGroup = talkGroupId.get(0);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                isDj = false;
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(2000);//休眠2秒
                            isDj = true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                //弹起
                MediaEngine.GetInstance().ME_PttReqRight_Async(currentPttGroup,
                        false, (press, success) -> {
                        });
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                isDj = true;
                //按下
                MediaEngine.GetInstance().ME_PttReqRight_Async(currentPttGroup,
                        true, (press, success) -> {
                        });
            }
            return true;

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back://返回主页
                getActivity().finish();
                break;
            case R.id.niv_qz://显示对讲组列表成员
                CustomPopupWindow popupWindow = new CustomPopupWindow(getContext());
                popupWindow.showAtLocation(getActivity().findViewById(R.id.tv_title),
                        Gravity.CENTER, 0, 180);
                popupWindow.setOnDismissListener(new popupDismissListener());
                break;

        }
    }

    //定义处理接收的方法
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void userEventBus(EventBusBean eventBusBean) {
        eventBusBeans =  null;
        eventBusBeans = new EventBusBean(eventBusBean.getTalkGroupId(),
                eventBusBean.getTalkGroupName(), eventBusBean.getDjzcy(),
                eventBusBean.getCurrentSpeaker());
        talkGroupId = eventBusBeans.getTalkGroupId();
        talkGroupName = eventBusBeans.getTalkGroupName();
        String jhr = eventBusBeans.getCurrentSpeaker();
        LogUtils.e("接收"+eventBusBean.toString());
        if (isFirst){
            initAdapter();
        }
        if (isDj) {
            if ("".equals(jhr) || "暂无".equals(jhr)) {
                tvJhr.setText("讲话人：暂无");
                tvDqzt.setText("当前状态：空闲");
            } else {
                tvJhr.setText("讲话人：" + jhr);
                tvDqzt.setText("当前状态：对讲中...");
            }
        }else {
            tvJhr.setText("讲话人：暂无");
            tvDqzt.setText("当前状态：空闲");
        }
    }

    private void initAdapter(){
        adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, talkGroupName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDjz.setAdapter(adapter);
        isFirst = false;
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
            List<ClusterNameBean> clusterNameBeans = new ArrayList<>();
            for (int i = 0; i<eventBusBeans.getDjzcy().get(currentGroupIds).size(); i++){
                ClusterNameBean clusterNameBean = new ClusterNameBean();
                clusterNameBean.setName(eventBusBeans.getDjzcy().get(currentGroupIds).get(i));
                clusterNameBeans.add(clusterNameBean);
            }
            //测量屏幕及模块宽高
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int height = dm.heightPixels;
            int width = dm.widthPixels;
            this.mContext = context;
            //打气筒
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //打气
            mContentView = mInflater.inflate(R.layout.layout_popup_cluster, null);

            RecyclerView recyclerView = mContentView.findViewById(R.id.rlv_recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL, false));

            clusterPopupAdapter = new ClusterPopupAdapter(getActivity(), clusterNameBeans);
            recyclerView.setAdapter(clusterPopupAdapter);

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

    @Override
    protected void loadData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销注册
        EventBus.getDefault().unregister(this);
    }

}
