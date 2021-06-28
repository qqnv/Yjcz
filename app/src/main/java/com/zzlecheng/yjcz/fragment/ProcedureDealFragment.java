package com.zzlecheng.yjcz.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.activity.CommentActivity;
import com.zzlecheng.yjcz.activity.VideoPlayActivity;
import com.zzlecheng.yjcz.adapter.ProcedureDealAdapter;
import com.zzlecheng.yjcz.base.BaseFragment;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.bean.ProcedureDealBean;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.MyImageViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import static com.zzlecheng.yjcz.MainActivity.me;
import static com.zzlecheng.yjcz.fragment.ClusterTalkBackFragment.currentPttGroup;

/**
 * @类名: ProcedureDealFragment
 * @描述: 流程-流程详情
 * @作者: huangchao
 * @时间: 2018/12/5 11:40 AM
 * @版本: 1.0.0
 */
public class ProcedureDealFragment extends BaseFragment {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
    @BindView(R.id.rv_procedure)
    RecyclerView rvProcedure;
    @BindView(R.id.ib_setUp)
    ImageButton ibSetUp;
    @BindView(R.id.fab_button)
    FloatingActionButton fabButton;


    private ProcedureDealAdapter procedureDealAdapter;
    private List<Object> beans = new ArrayList<>();
    //大图地址
    private String paths = "";

    private String lclsId = "";

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_procedure_deal;
    }

    @Override
    protected void initView() {
        tvTitle.setText(R.string.cllc);
        ibBack.setOnClickListener(v -> getActivity().finish());

        Bundle bundle = getActivity().getIntent().getExtras();
        lclsId = bundle.getString("lclsId");

        rvProcedure.setLayoutManager(new LinearLayoutManager(getContext()));
        procedureDealAdapter = new ProcedureDealAdapter(getContext(), beans);
        rvProcedure.setAdapter(procedureDealAdapter);
        procedureDealAdapter.buttonSetOnclick(new ProcedureDealAdapter.ButtonInterface() {
            @Override
            public void onCallClick(View view, String phone) {
                //拨打电话
                Uri uri = Uri.parse("tel:" + phone);
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                startActivity(intent);
            }

            @Override
            public void onMessageClick(View view, String phone) {
                //发送短信
                Intent intentFinalMessage = new Intent(Intent.ACTION_VIEW);
                intentFinalMessage.setType("vnd.android-dir/mms-sms");
                startActivity(intentFinalMessage);
            }

            @Override
            public void onCommentClick(View view, String lclsIds, String jdlsId) {
                Intent intent1 = new Intent(getContext(), CommentActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("lclsId", lclsId);
                bundle1.putString("jdlsId", jdlsId);
                intent1.putExtras(bundle1);
                getContext().startActivity(intent1);
            }

            @Override
            public void onImgClick(View view, String path, String lx) {
                if ("1".equals(lx)) {
                    Intent intent = new Intent(getContext(), VideoPlayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", path);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    //大图全屏展示
                    paths = path;
                    CustomPopupWindow popupWindow = new CustomPopupWindow(getContext());
                    popupWindow.showAtLocation(getActivity().findViewById(R.id.ib_back),
                            Gravity.CENTER, 0, 0);
                    popupWindow.setOnDismissListener(new popupDismissListener());
                }
            }
        });

        fabButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                //弹起
                me.ME_PttReqRight_Async(currentPttGroup, false, (press, success) -> {

                });
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                me.ME_PttReqRight_Async(currentPttGroup, true, (press, success) -> {

                });
            }
            return true;

        });
    }

    private void initData() {
        HttpMethods.getHttpMethods().getProcedureDeal(getContext(), lclsId, "0",
                new BaseObserver<List<ProcedureDealBean>>(getContext(), false) {
                    @Override
                    protected void onHandleSuccess(List<ProcedureDealBean> procedureBeans) {
                        beans.clear();
                        for (int i = 0; i < procedureBeans.size(); i++) {
                            ProcedureDealBean.LcxqBean headerBean = procedureBeans.get(i).getLcxq();
                            beans.add(headerBean);
                            List<ProcedureDealBean.PlBean> plBeans = procedureBeans.get(i).getPl();
                            for (int m = 0; m < plBeans.size(); m++) {
                                ProcedureDealBean.PlBean plBean = plBeans.get(m);
                                ProcedureDealBean.PlBean.PlxqBean contentBean = plBean.getPlxq();
                                beans.add(contentBean);
                                for (int n = 0; n < plBean.getPlfj().size(); n++) {
                                    ProcedureDealBean.PlBean.PlfjBean footerBean = plBean.getPlfj().get(n);
                                    beans.add(footerBean);
                                }
                            }

                        }
                        procedureDealAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected void loadData() {

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
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = f;
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
