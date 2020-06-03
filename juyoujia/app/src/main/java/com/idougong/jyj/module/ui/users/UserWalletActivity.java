package com.idougong.jyj.module.ui.users;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.di.AppCommonModule;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.idougong.jyj.module.contract.UserWalletContract;
import com.idougong.jyj.module.presenter.UserWalletPresenter;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.TargetClick;

import butterknife.BindView;
import butterknife.OnClick;

public class UserWalletActivity extends BaseActivity<UserWalletPresenter> implements UserWalletContract.View {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView iv_right;
    @BindView(R.id.tv_uw_balance)
    TextView tvUwBalance;
    UserWalletInfoBean userWalletInfoBeanBd;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_wallet;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        isClose = true;
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("我的钱包");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        iv_right.setVisibility(View.GONE);
        getPresenter().getUserWalletInfo();
    }

    @OnClick({R.id.toolbar,
            R.id.tv_user_wallet_rtd,
            R.id.tv_recharge,
            R.id.tv_withdrawal,
            R.id.tv_wallet_money_zq
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                finish();
                break;
            case R.id.tv_user_wallet_rtd:
                Intent intent = new Intent(getBaseContext(), UserWalletRecordTheDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_recharge:
                if (EmptyUtils.isEmpty(userWalletInfoBeanBd)) {
                    getPresenter().getUserWalletInfo();
                    return;
                }
                if (userWalletInfoBeanBd.isDisable()) {
                    toast("您的账号已被禁用此功能！");
                    return;
                }
                Intent intent1 = new Intent(getBaseContext(), UserWalletRechargeActivity.class);
                startActivityForResult(intent1, 2);
                break;
            case R.id.tv_withdrawal:
                if (EmptyUtils.isEmpty(userWalletInfoBeanBd)) {
                    getPresenter().getUserWalletInfo();
                    return;
                }
                if (userWalletInfoBeanBd.isDisable()) {
                    toast("您的账号已被禁用此功能！");
                    return;
                }
              /*  if (EmptyUtils.isEmpty(userWalletInfoBeanBd.getAliRealname())) {
                    intent = new Intent(getBaseContext(), UserNameCertificationAddActivity.class);
                    startActivityForResult(intent, 2);
                } else {
                    Intent intent2 = new Intent(getBaseContext(), UserWalletWithdrawalActivity.class);
                    intent2.putExtra("userWalletInfoBeanBd", userWalletInfoBeanBd);
                    startActivityForResult(intent2, 2);
                }*/
                break;
            case R.id.tv_wallet_money_zq:
                TargetClick.targetOnClick(getBaseContext(), AppCommonModule.API_BASE_URL + "h5common/jyj-acquire/index.html");
                break;
            default:
                break;
        }
    }


    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            getPresenter().getUserWalletInfo();
        } else {
            openLogin();
        }
    }

    @Override
    public void setUserWalletInfoResult(UserWalletInfoBean userWalletInfoBean) {
        if (!EmptyUtils.isEmpty(userWalletInfoBean)) {
            userWalletInfoBeanBd = userWalletInfoBean;
            tvUwBalance.setText(String.format("%.2f", userWalletInfoBean.getBalance()));
        }
    }


    @Override
    public void showErrorMsg(String msg, int code) {
        super.showErrorMsg(msg, code);
        if (code == -1) {
            if (login != null && showLog == false) {
                showLog = true;
                getPresenter().getUserLoginResult(login.getPhone(), "", "", login.getToken());
            } else {
                openLogin();
            }
            return;
        } else if (code == -2) {
            openLogin();
            return;
        } else if (code == -10) {
            return;
        }
        toast(msg);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 1) {
            loadUserInfo();
            getPresenter().getUserWalletInfo();
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

}
