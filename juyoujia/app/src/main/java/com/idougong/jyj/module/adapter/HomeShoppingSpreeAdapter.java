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

public class HomeShoppingSpreeAdapter extends BaseQuickAdapter<HomeShoppingSpreeBean, BaseViewHolder> {

    private AddShoppingCarInterface addShoppingCarInterface;

    public AddShoppingCarInterface getAddShoppingCarInterface() {
        return addShoppingCarInterface;
    }

    public void setAddShoppingCarInterface(AddShoppingCarInterface addShoppingCarInterface) {
        this.addShoppingCarInterface = addShoppingCarInterface;
    }

    public HomeShoppingSpreeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeShoppingSpreeBean item) {
        ImageView iv = helper.getView(R.id.iv_ss_ico);
        Glide.with(iv.getContext())
                .load(item.getImage())
                .placeholder(R.mipmap.shoppingmr) // can also be a drawable
                .error(R.mipmap.shoppingmr) // will be displayed if the image cannot be loaded
                .into(iv);
        helper.setText(R.id.tv_ss_name, item.getName());
        TextView tv_inventory = helper.getView(R.id.tv_inventory);
        if (item.getInventory() > 0) {
            tv_inventory.setVisibility(View.GONE);
        } else {
            tv_inventory.setVisibility(View.VISIBLE);
        }
        TextView tv_ss_discounted_prices = helper.getView(R.id.tv_ss_discounted_prices);
        if (item.getModel() == 1) {
            helper.setText(R.id.tv_ss_discounted_prices, "¥" + item.getCurrentPrice());
        } else {
            if (!EmptyUtils.isEmpty(item.getCurrentPrice()) && item.getCurrentPrice() > 0) {
                if (item.getPoint() > 0) {
                    tv_ss_discounted_prices.setText(TextUtil.FontHighlighting(tv_ss_discounted_prices.getContext(), "¥", String.format("%.2f", item.getCurrentPrice()), "+" + item.getPoint() + "积分", R.style.tv_ce_point1, R.style.tv_ce_point1, R.style.tv_ce_point1));
                } else {
                    tv_ss_discounted_prices.setText(TextUtil.FontHighlighting(tv_ss_discounted_prices.getContext(), "¥", String.format("%.2f", item.getCurrentPrice()), R.style.tv_ce_point1, R.style.tv_ce_point1));
                }
            } else {
                if (item.getPoint() > 0) {
                    tv_ss_discounted_prices.setText(TextUtil.FontHighlighting(tv_ss_discounted_prices.getContext(), item.getPoint() + "", "积分", R.style.tv_ce_point1, R.style.tv_ce_point1));
                }
            }
        }

        TextView tvSsOriginalPrice = helper.getView(R.id.tv_ss_original_price);
        tvSsOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if (!EmptyUtils.isEmpty(item.getSourcePrice()) && item.getSourcePrice() > 0) {
            tvSsOriginalPrice.setVisibility(View.VISIBLE);
            if (item.getSourcePoint() > 0) {
                tvSsOriginalPrice.setText("¥" + String.format("%.2f", item.getSourcePrice()) + "+" + item.getSourcePoint() + "积分");
            } else {
                tvSsOriginalPrice.setText("¥" + String.format("%.2f", item.getSourcePrice()));
            }
        } else {
            if (item.getSourcePoint() > 0) {
                tvSsOriginalPrice.setVisibility(View.VISIBLE);
                tvSsOriginalPrice.setText(item.getSourcePoint() + "积分");
            } else {
                tvSsOriginalPrice.setVisibility(View.GONE);
            }
        }

        helper.getView(R.id.tv_increase_number).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addShoppingCarInterface.addshoppingCar(helper.getLayoutPosition(), iv);
            }
        });
    }


    @Override
    public HomeShoppingSpreeBean getItem(int position) {
        return super.getItem(position);
    }

    public interface AddShoppingCarInterface {
        void addshoppingCar(int position, ImageView ivProductIcon);
    }


}
