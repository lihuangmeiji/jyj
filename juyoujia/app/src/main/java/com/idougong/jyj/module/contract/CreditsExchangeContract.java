package com.idougong.jyj.module.contract;

import android.widget.ImageView;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.ShoppingSpeciesBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface CreditsExchangeContract {
    interface View extends BaseView {
        void setShoppingDataResult(List<HomeShoppingSpreeBean> homeShoppingDataResult);
        void setHomeBannerResult(List<HomeBannerBean> homeBannerBeanList);
        void setUserLoginResult(LoginBean loginBean);
        void setShoppingSpeciesDataResult(List<ShoppingSpeciesBean> shoppingSpeciesDataResult);
        void setShoppingNumber(String str);
        void addShoppingCarResult(String strResult,ImageView imageView,int position);
        void reduceShoppingCarResult(String strResult,int position);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getShoppingDataResult(int pageNum, int pageSize,Integer categoryId);

        void getHomeBannerResult();

        void getUserLoginResult(String phone, String invCode, String smsCode, String token);

        void getShoppingSpeciesDataResult();

        void getShoppingNumber();

        void addShoppingCar(int productId,ImageView imageView,int position);

        void reduceShoppingCar(int productId,int position);
    }
}
