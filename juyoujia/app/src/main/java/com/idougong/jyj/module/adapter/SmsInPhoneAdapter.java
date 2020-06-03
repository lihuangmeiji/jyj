package com.idougong.jyj.module.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;

public class SmsInPhoneAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SmsInPhoneAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_content, item);
    }
}
