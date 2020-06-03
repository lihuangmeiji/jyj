package com.idougong.jyj.module.ui.users;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.DeliveryAddressBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.module.adapter.DeliveryAddressAdapter;
import com.idougong.jyj.module.contract.DeliveryAddressInfoContract;
import com.idougong.jyj.module.presenter.DeliveryAddressInfoPresenter;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.widget.RecycleViewDivider;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DeliveryAddressInfoActivity extends BaseActivity<DeliveryAddressInfoPresenter> implements DeliveryAddressInfoContract.View {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView ivRight;
    @BindView(R.id.rv_user_delivery_address)
    RecyclerView rvUserDeliveryAddress;

    DeliveryAddressAdapter deliveryAddressAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_delivery_address_info;
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
        toolbarTitle.setText("收货地址");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        ivRight.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        int openType = getIntent().getIntExtra("openType", 0);
        int daId = getIntent().getIntExtra("daId", 0);
        deliveryAddressAdapter = new DeliveryAddressAdapter(R.layout.item_delivery_address, openType);
        rvUserDeliveryAddress.setAdapter(deliveryAddressAdapter);
        deliveryAddressAdapter.setIsselect(daId);
        rvUserDeliveryAddress.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rvUserDeliveryAddress.addItemDecoration(new RecycleViewDivider(getBaseContext(), LinearLayoutManager.VERTICAL, 30, getBaseContext().getResources().getColor(R.color.color39)));
        deliveryAddressAdapter.bindToRecyclerView(rvUserDeliveryAddress);
        deliveryAddressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (openType == 1) {
                    deliveryAddressAdapter.setIsselect(deliveryAddressAdapter.getItem(i).getId());
                    deliveryAddressAdapter.notifyDataSetChanged();
                    Intent intent1 = new Intent();
                    intent1.putExtra("daId", deliveryAddressAdapter.getItem(i).getId());
                    intent1.putExtra("phone", deliveryAddressAdapter.getItem(i).getPhone());
                    intent1.putExtra("address", deliveryAddressAdapter.getItem(i).getDetailsAddress());
                    intent1.putExtra("name", deliveryAddressAdapter.getItem(i).getName());
                    setResult(1, intent1);
                    finish();
                } else {
                    Intent intent = new Intent(getBaseContext(), DeliveryAddressActivity.class);
                    intent.putExtra("daInfo", new Gson().toJson(deliveryAddressAdapter.getItem(i)));
                    startActivityForResult(intent, 11);
                }
            }
        });
        deliveryAddressAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (view.getId()) {
                    case R.id.iv_delivery_del:
                        new AlertDialog.Builder(DeliveryAddressInfoActivity.this)
                                .setMessage("确定要删除当前地址信息?")
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
                                        getPresenter().delDeliveryAddressInfo(deliveryAddressAdapter.getItem(i).getId());
                                    }
                                })
                                .show();
                        break;
                    case R.id.cb_item:
                        if (openType == 1) {
                            deliveryAddressAdapter.setIsselect(deliveryAddressAdapter.getItem(i).getId());
                            deliveryAddressAdapter.notifyDataSetChanged();
                            Intent intent1 = new Intent();
                            intent1.putExtra("daId", deliveryAddressAdapter.getItem(i).getId());
                            intent1.putExtra("phone", deliveryAddressAdapter.getItem(i).getPhone());
                            intent1.putExtra("address", deliveryAddressAdapter.getItem(i).getDetailsAddress());
                            intent1.putExtra("name", deliveryAddressAdapter.getItem(i).getName());
                            setResult(1, intent1);
                            finish();
                        }
                        break;
                }
            }
        });
        getPresenter().getDeliveryAddressInfoResult(0, 0);
    }


    @Override
    public void setDeliveryAddressInfoResult(List<DeliveryAddressBean> homeShoppingDataResult) {
        if (homeShoppingDataResult == null || homeShoppingDataResult.size() == 0) {
            deliveryAddressAdapter.loadMoreEnd();
            deliveryAddressAdapter.setEmptyView(R.layout.layout_empty);
            ImageView imageView = deliveryAddressAdapter.getEmptyView().findViewById(R.id.iv_empty_ico);
            imageView.setImageResource(R.mipmap.ic_empty2);
            TextView tv_empty_title = deliveryAddressAdapter.getEmptyView().findViewById(R.id.tv_empty_title);
            tv_empty_title.setText("您还没有地址信息哦~");
            return;
        }
        deliveryAddressAdapter.setNewData(homeShoppingDataResult);
        deliveryAddressAdapter.loadMoreEnd();
    }

    @Override
    public void delDeliveryAddressInfoResult(String str) {
        hiddenLoading();
        toast("删除成功");
        deliveryAddressAdapter.getData().clear();
        deliveryAddressAdapter.notifyDataSetChanged();
        getPresenter().getDeliveryAddressInfoResult(0, 0);
    }

    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            getPresenter().getDeliveryAddressInfoResult(0, 0);
        } else {
            openLogin();
        }
    }


    @OnClick({R.id.tv_add_delivery_address
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_add_delivery_address:
                Intent intent = new Intent(getBaseContext(), DeliveryAddressActivity.class);
                startActivityForResult(intent, 11);
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
            } else {
                openLogin();
            }
            return;
        } else if (code == -2) {
            openLogin();
            return;
        } else if (code == -10) {
            return;
        }else if (code == 405) {
            deliveryAddressAdapter.getData().clear();
            deliveryAddressAdapter.notifyDataSetChanged();
            deliveryAddressAdapter.setEmptyView(R.layout.layout_no_network);
            deliveryAddressAdapter.getEmptyView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPresenter().getDeliveryAddressInfoResult(0, 0);
                }
            });
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
    public void toast(String msg) {
        super.toast(msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == 1) {
            deliveryAddressAdapter.getData().clear();
            deliveryAddressAdapter.notifyDataSetChanged();
            getPresenter().getDeliveryAddressInfoResult(0, 0);
        }
    }
}
