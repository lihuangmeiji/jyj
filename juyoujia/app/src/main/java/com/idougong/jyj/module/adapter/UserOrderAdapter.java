package com.idougong.jyj.module.adapter;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idougong.jyj.R;
import com.idougong.jyj.model.DeliveryInfoBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.UserOrderBean;
import com.idougong.jyj.module.ui.users.UserOrderDetailedActivity;
import com.idougong.jyj.utils.TextUtil;
import com.idougong.jyj.widget.RecycleViewDivider;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by wujiajun on 2017/10/18.
 */

public class UserOrderAdapter extends BaseQuickAdapter<UserOrderBean, BaseViewHolder> {
    private UserOrderInterface userOrderInterface;

    public UserOrderInterface getUserOrderInterface() {
        return userOrderInterface;
    }

    public void setUserOrderInterface(UserOrderInterface userOrderInterface) {
        this.userOrderInterface = userOrderInterface;
    }

    public UserOrderAdapter(int layoutResId) {
        super(layoutResId);
    }

    float scrollX, scrollY;

    @Override
    protected void convert(final BaseViewHolder helper, final UserOrderBean item) {

        TextView tv_order_id = helper.getView(R.id.tv_order_id);
        tv_order_id.setText("订单编号:" + item.getOrderNo());
        ImageView iv_order_id = helper.getView(R.id.iv_order_id);
        iv_order_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userOrderInterface.UserOrderNoCopy(item.getOrderNo());
            }
        });
        TextView tv_uo_status = helper.getView(R.id.tv_uo_status);
        TextView tv_shopping_number = helper.getView(R.id.tv_shopping_number);
        RecyclerView recycler_view = helper.getView(R.id.child_recycler_view);
        helper.addOnClickListener(R.id.lin_order_goods).addOnClickListener(R.id.v_goods_ico);
       /* UserOrderVoListAdapter userOrderVoListAdapter = new UserOrderVoListAdapter(R.layout.item_confirm_order);
        userOrderVoListAdapter.addData(item.getProducts());*/
        UserShoppingIcoShowAdapter userShoppingIcoShowAdapter = new UserShoppingIcoShowAdapter(R.layout.item_shopping_ico);
        userShoppingIcoShowAdapter.setEnableLoadMore(true);
        if (item.getProducts() != null) {
            List<HomeShoppingSpreeBean> homeShoppingSpreeBeanList = new ArrayList<>();
            for (int i = 0; i < item.getProducts().size(); i++) {
                homeShoppingSpreeBeanList.add(item.getProducts().get(i).getProduct());
            }
            tv_shopping_number.setText("共" + homeShoppingSpreeBeanList.size() + "件");
            userShoppingIcoShowAdapter.setNewData(homeShoppingSpreeBeanList);
        }
        recycler_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        recycler_view.setAdapter(userShoppingIcoShowAdapter);
        userShoppingIcoShowAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(mContext, UserOrderDetailedActivity.class);
                intent.putExtra("orderNo", item.getOrderNo());
                mContext.startActivity(intent);
            }
        });
        recycler_view.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                scrollX = event.getX();
                scrollY = event.getY();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (v.getId() != 0 && Math.abs(scrollX - event.getX()) <= 5 && Math.abs(scrollY - event.getY()) <= 5) {
                    Intent intent = new Intent(mContext, UserOrderDetailedActivity.class);
                    intent.putExtra("orderNo", item.getOrderNo());
                    mContext.startActivity(intent);
                }
            }
            return false;
        });
        LinearLayout lin_uo_operation = helper.getView(R.id.lin_uo_operation);
        lin_uo_operation.removeAllViews();
        TextView tv_order_c_time = helper.getView(R.id.tv_order_c_time);
        tv_order_c_time.setText("下单时间:" + item.getCreateAt());
        TextView tv_order_l_time = helper.getView(R.id.tv_order_l_time);
        tv_order_l_time.setVisibility(View.GONE);
        if (item.getModel() == 1) {
            int num = 0;
            if (item.getProducts() != null && item.getProducts().size() > 0) {
                for (int i = 0; i < item.getProducts().size(); i++) {
                    num = num + item.getProducts().get(i).getNum();
                }
            }
            helper.setText(R.id.tv_uo_info, "¥" + String.format("%.2f", item.getFinalPrice()));
            tv_uo_status.setVisibility(View.VISIBLE);
            if (item.getStatus() == 1) {
                //ToastUtils.showLongToast("这是未支付1");
                tv_uo_status.setText("未支付");
                TextView textview = new TextView(mContext);
                textview.setBackground(mContext.getResources().getDrawable(R.drawable.bg_shape_order));
                textview.setTextColor(mContext.getResources().getColor(R.color.color37));
                textview.setPadding(13, 8, 13, 8);
                textview.setText("去支付");
                textview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userOrderInterface.payOrder(helper.getLayoutPosition(), 1);
                    }
                });
                lin_uo_operation.addView(textview);
            } else {
                if (item.getStatus() == 2) {
                    tv_uo_status.setText("已支付");
                } else if (item.getStatus() == 3) {
                    tv_uo_status.setText("已取消");
                } else if (item.getStatus() == 4) {
                    tv_uo_status.setText("已发货");
                } else if (item.getStatus() == 5) {
                    tv_uo_status.setText("已交易");
                } else if (item.getStatus() == 6) {
                    tv_uo_status.setText("退货/退款中");
                } else if (item.getStatus() == 7) {
                    tv_uo_status.setText("已完成");
                }
                if (item.isSalesReturn()) {
                    TextView textview = new TextView(mContext);
                    textview.setBackground(mContext.getResources().getDrawable(R.drawable.bg_shape_order1));
                    textview.setTextColor(mContext.getResources().getColor(R.color.tabwd));
                    textview.setPadding(13, 8, 13, 8);
                    textview.setText("退货退款");
                    textview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            userOrderInterface.refundsOrder(item.getId());
                        }
                    });
                    lin_uo_operation.addView(textview);
                }
                if (item.getDelivery() != null && item.getDelivery().getDeliveryInfo() != null) {
                    TextView textview = new TextView(mContext);
                    textview.setBackground(mContext.getResources().getDrawable(R.drawable.bg_shape_order1));
                    textview.setTextColor(mContext.getResources().getColor(R.color.black));
                    textview.setPadding(13, 8, 13, 8);
                    textview.setText("查看物流");
                    textview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Type type = new TypeToken<List<DeliveryInfoBean>>() {
                            }.getType();
                            List<DeliveryInfoBean> deliveryInfoBeanList = new Gson().fromJson(item.getDelivery().getDeliveryInfo(), type);
                            userOrderInterface.ShowLogistics(deliveryInfoBeanList, 1);
                        }
                    });
                    lin_uo_operation.addView(textview);
                }
            }
            if (lin_uo_operation.getChildCount() > 1) {
                LinearLayout.LayoutParams llParams;
                for (int i = 1; i < lin_uo_operation.getChildCount(); i++) {
                    View view = lin_uo_operation.getChildAt(i);
                    llParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                    llParams.setMarginStart(20);
                }
                helper.getView(R.id.v_live).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.v_live).setVisibility(View.GONE);
            }
        } else if (item.getModel() == 2) {
            TextView tv_uo_info = helper.getView(R.id.tv_uo_info);
            if (!EmptyUtils.isEmpty(item.getFinalPrice()) && item.getFinalPrice() > 0) {
                if (item.getPoint() > 0) {
                    tv_uo_info.setText(TextUtil.FontHighlighting(tv_uo_info.getContext(), "¥", "" + String.format("%.2f", item.getFinalPrice()), "+" + item.getPoint() + "积分", R.style.tv_ce_point1, R.style.tv_ce_point1, R.style.tv_ce_point1));
                } else {
                    tv_uo_info.setText(TextUtil.FontHighlighting(tv_uo_info.getContext(), "¥", "" + String.format("%.2f", item.getFinalPrice()), R.style.tv_ce_point1, R.style.tv_ce_point1));
                }
            } else {
                if (item.getPoint() > 0) {
                    tv_uo_info.setText(TextUtil.FontHighlighting(tv_uo_info.getContext(), "合计:", item.getPoint() + "积分", R.style.tv_ce_point1, R.style.tv_ce_point1));
                }
            }
            if (item.getType() == 1) {
                tv_uo_status.setVisibility(View.VISIBLE);
                if (item.getStatus() == 1) {
                    tv_uo_status.setText("未支付");
                    TextView textview = new TextView(mContext);
                    textview.setBackground(mContext.getResources().getDrawable(R.drawable.bg_shape_order));
                    textview.setTextColor(mContext.getResources().getColor(R.color.color37));
                    textview.setPadding(13, 8, 13, 8);
                    textview.setText("去支付");
                    textview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            userOrderInterface.payOrder(helper.getLayoutPosition(), 2);
                        }
                    });
                    lin_uo_operation.addView(textview);
                } else if (item.getStatus() == 2) {
                    tv_uo_status.setText("已支付");
                } else if (item.getStatus() == 3) {
                    tv_uo_status.setText("已取消");
                } else if (item.getStatus() == 4) {
                    tv_uo_status.setText("已发货");
                } else if (item.getStatus() == 5) {
                    tv_uo_status.setText("已交易");
                } else if (item.getStatus() == 6) {
                    tv_uo_status.setText("退货/退款中");
                } else if (item.getStatus() == 7) {
                    tv_uo_status.setText("已完成");
                }
                if (item.isSalesReturn()) {
                    TextView textview = new TextView(mContext);
                    textview.setBackground(mContext.getResources().getDrawable(R.drawable.bg_shape_order));
                    textview.setTextColor(mContext.getResources().getColor(R.color.color37));
                    textview.setPadding(13, 8, 13, 8);
                    textview.setText("取消订单");
                    textview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            userOrderInterface.userCancelOrder(item.getId());
                        }
                    });
                    lin_uo_operation.addView(textview);
                }
                if (item.getDelivery() != null) {
                    StringBuffer timeBuffer = new StringBuffer();
                    if (!EmptyUtils.isEmpty(item.getDelivery().getTime())) {
                        timeBuffer.append("配送时间:");
                        timeBuffer.append(TimeUtils.date2String(TimeUtils.string2Date(item.getDelivery().getTime()), "yyyy-MM-dd")
                                + " " + TimeUtils.date2String(TimeUtils.string2Date(item.getDelivery().getTime()), "HH:mm"));
                        if (!EmptyUtils.isEmpty(item.getDelivery().getTimeEnd())) {
                            timeBuffer.append("-" + TimeUtils.date2String(TimeUtils.string2Date(item.getDelivery().getTimeEnd()), "HH:mm"));
                        }
                    } else {
                        if (!EmptyUtils.isEmpty(item.getDelivery().getTimeEnd())) {
                            timeBuffer.append("配送时间:");
                            timeBuffer.append(TimeUtils.date2String(TimeUtils.string2Date(item.getDelivery().getTimeEnd()), "HH:mm"));
                        }
                    }
                    if (EmptyUtils.isEmpty(timeBuffer.toString())) {
                        tv_order_l_time.setVisibility(View.GONE);
                    } else {
                        tv_order_l_time.setText(timeBuffer.toString());
                        tv_order_l_time.setVisibility(View.VISIBLE);
                    }
                    if (item.getDelivery().getDeliveryInfo() != null) {
                        TextView textview = new TextView(mContext);
                        textview.setBackground(mContext.getResources().getDrawable(R.drawable.bg_shape_order1));
                        textview.setTextColor(mContext.getResources().getColor(R.color.black));
                        textview.setPadding(13, 8, 13, 8);
                        textview.setText("查看物流");
                        textview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Type type = new TypeToken<List<DeliveryInfoBean>>() {
                                }.getType();
                                List<DeliveryInfoBean> deliveryInfoBeanList = new Gson().fromJson(item.getDelivery().getDeliveryInfo(), type);
                                userOrderInterface.ShowLogistics(deliveryInfoBeanList, 1);
                            }
                        });
                        lin_uo_operation.addView(textview);
                    }
                }
                if (item.isSalesReturn() || item.getDelivery() != null) {
                    if (lin_uo_operation.getChildCount() > 1) {
                        LinearLayout.LayoutParams llParams;
                        for (int i = 1; i < lin_uo_operation.getChildCount(); i++) {
                            View view = lin_uo_operation.getChildAt(i);
                            llParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                            llParams.setMarginStart(20);
                        }

                    }
                    helper.getView(R.id.v_live).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.v_live).setVisibility(View.GONE);
                }
            } else if (item.getType() == 2) {
                if (!EmptyUtils.isEmpty(item.getExchangeOrder()) && !EmptyUtils.isEmpty(item.getExchangeOrder().getUseInfo())) {
                    helper.getView(R.id.v_live).setVisibility(View.VISIBLE);
                    TextView textview = new TextView(mContext);
                    textview.setBackground(mContext.getResources().getDrawable(R.drawable.bg_shape_order1));
                    textview.setTextColor(mContext.getResources().getColor(R.color.black));
                    textview.setPadding(13, 8, 13, 8);
                    textview.setText("查看状态");
                    textview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Type type = new TypeToken<List<DeliveryInfoBean>>() {
                            }.getType();
                            List<DeliveryInfoBean> deliveryInfoBeanList = new Gson().fromJson(item.getExchangeOrder().getUseInfo(), type);
                            userOrderInterface.ShowLogistics(deliveryInfoBeanList, 2);
                        }
                    });
                    lin_uo_operation.addView(textview);
                } else {
                    helper.getView(R.id.v_live).setVisibility(View.GONE);
                }
            } else if (item.getType() == 3) {
                if (item.getExchangeOrder() != null && item.getExchangeOrder().getStatus() == 1) {
                    helper.getView(R.id.v_live).setVisibility(View.VISIBLE);
                    TextView textview = new TextView(mContext);
                    textview.setBackground(mContext.getResources().getDrawable(R.drawable.bg_shape_order1));
                    textview.setTextColor(mContext.getResources().getColor(R.color.black));
                    textview.setPadding(13, 8, 13, 8);
                    textview.setText("兑换码");
                    textview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            userOrderInterface.ShowQrCodeImg(item.getModel(), item.getExchangeOrder().getTarget());
                        }
                    });
                    lin_uo_operation.addView(textview);
                } else {
                    helper.getView(R.id.v_live).setVisibility(View.GONE);
                }
            }
        } else if (item.getModel() == 3) {
            helper.setText(R.id.tv_uo_info, "¥" + String.format("%.2f", item.getFinalPrice()));
            tv_uo_status.setVisibility(View.VISIBLE);
            if (item.getStatus() == 1) {
                tv_uo_status.setText("未支付");
                TextView textview = new TextView(mContext);
                textview.setBackground(mContext.getResources().getDrawable(R.drawable.bg_shape_order));
                textview.setTextColor(mContext.getResources().getColor(R.color.color37));
                textview.setPadding(13, 8, 13, 8);
                textview.setText("去支付");
                textview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userOrderInterface.payOrder(helper.getLayoutPosition(), 3);
                    }
                });
                lin_uo_operation.addView(textview);
            } else {
                if (item.getDelivery() != null && item.getDelivery().getStatus() == 2) {
                    helper.getView(R.id.v_live).setVisibility(View.VISIBLE);
                    tv_order_l_time.setVisibility(View.VISIBLE);
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(TimeUtils.string2Date(item.getDelivery().getTime()));
                    int myear = calendar1.get(Calendar.YEAR);
                    int mMonth = calendar1.get(Calendar.MONTH) + 1;// 获取当前月份
                    int day = calendar1.get(Calendar.DAY_OF_MONTH);// 获取当前天数
                    int hour = calendar1.get(Calendar.HOUR_OF_DAY);// 获取当前天数
                    if (hour == 8) {
                        tv_order_l_time.setText("领取时间:" + myear + "-" + mMonth + "-" + day + "  早餐");
                    } else if (hour == 18) {
                        tv_order_l_time.setText("领取时间:" + myear + "-" + mMonth + "-" + day + "  晚餐");
                    } else {
                        tv_order_l_time.setText("领取时间:" + myear + "-" + mMonth + "-" + day + "  " + hour + ":00");
                    }
                    tv_uo_status.setText("已支付");
                    TextView textview = new TextView(mContext);
                    textview.setBackground(mContext.getResources().getDrawable(R.drawable.bg_shape_order));
                    textview.setTextColor(mContext.getResources().getColor(R.color.color37));
                    textview.setPadding(13, 8, 13, 8);
                    textview.setText("取货码");
                    textview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            userOrderInterface.ShowQrCodeImg(item.getModel(), item.getDelivery().getQrCodeImg());
                        }
                    });
                    lin_uo_operation.addView(textview);
                } else {
                    helper.getView(R.id.v_live).setVisibility(View.GONE);
                }
            }
        }

    }

    /**
     * 改变数量的接口
     */
    public interface UserOrderInterface {
        void BuyAgain(UserOrderBean item);

        void ShowQrCodeImg(int model, String qrCode);

        void ShowLogistics(List<DeliveryInfoBean> deliveryInfoBeanList, int type);

        void payOrder(int position, int payShoppingtype);

        void refundsOrder(int orderId);

        void UserOrderNoCopy(String orderNo);

        void userCancelOrder(int orderId);
    }
}
