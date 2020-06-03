package com.idougong.jyj.module.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.DailyMissionBean;


/**
 * Created by wujiajun on 2017/10/18.
 */

public class DailyMissionAdapter extends BaseQuickAdapter<DailyMissionBean, BaseViewHolder> {


    public DailyMissionAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, DailyMissionBean item) {
        ImageView iv = helper.getView(R.id.iv_dm_ico);
        Glide.with(iv.getContext())
                .load(item.getIcon())
                .placeholder(R.mipmap.shoppingmr) // can also be a drawable
                .error(R.mipmap.shoppingmr) // will be displayed if the image cannot be loaded
                .centerCrop()
                .into(iv);
        helper.setText(R.id.tv_dm_title, item.getName());
        helper.setText(R.id.tv_dm_content, item.getContent());
        helper.setText(R.id.tv_dm_operation, item.getOperation());
    }

    @Override
    public DailyMissionBean getItem(int position) {
        return super.getItem(position);
    }
}
