package com.idougong.jyj.module.adapter;

import android.widget.ImageView;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.UserShoppingCarBean;

/**
 * Created by wujiajun on 2017/10/18.
 */

public class UserShoppingIcoShowAdapter extends BaseQuickAdapter<HomeShoppingSpreeBean, BaseViewHolder> {


    public UserShoppingIcoShowAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, HomeShoppingSpreeBean item) {
        ImageView img = helper.getView(R.id.iv_shopping_ico);
        Glide.with(mContext).load(item.getImage()).into(img);
    }
}
