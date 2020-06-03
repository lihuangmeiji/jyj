package com.idougong.jyj.module.contract;

import android.widget.ImageView;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.GroupInfoBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserShoppingCarBean;
import com.idougong.jyj.model.UserShoppingCarConfirmBean;
import com.idougong.jyj.model.UserShoppingCarDivisionBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface TabCategorizeThirdContract {
    interface View extends BaseView {
        void setUserShoppingListResult(UserShoppingCarDivisionBean userShoppingCarDivisionBean);
        void setUserLoginResult(LoginBean loginBean);
        void setUserShoppingCarDelete(String result);
        void setUserShoppingCarUpateNumber(String result);
        void setUserShoppingCarConfirm(UserShoppingCarConfirmBean result);
        void setRecommendShoppingDataResult(List<HomeShoppingSpreeBean> homeShoppingDataResult);
        void addShoppingCarResult(String strResult, ImageView imageView);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserShoppingListResult();
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getUserShoppingCarDelete(String ids);
        void getUserShoppingCarUpateNumber(int productId,int num);
        void getUserShoppingCarConfirm(String ids,Integer couponId,boolean isUsed);
        void getRecommendShoppingDataResult();
        void addShoppingCar(int productId,ImageView imageView,int processingWayId,int skuId);
    }
}
