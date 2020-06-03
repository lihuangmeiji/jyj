/*
package com.idougong.jyj.module.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.HomeMultipleItem;
import com.idougong.jyj.module.ui.home.CreditsExchangeDetailedActivity;
import com.idougong.jyj.utils.TargetClick;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class HomeMultipleAdapter extends BaseMultiItemQuickAdapter<HomeMultipleItem, BaseViewHolder> {

    public HomeMultipleAdapter(List data) {
        super(data);
        addItemType(HomeMultipleItem.HOMETYPE1, R.layout.item_banner);
        addItemType(HomeMultipleItem.HOMETYPE2, R.layout.item_home_services);
        addItemType(HomeMultipleItem.HOMETYPE3, R.layout.item_banner1);
        addItemType(HomeMultipleItem.HOMETYPE4, R.layout.item_bottom);
        addItemType(HomeMultipleItem.HOMETYPE5, R.layout.item_bottom);
        addItemType(HomeMultipleItem.HOMETYPE6, R.layout.item_function_shopping_tj);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, HomeMultipleItem homeMultipleItem) {
        switch (baseViewHolder.getItemViewType()) {
            case HomeMultipleItem.HOMETYPE1:
                Banner banner = baseViewHolder.getView(R.id.banner);
                setPosts(homeMultipleItem.getBannerBeansList1(), banner);
                break;
            case HomeMultipleItem.HOMETYPE2:
                RecyclerView rvJyjService = baseViewHolder.getView(R.id.rv_jyj_service);
                rvJyjService.setLayoutManager(new GridLayoutManager(mContext, 4));
                UserServiceFunctionAdapter userServiceFunctionAdapter = new UserServiceFunctionAdapter(R.layout.item_home_function_division);
                rvJyjService.setAdapter(userServiceFunctionAdapter);
                userServiceFunctionAdapter.addData(homeMultipleItem.getConvenientFunctionsBeanList());
                userServiceFunctionAdapter.notifyDataSetChanged();
                break;
            case HomeMultipleItem.HOMETYPE3:
                Banner banner1 = baseViewHolder.getView(R.id.banner1);
                setPosts1(homeMultipleItem.getBannerBeansList2(), banner1);
                break;
            case HomeMultipleItem.HOMETYPE4:

                break;
            case HomeMultipleItem.HOMETYPE5:

                break;
            case HomeMultipleItem.HOMETYPE6:
                RecyclerView recyclerShoppingView = baseViewHolder.getView(R.id.post_list);
                recyclerShoppingView.setLayoutManager(new GridLayoutManager(mContext, 3));
                */
/* adapter 只负责灌输、适配数据，布局交给 LayoutManager，可复用 *//*

                HomeShoppingSpreeAdapter homeShoppingSpreeAdapter = new HomeShoppingSpreeAdapter(R.layout.item_home_shopping_spree);
                recyclerShoppingView.setAdapter(homeShoppingSpreeAdapter);
                homeShoppingSpreeAdapter.addData(homeMultipleItem.getHomeShoppingSpreeBeanList());
                homeShoppingSpreeAdapter.notifyDataSetChanged();
                break;
        }
    }




    private void setPosts(List<HomeBannerBean> homeBannerBeansList, Banner banner) {
        List<String> stringList = new ArrayList<>();
        if (homeBannerBeansList != null && homeBannerBeansList.size() > 0) {
            for (int i = 0; i < homeBannerBeansList.size(); i++) {
                stringList.add(homeBannerBeansList.get(i).getImg());
            }
        }
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context)
                        .load(path)
                        .asBitmap()
                        .skipMemoryCache(true)
                        .error(R.mipmap.homebannermr)
                        .fallback(R.mipmap.homebannermr)
                        .into(imageView);
            }
        });
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.ScaleInOut);
        //设置图片集合
        banner.setImages(stringList);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }


    private void setPosts1(List<HomeBannerBean> homeBannerBeansList, Banner banner) {
        List<String> stringList = new ArrayList<>();
        if (homeBannerBeansList != null && homeBannerBeansList.size() > 0) {
            for (int i = 0; i < homeBannerBeansList.size(); i++) {
                stringList.add(homeBannerBeansList.get(i).getImg());
            }
        }
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context)
                        .load(path)
                        .asBitmap()
                        .skipMemoryCache(true)
                        .error(R.mipmap.homebannermr)
                        .fallback(R.mipmap.homebannermr)
                        .into(imageView);
            }
        });
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.ScaleInOut);
        //设置图片集合
        banner.setImages(stringList);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }



}
*/
