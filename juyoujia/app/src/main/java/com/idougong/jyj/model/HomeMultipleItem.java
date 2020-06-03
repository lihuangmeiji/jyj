package com.idougong.jyj.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class HomeMultipleItem {


    private List<HomeBannerBean> bannerBeansList1;
    private List<ConvenientFunctionsBean> convenientFunctionsBeanList;
    private List<HomeBannerBean> bannerBeansList2;
    private List<HomeShoppingSpreeBean> panicBuyingList;
    private List<HomeShoppingSpreeBean> homeShoppingSpreeBeanList;



    public List<HomeBannerBean> getBannerBeansList1() {
        return bannerBeansList1;
    }

    public void setBannerBeansList1(List<HomeBannerBean> bannerBeansList1) {
        this.bannerBeansList1 = bannerBeansList1;
    }

    public List<ConvenientFunctionsBean> getConvenientFunctionsBeanList() {
        return convenientFunctionsBeanList;
    }

    public void setConvenientFunctionsBeanList(List<ConvenientFunctionsBean> convenientFunctionsBeanList) {
        this.convenientFunctionsBeanList = convenientFunctionsBeanList;
    }

    public List<HomeBannerBean> getBannerBeansList2() {
        return bannerBeansList2;
    }

    public void setBannerBeansList2(List<HomeBannerBean> bannerBeansList2) {
        this.bannerBeansList2 = bannerBeansList2;
    }

    public List<HomeShoppingSpreeBean> getPanicBuyingList() {
        return panicBuyingList;
    }

    public void setPanicBuyingList(List<HomeShoppingSpreeBean> panicBuyingList) {
        this.panicBuyingList = panicBuyingList;
    }

    public List<HomeShoppingSpreeBean> getHomeShoppingSpreeBeanList() {
        return homeShoppingSpreeBeanList;
    }

    public void setHomeShoppingSpreeBeanList(List<HomeShoppingSpreeBean> homeShoppingSpreeBeanList) {
        this.homeShoppingSpreeBeanList = homeShoppingSpreeBeanList;
    }

}
