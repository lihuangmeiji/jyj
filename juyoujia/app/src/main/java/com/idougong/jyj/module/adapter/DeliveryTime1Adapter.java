package com.idougong.jyj.module.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.FeedbacksBean;
import com.idougong.jyj.model.UserDeliveryTimeBean;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by wujiajun on 2017/10/18.
 */

public class DeliveryTime1Adapter extends BaseQuickAdapter<UserDeliveryTimeBean, BaseViewHolder> {

    private int selectItemId;


    public int getSelectItemId() {
        return selectItemId;
    }

    public void setSelectItemId(int selectItemId) {
        this.selectItemId = selectItemId;
    }

    public DeliveryTime1Adapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserDeliveryTimeBean item) {
        RelativeLayout rel_delivery_time_item = helper.getView(R.id.rel_delivery_time_item);
        TextView tv_delivery_time1 = helper.getView(R.id.tv_delivery_time1);
        Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar1.setTime(TimeUtils.string2Date(item.getDate(),"yyyy-MM-dd"));
        int mMonth = calendar1.get(Calendar.MONTH) + 1;// 获取当前月份
        int day = calendar1.get(Calendar.DAY_OF_MONTH);// 获取当前天数
        Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        int mday = calendar2.get(Calendar.DAY_OF_MONTH);// 获取当前天数
        if (day - mday == 0) {
            tv_delivery_time1.setText("今天 "+mMonth + "月" + day + "日");
        } else if (day - mday == 1) {
            tv_delivery_time1.setText("明天 "+mMonth + "月" + day + "日");
        } else if (day - mday == 2) {
            tv_delivery_time1.setText("后天 "+mMonth + "月" + day + "日");
        }else{
            tv_delivery_time1.setText(item.getDate());
        }
        if (selectItemId == helper.getPosition()) {
            rel_delivery_time_item.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            tv_delivery_time1.setTextColor(mContext.getResources().getColor(R.color.color75_sc));
        } else {
            rel_delivery_time_item.setBackgroundColor(mContext.getResources().getColor(R.color.color76_sc));
            tv_delivery_time1.setTextColor(mContext.getResources().getColor(R.color.color35));
        }
    }

    @Override
    public UserDeliveryTimeBean getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
