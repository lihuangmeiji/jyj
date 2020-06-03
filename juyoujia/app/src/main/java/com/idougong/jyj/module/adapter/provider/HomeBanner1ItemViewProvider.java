package com.idougong.jyj.module.adapter.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.idougong.jyj.R;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.provider.HomeBanner1ItemBean;
import com.idougong.jyj.model.provider.HomeBannerItemBean;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewBinder;

public class HomeBanner1ItemViewProvider extends ItemViewBinder<HomeBanner1ItemBean, HomeBanner1ItemViewProvider.ViewHolder> {

    private HomeBanner1Interface homeBannerInterface;

    public HomeBanner1Interface getHomeBannerInterface() {
        return homeBannerInterface;
    }

    public void setHomeBannerInterface(HomeBanner1Interface homeBannerInterface) {
        this.homeBannerInterface = homeBannerInterface;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        View root = layoutInflater.inflate(R.layout.item_banner1, viewGroup, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull HomeBanner1ItemBean homeBannerItemBean) {
        viewHolder.setPosts(homeBannerItemBean.getBannerBeansList1(),getHomeBannerInterface());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private Banner banner;

        private HomeBanner1Interface homeBannerInterface1;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            banner = (Banner) itemView.findViewById(R.id.banner1);
        }

        private void setPosts(List<HomeBannerBean> homeBannerBeansList, HomeBanner1Interface homeBannerInterface) {
            List<String> stringList = new ArrayList<>();
            if (homeBannerBeansList != null && homeBannerBeansList.size() > 0) {
                for (int i = 0; i < homeBannerBeansList.size(); i++) {
                    stringList.add(homeBannerBeansList.get(i).getImg());
                }
            }
            homeBannerInterface1=homeBannerInterface;
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    homeBannerInterface1.setOnBanner1Listener(homeBannerBeansList.get(position));
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

    /**
     * 点击事件
     */
    public interface HomeBanner1Interface {
        void setOnBanner1Listener(HomeBannerBean homeBannerBean);
    }
}
