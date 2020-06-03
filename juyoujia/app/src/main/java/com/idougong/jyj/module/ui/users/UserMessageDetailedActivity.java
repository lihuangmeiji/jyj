package com.idougong.jyj.module.ui.users;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.common.ui.SimpleActivity;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserMessage;
import com.idougong.jyj.module.contract.UserMessageDetailedContract;
import com.idougong.jyj.module.presenter.UserMessageDetailedPresenter;
import com.idougong.jyj.utils.ActivityCollector;

import butterknife.BindView;
import butterknife.OnClick;

public class UserMessageDetailedActivity extends BaseActivity<UserMessageDetailedPresenter> implements UserMessageDetailedContract.View {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView iv_right;
    @BindView(R.id.tv_user_message_detcontent)
    TextView tv_user_message_detcontent;
    @BindView(R.id.tv_usermessage_detailtime)
    TextView messageTime;
    @BindView(R.id.tv_usermessage_detailtitle)
    TextView messageTitle;

    int umId;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_message_detailed;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("消息详情");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        iv_right.setVisibility(View.GONE);
        umId = getIntent().getIntExtra("umId",0);
        getPresenter().getMessageDetailedResult(umId);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.toolbar)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                break;
        }
    }


    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hiddenLoading() {
        super.hiddenLoading();
    }

    @Override
    public void showErrorMsg(String msg,int code) {
        super.showErrorMsg(msg,code);
        if(code==-1){
            if (login != null && showLog == false) {
                showLog = true;
                getPresenter().getUserLoginResult(login.getPhone(), "", "", login.getToken());
            } else {
                openLogin();
            }
            return;
        }
        toast(msg);
    }

    @Override
    public void setMessageDetailedResult(UserMessage userMessage) {
        if(EmptyUtils.isEmpty(userMessage)){
            return;
        }
        tv_user_message_detcontent.setText(userMessage.getContent());
        messageTime.setText(TimeUtils.getFriendlyTimeSpanByNow(TimeUtils.string2Millis(userMessage.getCreateAt())));
        messageTitle.setText(userMessage.getTitle());
    }

    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
        } else {
            openLogin();
        }
    }

}
