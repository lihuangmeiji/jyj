package com.idougong.jyj.module.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.HandlingBean;

/**
 * Created by wujiajun on 2017/10/18.
 */

public class HandlingAdapter extends BaseQuickAdapter<HandlingBean, BaseViewHolder> {

    private int selectId;

    public int getSelectId() {
        return selectId;
    }

    public void setSelectId(int selectId) {
        this.selectId = selectId;
    }

    public HandlingAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HandlingBean item) {
        TextView tv_sku_name = helper.getView(R.id.tv_sku_name);
        tv_sku_name.setText(item.getAttr());
        if (selectId == item.getId()) {
            tv_sku_name.setSelected(true);
        } else {
            tv_sku_name.setSelected(false);
        }
    }
}
