package com.idougong.jyj.module.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.ConvenientFunctionsBean;


public class UserServiceFunctionAdapter extends BaseQuickAdapter<ConvenientFunctionsBean, BaseViewHolder> {

    public UserServiceFunctionAdapter(int layoutResId) {
        super(layoutResId);
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, ConvenientFunctionsBean convenientFunctionsBean) {
        baseViewHolder.setText(R.id.tv_function_title,convenientFunctionsBean.getName());
        ImageView iv_function_ico=baseViewHolder.getView(R.id.iv_function_ico);
        Glide.with(iv_function_ico.getContext())
                .load(convenientFunctionsBean.getIcon())
                .placeholder(R.mipmap.shoppingmr) // can also be a drawable
                .error(R.mipmap.shoppingmr) // will be displayed if the image cannot be loaded
                .into(iv_function_ico);
    }
}
