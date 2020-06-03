package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.RechargersTelephoneChargesBean;
import com.idougong.jyj.model.RtcOrderBean;
import com.idougong.jyj.model.UserIntegralBean;
import com.idougong.jyj.model.UserRechargeAliBean;
import com.idougong.jyj.model.UserRechargeWxBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface RechargersTelephoneChargesContract {
    interface View extends BaseView {
        void setRechargersTelephoneChargesListResult(List<RechargersTelephoneChargesBean> rechargersTelephoneChargesListResult);
        void getRtcOrderResult(RtcOrderBean rtcOrderBean);
        void setUserLoginResult(LoginBean loginBean);
        void setRtcRechargeWx(UserRechargeWxBean userRechargeWxBean);
        void setRtcrRechargeZfb(String str);
        void setRtcrRechargeWallet(String str);
        void setMethodOfPayment(PayMethodBean payMethodBean);
        void setUserWalletInfoResult(UserWalletInfoBean userWalletInfoBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getRechargersTelephoneChargesListResult();
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void addRtcOrderResult(String phone, Integer price);
        void getRtcRechargeWx(int billId);
        void getRtcrRechargeZfb(int billId);
        void getRtcrRechargeWallet(int billId);
        void getMethodOfPayment();
        void getUserWalletInfo();
    }
}



