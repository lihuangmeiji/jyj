package com.idougong.jyj.module.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.ShoppingSpeciesBean;

/**
 * Created by wujiajun on 2017/10/18.
 */

public class CreditsExchangeSpeciesTwoShowAdapter extends BaseQuickAdapter<ShoppingSpeciesBean, BaseViewHolder> {

    private int selIndex;

    public int getSelIndex() {
        return selIndex;
    }

    public void setSelIndex(int selIndex) {
        this.selIndex = selIndex;
    }

    public CreditsExchangeSpeciesTwoShowAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShoppingSpeciesBean item) {

        TextView tvCesName = helper.getView(R.id.tv_ces_name);
        tvCesName.setText(item.getName());
        if (getSelIndex() == helper.getLayoutPosition()) {
            tvCesName.setTextColor(mContext.getResources().getColor(R.color.color37));
            tvCesName.setSelected(true);
        } else {
            tvCesName.setTextColor(mContext.getResources().getColor(R.color.black));
            tvCesName.setSelected(false);
        }
    }
}
