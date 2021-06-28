package com.zzlecheng.yjcz.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.adapter.ReportControlAdapter;
import com.zzlecheng.yjcz.base.BaseActivity;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.bean.ReportTodayBean;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.LinearSpacingItemDecorationUtil;
import com.zzlecheng.yjcz.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @类名: ReportControlActivity
 * @描述: 值班管理
 * @作者: huangchao
 * @时间: 2018/9/3 下午3:49
 * @版本: 1.0.0
 */
public class ReportControlActivity extends BaseActivity {

    @BindView(R.id.rv_report)
    RecyclerView rvReport;
    @BindView(R.id.srl_report)
    SmartRefreshLayout srlReport;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.tv_reportTime)
    TextView tvReportTime;
    @BindView(R.id.tv_types)
    TextView tvTypes;
    private ReportControlAdapter reportControlAdapter;
    private List<ReportTodayBean> beans;
    private String userid = "";

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_report_control;
    }

    @Override
    protected void initView() {
        tvTitle.setText(R.string.sign_in);
        ibBack.setOnClickListener(v -> finish());
        beans = new ArrayList<>();
        rvReport.setHasFixedSize(true);
        rvReport.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        rvReport.addItemDecoration(new LinearSpacingItemDecorationUtil(8));
        reportControlAdapter = new ReportControlAdapter(this, beans);
        rvReport.setAdapter(reportControlAdapter);

        srlReport.setOnRefreshListener(refreshlayout -> {
            loadReport();
            if (srlReport.isRefreshing())
                srlReport.finishRefresh();
        });
    }

    @Override
    protected void loadData() {
        srlReport.autoRefresh();
    }

    private void loadReport() {
        HttpMethods.getHttpMethods().reportToday(this, userid,"0",
                new BaseObserver<List<ReportTodayBean>>(this, false) {
                    @Override
                    protected void onHandleSuccess(List<ReportTodayBean> reportTodayBeans) {
                        LogUtils.e(reportTodayBeans.toString() + "--");
                        beans.clear();
                        beans.addAll(reportTodayBeans);
                        reportControlAdapter.notifyDataSetChanged();
                    }
                });
    }

}
