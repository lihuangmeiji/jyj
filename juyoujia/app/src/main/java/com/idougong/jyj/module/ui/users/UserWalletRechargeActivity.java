package com.idougong.jyj.module.ui.users;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.net.EnvConstant;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserWalletRechargeBean;
import com.idougong.jyj.module.contract.UserWalletRechargeContract;
import com.idougong.jyj.module.presenter.UserWalletRechargePresenter;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.widget.CashierInputFilter;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import sign.AuthResult;
import sign.PayResult;

public class UserWalletRechargeActivity extends BaseActivity<UserWalletRechargePresenter> implements UserWalletRechargeContract.View {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView iv_right;
    @BindView(R.id.et_uw_recharge_money)
    EditText etUwRechargeMoney;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    //微信支付
    private IWXAPI api;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_wallet_recharge;
    }


    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("余额充值");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        iv_right.setVisibility(View.GONE);
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(EnvConstant.WX_KEY);
        InputFilter[] filters={new CashierInputFilter()};
        etUwRechargeMoney.setFilters(filters); //设置金额输入的过滤器，保证只能输入金额类型
        addWxPlayReceiver();
    }

    @OnClick({R.id.toolbar,
            R.id.tv_pay_wx,
            R.id.tv_pay_zfb
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                finish();
                break;
            case R.id.tv_pay_wx:
                if (EmptyUtils.isEmpty(etUwRechargeMoney.getText().toString().trim())) {
                    toast("请填写充值金额");
                    return;
                }
                try {
                    if (Double.valueOf(etUwRechargeMoney.getText().toString().trim()) < 1) {
                        toast("充值金额不能低于1元");
                        return;
                    }
                } catch (Exception e) {
                    toast("充值金额格式不正确");
                    return;
                }
                getPresenter().getUserWalletRechargeInfo(etUwRechargeMoney.getText().toString().trim(), 2);
                break;
            case R.id.tv_pay_zfb:
                if (EmptyUtils.isEmpty(etUwRechargeMoney.getText().toString().trim())) {
                    toast("请填写充值金额");
                    return;
                }
                try {
                    if (Double.valueOf(etUwRechargeMoney.getText().toString().trim()) < 1) {
                        toast("充值金额不能低于1元");
                        return;
                    }
                } catch (Exception e) {
                    toast("充值金额格式不正确");
                    return;
                }
                getPresenter().getUserWalletRechargeInfo(etUwRechargeMoney.getText().toString().trim(), 1);
                break;
        }
    }

    @Override
    public void showErrorMsg(String msg, int code) {
        super.showErrorMsg(msg, code);
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
    public void setUserWalletRechargeResult(UserWalletRechargeBean userWalletRechargeBean, int payType) {
        if (userWalletRechargeBean != null) {
            if (payType == 1) {
                if (!EmptyUtils.isEmpty(userWalletRechargeBean.getAliPayInfo())) {
                    pay(userWalletRechargeBean.getAliPayInfo());
                } else {
                    toast("支付宝订单生成失败");
                }
            } else if (payType == 2) {
                if (userWalletRechargeBean.getWxPayInfo() != null) {
                    PayReq req = new PayReq();
                    //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                    req.appId = userWalletRechargeBean.getWxPayInfo().getAppId();
                    req.partnerId = EnvConstant.WX_PARTNERID;
                    req.prepayId = userWalletRechargeBean.getWxPayInfo().getPrypayId();
                    req.nonceStr = userWalletRechargeBean.getWxPayInfo().getNonceStr();
                    req.timeStamp = userWalletRechargeBean.getWxPayInfo().getTimeStamp();
                    req.packageValue = userWalletRechargeBean.getWxPayInfo().getPackageX();
                    req.sign = userWalletRechargeBean.getWxPayInfo().getPaySign();
                    api.sendReq(req);
                } else {
                    toast("微信支付订单生成失败！");
                }
            }
        }
    }


    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(final String ranking) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(UserWalletRechargeActivity.this);
                Map<String, String> result = alipay.payV2(ranking, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */

                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        toast("充值成功");
                        Intent intent = new Intent();
                        setResult(1, intent);
                        finish();
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        toast("取消充值");
                    } else if (TextUtils.equals(resultStatus, "4000")) {
                        toast("支付失败");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        toast("支付异常！");
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        toast("授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()));
                    } else {
                        // 其他状态值则为授权失败
                        toast("授权失败" + String.format("authCode:%s", authResult.getAuthCode()));
                    }
                    break;

                }
                default:
                    break;
            }
        }

        ;
    };

    BroadcastReceiver broadcastReceiver;

    private void addWxPlayReceiver() {
        broadcastReceiver = new BroadcastReceiver()

        {
            @Override
            public void onReceive(Context context, Intent intent) {
                int type = intent.getIntExtra("type", 2);
                if (type == 1) {
                    toast("充值成功");
                    Intent intent1 = new Intent();
                    UserWalletRechargeActivity.this.setResult(1, intent1);
                    finish();
                } else if (type == 3) {
                    toast("取消充值");
                } else {
                    toast("支付失败");
                }
            }
        };
        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction("wxplay");
        registerReceiver(broadcastReceiver, intentToReceiveFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

}
