package com.idougong.jyj.module.adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.UserShoppingCarBean;

/**
 * Created by wujiajun on 2017/10/18.
 */

public class UserShoppingCarLoseAdapter extends BaseQuickAdapter<UserShoppingCarBean, BaseViewHolder> {

    public UserShoppingCarLoseAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, UserShoppingCarBean item) {
        ImageView img = helper.getView(R.id.iv_sc_ico);
        if (!EmptyUtils.isEmpty(item.getProduct())) {
            Glide.with(mContext).load(item.getProduct().getImage()).into(img);
            helper.setText(R.id.tv_sc_name, item.getProduct().getName());
            TextView tv_so_point = helper.getView(R.id.tv_so_price);
            if (!EmptyUtils.isEmpty(item.getProduct().getCurrentPrice()) && item.getProduct().getCurrentPrice() > 0) {
                tv_so_point.setVisibility(View.VISIBLE);
                if (item.getProduct().getPoint() > 0) {
                    tv_so_point.setText("¥" + String.format("%.2f", item.getProduct().getCurrentPrice()) + "+" + item.getProduct().getPoint() + "积分");
                } else {
                    tv_so_point.setText("¥" + String.format("%.2f", item.getProduct().getCurrentPrice()));
                }
            } else {
                if (item.getProduct().getPoint() > 0) {
                    tv_so_point.setVisibility(View.VISIBLE);
                    tv_so_point.setText(item.getProduct().getPoint() + "积分");
                } else {
                    tv_so_point.setVisibility(View.GONE);
                }
            }
            TextView tvSoOriginalPrice = helper.getView(R.id.tv_so_original_price);
            tvSoOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            if (!EmptyUtils.isEmpty(item.getProduct().getSourcePrice()) && item.getProduct().getSourcePrice() > 0) {
                tvSoOriginalPrice.setVisibility(View.VISIBLE);
                if (item.getProduct().getSourcePoint() > 0) {
                    tvSoOriginalPrice.setText("¥" + String.format("%.2f", item.getProduct().getSourcePrice()) + "+" + item.getProduct().getSourcePoint() + "积分");
                } else {
                    tvSoOriginalPrice.setText("¥" + String.format("%.2f", item.getProduct().getSourcePrice()));
                }
            } else {
                if (item.getProduct().getSourcePoint() > 0) {
                    tvSoOriginalPrice.setVisibility(View.VISIBLE);
                    tvSoOriginalPrice.setText(item.getProduct().getSourcePoint() + "积分");
                } else {
                    tvSoOriginalPrice.setVisibility(View.GONE);
                }
            }
        }
        helper.setText(R.id.tv_so_number, "" + item.getProductNum());

        helper.getView(R.id.tv_so_number).setVisibility(View.GONE);
        helper.getView(R.id.tv_decrease_number).setVisibility(View.GONE);
        helper.getView(R.id.tv_increase_number).setVisibility(View.GONE);
        if (item.isFailure()) {
            helper.getView(R.id.tv_inventory).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.tv_inventory).setVisibility(View.GONE);
        }
        TextView tv_sc_handling = helper.getView(R.id.tv_sc_handling);
        if (!EmptyUtils.isEmpty(item.getProcessingWayName())) {
            tv_sc_handling.setText("处理方式:" + item.getProcessingWayName());
            tv_sc_handling.setVisibility(View.VISIBLE);
        } else {
            tv_sc_handling.setVisibility(View.INVISIBLE);
        }
        final CheckBox checkBox = helper.getView(R.id.cb_item);
        if (item.isSelect()) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        helper.addOnClickListener(R.id.cb_item)
                .addOnClickListener(R.id.delGoods);
    }


}
