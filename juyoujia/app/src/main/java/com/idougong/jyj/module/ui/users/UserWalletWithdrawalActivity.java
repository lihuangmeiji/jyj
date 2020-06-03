package com.idougong.jyj.module.ui.users;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.UserWalletWithdrawalContract;
import com.idougong.jyj.module.presenter.UserWalletWithdrawalPresenter;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.TextUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class UserWalletWithdrawalActivity extends BaseActivity<UserWalletWithdrawalPresenter> implements UserWalletWithdrawalContract.View {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView iv_right;
    @BindView(R.id.tv_zfb_name)
    TextView tvZfbName;
    @BindView(R.id.tv_uw_send_code)
    TextView tvUwSendCode;
    @BindView(R.id.et_zfb_account)
    EditText etZfbAccount;
    @BindView(R.id.et_zfb_code)
    EditText etZfbCode;
    @BindView(R.id.et_us_withdrawal_money)
    EditText etUsWithdrawalMoney;
    @BindView(R.id.tv_us_withdrawal_money)
    TextView tvUsWithdrawalMoney;
    UserWalletInfoBean userWalletInfoBeanBd;
    CountDownTimer countDownTimer;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_wallet_withdrawal;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("提现");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        iv_right.setVisibility(View.GONE);
        userWalletInfoBeanBd = (UserWalletInfoBean) getIntent().getSerializableExtra("userWalletInfoBeanBd");
        if (EmptyUtils.isEmpty(userWalletInfoBeanBd)) {
            getPresenter().getUserWalletInfo();
        } else {
            etZfbAccount.setText(userWalletInfoBeanBd.getAliAccount());
            tvZfbName.setText(userWalletInfoBeanBd.getAliRealname());
            tvUsWithdrawalMoney.setText(TextUtil.FontHighlighting(getBaseContext(), "可提现余额¥" + String.format("%.2f", userWalletInfoBeanBd.getBalance())+ ",", "全部提现", R.style.tvUserAgreement1, R.style.userWalletInfoBeanBd));
        }
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long sin) {
                tvUwSendCode.setClickable(false); //设置不可点击
                tvUwSendCode.setText(sin / 1000 + "秒后可重新发送");  //设置倒计时时间
                tvUwSendCode.setTextColor(getBaseContext().getResources().getColor(R.color.tabwd));
            }

            @Override
            public void onFinish() {
                tvUwSendCode.setText("重新获取验证码");
                tvUwSendCode.setClickable(true);//重新获得点击
                tvUwSendCode.setTextColor(getBaseContext().getResources().getColor(R.color.color68));
            }
        };
    }

    @OnClick({R.id.toolbar,
            R.id.tv_uw_send_code,
            R.id.tv_us_withdrawal_apply_for,
            R.id.tv_us_withdrawal_money
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                finish();
                break;
            case R.id.tv_uw_send_code:
                if(login != null){
                    getPresenter().registerUserInfoSms(login.getPhone());
                }else{
                    openLogin();
                }
                break;
            case R.id.tv_us_withdrawal_apply_for:
                if (EmptyUtils.isEmpty(etZfbAccount.getText().toString().trim())) {
                    toast("请填写支付宝账号");
                    return;
                }
                if (EmptyUtils.isEmpty(etZfbCode.getText().toString().trim())) {
                    toast("请填写验证码");
                    return;
                }
                if (EmptyUtils.isEmpty(etUsWithdrawalMoney.getText().toString().trim())) {
                    toast("请填写提现金额");
                    return;
                }
                if (userWalletInfoBeanBd.getBalance() < Double.valueOf(etUsWithdrawalMoney.getText().toString().trim())) {
                    toast("可提现金额不足");
                    return;
                }
                if (Double.valueOf(etUsWithdrawalMoney.getText().toString().trim()) < 10) {
                    toast("提现金额不能低于10元");
                    return;
                }
                showLoading();
                getPresenter().addUserWalletWithdrawal(etUsWithdrawalMoney.getText().toString().trim(), etZfbAccount.getText().toString().trim(), etZfbCode.getText().toString().trim());
                break;
            case R.id.tv_us_withdrawal_money:
                if (EmptyUtils.isEmpty(etZfbAccount.getText().toString().trim())) {
                    toast("请填写支付宝账号");
                    return;
                }
                if (EmptyUtils.isEmpty(etZfbCode.getText().toString().trim())) {
                    toast("请填写验证码");
                    return;
                }

                if (EmptyUtils.isEmpty(userWalletInfoBeanBd.getBalance())) {
                    toast("提现金额获取失败,请填写提交申请！");
                    return;
                }
                if (Double.valueOf(userWalletInfoBeanBd.getBalance()) < 10) {
                    toast("提现金额不能低于10元");
                    return;
                }
                showLoading();
                getPresenter().addUserWalletWithdrawal(String.valueOf(userWalletInfoBeanBd.getBalance()), etZfbAccount.getText().toString().trim(), etZfbCode.getText().toString().trim());
                break;
        }
    }

    @Override
    public void showErrorMsg(String msg, int code) {
        super.showErrorMsg(msg, code);
        hiddenLoading();
        if (code == -1) {
            if (login != null && showLog == false) {
                showLog = true;
                getPresenter().getUserLoginResult(login.getPhone(), "", "", login.getToken());
            }else{
                openLogin();
            }
            return;
        }else if (code == -2) {
            openLogin();
            return;
        }  else if (code == -10) {
            return;
        }
        toast(msg);
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
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            loadUserInfo();
            getPresenter().getUserWalletInfo();
        }
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

    @Override
    public void UserWalletWithdrawalResult(String str) {
        hiddenLoading();
        toast("提现申请已提交成功");
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }

    @Override
    public void setUserWalletInfoResult(UserWalletInfoBean userWalletInfoBean) {
        if (!EmptyUtils.isEmpty(userWalletInfoBean)) {
            userWalletInfoBeanBd = userWalletInfoBean;
            tvUsWithdrawalMoney.setText(TextUtil.FontHighlighting(getBaseContext(), "可提现余额¥" + String.format("%.2f", userWalletInfoBean.getBalance()) + ",", "全部提现", R.style.tvUserAgreement1, R.style.userWalletInfoBeanBd));
        }
    }

    @Override
    public void registerUserInfoSmsResult(BaseResponseInfo baseResponseInfo) {
        if (baseResponseInfo.getCode() == 0) {
            toast("发送成功");
            if (countDownTimer != null) {
                countDownTimer.start();
            } else {
                countDownTimer = new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long sin) {
                        tvUwSendCode.setClickable(false); //设置不可点击
                        tvUwSendCode.setText(sin / 1000 + "秒后可重新发送");  //设置倒计时时间
                        tvUwSendCode.setTextColor(getBaseContext().getResources().getColor(R.color.tabwd));
                    }

                    @Override
                    public void onFinish() {
                        tvUwSendCode.setText("重新获取验证码");
                        tvUwSendCode.setClickable(true);//重新获得点击
                        tvUwSendCode.setTextColor(getBaseContext().getResources().getColor(R.color.color68));
                    }
                };
                countDownTimer.start();
            }
        } else {
            toast(baseResponseInfo.getMsg());
        }
    }
}
