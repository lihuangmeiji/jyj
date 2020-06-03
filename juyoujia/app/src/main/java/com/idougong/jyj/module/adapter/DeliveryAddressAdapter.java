package com.idougong.jyj.module.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.DeliveryAddressBean;
import com.idougong.jyj.model.ProductItemInfo;

/**
 * Created by wujiajun on 2017/10/18.
 */

public class DeliveryAddressAdapter extends BaseQuickAdapter<DeliveryAddressBean, BaseViewHolder> {

    private int openType;

    public int getIsselect() {
        return isselect;
    }

    public void setIsselect(int isselect) {
        this.isselect = isselect;
    }

    private int isselect;

    public DeliveryAddressAdapter(int layoutResId,int openType) {
        super(layoutResId);
        this.openType=openType;
    }

    @Override
    protected void convert(BaseViewHolder helper, DeliveryAddressBean item) {

        helper.setText(R.id.tv_sh_name, item.getName());
        helper.setText(R.id.tv_sh_phone, item.getPhone());
        helper.setText(R.id.tv_sh_address, item.getDetailsAddress());
        if (item.isIsDefault()) {
            helper.getView(R.id.tv_mr).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.tv_mr).setVisibility(View.GONE);
        }
        helper.addOnClickListener(R.id.iv_delivery_del).addOnClickListener(R.id.cb_item);
        ImageView iv_delivery_del=helper.getView(R.id.iv_delivery_del);
        CheckBox cb_item=helper.getView(R.id.cb_item);
        if(openType==1){
            iv_delivery_del.setVisibility(View.GONE);
            cb_item.setVisibility(View.VISIBLE);
        }else{
            iv_delivery_del.setVisibility(View.VISIBLE);
            cb_item.setVisibility(View.GONE);
        }
        if(getIsselect()==item.getId()){
            cb_item.setChecked(true);
        }else{
            cb_item.setChecked(false);
        }
    }
}
