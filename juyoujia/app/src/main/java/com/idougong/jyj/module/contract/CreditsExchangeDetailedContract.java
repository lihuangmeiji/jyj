package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.ConfirmOrderBean;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

public interface CreditsExchangeDetailedContract {
    interface View extends BaseView {
        void setShoppingDetailedResult(HomeShoppingSpreeBean homeShoppingSpreeBean);
        void setUserLoginResult(LoginBean loginBean);
        void setOnlineSupermarketGoodsOreder(ConfirmOrderBean confirmOrderBean);
        void setUpdateUserInfoResult(LoginBean loginBean);
        void addShoppingCarResult(String strResult);
        void setShoppingNumber(String str);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getShoppingDetailedResult(int id);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getOnlineSupermarketGoodsOreder(String jsonStr);
        void getUpdateUserInfoResult(int id);
        void addShoppingCar(int productId,int productNum,int processingWayId,int skuId);
        void getShoppingNumber();
    }
}
