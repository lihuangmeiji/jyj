package com.idougong.jyj.module.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.FeedbacksBean;
import com.idougong.jyj.model.UserDeliveryTimeBean;

/**
 * Created by wujiajun on 2017/10/18.
 */

public class DeliveryTime2Adapter extends BaseQuickAdapter<UserDeliveryTimeBean.TimeBean, BaseViewHolder> {

    private int selectItemId;


    public int getSelectItemId() {
        return selectItemId;
    }

    public void setSelectItemId(int selectItemId) {
        this.selectItemId = selectItemId;
    }


    public DeliveryTime2Adapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserDeliveryTimeBean.TimeBean item) {
        String timeName = item.getBeginTime()
                + "-" + item.getEndTime();
        TextView tv_delivery_time2=helper.getView(R.id.tv_delivery_time2);
        tv_delivery_time2.setText(timeName);
        if(selectItemId==helper.getPosition()){
            tv_delivery_time2.setTextColor(mContext.getResources().getColor(R.color.color75_sc));
        }else{
            tv_delivery_time2.setTextColor(mContext.getResources().getColor(R.color.color35));
        }
    }

    @Override
    public UserDeliveryTimeBean.TimeBean getItem(int position) {
        return super.getItem(position);
    }
}
