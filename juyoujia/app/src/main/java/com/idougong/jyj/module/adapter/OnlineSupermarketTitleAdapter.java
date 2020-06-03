package com.idougong.jyj.module.adapter;

import android.support.annotation.NonNull;

import com.idougong.jyj.R;
import com.idougong.jyj.model.OnlineSupermarketBean;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

public class OnlineSupermarketTitleAdapter extends MultiItemView<String> {

    @NonNull
    @Override
    public int getLayoutId() {
        return R.layout.item_online_supermarket_title;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull String str, int i) {
        viewHolder.setText(R.id.tv_os_title,str);
    }
}
