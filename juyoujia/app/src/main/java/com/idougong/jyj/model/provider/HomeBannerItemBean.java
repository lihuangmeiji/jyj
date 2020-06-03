package com.idougong.jyj.model.provider;

import android.view.Display;

import com.idougong.jyj.model.HomeBannerBean;

import java.util.List;

public class HomeBannerItemBean {
    private List<HomeBannerBean> bannerBeansList1;

    public List<HomeBannerBean> getBannerBeansList1() {
        return bannerBeansList1;
    }

    public void setBannerBeansList1(List<HomeBannerBean> bannerBeansList1) {
        this.bannerBeansList1 = bannerBeansList1;
    }

    private String homeTopBg;

    private Display display;

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public String getHomeTopBg() {
        return homeTopBg;
    }

    public void setHomeTopBg(String homeTopBg) {
        this.homeTopBg = homeTopBg;
    }

    private List<HomeBannerBean> bannerBeansList2;

    public List<HomeBannerBean> getBannerBeansList2() {
        return bannerBeansList2;
    }

    public void setBannerBeansList2(List<HomeBannerBean> bannerBeansList2) {
        this.bannerBeansList2 = bannerBeansList2;
    }
}
