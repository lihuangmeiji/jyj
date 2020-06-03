package com.idougong.jyj.module.adapter.provider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.idougong.jyj.R;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.provider.HomeBannerItemBean;
import com.idougong.jyj.utils.CornerTransform;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewBinder;

public class HomeBannerItemViewProvider extends ItemViewBinder<HomeBannerItemBean, HomeBannerItemViewProvider.ViewHolder> {

    private HomeBannerInterface homeBannerInterface;

    public HomeBannerInterface getHomeBannerInterface() {
        return homeBannerInterface;
    }

    public void setHomeBannerInterface(HomeBannerInterface homeBannerInterface) {
        this.homeBannerInterface = homeBannerInterface;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        View root = layoutInflater.inflate(R.layout.item_banner, viewGroup, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull HomeBannerItemBean homeBannerItemBean) {
        if (!EmptyUtils.isEmpty(homeBannerItemBean.getHomeTopBg())) {
            Glide.with(viewHolder.ivBannerBg.getContext())
                    .load(homeBannerItemBean.getHomeTopBg())
                    .asBitmap()
                    .into(viewHolder.ivBannerBg);
        }
        viewHolder.setPosts(homeBannerItemBean.getBannerBeansList1(), getHomeBannerInterface());
        viewHolder.setPosts1(homeBannerItemBean.getBannerBeansList2(), getHomeBannerInterface());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private Banner banner;
        private Banner banner1;
        private ImageView ivBannerBg;
        private HomeBannerInterface homeBannerInterface1;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            banner = (Banner) itemView.findViewById(R.id.banner);
            banner1 = (Banner) itemView.findViewById(R.id.banner1);
            ivBannerBg = (ImageView) itemView.findViewById(R.id.iv_banner_bg);
        }

        private void setPosts(List<HomeBannerBean> homeBannerBeansList, HomeBannerInterface homeBannerInterface) {
            List<String> stringList = new ArrayList<>();
            if (homeBannerBeansList != null && homeBannerBeansList.size() > 0) {
                for (int i = 0; i < homeBannerBeansList.size(); i++) {
                    stringList.add(homeBannerBeansList.get(i).getImg());
                }
            }
            homeBannerInterface1 = homeBannerInterface;
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    homeBannerInterface1.setOnBannerListener(homeBannerBeansList.get(position));
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
                    CornerTransform transformation = new CornerTransform(context, dip2px(context, 10));
                    transformation.setExceptCorner(false, false, false, false);
                    Glide.with(context)
                            .load(path)
                            .asBitmap()
                            .skipMemoryCache(true)
                            .error(R.mipmap.homebannermr)
                            .fallback(R.mipmap.homebannermr)
                            .transform(transformation)
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


        private void setPosts1(List<HomeBannerBean> homeBannerBeansList, HomeBannerInterface homeBannerInterface) {
            List<String> stringList = new ArrayList<>();
            if (homeBannerBeansList != null && homeBannerBeansList.size() > 0) {
                for (int i = 0; i < homeBannerBeansList.size(); i++) {
                    stringList.add(homeBannerBeansList.get(i).getImg());
                }
            }
            homeBannerInterface1 = homeBannerInterface;
            banner1.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    homeBannerInterface1.setOnBanner1Listener(homeBannerBeansList.get(position));
                }
            });
            //设置轮播时间
            banner1.setDelayTime(2000);
            //设置banner样式
            banner1.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            //设置图片加载器
            banner1.setImageLoader(new ImageLoader() {
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
            banner1.setBannerAnimation(Transformer.ScaleInOut);
            //设置图片集合
            banner1.setImages(stringList);
            //banner设置方法全部调用完毕时最后调用
            banner1.start();
        }

        public static int dip2px(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }
    }

    /**
     * 点击事件
     */
    public interface HomeBannerInterface {
        void setOnBannerListener(HomeBannerBean homeBannerBean);

        void setOnBanner1Listener(HomeBannerBean homeBannerBean);
    }
}
