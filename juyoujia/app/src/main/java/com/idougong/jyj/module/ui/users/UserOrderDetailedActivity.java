package com.idougong.jyj.module.ui.users;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.UserOrderBean;
import com.idougong.jyj.model.UserRechargeWxBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.idougong.jyj.module.adapter.UserOrderVoListAdapter;
import com.idougong.jyj.module.contract.UserOrderDetailedContract;
import com.idougong.jyj.module.presenter.UserOrderDetailedPresenter;
import com.idougong.jyj.module.ui.home.UserShoppingCarConfirmActivity;
import com.idougong.jyj.utils.ActivityCollector;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class UserOrderDetailedActivity extends BaseActivity<UserOrderDetailedPresenter> implements UserOrderDetailedContract.View {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView iv_right;
    @BindView(R.id.order_detail_status)
    TextView order_status;
    @BindView(R.id.rv_user_order_detail)
    RecyclerView userorderdetail;
    @BindView(R.id.tv_order_detail_totalprice)
    TextView totalPrice;
    @BindView(R.id.tv_delivery_price)
    TextView deliveryPrice;
    @BindView(R.id.tv_order_detail_id)
    TextView tvOrderNo;
    @BindView(R.id.button_copyid)
    TextView copyId;
    @BindView(R.id.tv_order_detail_time)
    TextView orderTime;
    @BindView(R.id.tv_order_delivery_time)
    TextView tvOrderDeliveryTime;
    @BindView(R.id.tv_order_detail_paytype)
    TextView payType;
    @BindView(R.id.ll_order_deliverinfo)
    LinearLayout orderDeliveryInfo;
    @BindView(R.id.tv_uo_operation1)
    TextView tvOrderOperation1;
    @BindView(R.id.tv_order_detail_description)
    TextView tvOrderDeliveryDescription;
    @BindView(R.id.tv_discounts_price)
    TextView tvDiscountsPrice;
    @BindView(R.id.tv_pay_price)
    TextView tvPayPrice;
    @BindView(R.id.tv_order_pay_time)
    TextView tvOrderPayTime;
    @BindView(R.id.tv_order_cancel_time)
    TextView tvOrderCancelTime;
    @BindView(R.id.rel_order_status)
    RelativeLayout relOrderStatus;
    int orderId;
    String orderNo;
    CountDownTimer timer;
    private double mtotalPrice = 0.00;
    int operation = 0;
    PayMethodBean payMethodBean;
    UserWalletInfoBean userWalletInfoBeanbd;

    @Override
    protected int getContentView() {
        return R.layout.activity_userorder_detailed;
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("订单详情");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        orderNo = getIntent().getStringExtra("orderNo");
        getPresenter().getUserOrderInfoResult(orderNo);
        getPresenter().getMethodOfPayment();
        getPresenter().getUserWalletInfo();
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }


    @OnClick({R.id.button_copyid, R.id.tv_uo_operation1
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_copyid:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("orderNo", tvOrderNo.getText().toString());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                toast("复制成功");
                break;
            case R.id.tv_uo_operation1:
                if (operation == 1) {
                    if(EmptyUtils.isEmpty(payMethodBean)){
                        getPresenter().getMethodOfPayment();
                        return;
                    }
                    new PayWayView(getBaseContext(), tvOrderNo);
                } else if (operation == 2) {
                    new AlertDialog.Builder(this)
                            .setMessage("确定要取消当前订单吗?")
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    getPresenter().getUserCancelOrderResult(orderId);
                                }
                            })
                            .show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getPresenter().getUserOrderInfoResult(orderNo);
        getPresenter().getMethodOfPayment();
        getPresenter().getUserWalletInfo();
    }

    @Override
    public void setUserCancelOrderResult(String str) {
        relOrderStatus.setVisibility(View.GONE);
        Intent intent = new Intent("orderRefresh");
        sendBroadcast(intent);
        toast("已取消当前订单！");
    }

    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            getPresenter().getUserOrderInfoResult(orderNo);
            getPresenter().getMethodOfPayment();
            getPresenter().getUserWalletInfo();
        } else {
            openLogin();
        }
    }

    @Override
    public void setUserOrderInfoResult(List<UserOrderBean> userOrderListResult) {
        if (!EmptyUtils.isEmpty(userOrderListResult) && userOrderListResult.size() > 0) {
            UserOrderBean userorder = userOrderListResult.get(0);
            orderId = userorder.getId();
            //判读订单状态
            switch (userorder.getStatus()) {
                case 1:
                    order_status.setText("未支付");
                    relOrderStatus.setVisibility(View.VISIBLE);
                    tvOrderOperation1.setBackgroundResource(R.drawable.bg_shape_ellipse);
                    tvOrderOperation1.setTextColor(getBaseContext().getResources().getColor(R.color.white));
                    tvOrderOperation1.setText("去支付");
                    operation = 1;
                   /* Date newdata = new Date();
                    if (userorder.getUpdateAt() != null) {

                        Date d1 = TimeUtils.string2Date(userorder.getUpdateAt());
                        long fmin = 5 * 60 * 1000;
                        long diff = TimeUtils.date2Millis(d1) + fmin - TimeUtils.date2Millis(newdata);
                        timer = new CountDownTimer(diff, 1000) {
                            @Override
                            public void onTick(long sin) {
                                long days = sin / (1000 * 60 * 60 * 24);
                                long hours = (sin - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60); //获取时
                                long minutes = (sin - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);  //获取分钟
                                long s = (sin / 1000 - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60);//获取秒
                                //Log.i("showTime", "setPosts: " + days + "天" + hours + "时" + minutes + "分" + s + "秒");
                                StringBuffer scid = new StringBuffer();
                                scid.append("去支付（剩余");

                                if (hours > 0) {
                                    if (hours < 10) {
                                        scid.append("0" + hours);
                                    } else {
                                        scid.append(hours);
                                    }
                                }
                                if (minutes < 10) {
                                    scid.append("0" + minutes);
                                } else {
                                    scid.append(minutes);
                                }
                                scid.append(":");
                                if (s < 10) {
                                    scid.append("0" + s);
                                } else {
                                    scid.append("" + s);
                                }
                                scid.append("）");
                                tvOrderOperation1.setText(scid.toString());
                            }

                            @Override
                            public void onFinish() {
                                tvOrderOperation1.setText("00:00");
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        order_status.setText("已取消");
                                        relOrderStatus.setVisibility(View.GONE);
                                        getPresenter().getUserOrderInfoResult(orderNo);
                                    }
                                }, 1000);//3秒后执行Runnable中的run方
                            }
                        };
                        timer.start();
                    }*/
                    break;
                case 2:
                    order_status.setText("已支付");
                    break;
                case 3:
                    order_status.setText("已取消");
                    break;
                case 4:
                    order_status.setText("已发货");
                    break;
                case 5:
                    order_status.setText("已交易");
                    break;
                case 6:
                    order_status.setText("退货/退款中");
                    break;
                case 7:
                    order_status.setText("已完成");
                    break;
            }
            //如果商品为实体商品 显示配送信息
            if (userorder.getModel() == 2 && userorder.getType() == 1) {
                orderDeliveryInfo.setVisibility(View.GONE);
                if (userorder.isSalesReturn()) {
                    operation = 2;
                    tvOrderOperation1.setText("取消订单");
                    relOrderStatus.setVisibility(View.VISIBLE);
                    tvOrderOperation1.setBackgroundResource(R.drawable.bg_shape_ellipse_order);
                    tvOrderOperation1.setTextColor(getBaseContext().getResources().getColor(R.color.tabwd));
                }
                if (userorder.getDelivery() != null) {
                    StringBuffer timeBuffer = new StringBuffer();
                    if (!EmptyUtils.isEmpty(userorder.getDelivery().getTime())) {
                        timeBuffer.append("配送时间: ");
                        timeBuffer.append(TimeUtils.date2String(TimeUtils.string2Date(userorder.getDelivery().getTime()), "yyyy-MM-dd")
                                + " " + TimeUtils.date2String(TimeUtils.string2Date(userorder.getDelivery().getTime()), "HH:mm"));
                        if (!EmptyUtils.isEmpty(userorder.getDelivery().getTimeEnd())) {
                            timeBuffer.append("-" + TimeUtils.date2String(TimeUtils.string2Date(userorder.getDelivery().getTimeEnd()), "HH:mm"));
                        }
                    } else {
                        if (!EmptyUtils.isEmpty(userorder.getDelivery().getTimeEnd())) {
                            timeBuffer.append("配送时间: ");
                            timeBuffer.append(TimeUtils.date2String(TimeUtils.string2Date(userorder.getDelivery().getTimeEnd()), "HH:mm"));
                        }
                    }
                    if (EmptyUtils.isEmpty(timeBuffer.toString())) {
                        tvOrderDeliveryTime.setVisibility(View.GONE);
                    } else {
                        tvOrderDeliveryTime.setText(timeBuffer.toString());
                        tvOrderDeliveryTime.setVisibility(View.VISIBLE);
                    }

                    if (!EmptyUtils.isEmpty(userorder.getDelivery().getDescription())) {
                        tvOrderDeliveryDescription.setVisibility(View.VISIBLE);
                        tvOrderDeliveryDescription.setText("订单备注: " + userorder.getDelivery().getDescription());
                    } else {
                        tvOrderDeliveryDescription.setVisibility(View.GONE);
                    }
                } else {
                    tvOrderDeliveryTime.setVisibility(View.GONE);
                    tvOrderDeliveryDescription.setVisibility(View.GONE);
                }
            }
            UserOrderVoListAdapter userOrderVoListAdapter = new UserOrderVoListAdapter(R.layout.item_orderdetail);
            userOrderVoListAdapter.addData(userorder.getProducts());
            userorderdetail.setLayoutManager(new LinearLayoutManager(userorderdetail.getContext()));
            userorderdetail.setAdapter(userOrderVoListAdapter);
            orderNo = userorder.getOrderNo();
            //设置订单id
            tvOrderNo.setText("订单号: " + userorder.getOrderNo());
            //下单时间
            orderTime.setText("下单时间: " + userorder.getUpdateAt());
            if (!EmptyUtils.isEmpty(userorder.getPayAt())) {
                tvOrderPayTime.setVisibility(View.VISIBLE);
                tvOrderPayTime.setText("支付时间: " + userorder.getPayAt());
            } else {
                tvOrderPayTime.setVisibility(View.GONE);
            }

            if (!EmptyUtils.isEmpty(userorder.getCancelAt())) {
                tvOrderCancelTime.setVisibility(View.VISIBLE);
                tvOrderCancelTime.setText("取消时间: " + userorder.getCancelAt());
            } else {
                tvOrderCancelTime.setVisibility(View.GONE);
            }
            totalPrice.setText("¥" + String.format("%.2f", userorder.getTotalPrice()));
            tvDiscountsPrice.setText("¥" + String.format("%.2f", userorder.getUsedAmount()));
            if (!EmptyUtils.isEmpty(userorder.getFinalPrice()) && userorder.getFinalPrice() > 0) {
                mtotalPrice = userorder.getFinalPrice();
                if (userorder.getPoint() > 0) {
                    tvPayPrice.setText("¥" + userorder.getFinalPrice() + "+" + userorder.getPoint() + "积分");
                    //.payType.setText("支付方式: " + "积分+现金");
                    if (userorder.getPayType() != null) {
                        //totalPrice.setText("¥"+userorder.getFinalPrice());
                        if (userorder.getPayType().equals("0")) {
                            payType.setText("支付方式: " + "积分+支付宝支付");
                        } else if (userorder.getPayType().equals("1")) {
                            payType.setText("支付方式: " + "积分+微信支付");
                        } else if (userorder.getPayType().equals("2")) {
                            payType.setText("支付方式: " + "积分+余额支付");
                        }
                    } else {
                        payType.setVisibility(View.GONE);
                    }
                } else {
                    tvPayPrice.setText("¥" + String.format("%.2f", userorder.getFinalPrice()));
                    if (userorder.getPayType() != null) {
                        //totalPrice.setText("¥"+userorder.getFinalPrice());
                        if (userorder.getPayType().equals("0")) {
                            payType.setText("支付方式: " + "支付宝支付");
                        } else if (userorder.getPayType().equals("1")) {
                            payType.setText("支付方式: " + "微信支付");
                        } else if (userorder.getPayType().equals("2")) {
                            payType.setText("支付方式: " + "余额支付");
                        }
                    } else {
                        payType.setVisibility(View.GONE);
                    }
                }
            } else {
                if (userorder.getPoint() > 0) {
                    tvPayPrice.setText(userorder.getPoint() + "积分");
                    payType.setText("支付方式: " + "积分抵扣");
                } else {
                    payType.setVisibility(View.GONE);
                }
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
                    Intent intent1 = new Intent(UserOrderDetailedActivity.this, UserPaySuccessActivity.class);
                    intent1.putExtra("orderNo", orderNo);
                    intent1.putExtra("mtotalPrice", mtotalPrice);
                    intent1.putExtra("payType", 1);
                    startActivity(intent1);
                    dismiss();
                }
            });
            lin_pay_zfb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_wx_select.setImageResource(R.mipmap.zfwx);
                    iv_zfb_select.setImageResource(R.mipmap.zfyx);
                    iv_xj_select.setImageResource(R.mipmap.zfwx);
                    Intent intent1 = new Intent(UserOrderDetailedActivity.this, UserPaySuccessActivity.class);
                    intent1.putExtra("orderNo", orderNo);
                    intent1.putExtra("payType", 0);
                    intent1.putExtra("mtotalPrice", mtotalPrice);
                    startActivity(intent1);
                    dismiss();
                }
            });
            lin_pay_wallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_wx_select.setImageResource(R.mipmap.zfwx);
                    iv_zfb_select.setImageResource(R.mipmap.zfwx);
                    iv_xj_select.setImageResource(R.mipmap.zfyx);
                    Intent intent1 = new Intent(UserOrderDetailedActivity.this, UserPaySuccessActivity.class);
                    intent1.putExtra("orderNo", orderNo);
                    intent1.putExtra("payType", 2);
                    intent1.putExtra("mtotalPrice", mtotalPrice);
                    startActivity(intent1);
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
    public void toast(String msg) {
        super.toast(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
