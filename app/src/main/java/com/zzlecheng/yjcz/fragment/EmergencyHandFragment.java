package com.zzlecheng.yjcz.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.activity.ProcedureActivity;
import com.zzlecheng.yjcz.adapter.EmergencyHandAdapter;
import com.zzlecheng.yjcz.base.BaseFragment;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.bean.EmergencyBean;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @类名: EmergencyHandFragment
 * @描述: 进行中
 * @作者: huangchao
 * @时间: 2018/12/11 3:44 PM
 * @版本: 1.0.0
 */
public class EmergencyHandFragment extends BaseFragment {
    @BindView(R.id.rv_report)
    RecyclerView rvReport;
    @BindView(R.id.srl_report)
    SmartRefreshLayout srlReport;

    private EmergencyHandAdapter emergencyHandAdapter;
    private List<EmergencyBean> beans;
    private boolean isFirstEnter = true;

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_emergency_hand;
    }

    @Override
    protected void initView() {
        beans = new ArrayList<>();

        rvReport.setHasFixedSize(true);
        rvReport.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_custom));
        emergencyHandAdapter = new EmergencyHandAdapter(getActivity(), beans);
//        rvReport.addItemDecoration(divider);
        rvReport.setAdapter(emergencyHandAdapter);

        emergencyHandAdapter.buttonOnClick((view,lclsId,lcId,lcmc) -> {
//            LogUtils.e("&lclsid="+lclsId+"&lcid="+lcId+"&zzjgbm="+ SharedPreferenceUtils.getInstance().getString("zzjgbm"));
            Intent intent = new Intent(getActivity(), ProcedureActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("lclsId", lclsId);
            bundle.putString("lcId",lcId);
            bundle.putString("lcmc",lcmc);
            intent.putExtras(bundle);
            startActivity(intent);
//
        });
    }

    @Override
    protected void loadData() {
        srlReport.setOnRefreshListener(refreshlayout -> {
            getEmergency();
        });
        getEmergency();

//        //触发自动刷新
//        if (isFirstEnter) {
//            isFirstEnter = false;
//            srlReport.autoRefresh();
//        } else {
//            getEmergency();
//        }
    }


    private void getEmergency() {
        HttpMethods.getHttpMethods().getEmergency(getActivity(), "1","0",
                new BaseObserver<List<EmergencyBean>>(getActivity(), false) {
                    @Override
                    protected void onHandleSuccess(List<EmergencyBean> emergencyBeans) {
                        if (emergencyBeans.size() > 0) {
                            beans.clear();
                            beans.addAll(emergencyBeans);
                            emergencyHandAdapter.notifyDataSetChanged();
                            srlReport.finishRefresh();
                        }else {
                            srlReport.finishRefresh();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        srlReport.finishRefresh();
                    }

                    @Override
                    protected void onHandleError(String msg) {
                        super.onHandleError(msg);
                        srlReport.finishRefresh();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getEmergency();
    }
}
