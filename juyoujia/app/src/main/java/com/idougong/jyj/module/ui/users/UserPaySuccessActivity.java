package com.idougong.jyj.module.ui.users;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.net.EnvConstant;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.AfterpayAdvertiseBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.UserOrderBean;
import com.idougong.jyj.model.UserRechargeWxBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.idougong.jyj.module.adapter.UserPaySuccessAdapter;
import com.idougong.jyj.module.contract.UserPaySuccessContract;
import com.idougong.jyj.module.presenter.UserPaySuccessPresenter;
import com.idougong.jyj.module.ui.home.UserShoppingCarConfirmActivity;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.AudioUtils;
import com.idougong.jyj.utils.TargetClick;
import com.idougong.jyj.widget.SpaceItemDecoration;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wang.avi.AVLoadingIndicatorView;


import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import sign.AuthResult;
import sign.PayResult;

public class UserPaySuccessActivity extends BaseActivity<UserPaySuccessPresenter> implements UserPaySuccessContract.View {
    private static final String TAG = "UserPaySuccessActivity";
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView iv_right;
    @BindView(R.id.rv_afterpay_advertise)
    RecyclerView recyclerView;

    @BindView(R.id.avi_pay_loading)
    AVLoadingIndicatorView aviPayLoading;
    @BindView(R.id.iv_pay_ico)
    ImageView ivPayIco;
    @BindView(R.id.tv_pay_status)
    TextView tvPayStatus;
    @BindView(R.id.tv_pay_ts)
    TextView tvPayTs;
    @BindView(R.id.tv_pay_again)
    TextView tvPayAgain;
    @BindView(R.id.tv_pay_order_look)
    TextView tvPayOrderLook;

    UserPaySuccessAdapter userPaySuccessAdapter;

    //微信支付
    private IWXAPI api;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    int payType = -1;
    String orderNo;
    int goPay;
    PayMethodBean payMethodBean;
    UserWalletInfoBean userWalletInfoBeanbd;
    private double mtotalPrice = 0.00;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_pay_success;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        ActivityCollector.addActivity(this);
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        iv_right.setVisibility(View.GONE);
        toolbarTitle.setText("支付结果");
        getPresenter().getAfterpayAdvertise();
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(EnvConstant.WX_KEY);
        addWxPlayReceiver();
        payType = getIntent().getIntExtra("payType", 0);
        orderNo = getIntent().getStringExtra("orderNo");
        mtotalPrice = getIntent().getDoubleExtra("mtotalPrice", 0.00);
        if (payType == 0) {
            getPresenter().addUserOrderPayAli(orderNo);
        } else if (payType == 1) {
            getPresenter().addUserOrderPayWx(orderNo);
        } else if (payType == 2) {
            getPresenter().addUserOrderPayWallet(orderNo);
        }
        userPaySuccessAdapter = new UserPaySuccessAdapter(R.layout.item_afterpayadvertise);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(15, 40));
        recyclerView.setAdapter(userPaySuccessAdapter);
        userPaySuccessAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int index) {
                if (EmptyUtils.isEmpty(userPaySuccessAdapter.getItem(index).getTarget())) {
                    return;
                }
                if (userPaySuccessAdapter.getItem(index).isNeedLogin() && login == null) {
                    openLogin();
                } else {
                    TargetClick.targetOnClick(getBaseContext(), userPaySuccessAdapter.getItem(index).getTarget());
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });
        getPresenter().getUserWalletInfo();
        getPresenter().getMethodOfPayment();
    }

    @OnClick({R.id.tv_pay_again,
            R.id.tv_pay_order_look
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_pay_again:
                if (EmptyUtils.isEmpty(payMethodBean)) {
                    getPresenter().getMethodOfPayment();
                    return;
                }
                new PayWayView(getBaseContext(), tvPayAgain);
                break;
            case R.id.tv_pay_order_look:
                Intent intent = new Intent(getBaseContext(), UserOrderDetailedActivity.class);
                intent.putExtra("orderNo", orderNo);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (goPay == 1) {
                    goPay = 0;
                    getPresenter().getUserOrderInfoResult(orderNo);
                }
            }
        }, 3000);//2秒后执行Runnable中的run方
    }

    @Override
    protected void onPause() {
        super.onPause();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goPay = 1;
            }
        }, 3000);//2秒后执行Runnable中的run方
    }

    @Override
    public void setAfterpayAdvertiseResult(final List<AfterpayAdvertiseBean> advertiseResult) {
        userPaySuccessAdapter.addData(advertiseResult);
        userPaySuccessAdapter.notifyDataSetChanged();
    }

    @Override
    public void addUserOrderPayWalletResult(String str) {
        hiddenLoading();
        toolbarTitle.setText("支付成功");
        ivPayIco.setVisibility(View.VISIBLE);
        aviPayLoading.setVisibility(View.GONE);
        tvPayStatus.setText("订单支付成功！");
        ivPayIco.setImageResource(R.mipmap.paysuccess);
        tvPayTs.setVisibility(View.GONE);
        tvPayAgain.setVisibility(View.GONE);
        Intent intent1 = new Intent("orderRefresh");
        sendBroadcast(intent1);
    }

    @Override
    public void addUserOrderPayWxResult(UserRechargeWxBean userWxPayBean) {
        hiddenLoading();
        if (userWxPayBean != null) {
            PayReq req = new PayReq();
            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
            req.appId = userWxPayBean.getAppId();
            req.partnerId = EnvConstant.WX_PARTNERID;
            req.prepayId = userWxPayBean.getPrypayId();
            req.nonceStr = userWxPayBean.getNonceStr();
            req.timeStamp = userWxPayBean.getTimeStamp();
            req.packageValue = userWxPayBean.getPackageX();
            req.sign = userWxPayBean.getPaySign();
            api.sendReq(req);
        } else {
            toast("微信支付信息生成失败");
        }
    }

    @Override
    public void addUserOrderPayAliResult(String str) {
        hiddenLoading();
        if (str != null) {
            pay(str);
        } else {
            toast("支付宝支付信息生成失败");
        }
    }

    @Override
    public void setUserOrderInfoResult(List<UserOrderBean> userOrderListResult) {
        if (!EmptyUtils.isEmpty(userOrderListResult) && userOrderListResult.size() > 0) {
            UserOrderBean userorder = userOrderListResult.get(0);
            if (userorder.getStatus() == 1) {
                toolbarTitle.setText("支付失败");
                aviPayLoading.setVisibility(View.GONE);
                tvPayStatus.setText("订单支付失败！");
                ivPayIco.setVisibility(View.VISIBLE);
                ivPayIco.setImageResource(R.mipmap.pay_failure);
            } else if (userorder.getStatus() == 2) {
                toolbarTitle.setText("支付成功");
                aviPayLoading.setVisibility(View.GONE);
                tvPayStatus.setText("订单支付成功！");
                ivPayIco.setImageResource(R.mipmap.paysuccess);
                tvPayTs.setVisibility(View.GONE);
                ivPayIco.setVisibility(View.VISIBLE);
                tvPayAgain.setVisibility(View.GONE);
                Intent intent1 = new Intent("orderRefresh");
                sendBroadcast(intent1);
            }
        }
    }

    @Override
    public void setMethodOfPayment(PayMethodBean payMethodBean) {
        this.payMethodBean = payMethodBean;
    }

    @Override
    public void setUserWalletInfoResult(UserWalletInfoBean userWalletInfoBean) {
        userWalletInfoBeanbd = userWalletInfoBean;
    }

    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            toolbarTitle.setText("支付失败");
            aviPayLoading.setVisibility(View.GONE);
            tvPayStatus.setText("订单支付失败！");
            ivPayIco.setVisibility(View.VISIBLE);
            ivPayIco.setImageResource(R.mipmap.pay_failure);
        } else {
            openLogin();
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
    public void showErrorMsg(String msg, int code) {
        super.showErrorMsg(msg, code);
        hiddenLoading();
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


    BroadcastReceiver broadcastReceiver;

    private void addWxPlayReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int type = intent.getIntExtra("type", 2);
                goPay = 0;
                if (type == 1) {
                    toolbarTitle.setText("支付成功");
                    aviPayLoading.setVisibility(View.GONE);
                    tvPayStatus.setText("订单支付成功！");
                    ivPayIco.setImageResource(R.mipmap.paysuccess);
                    tvPayTs.setVisibility(View.GONE);
                    ivPayIco.setVisibility(View.VISIBLE);
                    tvPayAgain.setVisibility(View.GONE);
                    Intent intent1 = new Intent("orderRefresh");
                    sendBroadcast(intent1);
                } else {
                    toolbarTitle.setText("支付失败");
                    aviPayLoading.setVisibility(View.GONE);
                    tvPayStatus.setText("订单支付失败！");
                    ivPayIco.setVisibility(View.VISIBLE);
                    ivPayIco.setImageResource(R.mipmap.pay_failure);
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


    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(final String ranking) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(UserPaySuccessActivity.this);
                String rankings = ranking;
                if (rankings == null) {
                } else {
                    Map<String, String> result = alipay.payV2(rankings, true);
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }


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
                    goPay = 0;
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        toolbarTitle.setText("支付成功");
                        ivPayIco.setImageResource(R.mipmap.paysuccess);
                        aviPayLoading.setVisibility(View.GONE);
                        tvPayStatus.setText("订单支付成功！");
                        tvPayTs.setVisibility(View.GONE);
                        tvPayAgain.setVisibility(View.GONE);
                        ivPayIco.setVisibility(View.VISIBLE);
                        Intent intent1 = new Intent("orderRefresh");
                        sendBroadcast(intent1);
                    } else {
                        toolbarTitle.setText("支付失败");
                        aviPayLoading.setVisibility(View.GONE);
                        ivPayIco.setVisibility(View.VISIBLE);
                        tvPayStatus.setText("订单支付失败！");
                        ivPayIco.setImageResource(R.mipmap.pay_failure);
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


    public class PayWayView extends PopupWindow {

        public PayWayView(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.view_pay_way, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));
            // LinearLayout ll_popup = (LinearLayout) view
            // .findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
            // R.anim.push_bottom_in_1));

            setWidth(ActionBar.LayoutParams.FILL_PARENT);
            setHeight(ActionBar.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            TextView tv_pay_money = (TextView) view.findViewById(R.id.tv_pay_money);
            TextView tv_pay_integral = (TextView) view.findViewById(R.id.tv_pay_integral);
            LinearLayout lin_pay_wx = (LinearLayout) view.findViewById(R.id.lin_pay_wx);
            LinearLayout lin_pay_zfb = (LinearLayout) view.findViewById(R.id.lin_pay_zfb);
            LinearLayout lin_pay_wallet = (LinearLayout) view.findViewById(R.id.lin_pay_wallet);
            LinearLayout ll_popup = view.findViewById(R.id.ll_popup);
            View view_pay = view.findViewById(R.id.view_pay);
            final ImageView iv_wx_select = (ImageView) view.findViewById(R.id.iv_wx_select);
            final ImageView iv_zfb_select = (ImageView) view.findViewById(R.id.iv_zfb_select);
            final ImageView iv_xj_select = (ImageView) view.findViewById(R.id.iv_xj_select);
            ImageView iv_wallet_money = (ImageView) view.findViewById(R.id.iv_wallet_money);
            TextView tv_wallet_money = (TextView) view.findViewById(R.id.tv_wallet_money);
            if (EmptyUtils.isEmpty(payMethodBean)) {
                lin_pay_wx.setVisibility(View.GONE);
                lin_pay_zfb.setVisibility(View.GONE);
            } else {
                if (payMethodBean.isAliPay()) {
                    lin_pay_zfb.setVisibility(View.VISIBLE);
                } else {
                    lin_pay_zfb.setVisibility(View.GONE);
                }
                if (payMethodBean.isWxPay()) {
                    lin_pay_wx.setVisibility(View.VISIBLE);
                } else {
                    lin_pay_wx.setVisibility(View.GONE);
                }
            }
            tv_pay_money.setText("￥" + String.format("%.2f", mtotalPrice));
            if (!EmptyUtils.isEmpty(userWalletInfoBeanbd)) {
                lin_pay_wallet.setVisibility(View.VISIBLE);
                if (userWalletInfoBeanbd.getBalance() < mtotalPrice) {
                    iv_wallet_money.setImageResource(R.mipmap.walletpayno);
                    lin_pay_wallet.setEnabled(false);
                    lin_pay_wallet.setClickable(false); //设置不可点击
                    tv_wallet_money.setTextColor(getResources().getColor(R.color.tabwd));
                } else {
                    iv_wallet_money.setImageResource(R.mipmap.walletpay);
                    lin_pay_wallet.setEnabled(true);
                    lin_pay_wallet.setClickable(true); //设置不可点击
                    tv_wallet_money.setTextColor(getResources().getColor(R.color.black));
                }
                tv_wallet_money.setText("余额支付(余额:" + String.format("%.2f", userWalletInfoBeanbd.getBalance()) + ")");
            } else {
                lin_pay_wallet.setVisibility(View.GONE);
            }

            lin_pay_wx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_wx_select.setImageResource(R.mipmap.zfyx);
                    iv_zfb_select.setImageResource(R.mipmap.zfwx);
                    iv_xj_select.setImageResource(R.mipmap.zfwx);
                    showLoading();
                    getPresenter().addUserOrderPayWx(orderNo);
                    dismiss();
                }
            });
            lin_pay_zfb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_wx_select.setImageResource(R.mipmap.zfwx);
                    iv_zfb_select.setImageResource(R.mipmap.zfyx);
                    iv_xj_select.setImageResource(R.mipmap.zfwx);
                    showLoading();
                    getPresenter().addUserOrderPayAli(orderNo);
                    dismiss();
                }
            });
            lin_pay_wallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_wx_select.setImageResource(R.mipmap.zfwx);
                    iv_zfb_select.setImageResource(R.mipmap.zfwx);
                    iv_xj_select.setImageResource(R.mipmap.zfyx);
                    showLoading();
                    getPresenter().addUserOrderPayWallet(orderNo);
                    dismiss();
                }
            });

            view_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            ll_popup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
