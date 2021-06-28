package com.zzlecheng.yjcz.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.adapter.EmergencyHandAdapter;
import com.zzlecheng.yjcz.adapter.ProcedureSeeAdapter;
import com.zzlecheng.yjcz.base.BaseActivity;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.bean.ProcedureSeeBean;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @类名: ProcedureSeeActivity
 * @描述: 流程节点查看
 * @作者: huangchao
 * @时间: 2018/12/24 4:22 PM
 * @版本: 1.0.0
 */
public class ProcedureSeeActivity extends BaseActivity {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
    @BindView(R.id.tv_jdName)
    TextView tvJdName;
    @BindView(R.id.tv_jdDescribe)
    TextView tvJdDescribe;
    @BindView(R.id.rlv_recyclerView)
    RecyclerView rlvRecyclerView;

    private String id = "";

    private ProcedureSeeAdapter procedureSeeAdapter;
    private List<ProcedureSeeBean.PlBean> beans;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_procedure_see;
    }

    @Override
    protected void initView() {
        ibBack.setOnClickListener(v -> finish());
        tvTitle.setText("节点详情");

        beans = new ArrayList<>();

        rlvRecyclerView.setHasFixedSize(true);
        rlvRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_custom));
        procedureSeeAdapter = new ProcedureSeeAdapter(this, beans);
        rlvRecyclerView.addItemDecoration(divider);
        rlvRecyclerView.setAdapter(procedureSeeAdapter);
    }

    @Override
    protected void loadData() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        LogUtils.e(id);
        HttpMethods.getHttpMethods().getJd(this, id,"0",
                new BaseObserver<ProcedureSeeBean>(this, false) {
                    @Override
                    protected void onHandleSuccess(ProcedureSeeBean jdBean) {
                        tvJdName.setText(jdBean.getJdxq().getNodename());
                        tvJdDescribe.setText(jdBean.getJdxq().getNodedescribe());
//                        if (!"[]".equals(jdBean.getPl().toString().trim())){
//                            beans.clear();
//                            beans.addAll(jdBean.getPl());
//                            procedureSeeAdapter.notifyDataSetChanged();
//                        }
                    }
                });
    }

}
