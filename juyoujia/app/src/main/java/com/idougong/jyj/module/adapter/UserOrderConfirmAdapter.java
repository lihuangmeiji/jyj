package com.idougong.jyj.module.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.UserShoppingCarBean;
import com.idougong.jyj.utils.ArithmeticUtils;
import com.idougong.jyj.utils.TextUtil;

public class UserOrderConfirmAdapter extends BaseQuickAdapter<UserShoppingCarBean, BaseViewHolder> {

    public UserOrderConfirmAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, UserShoppingCarBean productsBean) {
        if (productsBean.getProduct() != null) {
            ImageView iv = viewHolder.getView(R.id.iv_co_ico);
            Glide.with(iv.getContext())
                    .load(productsBean.getProduct().getImage())
                    .placeholder(R.mipmap.shoppingmr) // can also be a drawable
                    .error(R.mipmap.shoppingmr) // will be displayed if the image cannot be loaded
                    .centerCrop()
                    .into(iv);
            viewHolder.setText(R.id.tv_co_name, productsBean.getProduct().getName());
            viewHolder.setText(R.id.tv_co_number, "*" + productsBean.getProductNum());
            TextView tv_co_sku = viewHolder.getView(R.id.tv_co_sku);
            StringBuffer scInxfo = new StringBuffer();
            if(!EmptyUtils.isEmpty(productsBean.getSkuName())){
                scInxfo.append("规格:"+productsBean.getSkuName()+"\n");
            }
            if (!EmptyUtils.isEmpty(productsBean.getProcessingWayName())) {
                scInxfo.append("处理方式:" + productsBean.getProcessingWayName());
            }
            if(scInxfo.toString()!=null){
                tv_co_sku.setText(scInxfo.toString());
                tv_co_sku.setVisibility(View.VISIBLE);
            }else {
                tv_co_sku.setVisibility(View.INVISIBLE);
            }
            if (productsBean.getProduct().getModel() == 2) {
                TextView tv_co_price = viewHolder.getView(R.id.tv_co_price);
                if (!EmptyUtils.isEmpty(productsBean.getProduct().getCurrentPrice()) && productsBean.getProduct().getCurrentPrice() > 0) {
                    if (productsBean.getProduct().getPoint() > 0) {
                        tv_co_price.setText("¥" + String.format("%.2f", ArithmeticUtils.add(productsBean.getProduct().getCurrentPrice(), productsBean.getSkuPrice())) + "+" + productsBean.getProduct().getPoint() + "积分");
                    } else {
                        tv_co_price.setText("¥" + String.format("%.2f", ArithmeticUtils.add(productsBean.getProduct().getCurrentPrice(), productsBean.getSkuPrice())));
                    }
                } else {
                    if (productsBean.getProduct().getPoint() > 0) {
                        tv_co_price.setText(productsBean.getProduct().getPoint() + "积分");
                    }
                }
                //viewHolder.setText(R.id.tv_co_price,"积分" + productsBean.getProduct().getPoint());
            } else {
                viewHolder.setText(R.id.tv_co_price, "¥" + String.format("%.2f", productsBean.getProduct().getCurrentPrice()));
            }
        } else {
            viewHolder.getView(R.id.iv_co_ico).setVisibility(View.GONE);
            viewHolder.getView(R.id.tv_co_name).setVisibility(View.GONE);
            viewHolder.getView(R.id.tv_co_number).setVisibility(View.GONE);
            viewHolder.getView(R.id.tv_co_price).setVisibility(View.GONE);
        }
    }
}
