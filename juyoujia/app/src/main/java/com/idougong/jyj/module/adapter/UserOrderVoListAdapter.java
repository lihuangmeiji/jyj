package com.idougong.jyj.module.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.OnlineSupermarketBean;
import com.idougong.jyj.model.UserOrderBean;
import com.idougong.jyj.model.UserOrderVoListBean;
import com.idougong.jyj.model.UserShoppingCarBean;
import com.idougong.jyj.utils.TextUtil;

public class UserOrderVoListAdapter extends BaseQuickAdapter<UserOrderBean.ProductsBean, BaseViewHolder> {

    public UserOrderVoListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, UserOrderBean.ProductsBean productsBean) {
        if (productsBean.getProduct() != null) {
            ImageView iv = viewHolder.getView(R.id.iv_co_ico);
            Glide.with(iv.getContext())
                    .load(productsBean.getProduct().getImage())
                    .placeholder(R.mipmap.shoppingmr) // can also be a drawable
                    .error(R.mipmap.shoppingmr) // will be displayed if the image cannot be loaded
                    .centerCrop()
                    .into(iv);
            viewHolder.setText(R.id.tv_co_name, productsBean.getProduct().getName());
            viewHolder.setText(R.id.tv_co_number, "*" + productsBean.getNum());
            if (productsBean.getProduct().getModel() == 2) {
                TextView tv_co_price = viewHolder.getView(R.id.tv_co_price);
                if (!EmptyUtils.isEmpty(productsBean.getProduct().getCurrentPrice()) && productsBean.getProduct().getCurrentPrice() > 0) {
                    if (productsBean.getProduct().getPoint() > 0) {
                        tv_co_price.setText(TextUtil.FontHighlighting(tv_co_price.getContext(), "¥", "" +String.format("%.2f",  productsBean.getProduct().getCurrentPrice()), "+" + productsBean.getProduct().getPoint() + "积分", R.style.tv_ce_point1, R.style.tv_ce_point1, R.style.tv_ce_point1));
                    } else {
                        tv_co_price.setText(TextUtil.FontHighlighting(tv_co_price.getContext(), "¥", "" + String.format("%.2f",  productsBean.getProduct().getCurrentPrice()), R.style.tv_ce_point1, R.style.tv_ce_point1));
                    }
                } else {
                    if (productsBean.getProduct().getPoint() > 0) {
                        tv_co_price.setText(TextUtil.FontHighlighting(tv_co_price.getContext(), productsBean.getProduct().getPoint() + "", "积分", R.style.tv_ce_point1, R.style.tv_ce_point1));
                    }
                }
                //viewHolder.setText(R.id.tv_co_price,"积分" + productsBean.getProduct().getPoint());
            } else {
                viewHolder.setText(R.id.tv_co_price, "¥" + String.format("%.2f",  productsBean.getProduct().getCurrentPrice()));
            }
        }
        TextView tv_co_sku=viewHolder.getView(R.id.tv_co_sku);

        StringBuffer scInxfo = new StringBuffer();
        if(!EmptyUtils.isEmpty(productsBean.getSku())){
            scInxfo.append("规格:"+productsBean.getSku().getName()+"\n");
        }
        if (!EmptyUtils.isEmpty(productsBean.getProcessingWay())) {
            scInxfo.append("处理方式:" + productsBean.getProcessingWay().getAttr());
        }
        if(scInxfo.toString()!=null){
            tv_co_sku.setText(scInxfo.toString());
            tv_co_sku.setVisibility(View.VISIBLE);
        }else {
            tv_co_sku.setVisibility(View.INVISIBLE);
        }
    }
}
