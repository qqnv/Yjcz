package com.zzlecheng.yjcz.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private final int normal;
    private final int margin;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.top = normal;
        outRect.bottom = normal;
        if (parent.getChildAdapterPosition(view) % 3 == 0) {
            outRect.right = normal;
            outRect.left = margin;
        } else if (parent.getChildAdapterPosition(view) % 3 == 1) {
            outRect.right = margin;
            outRect.left = margin;
        } else if (parent.getChildAdapterPosition(view) % 3 == 2) {
            outRect.right = normal;
            outRect.left = margin;
        }
    }

    public SpacesItemDecoration(int normal, int margin) {
        this.normal = normal;
        this.margin = margin;
    }

}
