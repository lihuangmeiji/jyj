package com.idougong.jyj.module.adapter.provider;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.idougong.jyj.R;
import com.idougong.jyj.model.ConvenientFunctionsBean;
import com.idougong.jyj.model.provider.HomeServicesItemBean;
import com.idougong.jyj.module.adapter.UserServiceFunctionAdapter;


import java.util.List;

import me.drakeet.multitype.ItemViewBinder;

public class HomeServicesItemViewProvider extends ItemViewBinder<HomeServicesItemBean, HomeServicesItemViewProvider.ViewHolder> {
    private HomeServicesInterface homeServicesInterface;

    public HomeServicesInterface getHomeServicesInterface() {
        return homeServicesInterface;
    }

    public void setHomeServicesInterface(HomeServicesInterface homeServicesInterface) {
        this.homeServicesInterface = homeServicesInterface;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        View root = layoutInflater.inflate(R.layout.item_home_services, viewGroup, false);
        return new ViewHolder(root,homeServicesInterface);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull HomeServicesItemBean homeServicesItemBean) {
        viewHolder.setPosts(homeServicesItemBean.getConvenientFunctionsBeanList());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        private UserServiceFunctionAdapter adapter;


        private ViewHolder(@NonNull final View itemView,HomeServicesInterface homeServicesInterface) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.rv_jyj_service);
            recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 4));
            /* adapter 只负责灌输、适配数据，布局交给 LayoutManager，可复用 */
            adapter = new UserServiceFunctionAdapter(R.layout.item_home_function_division);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    homeServicesInterface.homeServiceOnClickListener(adapter.getItem(i));
                }
            });
        }

        private void setPosts(List<ConvenientFunctionsBean> posts) {
            if(posts!=null&&posts.size()>8){
                recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 5));
            }
            adapter.getData().clear();
            adapter.addData(posts);
            adapter.notifyDataSetChanged();
        }
    }


    /**
     * 点击事件
     */
    public interface HomeServicesInterface {
        void homeServiceOnClickListener(ConvenientFunctionsBean data);
    }
}
