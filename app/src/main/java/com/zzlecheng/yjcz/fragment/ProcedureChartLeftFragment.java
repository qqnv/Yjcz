package com.zzlecheng.yjcz.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.base.BaseFragment;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;
import com.zzlecheng.yjcz.view.MyProcedureView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

import static com.zzlecheng.yjcz.MainActivity.me;
import static com.zzlecheng.yjcz.fragment.ClusterTalkBackFragment.currentPttGroup;

/**
 * @类名: ProcedureChartLeftFragment
 * @描述:
 * @作者: huangchao
 * @时间: 2019/1/11 9:55 AM
 * @版本: 1.0.0
 */
public class ProcedureChartLeftFragment extends BaseFragment {

    @BindView(R.id.my_procedureView)
    MyProcedureView myProcedureView;
    @BindView(R.id.fab_button)
    FloatingActionButton fabButton;

    private String lcId = "";
    private String lclsId = "";
    private String lcmc = "";

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_procedure_chart_left;
    }

    @Override
    protected void initView() {


        Bundle bundle = getActivity().getIntent().getExtras();
        lcId = bundle.getString("lcId");
        lclsId = bundle.getString("lclsId");
        lcmc = bundle.getString("lcmc");
        SharedPreferenceUtils.getInstance().putString("lcId", lcId);
        SharedPreferenceUtils.getInstance().putString("lclsId", lclsId);
        SharedPreferenceUtils.getInstance().putString("lcmc",lcmc);
        LogUtils.e("&lcid=" + lcId + "&lclsid=" + lclsId + "&zzjgbm=" +
                SharedPreferenceUtils.getInstance().getString("zzjgbm"));
        myProcedureView = new MyProcedureView(getContext());

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


    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销注册
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void loadData() {

    }

}
