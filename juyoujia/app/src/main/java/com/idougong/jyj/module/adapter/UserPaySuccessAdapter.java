package com.idougong.jyj.module.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.AfterpayAdvertiseBean;
import com.idougong.jyj.widget.ImageView_69_16;

public class UserPaySuccessAdapter extends BaseQuickAdapter<AfterpayAdvertiseBean,BaseViewHolder>{

    public UserPaySuccessAdapter(int layoutResId){super(layoutResId);}

    @Override
    protected void convert(BaseViewHolder baseViewHolder, AfterpayAdvertiseBean afterpayAdvertiseBean) {

        ImageView_69_16 advertise = baseViewHolder.getView(R.id.iv_afterpayadvertise);
        Glide.with(advertise.getContext())
                .load(afterpayAdvertiseBean.getPicture())
                .into(advertise);

    }


}
