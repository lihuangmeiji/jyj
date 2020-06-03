package com.idougong.jyj.widget;

import android.content.Context;
import android.util.AttributeSet;

public class ImageView_75_59 extends android.support.v7.widget.AppCompatImageView {

    public ImageView_75_59(Context context) {
        super(context);
    }

    public ImageView_75_59(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageView_75_59(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int finalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int finalHeight = (int) (finalWidth * 59f / 75);
        //Log.i("widthAndHeight", "onMeasure: finalHeight:"+finalHeight+"--finalWidth:"+finalWidth);
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));
    }
}
