package com.idougong.jyj.module.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.ProductItemInfo;
import com.idougong.jyj.model.SpecificationsBean;

/**
 * Created by wujiajun on 2017/10/18.
 */

public class SpecificationsBeanAdapter extends BaseQuickAdapter<SpecificationsBean, BaseViewHolder> {

    public SpecificationsBeanAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, SpecificationsBean item) {
        helper.setText(R.id.tv_specs_key,item.getKey());
        helper.setText(R.id.tv_specs_values,item.getVal());
    }
}
