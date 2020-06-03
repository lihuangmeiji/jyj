package com.idougong.jyj.module.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.UserShoppingCarBean;

/**
 * Created by wujiajun on 2017/10/18.
 */

public class UserShoppingCarConfirmAdapter extends BaseQuickAdapter<UserShoppingCarBean, BaseViewHolder> {


    public UserShoppingCarConfirmAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, UserShoppingCarBean item) {
        ImageView img = helper.getView(R.id.iv_sc_ico);
        if (!EmptyUtils.isEmpty(item.getProduct())) {
            Glide.with(mContext).load(item.getProduct().getImage()).into(img);
            helper.setText(R.id.tv_sc_name, item.getProduct().getName());
            helper.setText(R.id.tv_so_price, "¥" + item.getProduct().getCurrentPrice());
        }
        helper.setText(R.id.tv_so_number, "数量:" + item.getProductNum()+"份");
    }
}
