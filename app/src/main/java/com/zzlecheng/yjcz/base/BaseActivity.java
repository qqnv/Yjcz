package com.zzlecheng.yjcz.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zzlecheng.yjcz.utils.ActivityManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @类名: BaseActivity
 * @描述: 基类，所有activity继承自该类
 * @作者: huangchao
 * @时间: 2018/8/20 下午2:38
 * @版本: 1.0.0
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    private Unbinder unbind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //standard栈顶复用模式
        ActivityManager.getInstance().pushActivity(this);
        setContentView(setLayoutRes());
        //butterKnife绑定
        unbind = ButterKnife.bind(this);
        initView();
        loadData();
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
    protected void onDestroy() {
        super.onDestroy();
        unbind.unbind();
        ActivityManager.getInstance().popActivity(this);
    }

}
