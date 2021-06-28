package com.zzlecheng.yjcz.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @类名: BaseFragment
 * @描述: fragment基类
 * @作者: huangchao
 * @时间: 2018/12/05 下午10:47
 * @版本: 1.0.0
 */
public abstract class BaseFragment extends RxFragment {

    private Unbinder unbind;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(setLayoutRes(), container, false);
        //butterKnife绑定
        unbind = ButterKnife.bind(this, view);
        initView();
        loadData();
        return view;
    }

    /**
     * 设置布局ID
     *
     * @return
     */
    protected abstract int setLayoutRes();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 加载数据
     */
    protected abstract void loadData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }
}
