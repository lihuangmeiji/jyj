package com.idougong.jyj.module.adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.ProductItemInfo;
import com.idougong.jyj.utils.TextUtil;

/**
 * Created by wujiajun on 2017/10/18.
 */

public class PanicBuyingAdapter extends BaseQuickAdapter<HomeShoppingSpreeBean, BaseViewHolder> {

    public PanicBuyingAdapter(int layoutResId) {
        super(layoutResId);
    }

    private boolean pbStatus;

    public boolean isPbStatus() {
        return pbStatus;
    }

    public void setPbStatus(boolean pbStatus) {
        this.pbStatus = pbStatus;
    }

    private AddShoppingCarInterface addShoppingCarInterface;

    public AddShoppingCarInterface getAddShoppingCarInterface() {
        return addShoppingCarInterface;
    }

    public void setAddShoppingCarInterface(AddShoppingCarInterface addShoppingCarInterface) {
        this.addShoppingCarInterface = addShoppingCarInterface;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeShoppingSpreeBean item) {
        ImageView iv = helper.getView(R.id.iv_pb_ico);
        Glide.with(iv.getContext())
                .load(item.getImage())
                .placeholder(R.mipmap.shoppingmr) // can also be a drawable
                .error(R.mipmap.shoppingmr) // will be displayed if the image cannot be loaded
                .centerCrop()
                .into(iv);
        TextView tv_ce_point = helper.getView(R.id.tv_pb_discounted_prices);
        if (!EmptyUtils.isEmpty(item.getCurrentPrice()) && item.getCurrentPrice() > 0) {
            tv_ce_point.setVisibility(View.VISIBLE);
            if (item.getPoint() > 0) {
                tv_ce_point.setText("¥" + String.format("%.2f", item.getCurrentPrice()) + "+" + item.getPoint() + "积分");
            } else {
                tv_ce_point.setText("¥" + String.format("%.2f", item.getCurrentPrice()));
            }
        } else {
            if (item.getPoint() > 0) {
                tv_ce_point.setVisibility(View.VISIBLE);
                tv_ce_point.setText(item.getPoint() + "积分");
            } else {
                tv_ce_point.setVisibility(View.GONE);
            }
        }
        helper.setText(R.id.tv_pb_name, item.getName());
        TextView tvCeOriginalPrice = helper.getView(R.id.tv_pb_original_integral);
        tvCeOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if (!EmptyUtils.isEmpty(item.getSourcePrice()) && item.getSourcePrice() > 0) {
            tvCeOriginalPrice.setVisibility(View.VISIBLE);
            if (item.getSourcePoint() > 0) {
                tvCeOriginalPrice.setText("¥" + String.format("%.2f", item.getSourcePrice()) + "+" + item.getSourcePoint() + "积分");
            } else {
                tvCeOriginalPrice.setText("¥" + String.format("%.2f", item.getSourcePrice()));
            }
        } else {
            if (item.getSourcePoint() > 0) {
                tvCeOriginalPrice.setVisibility(View.VISIBLE);
                tvCeOriginalPrice.setText(item.getSourcePoint() + "积分");
            } else {
                tvCeOriginalPrice.setVisibility(View.GONE);
            }
        }
        TextView tvSoldTotal = helper.getView(R.id.tv_sold_total);
        tvSoldTotal.setText("已抢" + item.getSoldTotal() + "件");
        ImageView iv_add_goods = helper.getView(R.id.iv_add_goods);
        if(pbStatus){
            iv_add_goods.setVisibility(View.VISIBLE);
        }else{
            iv_add_goods.setVisibility(View.GONE);
        }
        iv_add_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addShoppingCarInterface.addshoppingCar(helper.getLayoutPosition(), iv);
            }
        });
    }

    public interface AddShoppingCarInterface {
        void addshoppingCar(int position, ImageView ivProductIcon);
    }

}
