package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.FeedbacksBean;
import com.idougong.jyj.model.HomeDetailedBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface HomeDetailedContract {
    interface View extends BaseView {
        void setHomeDetailedResult(HomeDetailedBean homeDetailedBean);
        void setHomeDetailedAddVoteResult(String str);
        void setHomeDetailedAddFeedbackResult(String str, int position);
        void setHomeDetailedAddForwardResult(String str);
        void setHomeDetailedUpdateFeedbackResult(List<FeedbacksBean> feedbacksBeanList);
        void setUserLoginResult(LoginBean loginBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getHomeDetailedResult(int id, boolean countReadNum);
        void getHomeDetailedAddVoteResult(String ids, int viId);
        void getHomeDetailedAddFeedbackResult(int cId, int cftId, int position);
        void getHomeDetailedAddForwardResult(int id);
        void getHomeDetailedUpdateFeedbackResult(int cId);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
    }
}
