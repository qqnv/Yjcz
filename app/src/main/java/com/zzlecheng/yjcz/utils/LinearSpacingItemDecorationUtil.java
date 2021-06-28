package com.zzlecheng.yjcz.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 类描述：RecyclerView的item的间隔
 */
public class LinearSpacingItemDecorationUtil extends RecyclerView.ItemDecoration {

    private int spacing;

    public LinearSpacingItemDecorationUtil(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.left = spacing / 2;
        outRect.right = spacing / 2;
        outRect.bottom = spacing;
//        if (parent.getChildAdapterPosition(view) == 0) {
        outRect.top = spacing;
//        }
    }
}
