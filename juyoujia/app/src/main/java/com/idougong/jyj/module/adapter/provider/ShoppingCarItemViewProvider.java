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

import com.blankj.utilcode.util.EmptyUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.idougong.jyj.R;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.UserShoppingCarBean;
import com.idougong.jyj.model.provider.HomeShoppingItemBean;
import com.idougong.jyj.model.provider.ShoppingCarItemBean;
import com.idougong.jyj.module.adapter.HomeShoppingSpreeAdapter;
import com.idougong.jyj.module.adapter.UserShoppingCarAdapter;
import com.idougong.jyj.module.ui.home.CreditsExchangeDetailedActivity;
import com.idougong.jyj.widget.RecycleViewDivider;

import java.util.List;

import me.drakeet.multitype.ItemViewBinder;

public class ShoppingCarItemViewProvider extends ItemViewBinder<ShoppingCarItemBean, ShoppingCarItemViewProvider.ViewHolder> {

    private ShoppingCarInterface shoppingCarInterface;

    public ShoppingCarInterface getShoppingCarInterface() {
        return shoppingCarInterface;
    }

    public void setShoppingCarInterface(ShoppingCarInterface shoppingCarInterface) {
        this.shoppingCarInterface = shoppingCarInterface;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        View root = layoutInflater.inflate(R.layout.item_home_services, viewGroup, false);
        return new ViewHolder(root, shoppingCarInterface);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull ShoppingCarItemBean homeShoppingItemBean) {
        viewHolder.setPosts(homeShoppingItemBean.getUserShoppingCarBeanList());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        private UserShoppingCarAdapter userShoppingCarAdapter;


        private ViewHolder(@NonNull final View itemView,ShoppingCarInterface shoppingCarInterface) {
            super(itemView);

            recyclerView = (RecyclerView) itemView.findViewById(R.id.rv_jyj_service);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            /* adapter 只负责灌输、适配数据，布局交给 LayoutManager，可复用 */
            userShoppingCarAdapter = new UserShoppingCarAdapter(R.layout.item_shopping_car);
            userShoppingCarAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
            userShoppingCarAdapter.bindToRecyclerView(recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            recyclerView.addItemDecoration(new RecycleViewDivider(itemView.getContext(), LinearLayoutManager.VERTICAL, 1, itemView.getContext().getResources().getColor(R.color.color40)));
            recyclerView.setAdapter(userShoppingCarAdapter);
            userShoppingCarAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    Intent intent = new Intent(itemView.getContext(), CreditsExchangeDetailedActivity.class);
                    intent.putExtra("shoppingId", userShoppingCarAdapter.getItem(i).getProductId());
                    itemView.getContext().startActivity(intent);
                }
            });
            userShoppingCarAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    switch (view.getId()) {
                        case R.id.cb_item:
                            shoppingCarInterface.checkChild(i, !userShoppingCarAdapter.getItem(i).isSelect());
                            break;
                        case R.id.tv_decrease_number:
                            shoppingCarInterface.doDecrease(i);
                            break;
                        case R.id.tv_increase_number:
                            shoppingCarInterface.doIncrease(i);
                            break;
                        case R.id.delGoods:
                            shoppingCarInterface.childDelete(i);
                            break;
                        case R.id.lin_item_info:
                            Intent intent = new Intent(itemView.getContext(), CreditsExchangeDetailedActivity.class);
                            intent.putExtra("shoppingId", userShoppingCarAdapter.getItem(i).getProductId());
                            itemView.getContext().startActivity(intent);
                            break;
                    }
                }
            });
        }

        private void setPosts(List<UserShoppingCarBean> posts) {
            userShoppingCarAdapter.getData().clear();
            userShoppingCarAdapter.addData(posts);
        }
    }

    /**
     * 点击事件
     */
    public interface ShoppingCarInterface {
        void checkChild(int position,boolean isSelect);

        void doDecrease(int position);

        void doIncrease(int position);

        void childDelete(int position);
    }
}
