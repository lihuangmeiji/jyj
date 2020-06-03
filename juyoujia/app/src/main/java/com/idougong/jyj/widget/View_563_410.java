package com.idougong.jyj.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class View_563_410 extends View{
    public View_563_410(Context context) {
        super(context);
    }

    public View_563_410(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public View_563_410(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int finalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int finalHeight = (int) (finalWidth * 410.0f / 563);
        //Log.i("widthAndHeight", "onMeasure: finalHeight:"+finalHeight+"--finalWidth:"+finalWidth);
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));
    }
}
