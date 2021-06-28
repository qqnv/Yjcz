package com.zzlecheng.yjcz.activity;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.base.BaseActivity;
import com.zzlecheng.yjcz.fragment.ProcedureChartFragment;
import com.zzlecheng.yjcz.fragment.ProcedureChatFragment;
import com.zzlecheng.yjcz.fragment.ProcedureDealFragment;
import com.zzlecheng.yjcz.fragment.ProcedureMainFragment;
import com.zzlecheng.yjcz.utils.LogUtils;

import sakura.bottomtabbar.BottomTabBar;

/**
 * @类名: ProcedureActivity
 * @描述: 流程模块
 * @作者: huangchao
 * @时间: 2018/12/5 11:33 AM
 * @版本: 1.0.0
 */
public class ProcedureActivity extends BaseActivity {
    @Override
    protected int setLayoutRes() {
        return R.layout.activity_procedure;
    }

    @Override
    protected void initView() {
        ((BottomTabBar) findViewById(R.id.BottomTabBar))
                .initFragmentorViewPager(getSupportFragmentManager())
                .addReplaceLayout(R.id.fl_content)
                .setImgSize(50, 50)
                .setTabPadding(15, 0, 0)
                .setFontSize(11)
                .setChangeColor(getResources().getColor(R.color.orange), getResources().getColor(R.color.white))
                .setTabBarBackgroundResource(R.color.main)
                .addTabItem("处理流程", getResources().getDrawable(R.mipmap.chuli_x),
                        getResources().getDrawable(R.mipmap.chuli), ProcedureDealFragment.class)
                .addTabItem("流程图", getResources().getDrawable(R.mipmap.liucheng_x),
                        getResources().getDrawable(R.mipmap.liucheng), ProcedureChartFragment.class)
                .addTabItem("应急会议室", getResources().getDrawable(R.mipmap.drhyh),
                        getResources().getDrawable(R.mipmap.drhy), ProcedureChatFragment.class)
                .addTabItem("关键点", getResources().getDrawable(R.mipmap.guanjiandian_x),
                        getResources().getDrawable(R.mipmap.guanjiandian), ProcedureMainFragment.class)
                .setOnTabChangeListener((position, V) -> LogUtils.e(""))
                .commit();
    }

    @Override
    protected void loadData() {

    }


}
