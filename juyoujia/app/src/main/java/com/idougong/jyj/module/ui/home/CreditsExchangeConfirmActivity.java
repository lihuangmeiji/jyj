package com.idougong.jyj.module.ui.home;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.net.EnvConstant;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.CreditsExchangePayBean;
import com.idougong.jyj.model.DeliveryAddressBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.OnlineSupermarketOrderBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.idougong.jyj.module.contract.CreditsExchangeConfirmContract;
import com.idougong.jyj.module.presenter.CreditsExchangeConfirmPresenter;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.TextUtil;
import com.idougong.jyj.widget.RoundImageView1;
import com.idougong.jyj.widget.UserPromptPopupWindow;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import sign.AuthResult;
import sign.PayResult;

public class CreditsExchangeConfirmActivity extends BaseActivity<CreditsExchangeConfirmPresenter> implements CreditsExchangeConfirmContract.View, UserPromptPopupWindow.UserPromptPopupWindowInterface {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView iv_right;
    @BindView(R.id.iv_sh_ico)
    RoundImageView1 ivShIco;
    @BindView(R.id.tv_sh_name)
    TextView tvShName;
    @BindView(R.id.tv_sh_point)
    TextView tvShPoint;
    int shoppingId = 0;
    @BindView(R.id.lin_type1)
    LinearLayout linType1;
    @BindView(R.id.lin_type2)
    LinearLayout linType2;
    @BindView(R.id.lin_type3)
    LinearLayout linType3;
    @BindView(R.id.iv_save_qr_code)
    ImageView ivSaveQrCode;
    @BindView(R.id.et_user_phone)
    EditText etUserPhone;
    @BindView(R.id.vs_showerror)
    ViewStub vs_showerror;
    @BindView(R.id.sv_confirm_order)
    ScrollView svConfirmOrder;
    @BindView(R.id.et_user_sh_phone)
    EditText etUserShPhone;
    @BindView(R.id.et_user_sh_name)
    EditText etUserShName;
    @BindView(R.id.et_user_sh_address)
    EditText etUserShAddress;
    @BindView(R.id.tv_ce_confirm)
    TextView tvCeConfirm;
    @BindView(R.id.type1_et_product_detail)
    EditText productDetail1;
    @BindView(R.id.type1_separator)
    View separator;
    List<OnlineSupermarketOrderBean> onlineSupermarketOrderBeanList;
    int operation = 0;
    int type = 0;
    HomeShoppingSpreeBean homeShoppingSpreeBean;
    //微信支付
    private IWXAPI api;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    PayMethodBean payMethodBean;
    UserWalletInfoBean userWalletInfoBeanbd;
    String orderNo;
    String qrcode;

    @Override
    protected int getContentView() {
        return R.layout.activity_credits_exchange_confirm;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        onlineSupermarketOrderBeanList = new ArrayList<>();
        commonTheme();
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("确认信息");
        toolbarTitle.setTextColor(getBaseContext().getResources().getColor(R.color.white));
        toolbar.setBackgroundColor(getResources().getColor(R.color.color49));
        toolbar.setNavigationIcon(R.mipmap.ic_return_video);
        iv_right.setVisibility(View.GONE);
        shoppingId = getIntent().getIntExtra("shoppingId", 0);
        operation = 1;
        getPresenter().getShoppingDetailedResult(shoppingId);
        getPresenter().getUserDeliveryAddressResult();
        getPresenter().getMethodOfPayment();
        if (login != null) {
            etUserPhone.setText(login.getPhone());
        }
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(EnvConstant.WX_KEY);
        addWxPlayReceiver();
		getPresenter().getUserWalletInfo();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EmptyUtils.isEmpty(orderNo)) {
                    finish();
                } else {
                    UserPromptPopupWindow userPromptPopupWindow = new UserPromptPopupWindow(getBaseContext(), toolbar, "取消订单", "是否放弃当前订单？", 0);
                    userPromptPopupWindow.setUserPromptPopupWindowInterface(CreditsExchangeConfirmActivity.this);
                }
            }
        });
    }

    @OnClick({R.id.toolbar,
            R.id.tv_ce_confirm
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:

                break;
            case R.id.tv_ce_confirm:
                operation = 2;
                Gson gson = new Gson();
                if (type == 1) {
                    if (EmptyUtils.isEmpty(etUserShPhone.getText().toString().trim())) {
                        toast("请输入收货人手机号");
                        return;
                    }
                    if (!isMobile(etUserShPhone.getText().toString().trim())) {
                        toast("手机号格式不正确");
                        return;
                    }
                    if (EmptyUtils.isEmpty(etUserShAddress.getText().toString().trim())) {
                        toast("请输入收货人地址");
                        return;
                    }
                    if (EmptyUtils.isEmpty(etUserShName.getText().toString().trim())) {
                        toast("请输入收货人姓名");
                        return;
                    }
                    if (!EmptyUtils.isEmpty(homeShoppingSpreeBean.getCurrentPrice()) && homeShoppingSpreeBean.getCurrentPrice() > 0) {
                        new PayWayView(getBaseContext(), tvShName);
                    } else {
                        getPresenter().addUserMarketCreateOrderResult(etUserShPhone.getText().toString().trim(), gson.toJson(onlineSupermarketOrderBeanList), etUserShName.getText().toString().trim(), etUserShAddress.getText().toString().trim(), orderNo, null, productDetail1.getText().toString().trim());
                    }
                } else if (type == 2) {
                    if (EmptyUtils.isEmpty(etUserPhone.getText().toString().trim())) {
                        toast("请输入联系人手机号");
                        return;
                    }
                    if (!isMobile(etUserPhone.getText().toString().trim())) {
                        toast("手机号格式不正确");
                        return;
                    }
                    if (!EmptyUtils.isEmpty(homeShoppingSpreeBean.getCurrentPrice()) && homeShoppingSpreeBean.getCurrentPrice() > 0) {
                        new PayWayView(getBaseContext(), tvShName);
                    } else {
                        getPresenter().addUserMarketCreateOrderResult(etUserPhone.getText().toString().trim(), gson.toJson(onlineSupermarketOrderBeanList), null, null, orderNo, null,null);
                    }
                } else if (type == 3) {
                    if (!EmptyUtils.isEmpty(homeShoppingSpreeBean.getCurrentPrice()) && homeShoppingSpreeBean.getCurrentPrice() > 0) {
                        new PayWayView(getBaseContext(), tvShName);
                    } else {
                        getPresenter().addUserMarketCreateOrderResult(null, gson.toJson(onlineSupermarketOrderBeanList), null, null, orderNo, null,null);
                    }
                }
                break;
        }
    }


    @Override
    public void toast(String msg) {
        super.toast(msg);
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
        tvCeConfirm.setEnabled(true);
        tvCeConfirm.setSelected(true);
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
        }  else if (code == 405) {
            svConfirmOrder.setVisibility(View.GONE);
            vs_showerror.setLayoutResource(R.layout.layout_no_network);
            //vs_showerror.inflate();
            vs_showerror.setVisibility(View.VISIBLE);
            LinearLayout lin_load = (LinearLayout) findViewById(R.id.lin_load);
            lin_load.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPresenter().getShoppingDetailedResult(shoppingId);
                }
            });
        } else if (code == -10) {
            return;
        }
        toast(msg);
    }

    @Override
    public void setShoppingDetailedResult(HomeShoppingSpreeBean homeShoppingSpreeBean) {
        if (homeShoppingSpreeBean != null) {
            this.homeShoppingSpreeBean = homeShoppingSpreeBean;
            Glide.with(getBaseContext()).load(homeShoppingSpreeBean.getImage()).into(ivShIco);
            tvShName.setText(homeShoppingSpreeBean.getName());
            //tvShPoint.setText(TextUtil.FontHighlighting(getBaseContext(), homeShoppingSpreeBean.getPoint() + "", "积分", R.style.tvShPoint2, R.style.tvShPoint1));
            if (!EmptyUtils.isEmpty(homeShoppingSpreeBean.getCurrentPrice()) && homeShoppingSpreeBean.getCurrentPrice() > 0) {
                if (homeShoppingSpreeBean.getPoint() > 0) {
                    tvShPoint.setText(TextUtil.FontHighlighting(getBaseContext(), "¥", "" + homeShoppingSpreeBean.getCurrentPrice(), "+" + homeShoppingSpreeBean.getPoint() + "积分", R.style.tv_ce_point1, R.style.tv_ce_point1, R.style.tv_ce_point1));
                } else {
                    tvShPoint.setText(TextUtil.FontHighlighting(getBaseContext(), "¥", "" + homeShoppingSpreeBean.getCurrentPrice(), R.style.tv_ce_point1, R.style.tv_ce_point1));
                }
            } else {
                if (homeShoppingSpreeBean.getPoint() > 0) {
                    tvShPoint.setText(TextUtil.FontHighlighting(getBaseContext(), homeShoppingSpreeBean.getPoint() + "", "积分", R.style.tv_ce_point1, R.style.tv_ce_point1));
                }
            }
            OnlineSupermarketOrderBean onlineSupermarketOrderBean = new OnlineSupermarketOrderBean();
            onlineSupermarketOrderBean.setNum(1);
            onlineSupermarketOrderBean.setProductId(homeShoppingSpreeBean.getId());
            onlineSupermarketOrderBeanList.add(onlineSupermarketOrderBean);
            if (homeShoppingSpreeBean.getType() == 1) {
                type = 1;
                linType1.setVisibility(View.VISIBLE);
                linType2.setVisibility(View.GONE);
                linType3.setVisibility(View.GONE);
                if (homeShoppingSpreeBean.isDescDisplay()){
                    productDetail1.setVisibility(View.VISIBLE);
                    separator.setVisibility(View.VISIBLE);
                }
            } else if (homeShoppingSpreeBean.getType() == 2) {
                type = 2;
                linType1.setVisibility(View.GONE);
                linType2.setVisibility(View.VISIBLE);
                linType3.setVisibility(View.GONE);
            } else if (homeShoppingSpreeBean.getType() == 3) {
                type = 3;
            }
        } else {
            toast("数据加载失败");
        }
    }

    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            if (operation == 1) {
                getPresenter().getShoppingDetailedResult(shoppingId);
            } else {
                toast("服务未响应，请重新操作！");
            }
        } else {
            openLogin();
        }
    }

    @Override
    public void getUserMarketCreateOrderResult(CreditsExchangePayBean creditsExchangePayBean, String payType) {
     /*   hiddenLoading();
        tvCeConfirm.setEnabled(true);
        tvCeConfirm.setSelected(true);
        if (EmptyUtils.isEmpty(creditsExchangePayBean)) {
            toast("兑换失败!");
        } else {
            if (EmptyUtils.isEmpty(payType)) {
                Intent intent = new Intent(getBaseContext(), CreditsExchangeSuccessActivity.class);
                intent.putExtra("name", homeShoppingSpreeBean.getName());
                intent.putExtra("image", homeShoppingSpreeBean.getImage());
                intent.putExtra("point", homeShoppingSpreeBean.getPoint());
                intent.putExtra("currentPrice", homeShoppingSpreeBean.getCurrentPrice());
                intent.putExtra("type", type);
                startActivity(intent);
                finish();
            } else {
                orderNo = creditsExchangePayBean.getOrderNo();
                if (payType.trim().equals("0")) {
                    if (creditsExchangePayBean.getAliCallback() != null) {
                        pay(creditsExchangePayBean.getAliCallback());
                    } else {
                        toast("支付宝支付信息生成失败");
                    }
                } else if (payType.trim().equals("1")) {
                    if (creditsExchangePayBean.getPayResponse() != null) {
                        PayReq req = new PayReq();
                        //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                        req.appId = creditsExchangePayBean.getPayResponse().getAppId();
                        req.partnerId = "1528772321";
                        req.prepayId = creditsExchangePayBean.getPayResponse().getPrypayId();
                        req.nonceStr = creditsExchangePayBean.getPayResponse().getNonceStr();
                        req.timeStamp = creditsExchangePayBean.getPayResponse().getTimeStamp();
                        req.packageValue = creditsExchangePayBean.getPayResponse().getPackageX();
                        req.sign = creditsExchangePayBean.getPayResponse().getPaySign();
                        api.sendReq(req);
                    } else {
                        toast("微信支付信息生成失败");
                    }
                } else if (payType.trim().equals("2")) {
                    Intent intent = new Intent(getBaseContext(), CreditsExchangeSuccessActivity.class);
                    intent.putExtra("name", homeShoppingSpreeBean.getName());
                    intent.putExtra("image", homeShoppingSpreeBean.getImage());
                    intent.putExtra("point", homeShoppingSpreeBean.getPoint());
                    intent.putExtra("currentPrice", homeShoppingSpreeBean.getCurrentPrice());
                    intent.putExtra("type", type);
                    startActivity(intent);
                    finish();
                }
            }
        }*/
    }

    @Override
    public void setUserDeliveryAddressResult(DeliveryAddressBean userDeliveryAddressResult) {
        if (!EmptyUtils.isEmpty(userDeliveryAddressResult)) {
            etUserShAddress.setText(userDeliveryAddressResult.getDetailsAddress());
            etUserShPhone.setText(userDeliveryAddressResult.getPhone());
            etUserShName.setText(userDeliveryAddressResult.getName());
        }
    }

    @Override
    public void setMethodOfPayment(PayMethodBean payMethodBean) {
        this.payMethodBean = payMethodBean;
    }

    @Override
    public void setUserMarketOrderCancelResult(String str) {
        hiddenLoading();
        finish();
    }

    @Override
    public void setUserWalletInfoResult(UserWalletInfoBean userWalletInfoBean) {
        userWalletInfoBeanbd = userWalletInfoBean;
    }

    @Override
    protected void onPause() {
        super.onPause();
        operation = 0;
    }

    @Override
    public void setCancelOnClickListener() {

    }

    @Override
    public void setConfirmOnClickListener() {
        showLoading();
        getPresenter().getUserMarketOrderCancelResult(orderNo);
    }


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
            tv_pay_money.setText("￥" + homeShoppingSpreeBean.getCurrentPrice());
            if (!EmptyUtils.isEmpty(userWalletInfoBeanbd)) {
                lin_pay_wallet.setVisibility(View.VISIBLE);
                if (userWalletInfoBeanbd.getBalance() < homeShoppingSpreeBean.getCurrentPrice()) {
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
                    Gson gson = new Gson();
                    if (type == 1) {
                        getPresenter().addUserMarketCreateOrderResult(etUserShPhone.getText().toString().trim(), gson.toJson(onlineSupermarketOrderBeanList), etUserShName.getText().toString().trim(), etUserShAddress.getText().toString().trim(), orderNo, "1",productDetail1.getText().toString().trim());
                    } else if (type == 2) {
                        getPresenter().addUserMarketCreateOrderResult(etUserPhone.getText().toString().trim(), gson.toJson(onlineSupermarketOrderBeanList), null, null, orderNo, "1",null);
                    } else if (type == 3) {
                        getPresenter().addUserMarketCreateOrderResult(null, gson.toJson(onlineSupermarketOrderBeanList), null, null, orderNo, "1",null);
                    }
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
                    Gson gson = new Gson();
                    if (type == 1) {
                        getPresenter().addUserMarketCreateOrderResult(etUserShPhone.getText().toString().trim(), gson.toJson(onlineSupermarketOrderBeanList), etUserShName.getText().toString().trim(), etUserShAddress.getText().toString().trim(), orderNo, "0",productDetail1.getText().toString().trim());
                    } else if (type == 2) {
                        getPresenter().addUserMarketCreateOrderResult(etUserPhone.getText().toString().trim(), gson.toJson(onlineSupermarketOrderBeanList), null, null, orderNo, "0",null);
                    } else if (type == 3) {
                        getPresenter().addUserMarketCreateOrderResult(null, gson.toJson(onlineSupermarketOrderBeanList), null, null, orderNo, "0",null);
                    }
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
                    Gson gson = new Gson();
                    if (type == 1) {
                        getPresenter().addUserMarketCreateOrderResult(etUserShPhone.getText().toString().trim(), gson.toJson(onlineSupermarketOrderBeanList), etUserShName.getText().toString().trim(), etUserShAddress.getText().toString().trim(), orderNo, "2",null);
                    } else if (type == 2) {
                        getPresenter().addUserMarketCreateOrderResult(etUserPhone.getText().toString().trim(), gson.toJson(onlineSupermarketOrderBeanList), null, null, orderNo, "2",null);
                    } else if (type == 3) {
                        getPresenter().addUserMarketCreateOrderResult(null, gson.toJson(onlineSupermarketOrderBeanList), null, null, orderNo, "2",null);
                    }
                    dismiss();
                }
            });

            view_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvCeConfirm.setEnabled(true);
                    tvCeConfirm.setSelected(true);
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


    BroadcastReceiver broadcastReceiver;

    private void addWxPlayReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int type = intent.getIntExtra("type", 2);
                if (type == 1) {
                    intent = new Intent(getBaseContext(), CreditsExchangeSuccessActivity.class);
                    intent.putExtra("name", homeShoppingSpreeBean.getName());
                    intent.putExtra("image", homeShoppingSpreeBean.getImage());
                    intent.putExtra("point", homeShoppingSpreeBean.getPoint());
                    intent.putExtra("currentPrice", homeShoppingSpreeBean.getCurrentPrice());
                    intent.putExtra("type", type);
                    intent.putExtra("qrcode", qrcode);
                    startActivity(intent);
                    finish();
                } else if (type == 3) {
                    toast("取消支付");
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


    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(final String ranking) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(CreditsExchangeConfirmActivity.this);
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

                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Intent intent = new Intent(getBaseContext(), CreditsExchangeSuccessActivity.class);
                        intent.putExtra("name", homeShoppingSpreeBean.getName());
                        intent.putExtra("image", homeShoppingSpreeBean.getImage());
                        intent.putExtra("point", homeShoppingSpreeBean.getPoint());
                        intent.putExtra("currentPrice", homeShoppingSpreeBean.getCurrentPrice());
                        intent.putExtra("type", type);
                        intent.putExtra("qrcode", qrcode);
                        startActivity(intent);
                        finish();
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        toast("取消支付");
                    } else if (TextUtils.equals(resultStatus, "4000")) {
                        toast("支付失败");
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
}
