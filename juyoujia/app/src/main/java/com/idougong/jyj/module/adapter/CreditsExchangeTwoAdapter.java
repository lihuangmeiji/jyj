package com.idougong.jyj.module.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.HomeShoppingSpreeTwoBean;
import com.idougong.jyj.module.ui.home.CreditsExchangeDetailedActivity;
import com.idougong.jyj.widget.GridDividerItemDecoration;
import com.idougong.jyj.widget.SpaceItemDecoration;

/**
 * Created by wujiajun on 2017/10/18.
 */

public class CreditsExchangeTwoAdapter extends BaseQuickAdapter<HomeShoppingSpreeTwoBean, BaseViewHolder> {

    private CreditsExchangeTwoInterface creditsExchangeTwoInterface;

    public CreditsExchangeTwoInterface getCreditsExchangeTwoInterface() {
        return creditsExchangeTwoInterface;
    }

    public void setCreditsExchangeTwoInterface(CreditsExchangeTwoInterface creditsExchangeTwoInterface) {
        this.creditsExchangeTwoInterface = creditsExchangeTwoInterface;
    }

    private boolean hideTitle;

    public boolean isHideTitle() {
        return hideTitle;
    }

    public void setHideTitle(boolean hideTitle) {
        this.hideTitle = hideTitle;
    }

    public CreditsExchangeTwoAdapter(int layoutResId) {
        super(layoutResId);
        this.isAddfgx=true;
    }

    boolean isAddfgx = false;

    @Override
    protected void convert(final BaseViewHolder helper, final HomeShoppingSpreeTwoBean item) {
        TextView tvSpeciesName = helper.getView(R.id.tv_species_name);
        tvSpeciesName.setText(item.getName());
        if (hideTitle) {
            tvSpeciesName.setVisibility(View.GONE);
        } else {
            tvSpeciesName.setVisibility(View.VISIBLE);
        }
        RecyclerView recyclerView = helper.getView(R.id.recycler_view);
        CreditsExchangeAdapter creditsExchangeAdapter = new CreditsExchangeAdapter(R.layout.item_credits_exchange1);
        creditsExchangeAdapter.setNewData(item.getProducts());
        creditsExchangeAdapter.setAddShoppingCarInterface(new CreditsExchangeAdapter.AddShoppingCarInterface() {
            @Override
            public void addshoppingCar(int position, ImageView ivProductIcon) {
                creditsExchangeTwoInterface.addshoppingCar(creditsExchangeAdapter.getItem(position), ivProductIcon);
            }

        });
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        recyclerView.setAdapter(creditsExchangeAdapter);
        creditsExchangeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(mContext, CreditsExchangeDetailedActivity.class);
                intent.putExtra("shoppingId", creditsExchangeAdapter.getItem(i).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public HomeShoppingSpreeTwoBean getItem(int position) {
        return super.getItem(position);
    }

    public interface CreditsExchangeTwoInterface {
        void addshoppingCar(HomeShoppingSpreeBean homeShoppingSpreeBean, ImageView ivProductIcon);

    }
}
