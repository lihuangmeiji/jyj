package com.idougong.jyj.module.adapter;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.UserWalletRecordTheDetailBean;

public class UserWalletRecordTheDetailAdapter extends BaseQuickAdapter<UserWalletRecordTheDetailBean, BaseViewHolder> {

    public UserWalletRecordTheDetailAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserWalletRecordTheDetailBean item) {
        helper.setText(R.id.tv_user_wrd_title, item.getTypeDesc());
        TextView tv_user_wrd_money = helper.getView(R.id.tv_user_wrd_money);
        if (item.getAmount() < 0) {
            tv_user_wrd_money.setText("-¥" + String.format("%.2f", (0 - item.getAmount())));
            tv_user_wrd_money.setTextColor(tv_user_wrd_money.getContext().getResources().getColor(R.color.black));
        } else {
            tv_user_wrd_money.setText("+¥" + String.format("%.2f", item.getAmount()));
            tv_user_wrd_money.setTextColor(tv_user_wrd_money.getContext().getResources().getColor(R.color.color37));
        }

        if (!EmptyUtils.isEmpty(item.getDescriptionArr())&&item.getDescriptionArr().size() > 0) {
            helper.getView(R.id.tv_user_wrd_state).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_user_wrd_state, item.getDescriptionArr().get(item.getDescriptionArr().size()-1));
        } else {
            helper.getView(R.id.tv_user_wrd_state).setVisibility(View.GONE);
        }
        if(!EmptyUtils.isEmpty(item.getReason())){
            helper.getView(R.id.tv_user_wrd_cause).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_user_wrd_cause, item.getReason());
        }else{
            helper.getView(R.id.tv_user_wrd_cause).setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_user_wrd_time, TimeUtils.date2String(TimeUtils.string2Date(item.getCreateAt()), "yyyy/MM/dd HH:mm"));
    }
}
