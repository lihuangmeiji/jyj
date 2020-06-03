package com.idougong.jyj.module.contract;

import android.widget.ImageView;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.provider.HomePbShoppingItemBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface HomePanicBuyingContract {
    interface View extends BaseView {
        void setPanicBuyingDataResult(List<HomeShoppingSpreeBean> homeShoppingDataResult);
        void setPanicBuyingTimeDataResult(List<HomePbShoppingItemBean> homePbShoppingItemBeanList);
        void addShoppingCarResult(String strResult, ImageView imageView);
        void setUserLoginResult(LoginBean loginBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getPanicBuyingDataResult(int pageNum,int pageSize,String seckillTime);
        void getPanicBuyingTimeDataResult();
        void addShoppingCar(int productId,ImageView imageView,int processingWayId, int skuId);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
    }
}
