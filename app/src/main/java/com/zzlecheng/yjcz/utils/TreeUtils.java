package com.zzlecheng.yjcz.utils;

import android.util.Log;

import com.zzlecheng.yjcz.bean.TreePoint;

import java.util.HashMap;

/**
 * Created by xulc on 2018/7/27.
 */

public class TreeUtils {
    //第一级别为0
    public static int getLevel(TreePoint treePoint, HashMap<String, TreePoint> map) {
        if ("0".equals(treePoint.getPARENTID())) {
            return 0;
        } else {
            if (null != treePoint.getPARENTID()){
                return 1 + getLevel(getTreePoint(treePoint.getPARENTID(), map), map);
            }else {
                return 0;
            }
        }
    }


    public static TreePoint getTreePoint(String ID, HashMap<String, TreePoint> map) {
        if (map.containsKey(ID)) {
            return map.get(ID);
        }
        Log.e("xlc", "ID:" + ID);
        return null;
    }
}
