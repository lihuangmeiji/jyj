package com.idougong.jyj.module.contract;

import android.widget.ImageView;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface UserSearchResultContract {
    interface View extends BaseView {
        void setSearchShoppingDataResult(List<HomeShoppingSpreeBean> homeShoppingDataResult);
        void addShoppingCarResult(String strResult, ImageView imageView);
        void setUserLoginResult(LoginBean loginBean);
        void setShoppingNumber(String str);
        void setRecommendShoppingDataResult(List<HomeShoppingSpreeBean> homeShoppingDataResult);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getSearchShoppingDataResult(String spname);
        void addShoppingCar(int productId,ImageView imageView,int processingWayId,int skuId);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getShoppingNumber();
        void getRecommendShoppingDataResult();
    }
}
