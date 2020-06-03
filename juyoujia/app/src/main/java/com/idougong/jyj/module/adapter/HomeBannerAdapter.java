package com.idougong.jyj.module.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.ProductItemInfo;

/**
 * Created by wujiajun on 2017/10/18.
 */

public class HomeBannerAdapter extends BaseQuickAdapter<HomeBannerBean, BaseViewHolder> {

    public HomeBannerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeBannerBean item) {
        ImageView img = helper.getView(R.id.iv_home_banner);
        Glide.with(mContext).load(item.getImg()).into(img);
    }
}
