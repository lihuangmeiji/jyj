package com.idougong.jyj.module.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.CalendarBean;
import com.idougong.jyj.model.HomeConfigurationInformationBean;
import com.idougong.jyj.model.ProductItemInfo;
import com.idougong.jyj.widget.ImageView_126_150;

import java.util.Calendar;

/**
 * Created by wujiajun on 2017/10/18.
 */

public class CalendarAdapter extends BaseQuickAdapter<CalendarBean.CheckInListImgBean, BaseViewHolder> {


    public CalendarAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CalendarBean.CheckInListImgBean item) {
        TextView tv_calendar = helper.getView(R.id.tv_calendar);
        tv_calendar.setText("¥" + item.getAmount());
        tv_calendar.setTextColor(Color.parseColor(item.getTextColor()));
        helper.setText(R.id.tv_calendar_time,(helper.getLayoutPosition()+1)+"天");
        if(item.isState()){
            tv_calendar.setVisibility(View.VISIBLE);
        }else {
            tv_calendar.setVisibility(View.GONE);
        }
        ImageView_126_150 iv_signbg = helper.getView(R.id.iv_signbg);
        Glide.with(mContext)
                .load(item.getImg())
                .placeholder(R.mipmap.sign_on) // can also be a drawable
                .error(R.mipmap.sign_on) // will be displayed if the image cannot be loaded
                .into(iv_signbg);
    }

    @Override
    public CalendarBean.CheckInListImgBean getItem(int position) {
        return super.getItem(position);
    }
}
