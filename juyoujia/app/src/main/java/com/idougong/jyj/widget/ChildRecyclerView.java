package com.idougong.jyj.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.contrarywind.view.WheelView;

public class ChildRecyclerView extends RecyclerView {

    public ChildRecyclerView(Context context) {
        super(context, null);
    }

    public ChildRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs, -1);
    }

    public ChildRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        return true;
    }


}
