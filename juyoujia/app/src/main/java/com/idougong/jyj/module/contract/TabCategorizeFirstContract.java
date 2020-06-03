package com.idougong.jyj.module.contract;

import android.widget.ImageView;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.ConvenientFunctionsBean;
import com.idougong.jyj.model.FlickerScreenBean;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.HomeConfigurationInformationBean;
import com.idougong.jyj.model.HomeDataBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PagingInformationBean;
import com.idougong.jyj.model.RewardBean;
import com.idougong.jyj.model.StoreInfoBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.model.provider.HomePbShoppingItemBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface TabCategorizeFirstContract {
    interface View extends BaseView {
        void setHomeBannerResult(List<HomeBannerBean> homeBannerBeanList);
        void setHomeBanner1Result(List<HomeBannerBean> homeBannerBeanList);
        void setHomeBanner2Result(List<HomeBannerBean> homeBannerBeanList);
        void setFunctionDivisionOne(List<ConvenientFunctionsBean> convenientFunctionsBeanList);
        void setHomeShoppingDataResult(List<HomeShoppingSpreeBean> homeShoppingDataResult);
        void addShoppingCarResult(String strResult, ImageView imageView);
        void setUserLoginResult(LoginBean loginBean);
        void setHomePanicBuyingDataResult(HomePbShoppingItemBean homePanicBuyingDataResult);
        void setHomeConfigurationInformation(List<HomeConfigurationInformationBean> homeConfigurationInformationBeanList);
        void setUserMessageNumber(String str);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getHomeBannerResult();
        void getHomeBanner1Result();
        void getHomeBanner2Result();
        void getFunctionDivisionOne();
        void getHomeShoppingDataResult();
        void addShoppingCar(int productId,ImageView imageView,int processingWayId,int skuId);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getHomePanicBuyingDataResult();
        void getHomeConfigurationInformation();
        void getUserMessageNumber();
    }
}
