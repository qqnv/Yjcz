package com.zzlecheng.yjcz.fragment;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.activity.ClusterCallMoreActivity;
import com.zzlecheng.yjcz.activity.ClusterCallOneActivity;
import com.zzlecheng.yjcz.adapter.ClusterMainListAdapter;
import com.zzlecheng.yjcz.base.BaseFragment;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.base.Commons;
import com.zzlecheng.yjcz.bean.ClusterMailListBean;
import com.zzlecheng.yjcz.bean.TreePoint;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.TreeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.KEYGUARD_SERVICE;
import static com.zzlecheng.yjcz.activity.ClusterSchedulingActivity.isVideo;
import static com.zzlecheng.yjcz.activity.ClusterSchedulingActivity.me;

/**
 * @类名: ClusterMailListFragment
 * @描述: 通讯录列表
 * @作者: huangchao
 * @时间: 2019/1/18 10:12 AM
 * @版本: 1.0.0
 */
public class ClusterMailListFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_complete)
    TextView tvComplete;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
    @BindView(R.id.et_filter)
    EditText etFilter;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.fab_button)
    FloatingActionButton fabButton;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.tv_one)
    TextView tvOne;
    @BindView(R.id.tv_two)
    TextView tvTwo;
    @BindView(R.id.tv_three)
    TextView tvThree;
    @BindView(R.id.tv_four)
    TextView tvFour;
    @BindView(R.id.tv_five)
    TextView tvFive;
    @BindView(R.id.tv_six)
    TextView tvSix;
    @BindView(R.id.tv_seven)
    TextView tvSeven;
    @BindView(R.id.tv_eight)
    TextView tvEight;
    @BindView(R.id.tv_nine)
    TextView tvNine;
    @BindView(R.id.tv_x)
    TextView tvX;
    @BindView(R.id.tv_zero)
    TextView tvZero;
    @BindView(R.id.tv_j)
    TextView tvJ;
    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.tv_audio)
    TextView tvAudio;
    @BindView(R.id.tv_video)
    TextView tvVideo;
    @BindView(R.id.ll_keyboard)
    LinearLayout llKeyboard;
    Unbinder unbinder;
    private int height;
    private int width;
    private int popupHeight;
    private int popupWidth;

    CustomPopupWindow popupWindow;

    private ClusterMainListAdapter adapter;
    private List<TreePoint> pointList = new ArrayList<>();
    private HashMap<String, TreePoint> pointMap = new HashMap<>();

    private StringBuilder callNumber = null;

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_cluster_mail_list;
    }

    @Override
    protected void initView() {

        //测量屏幕及模块宽高
        DisplayMetrics dm = getResources().getDisplayMetrics();
        height = dm.heightPixels;
        width = dm.widthPixels;
        popupWidth = width / 4 * 3;
        popupHeight = height / 10;

        ibBack.setOnClickListener(v -> getActivity().finish());
        tvTitle.setText("通讯录");
        adapter = new ClusterMainListAdapter(getContext(), pointList, pointMap);
        listView.setAdapter(adapter);
        adapter.buttonSetOnclick((view, id, name, az) -> {
            if (!"".equals(az)) {
                popupWindow = new CustomPopupWindow(getContext());
                int[] location = new int[2];
                int popupWidth1 = view.getMeasuredWidth();
                int popupHeight1 = view.getMeasuredHeight();
                view.getLocationOnScreen(location);
                popupWindow.showAtLocation(view, Gravity.NO_GRAVITY,
                        (location[0] + view.getWidth() / 2) - popupWidth1 / 8,
                        location[1] - popupHeight1);
                popupWindow.setOnDismissListener(new popupDismissListener());
            }
        });

        fabButton.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        tvOne.setOnClickListener(this);
        tvTwo.setOnClickListener(this);
        tvThree.setOnClickListener(this);
        tvFour.setOnClickListener(this);
        tvFive.setOnClickListener(this);
        tvSix.setOnClickListener(this);
        tvSeven.setOnClickListener(this);
        tvEight.setOnClickListener(this);
        tvNine.setOnClickListener(this);
        tvX.setOnClickListener(this);
        tvZero.setOnClickListener(this);
        tvJ.setOnClickListener(this);
        tvClose.setOnClickListener(this);
        tvAudio.setOnClickListener(this);
        tvVideo.setOnClickListener(this);
        tvComplete.setOnClickListener(this);
//        initMainData();
        addListener();
    }

    private void initMainData() {
        HttpMethods.getHttpMethods().getClusterMainList(getContext(), "0",
                new BaseObserver<List<ClusterMailListBean>>(getContext(), false) {
                    @Override
                    protected void onHandleSuccess(List<ClusterMailListBean> clusterMailListBeans) {
                        pointList.clear();
                        for (int i = 0; i < clusterMailListBeans.size(); i++) {
                            String parentId;
                            if ("".equals(clusterMailListBeans.get(i).getPid())) {
                                parentId = "0";
                            } else {
                                parentId = clusterMailListBeans.get(i).getPid();
                            }
                            String id = clusterMailListBeans.get(i).getId();
                            String name = clusterMailListBeans.get(i).getLabel();
                            String azkjid = clusterMailListBeans.get(i).getAzkjid();
                            String isLeaf;
                            if ("".equals(clusterMailListBeans.get(i).getType())) {//若为空则是最后一层
                                isLeaf = "1";
                            } else {
                                isLeaf = "0";
                            }
                            pointList.add(new TreePoint(id, name, parentId, isLeaf, i + 1, azkjid));
                        }
                        //打乱集合中的数据
                        Collections.shuffle(pointList);
                        //对集合中的数据重新排序
                        updateData();
                    }
                });
    }

    public void addListener() {

        listView.setOnItemClickListener((parent, view, position, id) -> adapter.onItemClickAll(position, view));

        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchAdapter(s);
            }
        });
    }

    private void searchAdapter(Editable s) {
        adapter.setKeyword(s.toString());
    }

    //对数据排序 深度优先
    private void updateData() {
        for (TreePoint treePoint : pointList) {
            pointMap.put(treePoint.getID(), treePoint);
        }
        Collections.sort(pointList, new Comparator<TreePoint>() {
            @Override
            public int compare(TreePoint lhs, TreePoint rhs) {
                int llevel = TreeUtils.getLevel(lhs, pointMap);
                int rlevel = TreeUtils.getLevel(rhs, pointMap);
                if (llevel == rlevel) {
                    if (lhs.getPARENTID().equals(rhs.getPARENTID())) {  //左边小
                        return lhs.getDISPLAY_ORDER() > rhs.getDISPLAY_ORDER() ? 1 : -1;
                    } else {  //如果父辈id不相等
                        //同一级别，不同父辈
                        TreePoint ltreePoint = TreeUtils.getTreePoint(lhs.getPARENTID(), pointMap);
                        TreePoint rtreePoint = TreeUtils.getTreePoint(rhs.getPARENTID(), pointMap);
                        return compare(ltreePoint, rtreePoint);  //父辈
                    }
                } else {  //不同级别
                    if (llevel > rlevel) {   //左边级别大       左边小
                        if (lhs.getPARENTID().equals(rhs.getID())) {
                            return 1;
                        } else {
                            TreePoint lreasonTreePoint = TreeUtils.getTreePoint(lhs.getPARENTID(), pointMap);
                            return compare(lreasonTreePoint, rhs);
                        }
                    } else {   //右边级别大   右边小
                        if (rhs.getPARENTID().equals(lhs.getID())) {
                            return -1;
                        }
                        TreePoint reasonTreePoint = TreeUtils.getTreePoint(rhs.getPARENTID(), pointMap);
                        return compare(lhs, reasonTreePoint);
                    }
                }
            }
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_button://点击弹出软键盘
                if (callNumber == null) {
                    callNumber = new StringBuilder();
                }
                llKeyboard.setVisibility(View.VISIBLE);
                fabButton.setVisibility(View.GONE);
                break;
            case R.id.iv_delete://删除最后一位
                if (callNumber.length() > 0) {
                    callNumber = callNumber.deleteCharAt(callNumber.length() - 1);
                    tvContent.setText(callNumber);
                }
                break;
            case R.id.tv_one:
                callNumber.append("1");
                tvContent.setText(callNumber);
                break;
            case R.id.tv_two:
                callNumber.append("2");
                tvContent.setText(callNumber);
                break;
            case R.id.tv_three:
                callNumber.append("3");
                tvContent.setText(callNumber);
                break;
            case R.id.tv_four:
                callNumber.append("4");
                tvContent.setText(callNumber);
                break;
            case R.id.tv_five:
                callNumber.append("5");
                tvContent.setText(callNumber);
                break;
            case R.id.tv_six:
                callNumber.append("6");
                tvContent.setText(callNumber);
                break;
            case R.id.tv_seven:
                callNumber.append("7");
                tvContent.setText(callNumber);
                break;
            case R.id.tv_eight:
                callNumber.append("8");
                tvContent.setText(callNumber);
                break;
            case R.id.tv_nine:
                callNumber.append("9");
                tvContent.setText(callNumber);
                break;
            case R.id.tv_x:
                callNumber.append("*");
                tvContent.setText(callNumber);
                break;
            case R.id.tv_zero:
                callNumber.append("0");
                tvContent.setText(callNumber);
                break;
            case R.id.tv_j:
                callNumber.append("#");
                tvContent.setText(callNumber);
                break;
            case R.id.tv_close://点击关闭软件盘
                llKeyboard.setVisibility(View.GONE);
                fabButton.setVisibility(View.VISIBLE);
                callNumber = null;
                tvContent.setText("");
                break;
            case R.id.tv_audio://发起语音呼叫
                me.ME_MakeCall(String.valueOf(callNumber), false);
                isVideo = false;
                showCallActivity();
                break;
            case R.id.tv_video://发起视频呼叫
                me.ME_MakeCall(String.valueOf(callNumber), true);
                isVideo = true;
                showCallActivity();
                break;
            case R.id.tv_complete:
                startActivity(new Intent(getContext(), ClusterCallMoreActivity.class));
                break;
        }
    }

    /**
     * 查看人员弹框
     */
    class CustomPopupWindow extends PopupWindow implements View.OnClickListener {

        Context mContext;
        private LayoutInflater mInflater;
        private View mContentView;

        public CustomPopupWindow(Context context) {
            super(context);

            this.mContext = context;
            //打气筒
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //打气
            mContentView = mInflater.inflate(R.layout.layout_pop_cluster_mail, null);

            LinearLayout llAudio = mContentView.findViewById(R.id.ll_audio);
            LinearLayout llVideo = mContentView.findViewById(R.id.ll_video);
            LinearLayout llMessage = mContentView.findViewById(R.id.ll_message);

            llAudio.setOnClickListener(this);
            llVideo.setOnClickListener(this);
            llMessage.setOnClickListener(this);

            //设置View
            setContentView(mContentView);
            //设置宽与高
            setWidth(popupWidth);
            setHeight(popupHeight);
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

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_audio://拨打语音电话
                    me.ME_MakeCall(Commons.CALL_NAME, false);
                    isVideo = false;
                    showCallActivity();
                    return;
                case R.id.ll_video://拨打视频电话
                    me.ME_MakeCall(Commons.CALL_NAME, true);
                    isVideo = true;
                    showCallActivity();
                    return;
                case R.id.ll_message://发送短信
                    return;
            }
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
     * 跳转到接听界面
     */
    private void showCallActivity() {
        //解锁
        KeyguardManager keyguardManager = (KeyguardManager) getActivity().getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
        keyguardLock.disableKeyguard();
        //跳转
        Intent intent = new Intent(getContext(), ClusterCallOneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        initMainData();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        llKeyboard.setVisibility(View.GONE);
        fabButton.setVisibility(View.VISIBLE);
        callNumber = null;
        tvContent.setText("");
    }
}
