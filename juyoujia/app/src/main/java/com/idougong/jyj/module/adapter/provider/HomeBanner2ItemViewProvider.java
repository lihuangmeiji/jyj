package com.idougong.jyj.module.adapter.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.idougong.jyj.R;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.provider.HomeBanner1ItemBean;
import com.idougong.jyj.model.provider.HomeBanner2ItemBean;
import com.idougong.jyj.module.adapter.HomeBannerAdapter;
import com.idougong.jyj.module.adapter.UserServiceFunctionAdapter;
import com.idougong.jyj.widget.GridDividerItemDecoration;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewBinder;

public class HomeBanner2ItemViewProvider extends ItemViewBinder<HomeBanner2ItemBean, HomeBanner2ItemViewProvider.ViewHolder> {

   private HomeBanner2Interface homeBanner2Interface;

    public HomeBanner2Interface getHomeBanner2Interface() {
        return homeBanner2Interface;
    }

    public void setHomeBanner2Interface(HomeBanner2Interface homeBanner2Interface) {
        this.homeBanner2Interface = homeBanner2Interface;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        View root = layoutInflater.inflate(R.layout.item_home_baner, viewGroup, false);
        return new ViewHolder(root,homeBanner2Interface);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull HomeBanner2ItemBean homeBannerItemBean) {
        viewHolder.setPosts(homeBannerItemBean.getBannerBeansList1());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerView;
        private HomeBannerAdapter adapter;
        private LinearLayout linBanner2;

        private ViewHolder(@NonNull View itemView,HomeBanner2Interface homeBanner2Interface) {
            super(itemView);
            linBanner2=itemView.findViewById(R.id.lin_banner2);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.rv_jyj_banner);
            recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 2));
            recyclerView.addItemDecoration(new GridDividerItemDecoration(30, itemView.getContext().getResources().getColor(R.color.white)));
            /* adapter 只负责灌输、适配数据，布局交给 LayoutManager，可复用 */
            adapter = new HomeBannerAdapter(R.layout.item_home_baner_contnet);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    homeBanner2Interface.setOnBannerListener(adapter.getItem(i));
                }
            });
        }

        private void setPosts(List<HomeBannerBean> homeBannerBeansList) {
            adapter.getData().clear();
            if(!EmptyUtils.isEmpty(homeBannerBeansList)&&homeBannerBeansList.size()>0){
                linBanner2.setVisibility(View.VISIBLE);
            }else{
                linBanner2.setVisibility(View.GONE);
            }
            if(homeBannerBeansList.size()>2){
                recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 3));
            }else{
                recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 2));
            }
            adapter.addData(homeBannerBeansList);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 点击事件
     */
    public interface HomeBanner2Interface {
        void setOnBannerListener(HomeBannerBean homeBannerBean);
    }
}
