package com.idougong.jyj.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ParentRecyclerView extends RecyclerView {

    public ParentRecyclerView(Context context) {
        super(context, null);
    }

    public ParentRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs, -1);
    }

    public ParentRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return true;
    }
}
