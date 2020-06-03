package com.idougong.jyj.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class View_3_1 extends View {


    public View_3_1(Context context) {
        super(context);
    }

    public View_3_1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public View_3_1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int finalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int finalHeight = (int) (finalWidth * 1.0f / 3);
        //Log.i("widthAndHeight", "onMeasure: finalHeight:"+finalHeight+"--finalWidth:"+finalWidth);
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));
    }
}
