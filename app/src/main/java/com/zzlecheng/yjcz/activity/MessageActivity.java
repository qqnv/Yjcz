package com.zzlecheng.yjcz.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.adapter.MyFragmentAdapter;
import com.zzlecheng.yjcz.base.BaseActivity;
import com.zzlecheng.yjcz.fragment.MessageAllFragment;
import com.zzlecheng.yjcz.fragment.MessageReadFragment;
import com.zzlecheng.yjcz.fragment.MessageUnReadFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @类名: MessageActivity
 * @描述: 消息中心
 * @作者: huangchao
 * @时间: 2018/7/25 下午4:49
 * @版本: 1.0.0
 */
public class MessageActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
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
        tvTitle.setText("消息中心");

        mTitles.add("未读");
        mTitles.add("已读");
        mTitles.add("全部");
        mFragments.add(new MessageUnReadFragment());
        mFragments.add(new MessageReadFragment());
        mFragments.add(new MessageAllFragment());


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