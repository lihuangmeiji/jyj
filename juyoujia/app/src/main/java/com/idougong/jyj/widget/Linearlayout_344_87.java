package com.idougong.jyj.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class Linearlayout_344_87 extends LinearLayout{

    public Linearlayout_344_87(Context context) {
        super(context);
    }

    public Linearlayout_344_87(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Linearlayout_344_87(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int finalWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int finalHeight = (int) (finalWidth * 87.0f / 344);
        //Log.i("widthAndHeight", "onMeasure: finalHeight:"+finalHeight+"--finalWidth:"+finalWidth);
        super.onMeasure(
                View.MeasureSpec.makeMeasureSpec(finalWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(finalHeight, View.MeasureSpec.EXACTLY));
    }
}
