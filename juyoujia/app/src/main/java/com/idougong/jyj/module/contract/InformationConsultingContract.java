package com.idougong.jyj.module.contract;

import com.idougong.jyj.model.HomeDataBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;
import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import java.util.List;
public interface InformationConsultingContract {
    interface View extends BaseView {
        void setHomeDataResult(List<HomeDataBean> homeDataBeanList);
        void setUserLoginResult(LoginBean loginBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getHomeDataResult(int pageNum, int pageSize);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
    }
}
