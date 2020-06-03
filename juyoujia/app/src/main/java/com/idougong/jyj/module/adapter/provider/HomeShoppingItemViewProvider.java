package com.idougong.jyj.module.adapter.provider;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.EmptyUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.idougong.jyj.R;
import com.idougong.jyj.model.ConvenientFunctionsBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.provider.HomeShoppingItemBean;
import com.idougong.jyj.module.adapter.CreditsExchangeAdapter;
import com.idougong.jyj.module.adapter.HomeShoppingSpreeAdapter;
import com.idougong.jyj.widget.GridDividerItemDecoration;


import java.util.List;

import me.drakeet.multitype.ItemViewBinder;

public class HomeShoppingItemViewProvider extends ItemViewBinder<HomeShoppingItemBean, HomeShoppingItemViewProvider.ViewHolder> {

    private HomeShoppingInterface homeShoppingInterface;

    public HomeShoppingInterface getHomeShoppingInterface() {
        return homeShoppingInterface;
    }

    public void setHomeShoppingInterface(HomeShoppingInterface homeShoppingInterface) {
        this.homeShoppingInterface = homeShoppingInterface;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        View root = layoutInflater.inflate(R.layout.item_function_shopping_tj, viewGroup, false);
        return new ViewHolder(root, homeShoppingInterface);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull HomeShoppingItemBean homeShoppingItemBean) {
        viewHolder.setPosts(homeShoppingItemBean.getHomeShoppingSpreeBeanList());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        private CreditsExchangeAdapter adapter;
        private LinearLayout linHomeGoods;

        private ViewHolder(@NonNull final View itemView, HomeShoppingInterface homeShoppingInterface) {
            super(itemView);
            linHomeGoods = itemView.findViewById(R.id.lin_home_goods);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.post_list);
            recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 2));
            recyclerView.addItemDecoration(new GridDividerItemDecoration(20, itemView.getContext().getResources().getColor(R.color.white)));
            /* adapter 只负责灌输、适配数据，布局交给 LayoutManager，可复用 */
            adapter = new CreditsExchangeAdapter(R.layout.item_credits_exchange);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    homeShoppingInterface.homeShoppingItemOnClickListener(adapter.getItem(i));
                }
            });
            adapter.setAddShoppingCarInterface(new CreditsExchangeAdapter.AddShoppingCarInterface() {
                @Override
                public void addshoppingCar(int position, ImageView ivProductIcon) {
                    homeShoppingInterface.homeShoppingAddOnClickListener(adapter.getItem(position), ivProductIcon);
                }
            });
        }

        private void setPosts(List<HomeShoppingSpreeBean> posts) {
            if (!EmptyUtils.isEmpty(posts) && posts.size() > 0) {
                linHomeGoods.setVisibility(View.VISIBLE);
            } else {
                linHomeGoods.setVisibility(View.GONE);
            }
            adapter.getData().clear();
            adapter.addData(posts);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 点击事件
     */
    public interface HomeShoppingInterface {
        void homeShoppingItemOnClickListener(HomeShoppingSpreeBean homeShoppingSpreeBean);

        void homeShoppingAddOnClickListener(HomeShoppingSpreeBean homeShoppingSpreeBean, ImageView ivProductIcon);
    }
}
