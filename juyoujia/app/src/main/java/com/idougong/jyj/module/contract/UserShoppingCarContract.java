package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserMessage;
import com.idougong.jyj.model.UserShoppingCarBean;
import com.idougong.jyj.model.UserShoppingCarConfirmBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface UserShoppingCarContract {
    interface View extends BaseView {
        void setUserShoppingListResult(List<UserShoppingCarBean> userShoppingCarBeanList);
        void setUserLoginResult(LoginBean loginBean);
        void setUserShoppingCarDelete(String result);
        void setUserShoppingCarUpateNumber(String result);
        void setUserShoppingCarConfirm(UserShoppingCarConfirmBean result);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserShoppingListResult();
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getUserShoppingCarDelete(String ids);
        void getUserShoppingCarUpateNumber(int productId,int num);
        void getUserShoppingCarConfirm(String ids,Integer couponId,boolean isUsed);
    }
}
