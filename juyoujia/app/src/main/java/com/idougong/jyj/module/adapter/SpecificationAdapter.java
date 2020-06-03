package com.idougong.jyj.module.adapter;

import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.HandlingBean;
import com.idougong.jyj.model.RefundsBean;
import com.idougong.jyj.model.SkuInfoBean;

/**
 * Created by wujiajun on 2017/10/18.
 */

public class SpecificationAdapter extends BaseQuickAdapter<SkuInfoBean, BaseViewHolder> {

    private int selectId;

    public int getSelectId() {
        return selectId;
    }

    public void setSelectId(int selectId) {
        this.selectId = selectId;
    }

    public SpecificationAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, SkuInfoBean item) {
        TextView tv_sku_name = helper.getView(R.id.tv_sku_name);
        tv_sku_name.setText(item.getName());
        if (selectId == item.getId()) {
            tv_sku_name.setSelected(true);
        } else {
            tv_sku_name.setSelected(false);
        }
    }
}
