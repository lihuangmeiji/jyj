package com.idougong.jyj.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class Relativelayout_1_1 extends RelativeLayout{
    public Relativelayout_1_1(Context context) {
        super(context);
    }

    public Relativelayout_1_1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Relativelayout_1_1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int finalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int finalHeight = (int) (finalWidth);
        //Log.i("widthAndHeight", "onMeasure: finalHeight:"+finalHeight+"--finalWidth:"+finalWidth);
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));
    }
}