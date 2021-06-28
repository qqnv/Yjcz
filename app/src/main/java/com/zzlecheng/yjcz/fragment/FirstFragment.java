package com.zzlecheng.yjcz.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.activity.ReportControlActivity;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.bean.CommonBean;
import com.zzlecheng.yjcz.bean.ReportTodayBean;
import com.zzlecheng.yjcz.bean.ReportTypesBean;
import com.zzlecheng.yjcz.bean.UnReportBean;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;
import com.zzlecheng.yjcz.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @类名: SecondFragment
 * @描述:
 * @作者: huangchao
 * @时间: 2018/8/21 上午9:23
 * @版本: 1.0.0
 */
public class FirstFragment extends Fragment {
    @BindView(R.id.btn_content1)
    Button btnContent1;
    Unbinder unbinder;
    @BindView(R.id.btn_content2)
    Button btnContent2;
    @BindView(R.id.btn_content3)
    Button btnContent3;
    @BindView(R.id.btn_content4)
    Button btnContent4;

    //报班人员部门ID
    private String departmentid;
    //报班类型
    private String types = "0";
    //报班人员ID
    private String userid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }


    private void initView() {
        btnContent1.setOnClickListener(view -> onButton1());
        btnContent2.setOnClickListener(view -> onButton2());
        btnContent3.setOnClickListener(view -> unReport());
        btnContent4.setOnClickListener(view -> startActivity(
                new Intent(getContext(), ReportControlActivity.class)));
        departmentid = SharedPreferenceUtils.getInstance().getString("departmentid");
        userid = SharedPreferenceUtils.getInstance().getString("userid");
    }

    private void initData() {
        //查询报班情况
        HttpMethods.getHttpMethods().reportToday(getContext(), userid,"0",
                new BaseObserver<List<ReportTodayBean>>(getContext(), false) {
                    @Override
                    protected void onHandleSuccess(List<ReportTodayBean> reportTodayBeans) {
                        LogUtils.e(reportTodayBeans.toString());
                    }
                });
        HttpMethods.getHttpMethods().reportTypes(getContext(),"0",
                new BaseObserver<ReportTypesBean>(getContext(), false) {
                    @Override
                    protected void onHandleSuccess(ReportTypesBean reportTypesBean) {
                        LogUtils.e(reportTypesBean.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LogUtils.e(e.toString());
                    }
                });
    }

    private void onButton1() {
        Runnable runnable = () -> ToastUtils.showShortToast(getActivity(), "abcd");
        runnable.run();
    }

    /**
     * 报班
     */
    private void onButton2() {
        CustomPopupWindow popupWindow = new CustomPopupWindow(getActivity());
        popupWindow.showAsDropDown(getActivity().findViewById(R.id.btn_content1),
                Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    /**
     * 报班弹框
     */
    class CustomPopupWindow extends PopupWindow implements View.OnClickListener {

        private View window;

        public CustomPopupWindow(Activity context) {
            super(context);
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            window = inflater.inflate(R.layout.layout_popup_bb, null);
            window.findViewById(R.id.rb_day).setOnClickListener(this);
            window.findViewById(R.id.rb_night).setOnClickListener(this);
            window.findViewById(R.id.rb_allDay).setOnClickListener(this);
            window.findViewById(R.id.tv_cancel).setOnClickListener(this);
            window.findViewById(R.id.tv_sure).setOnClickListener(this);
            //设置PopWindow的view
            this.setContentView(window);
            //设置弹出窗体的宽
            this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            //设置弹出窗体的高
            this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            //设置弹出窗体可点击
            this.setFocusable(true);
            //设置弹出窗体动画
//        this.setAnimationStyle();
            //设置PopWindow弹出窗体的背景
            this.setBackgroundDrawable(new ColorDrawable(0x00ff0000));
            //获取手指触摸位置，若在窗口外则让窗体消失
//            window.setOnTouchListener((view, motionEvent) -> {
//                int height = window.findViewById(R.id.ll_pop).getTop();
//                int y = (int) motionEvent.getY();
//                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                        dismiss();
//                    }
//                }
//                return true;
//            });

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rb_day:
                    types = "1";
                    return;
                case R.id.rb_night:
                    types = "2";
                    return;
                case R.id.rb_allDay:
                    types = "3";
                    return;
                case R.id.tv_cancel:
                    types = "0";
                    dismiss();
                    return;
                case R.id.tv_sure:
                    if (types == "0") {
                        ToastUtils.showShortToast(getContext(), "请选择报班类型");
                    } else {
                        report();
                        dismiss();
                    }
                    return;
            }
        }
    }

    /**
     * 报班
     */
    private void report() {
        LogUtils.e(departmentid + "-" + types + "-" + userid);
        HttpMethods.getHttpMethods().report(getContext(),departmentid, types, userid,"0",
                new BaseObserver<CommonBean>(getContext(), true) {
                    @Override
                    protected void onHandleSuccess(CommonBean reportBean) {
                        ToastUtils.showShortToast(getContext(), "报班成功");
                    }
                });
    }

    private void unReport() {
        HttpMethods.getHttpMethods().unReport(getContext(),departmentid, userid,"0",
                new BaseObserver<UnReportBean>(getContext(), true) {
                    @Override
                    protected void onHandleSuccess(UnReportBean unReportBean) {
                        ToastUtils.showShortToast(getContext(), "退班成功");
                    }
                });
    }
}