package com.zzlecheng.yjcz.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.adapter.MyFragmentAdapter;
import com.zzlecheng.yjcz.base.BaseFragment;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.view.ViewPagerSlide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class ProcedureChartFragment extends BaseFragment implements ViewPager.OnPageChangeListener{

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPagerSlide viewpager;

    private List<Fragment> mFragments = new ArrayList<>();//标签页集合
    private MyFragmentAdapter mFragmentAdapter;//自定义适配器
    private List<String> mTitles = new ArrayList<>();//标签集合

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_procedure_chart;
    }

    @Override
    protected void initView() {
        ibBack.setOnClickListener(v -> getActivity().finish());
        tvTitle.setText("流程");

        mTitles.add("流程图");
        mTitles.add("我的任务");
        mFragments.add(new ProcedureChartLeftFragment());
        mFragments.add(new ProcedureChartRightFragment());

        mFragmentAdapter = new MyFragmentAdapter(getChildFragmentManager(), mFragments, mTitles);
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
