package com.zzlecheng.yjcz.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.adapter.MyFragmentAdapter;
import com.zzlecheng.yjcz.base.BaseActivity;
import com.zzlecheng.yjcz.fragment.EmergencyEndFragment;
import com.zzlecheng.yjcz.fragment.EmergencyHandFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @类名: EmergencyActivity
 * @描述: 应急演练或应急处置
 * @作者: huangchao
 * @时间: 2018/9/4 下午7:16
 * @版本: 1.0.0
 */
public class EmergencyActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private boolean isYl;

    private List<Fragment> mFragments = new ArrayList<>();//标签页集合
    private MyFragmentAdapter mFragmentAdapter;//自定义适配器
    private List<String> mTitles = new ArrayList<>();//标签集合

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_emergency;
    }

    @Override
    protected void initView() {
        ibBack.setOnClickListener(v -> finish());
        Bundle bundle = getIntent().getExtras();
        isYl = bundle.getBoolean("isYl");
        if (isYl) {//演练
            tvTitle.setText(R.string.drill);
        } else {//处置
            tvTitle.setText(R.string.emergency);
        }

        mTitles.add("进行中");
        mTitles.add("已结束");
        mFragments.add(new EmergencyHandFragment());
        mFragments.add(new EmergencyEndFragment());


        mFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), mFragments, mTitles);
        viewpager.setAdapter(mFragmentAdapter);//ViewPager设置适配器
        viewpager.addOnPageChangeListener(this);//将ＶｉｅｗＰａｇｅｒ加入到该Ａｃｔｉｖｉｔｙ中
        tablayout.setTabMode(TabLayout.MODE_FIXED);//设置ＴａｂＬａｙｏｕｔ模式，当前为系统默认模式．
        tablayout.setupWithViewPager(viewpager);//将ViewPager和Tablayout关联起来
        tablayout.setTabsFromPagerAdapter(mFragmentAdapter);//TabLayout设置加适配器
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
