package com.idougong.jyj.module.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserShoppingCarBean;
import com.idougong.jyj.model.UserShoppingCarConfirmBean;
import com.idougong.jyj.module.adapter.UserShoppingCarAdapter;
import com.idougong.jyj.module.contract.UserShoppingCarContract;
import com.idougong.jyj.module.presenter.UserShoppingCarPresenter;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.widget.RecycleViewDivider;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class UserShoppingCarActivity extends BaseActivity<UserShoppingCarPresenter> implements UserShoppingCarContract.View {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView iv_right;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.vs_showerror)
    ViewStub vs_showerror;
    @BindView(R.id.tv_pay_money)
    TextView tv_pay_money;
    @BindView(R.id.tv_pay_shpnumber)
    TextView tv_pay_shpnumber;
    @BindView(R.id.cb_all_shopping)
    CheckBox cb_all_shopping;
    @BindView(R.id.lin_pay_bottom)
    LinearLayout lin_pay_bottom;

    UserShoppingCarAdapter userShoppingCarAdapter;

    double mtotalPrice = 0.00;
    int number = 0;
    int deleteIndex = 0;
    StringBuffer scid;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_shopping_car;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("购物车");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setText("删除");
        tv_pay_money.setText("¥0.00");
        tv_pay_shpnumber.setText("共0件商品");
        userShoppingCarAdapter = new UserShoppingCarAdapter(R.layout.item_shopping_car);
        userShoppingCarAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        userShoppingCarAdapter.bindToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.addItemDecoration(new RecycleViewDivider(getBaseContext(), LinearLayoutManager.VERTICAL, 1, getBaseContext().getResources().getColor(R.color.color40)));
        recyclerView.setAdapter(userShoppingCarAdapter);
        getPresenter().getUserShoppingListResult();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });
        addShoppingCarReceiver();
    }

    @Override
    public void setUserShoppingListResult(List<UserShoppingCarBean> userShoppingCarBeanList) {
        if (userShoppingCarBeanList == null || userShoppingCarBeanList.size() == 0) {
            userShoppingCarAdapter.loadMoreEnd();
            userShoppingCarAdapter.getData().clear();
            userShoppingCarAdapter.notifyDataSetChanged();
            userShoppingCarAdapter.setEmptyView(R.layout.layout_empty);
            lin_pay_bottom.setVisibility(View.GONE);
            return;
        } else {
            lin_pay_bottom.setVisibility(View.VISIBLE);
        }
        userShoppingCarAdapter.setNewData(userShoppingCarBeanList);
        userShoppingCarAdapter.loadMoreEnd();
        cb_all_shopping.setChecked(true);
        doCheckAll();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                setResult(1);
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
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
    public void setUserShoppingCarDelete(String result) {
        hiddenLoading();
        toast("商品删除成功");
        getPresenter().getUserShoppingListResult();
        Intent intent = new Intent("shopping");
        sendBroadcast(intent);
    }

    @Override
    public void setUserShoppingCarUpateNumber(String result) {
        hiddenLoading();
        userShoppingCarAdapter.notifyDataSetChanged();
        calulate();
        Intent intent = new Intent("shopping");
        sendBroadcast(intent);
    }

    @Override
    public void setUserShoppingCarConfirm(UserShoppingCarConfirmBean result) {
        Intent intent = new Intent(getBaseContext(), UserShoppingCarConfirmActivity.class);
        intent.putExtra("sccResult", new Gson().toJson(result));
        startActivityForResult(intent, 11);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == 1) {
            getPresenter().getUserShoppingListResult();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent intent1 = new Intent();
                setResult(1, intent1);
                finish();
                return true;
        }
        return super.onKeyUp(keyCode, event);
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
        } else if (code == -2) {
            openLogin();
            return;
        } else if (code == 1) {
            getPresenter().getUserShoppingListResult();
        } else if (code == 405) {
            recyclerView.setVisibility(View.GONE);
            vs_showerror.setLayoutResource(R.layout.layout_no_network);
            //vs_showerror.inflate();
            vs_showerror.setVisibility(View.VISIBLE);
            LinearLayout lin_load = (LinearLayout) findViewById(R.id.lin_load);
            lin_load.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPresenter().getUserShoppingListResult();
                }
            });
        } else if (code == -10) {
            userShoppingCarAdapter.notifyDataSetChanged();
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

    @OnClick({R.id.cb_all_shopping,
            R.id.tv_order_place,
            R.id.iv_right
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_all_shopping:
                doCheckAll();
                break;
            case R.id.tv_order_place:
                if (userShoppingCarAdapter.getData().size() == 0) {
                    toast("购物车暂无商品");
                    return;
                }
                if (EmptyUtils.isEmpty(scid.toString())) {
                    toast("请勾选要购买的商品");
                    return;
                }
                Log.i("aa", "onViewClicked: " + scid.toString().substring(0, scid.toString().lastIndexOf(",")));
                getPresenter().getUserShoppingCarConfirm(scid.toString().substring(0, scid.toString().lastIndexOf(",")), null, true);
                break;
            case R.id.iv_right:
                if (userShoppingCarAdapter.getData().size() == 0) {
                    toast("购物车暂无商品");
                    return;
                }
                if (EmptyUtils.isEmpty(scid.toString())) {
                    toast("请勾选要购买的商品");
                    return;
                }
                new AlertDialog.Builder(this)
                        .setMessage("确定要删除选中的商品吗?")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getPresenter().getUserShoppingCarDelete(scid.toString().substring(0, scid.toString().lastIndexOf(",")));
                            }
                        })
                        .show();
                break;
        }
    }


    public void doIncrease(int groupPosition, boolean isChecked) {
        if (userShoppingCarAdapter.getData().size() > groupPosition) {
            int count = userShoppingCarAdapter.getData().get(groupPosition).getProductNum();
            count++;
            userShoppingCarAdapter.getData().get(groupPosition).setProductNum(count);
            try {
                showLoading();
                getPresenter().getUserShoppingCarUpateNumber(userShoppingCarAdapter.getData().get(groupPosition).getProduct().getId(), count);
            } catch (Exception e) {
                Log.i("getId", "doDecrease: getId获取失败");
            }
        } else {
            toast("数据有误");
        }
    }


    public void doDecrease(final int groupPosition, boolean isChecked) {
        if (userShoppingCarAdapter.getData().size() > groupPosition) {
            int count = userShoppingCarAdapter.getData().get(groupPosition).getProductNum();
            if (count == 1) {
                new AlertDialog.Builder(this)
                        .setMessage("确定要删除此商品吗?")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getPresenter().getUserShoppingCarDelete(userShoppingCarAdapter.getData().get(groupPosition).getProduct().getId() + "");
                            }
                        })
                        .show();
                return;
            }
            count--;
            userShoppingCarAdapter.getData().get(groupPosition).setProductNum(count);
            try {
                showLoading();
                getPresenter().getUserShoppingCarUpateNumber(userShoppingCarAdapter.getData().get(groupPosition).getProduct().getId(), count);
            } catch (Exception e) {
                Log.i("getId", "doDecrease: getId获取失败");
            }
        } else {
            toast("数据有误");
        }
    }


    public void checkChild(int groupPosition, boolean isChecked) {
        if (userShoppingCarAdapter.getData().size() > groupPosition) {
            userShoppingCarAdapter.getData().get(groupPosition).setSelect(isChecked);
            userShoppingCarAdapter.notifyDataSetChanged();
            if (isCheckAll()) {
                cb_all_shopping.setChecked(true);//全选
            } else {
                cb_all_shopping.setChecked(false);//反选
            }
            calulate();
        } else {
            toast("数据有误");
        }
    }


    public void childDelete(int groupPosition, int childPosition) {
        if (userShoppingCarAdapter.getData().size() > groupPosition) {
            deleteIndex = groupPosition;
            try {
                showLoading();
                getPresenter().getUserShoppingCarDelete(userShoppingCarAdapter.getData().get(groupPosition).getProduct().getId() + "");
            } catch (Exception e) {
                Log.i("getId", "doDecrease: getId获取失败");
            }
        } else {
            toast("数据有误");
        }
    }


    private void doCheckAll() {
        for (int i = 0; i < userShoppingCarAdapter.getData().size(); i++) {
            userShoppingCarAdapter.getData().get(i).setSelect(cb_all_shopping.isChecked());
        }
        userShoppingCarAdapter.notifyDataSetChanged();
        calulate();
    }


    private boolean isCheckAll() {
        for (UserShoppingCarBean group : userShoppingCarAdapter.getData()) {
            if (!group.isSelect()) {
                return false;
            }
        }
        return true;
    }


    private void calulate() {
        mtotalPrice = 0.00;
        number = 0;
        scid = new StringBuffer();
        for (int i = 0; i < userShoppingCarAdapter.getData().size(); i++) {
            UserShoppingCarBean group = userShoppingCarAdapter.getData().get(i);
            if (group.isSelect()) {
                number += group.getProductNum();
                if (!EmptyUtils.isEmpty(group.getProduct())) {
                    mtotalPrice += group.getProductNum() * group.getProduct().getCurrentPrice();
                    scid.append(group.getProduct().getId() + ",");
                }
            }
        }
        DecimalFormat df = new DecimalFormat("######0.00");
        tv_pay_money.setText("合计：￥" + df.format(mtotalPrice) + "");
        tv_pay_shpnumber.setText("共" + number + "件商品");
    }


    BroadcastReceiver broadcastReceiver;

    private void addShoppingCarReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getPresenter().getUserShoppingListResult();
            }
        };
        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction("shoppingcar");
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
