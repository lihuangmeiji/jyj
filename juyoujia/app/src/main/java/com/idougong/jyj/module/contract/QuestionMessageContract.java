package com.idougong.jyj.module.contract;

import android.widget.ScrollView;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.QuestionBean;
import com.idougong.jyj.model.UserMessage;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.io.File;
import java.util.List;

public interface QuestionMessageContract {
    interface View extends BaseView {
        void addquestionMessageResult(QuestionBean questionBean);
        void setFileUploadResult(String str);
        void refreshUserTimeResult(BaseResponseInfo result);
        void setUserLoginResult(LoginBean loginBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void addquestionMessage(String img, String feedback, String groupCode);
        void getFileUploadResult(File file);
        void refreshUserTime();
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
    }
}
