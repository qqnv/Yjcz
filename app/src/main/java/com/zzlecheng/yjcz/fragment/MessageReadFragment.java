package com.zzlecheng.yjcz.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.activity.ProcedureActivity;
import com.zzlecheng.yjcz.adapter.MessageCommonAdapter;
import com.zzlecheng.yjcz.base.BaseFragment;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.bean.MessageBean;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @类名: MessageUnReadFragment
 * @描述: 已读消息
 * @作者: huangchao
 * @时间: 2018/12/27 4:45 PM
 * @版本: 1.0.0
 */
public class MessageReadFragment extends BaseFragment {
    @BindView(R.id.rv_report)
    RecyclerView rvReport;
    @BindView(R.id.srl_report)
    SmartRefreshLayout srlReport;

    private MessageCommonAdapter messageCommenAdapter;
    private List<MessageBean> beans;
    private boolean isFirstEnter = true;
    private String userId = "";

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_message_unread;
    }

    @Override
    protected void initView() {

        beans = new ArrayList<>();
        userId = SharedPreferenceUtils.getInstance().getString("userid");

        rvReport.setHasFixedSize(true);
        rvReport.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        messageCommenAdapter = new MessageCommonAdapter(getActivity(), beans);
        rvReport.setAdapter(messageCommenAdapter);

        messageCommenAdapter.buttonOnClick((view, id, lcid, lclsid, lcmc) -> {
            Intent intent = new Intent(getActivity(), ProcedureActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("lclsId", lclsid);
            bundle.putString("lcId",lcid);
            bundle.putString("lcmc",lcmc);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    @Override
    protected void loadData() {
        srlReport.setOnRefreshListener(refreshlayout -> {
            getMessage();
        });

        //触发自动刷新
        if (isFirstEnter) {
            isFirstEnter = false;
            srlReport.autoRefresh();
        } else {
            getMessage();
        }
    }

    private void getMessage() {
        HttpMethods.getHttpMethods().getMessage(getActivity(), userId,"1","0",
                new BaseObserver<List<MessageBean>>(getActivity(), false) {
                    @Override
                    protected void onHandleSuccess(List<MessageBean> messageBeans) {
                        if (messageBeans.size() > 0) {
                            beans.clear();
                            beans.addAll(messageBeans);
                            messageCommenAdapter.notifyDataSetChanged();
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
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        srlReport.autoRefresh();
    }

}
