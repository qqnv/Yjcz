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
import com.zzlecheng.yjcz.activity.ProcedureOperationActivity;
import com.zzlecheng.yjcz.adapter.EmergencyHandAdapter;
import com.zzlecheng.yjcz.adapter.ProcedureChartRightAdapter;
import com.zzlecheng.yjcz.base.BaseFragment;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.base.Commons;
import com.zzlecheng.yjcz.bean.CommonBean;
import com.zzlecheng.yjcz.bean.EmergencyBean;
import com.zzlecheng.yjcz.bean.ProcedureBean;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.AlertDialogManager;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;
import com.zzlecheng.yjcz.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @类名: ProcedureChartRightFragment
 * @描述: 进行中
 * @作者: huangchao
 * @时间: 2018/12/11 3:44 PM
 * @版本: 1.0.0
 */
public class ProcedureChartRightFragment extends BaseFragment {
    @BindView(R.id.rv_report)
    RecyclerView rvReport;
    @BindView(R.id.srl_report)
    SmartRefreshLayout srlReport;

    private ProcedureChartRightAdapter procedureChartRightAdapter;
    private List<ProcedureBean> beans;

    private AlertDialogManager mDialog;

    private String lcId = "";
    private String lclsId = "";
    private String lcmc = "";
    private String zzjgbm = "";
    private String userId = "";

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_emergency_hand;
    }

    @Override
    protected void initView() {

        beans = new ArrayList<>();

        Bundle bundle1 = getActivity().getIntent().getExtras();
        lcId = bundle1.getString("lcId");
        lclsId = bundle1.getString("lclsId");
        lcmc = bundle1.getString("lcmc");

        zzjgbm = SharedPreferenceUtils.getInstance().getString("zzjgbm");
        userId = SharedPreferenceUtils.getInstance().getString("userid");

        rvReport.setHasFixedSize(true);
        rvReport.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_custom));
        procedureChartRightAdapter = new ProcedureChartRightAdapter(getActivity(), beans);
//        rvReport.addItemDecoration(divider);
        rvReport.setAdapter(procedureChartRightAdapter);

        procedureChartRightAdapter.buttonOnClick((view,zt, state, content, jdid, jdname) -> {
            if ("1".equals(zt)){
                return;
            }
            if (Commons.NODE_TYPE_SURE.equals(state)) {//待确认
                AlertDialogManager.Builder builder = new AlertDialogManager.Builder(getContext());
                builder.setTitle("确认")
                        .setMessage(content)
                        .setPositiveButton("确认", (dialog, which) -> {
                            commitProcedure(lclsId, jdid, jdname, "确认", "node_type_sure");
                        })
                        .setNegativeButton("取消", (dialog, which) ->
                                mDialog.dismiss()
                        );
                mDialog = builder.create();
                mDialog.show();
            } else if (Commons.NODE_TYPE_MAKE.equals(state)){//待操作
                operation(lclsId, jdid, jdname);
            } else if (Commons.NODE_TYPE_END.equals(state)){
                AlertDialogManager.Builder builder = new AlertDialogManager.Builder(getContext());
                builder.setTitle("结束")
                        .setMessage(content)
                        .setPositiveButton("确认", (dialog, which) -> {
                            commitProcedure(lclsId, jdid, jdname, "结束", "node_type_end");
                        })
                        .setNegativeButton("取消", (dialog, which) ->
                                mDialog.dismiss()
                        );
                mDialog = builder.create();
                mDialog.show();
            }
        });
    }

    @Override
    protected void loadData() {
        srlReport.setOnRefreshListener(refreshlayout -> {
            getEmergency();
        });

        //触发自动刷新
//        if (isFirstEnter) {
//            isFirstEnter = false;
//            srlReport.autoRefresh();
//        } else {
            getEmergency();
//        }
    }


    private void getEmergency() {
        HttpMethods.getHttpMethods().getMyProcedure(getActivity(), lcId, lclsId, zzjgbm, "0",
                new BaseObserver<List<ProcedureBean>>(getActivity(), false) {
                    @Override
                    protected void onHandleSuccess(List<ProcedureBean> procedureBeans) {
                        if (procedureBeans.size() > 0) {
                            beans.clear();
                            beans.addAll(procedureBeans);
                            procedureChartRightAdapter.notifyDataSetChanged();
                            srlReport.finishRefresh();
                        } else {
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
                        srlReport.finishRefresh();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
//        srlReport.autoRefresh();
        getEmergency();
    }

    /**
     * 确认或结束流程
     *
     * @param lclsid
     * @param jdid
     * @param
     * @param jdname
     */
    private void commitProcedure(String lclsid, String jdid, String jdname, String state, String czlx) {
        userId = SharedPreferenceUtils.getInstance().getString("userid");
//        LogUtils.e(lclsid+"-"+jdid+"-"+state+"-"+jdname+"-"+userId+"-"+czlx+"-"+lcmc);
        HttpMethods.getHttpMethods().commitProcedure(getContext(), lcId, lclsid, jdid, state, jdname,
                userId, "", czlx, lcmc, "","0",
                new BaseObserver<CommonBean>(getContext(), false) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        ToastUtils.showShortToast(getContext(), state + "成功");
                        getEmergency();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.toString());
                    }

                    @Override
                    protected void onHandleError(String msg) {
                        LogUtils.e(msg);
                    }
                });
    }

    /**
     * 操作
     */
    private void operation(String lclsid, String jdid, String jdname) {
        Intent intent = new Intent(getContext(), ProcedureOperationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("lcid", lcId);
        bundle.putString("lclsId", lclsid);
        bundle.putString("jdId", jdid);
        bundle.putString("jdName", jdname);
        bundle.putString("lcmc", lcmc);
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }

}
