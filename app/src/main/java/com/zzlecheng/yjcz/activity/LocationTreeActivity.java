package com.zzlecheng.yjcz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.adapter.TreeAdapter;
import com.zzlecheng.yjcz.base.BaseActivity;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.bean.LocationTreeBean;
import com.zzlecheng.yjcz.bean.TreePoint;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.TreeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * @类名: LocationTreeActivity
 * @描述: 事件上报的上报位置
 * @作者: huangchao
 * @时间: 2019/1/15 11:35 AM
 * @版本: 1.0.0
 */
public class LocationTreeActivity extends BaseActivity {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
    @BindView(R.id.et_filter)
    EditText etFilter;
    @BindView(R.id.listView)
    ListView listView;

    private TreeAdapter adapter;
    private List<TreePoint> pointList = new ArrayList<>();
    private HashMap<String, TreePoint> pointMap = new HashMap<>();

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_location_tree;
    }

    @Override
    protected void initView() {
        ibBack.setOnClickListener(v -> finish());
        tvTitle.setText("上报位置");
        adapter = new TreeAdapter(this, pointList, pointMap);
        listView.setAdapter(adapter);
        adapter.buttonSetOnclick((view, id,name) -> {
            Intent intent = new Intent();
            intent.putExtra("id",id);
            intent.putExtra("name",name);
            this.setResult(RESULT_OK,intent);
            finish();
        });
//        initData();
        initMainData();
        addListener();
    }

    private void initMainData() {
        HttpMethods.getHttpMethods().locationTree(this, "lc_yaqy", "0",
                new BaseObserver<List<LocationTreeBean>>(this, false) {
                    @Override
                    protected void onHandleSuccess(List<LocationTreeBean> locationTreeBeans) {
                        pointList.clear();
                        for (int i = 0;i < locationTreeBeans.size();i++){
                            String parentId;
                            if ("".equals(locationTreeBeans.get(i).getPid())){
                                parentId = "0";
                            }else {
                                parentId = locationTreeBeans.get(i).getPid();
                            }
                            String id = locationTreeBeans.get(i).getId();
                            String name = locationTreeBeans.get(i).getLabel();
                            pointList.add(new TreePoint(id,name,parentId,"0",i+1,""));
                        }
                        //打乱集合中的数据
                        Collections.shuffle(pointList);
                        //对集合中的数据重新排序
                        updateData();
                    }
                });
    }

//    private void initData() {
//        pointList.clear();
//        int id = 1000;
//        int parentId = 0;
//        int parentId2 = 0;
//        int parentId3 = 0;
//        for (int i = 1; i < 5; i++) {
//            id++;
//            pointList.add(new TreePoint("" + id, "分类" + i, "" + parentId, "0", i));
//            for (int j = 1; j < 5; j++) {
//                if (j == 1) {
//                    parentId2 = id;
//                }
//                id++;
//                pointList.add(new TreePoint("" + id, "分类" + i + "_" + j, "" + parentId2, "0", j));
//                for (int k = 1; k < 5; k++) {
//                    if (k == 1) {
//                        parentId3 = id;
//                    }
//                    id++;
//                    pointList.add(new TreePoint("" + id, "分类" + i + "_" + j + "_" + k, "" + parentId3, "1", k));
//                }
//            }
//        }
//        //打乱集合中的数据
//        Collections.shuffle(pointList);
//        //对集合中的数据重新排序
//        updateData();
//    }

    public void addListener() {

        listView.setOnItemClickListener((parent, view, position, id) -> adapter.onItemClick(position));

        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchAdapter(s);
            }
        });
    }

    private void searchAdapter(Editable s) {
        adapter.setKeyword(s.toString());
    }

    //对数据排序 深度优先
    private void updateData() {
        for (TreePoint treePoint : pointList) {
            pointMap.put(treePoint.getID(), treePoint);
        }
        Collections.sort(pointList, new Comparator<TreePoint>() {
            @Override
            public int compare(TreePoint lhs, TreePoint rhs) {
                int llevel = TreeUtils.getLevel(lhs, pointMap);
                int rlevel = TreeUtils.getLevel(rhs, pointMap);
                if (llevel == rlevel) {
                    if (lhs.getPARENTID().equals(rhs.getPARENTID())) {  //左边小
                        return lhs.getDISPLAY_ORDER() > rhs.getDISPLAY_ORDER() ? 1 : -1;
                    } else {  //如果父辈id不相等
                        //同一级别，不同父辈
                        TreePoint ltreePoint = TreeUtils.getTreePoint(lhs.getPARENTID(), pointMap);
                        TreePoint rtreePoint = TreeUtils.getTreePoint(rhs.getPARENTID(), pointMap);
                        return compare(ltreePoint, rtreePoint);  //父辈
                    }
                } else {  //不同级别
                    if (llevel > rlevel) {   //左边级别大       左边小
                        if (lhs.getPARENTID().equals(rhs.getID())) {
                            return 1;
                        } else {
                            TreePoint lreasonTreePoint = TreeUtils.getTreePoint(lhs.getPARENTID(), pointMap);
                            return compare(lreasonTreePoint, rhs);
                        }
                    } else {   //右边级别大   右边小
                        if (rhs.getPARENTID().equals(lhs.getID())) {
                            return -1;
                        }
                        TreePoint rreasonTreePoint = TreeUtils.getTreePoint(rhs.getPARENTID(), pointMap);
                        return compare(lhs, rreasonTreePoint);
                    }
                }
            }
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void loadData() {

    }

}
