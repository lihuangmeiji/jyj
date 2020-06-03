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
import com.idougong.jyj.utils.TextUtil;

/**
 * Created by wujiajun on 2017/10/18.
 */

public class CreditsExchangeAdapter extends BaseQuickAdapter<HomeShoppingSpreeBean, BaseViewHolder> {

    private int pbStatus;

    public int isPbStatus() {
        return pbStatus;
    }

    public void setPbStatus(int pbStatus) {
        this.pbStatus = pbStatus;
    }

    public CreditsExchangeAdapter(int layoutResId) {
        super(layoutResId);
    }

    private AddShoppingCarInterface addShoppingCarInterface;

    public AddShoppingCarInterface getAddShoppingCarInterface() {
        return addShoppingCarInterface;
    }

    public void setAddShoppingCarInterface(AddShoppingCarInterface addShoppingCarInterface) {
        this.addShoppingCarInterface = addShoppingCarInterface;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final HomeShoppingSpreeBean item) {
        final ImageView iv = helper.getView(R.id.iv_ce_ico);
        Glide.with(iv.getContext())
                .load(item.getImage())
                .placeholder(R.mipmap.shoppingmr) // can also be a drawable
                .error(R.mipmap.shoppingmr) // will be displayed if the image cannot be loaded
                .centerCrop()
                .into(iv);
        helper.setText(R.id.tv_ce_name, item.getName());
        TextView tv_inventory = helper.getView(R.id.tv_inventory);
        if (item.getInventory() > 0) {
            tv_inventory.setVisibility(View.GONE);
        } else {
            tv_inventory.setVisibility(View.VISIBLE);
        }
        TextView tv_ce_point = helper.getView(R.id.tv_ce_point);
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
        TextView tvCeOriginalPrice = helper.getView(R.id.tv_ce_original_price);
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
        TextView tv_increase_number = helper.getView(R.id.tv_increase_number);
        tv_increase_number.setVisibility(View.VISIBLE);
        if (pbStatus==1) {
            tv_increase_number.setBackground(null);
            tv_increase_number.setText("已结束");
        }else if (pbStatus==2) {
            tv_increase_number.setBackgroundResource(R.mipmap.pb_add);
            tv_increase_number.setText(null);
            tv_increase_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addShoppingCarInterface.addshoppingCar(helper.getLayoutPosition(), iv);
                }
            });
        }else  if (pbStatus==3) {
            tv_increase_number.setBackground(null);
            tv_increase_number.setText("即将开始");
        } else  if (pbStatus==4) {
            tv_increase_number.setVisibility(View.GONE);
        }  else {
            tv_increase_number.setBackgroundResource(R.mipmap.shopping_car_add);
            tv_increase_number.setText(null);
            tv_increase_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addShoppingCarInterface.addshoppingCar(helper.getLayoutPosition(), iv);
                }
            });
        }
    }

    @Override
    public HomeShoppingSpreeBean getItem(int position) {
        return super.getItem(position);
    }

    public interface AddShoppingCarInterface {
        void addshoppingCar(int position, ImageView ivProductIcon);
    }

}
