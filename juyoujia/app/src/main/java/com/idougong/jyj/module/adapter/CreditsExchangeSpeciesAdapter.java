package com.idougong.jyj.module.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.ShoppingSpeciesBean;
import com.idougong.jyj.utils.TextUtil;
import com.idougong.jyj.widget.CircleImageView;

/**
 * Created by wujiajun on 2017/10/18.
 */

public class CreditsExchangeSpeciesAdapter extends BaseQuickAdapter<ShoppingSpeciesBean, BaseViewHolder> {

    private int selIndex;

    public int getSelIndex() {
        return selIndex;
    }

    public void setSelIndex(int selIndex) {
        this.selIndex = selIndex;
    }
    public CreditsExchangeSpeciesAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShoppingSpeciesBean item) {
        CircleImageView iv = helper.getView(R.id.iv_ces_ico);
        Glide.with(iv.getContext())
                .load(item.getIcon())
                .error(R.mipmap.shoppingmr) // will be displayed if the image cannot be loaded
                .into(iv);
        helper.setText(R.id.tv_ces_name, item.getName());
        LinearLayout lin_ces_bg=helper.getView(R.id.lin_ces_bg);
        if(getSelIndex()==item.getId()){
            helper.getView(R.id.tv_ces_bs).setBackgroundResource(R.drawable.bg_blue_rectanglecircle);
            helper.getView(R.id.tv_ces_bs).getBackground().setAlpha(255);
            helper.setTextColor(R.id.tv_ces_name, Color.BLUE);
            lin_ces_bg.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else {
            helper.setTextColor(R.id.tv_ces_name,Color.BLACK);
            if (helper.getView(R.id.tv_ces_bs).getBackground()!= null) {
                helper.getView(R.id.tv_ces_bs).getBackground().setAlpha(0);
            }
            lin_ces_bg.setBackgroundColor(mContext.getResources().getColor(R.color.color69_sc));
        }
    }
}
