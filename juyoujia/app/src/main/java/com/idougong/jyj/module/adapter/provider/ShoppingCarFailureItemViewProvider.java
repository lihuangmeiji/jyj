package com.idougong.jyj.module.adapter.provider;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.idougong.jyj.R;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.UserShoppingCarBean;
import com.idougong.jyj.model.provider.HomeShoppingItemBean;
import com.idougong.jyj.model.provider.ShoppingCarFailureItemBean;
import com.idougong.jyj.model.provider.ShoppingCarItemBean;
import com.idougong.jyj.module.adapter.HomeShoppingSpreeAdapter;
import com.idougong.jyj.module.adapter.UserShoppingCarAdapter;
import com.idougong.jyj.module.adapter.UserShoppingCarLoseAdapter;
import com.idougong.jyj.module.ui.home.CreditsExchangeDetailedActivity;
import com.idougong.jyj.widget.RecycleViewDivider;

import java.util.List;

import me.drakeet.multitype.ItemViewBinder;

public class ShoppingCarFailureItemViewProvider extends ItemViewBinder<ShoppingCarFailureItemBean, ShoppingCarFailureItemViewProvider.ViewHolder> {


    private ShoppingCarFailureInterface shoppingCarFailureInterface;

    public ShoppingCarFailureInterface getShoppingCarFailureInterface() {
        return shoppingCarFailureInterface;
    }

    public void setShoppingCarFailureInterface(ShoppingCarFailureInterface shoppingCarFailureInterface) {
        this.shoppingCarFailureInterface = shoppingCarFailureInterface;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        View root = layoutInflater.inflate(R.layout.item_shopping_car_lose, viewGroup, false);
        return new ViewHolder(root,shoppingCarFailureInterface);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull ShoppingCarFailureItemBean shoppingCarItemBean) {
        viewHolder.setPosts(shoppingCarItemBean.getUserShoppingCarBeanList());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        private UserShoppingCarLoseAdapter userShoppingCarAdapter;
        private TextView tvLoseGoods;


        private ViewHolder(@NonNull final View itemView,ShoppingCarFailureInterface shoppingCarFailureInterface) {
            super(itemView);
            tvLoseGoods=itemView.findViewById(R.id.tv_lose_goods);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.rv_lose_goods);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            /* adapter 只负责灌输、适配数据，布局交给 LayoutManager，可复用 */
            userShoppingCarAdapter = new UserShoppingCarLoseAdapter(R.layout.item_shopping_car);
            userShoppingCarAdapter.bindToRecyclerView(recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            recyclerView.addItemDecoration(new RecycleViewDivider(itemView.getContext(), LinearLayoutManager.VERTICAL, 1, itemView.getContext().getResources().getColor(R.color.color40)));
            recyclerView.setAdapter(userShoppingCarAdapter);
            userShoppingCarAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    switch (view.getId()) {
                        case R.id.cb_item:
                            shoppingCarFailureInterface.checkChild(i, !userShoppingCarAdapter.getItem(i).isSelect());
                            break;
                        case R.id.delGoods:
                            shoppingCarFailureInterface.childDelete(i);
                            break;
                    }
                }
            });
        }

        private void setPosts(List<UserShoppingCarBean> posts) {
            if(posts!=null&&posts.size()>0){
                tvLoseGoods.setVisibility(View.VISIBLE);
            }else{
                tvLoseGoods.setVisibility(View.GONE);
            }
            userShoppingCarAdapter.setNewData(posts);
        }
    }


    /**
     * 点击事件
     */
    public interface ShoppingCarFailureInterface {
        void checkChild(int position,boolean isSelect);
        void childDelete(int position);
    }
}
