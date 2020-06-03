package com.idougong.jyj.module.contract;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.UserIntegralBean;
import com.idougong.jyj.model.UserMessage;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface UserMessageContract {
    interface View extends BaseView {
        void setUserMessageListResult(List<UserMessage> userMessageListResult);
        void setUserLoginResult(LoginBean loginBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserMessageListResult(int pageNum, int pageSize);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
    }
}
