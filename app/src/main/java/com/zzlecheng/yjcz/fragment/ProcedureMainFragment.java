package com.zzlecheng.yjcz.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.base.BaseFragment;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.bean.GjdBean;
import com.zzlecheng.yjcz.bean.GjdfjBean;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @类名: ProcedureMainFragment
 * @描述: 流程-关键点
 * @作者: huangchao
 * @时间: 2018/12/5 11:42 AM
 * @版本: 1.0.0
 */
public class ProcedureMainFragment extends BaseFragment {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
    @BindView(R.id.tv_gjd)
    TextView tvGjd;

    private String lcid = "";

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_procedure_main;
    }

    @Override
    protected void initView() {
        tvTitle.setText(R.string.gjd);
        ibBack.setOnClickListener(v -> getActivity().finish());
    }

    @Override
    protected void loadData() {

        Bundle bundle = getActivity().getIntent().getExtras();
        lcid = bundle.getString("lcId");

        //关键点内容
        HttpMethods.getHttpMethods().getGjd(getContext(), lcid, "0",
                new BaseObserver<List<GjdBean>>(getContext(), false) {
                    @Override
                    protected void onHandleSuccess(List<GjdBean> gjdBeans) {
                        if (gjdBeans.size() > 0) {//判断是否有关键点
                            if ("".equals(gjdBeans.get(0).getContent())){//判断关键点是否配置为空
                                tvGjd.setText("暂无关键点，请在pc端配置后查看");
                            }else {
                                tvGjd.setText(gjdBeans.get(0).getContent());
                            }
                        } else {
                            tvGjd.setText("暂无关键点，请在pc端配置后查看");
                        }
                    }

                    @Override
                    protected void onHandleError(String msg) {
                        LogUtils.e(msg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.toString());
                    }
                });


    }

}
