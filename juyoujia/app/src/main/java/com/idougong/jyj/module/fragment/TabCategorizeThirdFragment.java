package com.idougong.jyj.module.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.BaseFragment;
import com.idougong.jyj.common.ui.BaseLazyFragment;
import com.idougong.jyj.common.ui.SimpleFragment;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserFunctionListBean;
import com.idougong.jyj.model.UserShoppingCarBean;
import com.idougong.jyj.model.UserShoppingCarConfirmBean;
import com.idougong.jyj.model.UserShoppingCarDivisionBean;
import com.idougong.jyj.model.provider.ShoppingCarFailureItemBean;
import com.idougong.jyj.model.provider.ShoppingCarItemBean;
import com.idougong.jyj.module.adapter.CreditsExchangeAdapter;
import com.idougong.jyj.module.adapter.UserShoppingCarAdapter;
import com.idougong.jyj.module.adapter.provider.ShoppingCarFailureItemViewProvider;
import com.idougong.jyj.module.adapter.provider.ShoppingCarItemViewProvider;
import com.idougong.jyj.module.contract.TabCategorizeThirdContract;
import com.idougong.jyj.module.presenter.TabCategorizeThirdPresenter;
import com.idougong.jyj.module.ui.MainActivity;
import com.idougong.jyj.module.ui.home.CreditsExchangeDetailedActivity;
import com.idougong.jyj.module.ui.home.UserShoppingCarConfirmActivity;
import com.idougong.jyj.utils.ArithmeticUtils;
import com.idougong.jyj.utils.TargetClick;
import com.idougong.jyj.widget.GridDividerItemDecoration;
import com.idougong.jyj.widget.RecycleViewDivider;
import com.idougong.jyj.widget.dialog.SpecificationSelectView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class TabCategorizeThirdFragment extends BaseLazyFragment<TabCategorizeThirdPresenter> implements TabCategorizeThirdContract.View, CreditsExchangeAdapter.AddShoppingCarInterface {

    @BindView(R.id.lin_shopping_car)
    LinearLayout linShoppingCar;
    @BindView(R.id.cl_shopping_car)
    CoordinatorLayout cl_shopping_car;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_pay_money)
    TextView tv_pay_money;
    @BindView(R.id.cb_all_shopping)
    CheckBox cb_all_shopping;
    @BindView(R.id.lin_pay_bottom)
    LinearLayout lin_pay_bottom;
    @BindView(R.id.recycler_view_shopping)
    RecyclerView recyclerViewShopping;
    @BindView(R.id.tv_shopping_del)
    TextView tvShoppingDel;


    CreditsExchangeAdapter creditsExchangeAdapter;

    double mtotalPrice = 0.00;
    int number = 0;

    StringBuffer scid;
    StringBuffer scidLose;
    private Items items;
    private MultiTypeAdapter adapter;


    List<UserShoppingCarBean> userShoppingCarList;
    List<UserShoppingCarBean> userShoppingCarLoseList;

    @Override
    public void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        tv_pay_money.setText("¥0.00");
        cb_all_shopping.setText("全选(0)");

        userShoppingCarList = new ArrayList<>();
        userShoppingCarLoseList = new ArrayList<>();

        items = new Items();
        adapter = new MultiTypeAdapter(items);
        ShoppingCarItemViewProvider shoppingCarItemViewProvider = new ShoppingCarItemViewProvider();
        shoppingCarItemViewProvider.setShoppingCarInterface(new ShoppingCarItemViewProvider.ShoppingCarInterface() {
            @Override
            public void checkChild(int position, boolean isSelect) {
                checkChildRealize(position, isSelect);
            }

            @Override
            public void doDecrease(int position) {
                doDecreaseRealize(position);
            }

            @Override
            public void doIncrease(int position) {
                doIncreaseRealize(position);
            }

            @Override
            public void childDelete(int position) {
                childDeleteRealize(position);
            }
        });
        adapter.register(ShoppingCarItemBean.class, shoppingCarItemViewProvider);

        ShoppingCarFailureItemViewProvider shoppingCarFailureItemViewProvider = new ShoppingCarFailureItemViewProvider();
        shoppingCarFailureItemViewProvider.setShoppingCarFailureInterface(new ShoppingCarFailureItemViewProvider.ShoppingCarFailureInterface() {
            @Override
            public void checkChild(int position, boolean isSelect) {
                checkChildLoseRealize(position, isSelect);
            }

            @Override
            public void childDelete(int position) {
                childDeleteLoseRealize(position);
            }
        });
        adapter.register(ShoppingCarFailureItemBean.class, shoppingCarFailureItemViewProvider);

        //可以购买商品
        ShoppingCarItemBean shoppingCarItemBean = new ShoppingCarItemBean();
        shoppingCarItemBean.setUserShoppingCarBeanList(userShoppingCarList);
        items.add(shoppingCarItemBean);

        ShoppingCarFailureItemBean shoppingCarFailureItemBean = new ShoppingCarFailureItemBean();
        shoppingCarFailureItemBean.setUserShoppingCarBeanList(userShoppingCarLoseList);
        items.add(shoppingCarFailureItemBean);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.color39)));
        recyclerView.setAdapter(adapter);

        creditsExchangeAdapter = new CreditsExchangeAdapter(R.layout.item_credits_exchange);
        creditsExchangeAdapter.setEnableLoadMore(true);
        creditsExchangeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        creditsExchangeAdapter.setAddShoppingCarInterface(this);
        recyclerViewShopping.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewShopping.setAdapter(creditsExchangeAdapter);
        recyclerViewShopping.addItemDecoration(new GridDividerItemDecoration(20, getContext().getResources().getColor(R.color.color39)));
        creditsExchangeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(getContext(), CreditsExchangeDetailedActivity.class);
                intent.putExtra("shoppingId", creditsExchangeAdapter.getItem(i).getId());
                startActivity(intent);
            }
        });

    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_tab_categorize_third;
    }


    @Override
    protected void loadLazyData() {
        getPresenter().getUserShoppingListResult();
        getPresenter().getRecommendShoppingDataResult();
    }

    @Override
    public void setUserShoppingListResult(UserShoppingCarDivisionBean userShoppingCarDivisionBean) {
        if (userShoppingCarDivisionBean == null || ((userShoppingCarDivisionBean.getFailureTrue() == null || userShoppingCarDivisionBean.getFailureTrue().size() == 0) && (userShoppingCarDivisionBean.getFailureFalse() == null || userShoppingCarDivisionBean.getFailureFalse().size() == 0))) {
            linShoppingCar.setVisibility(View.GONE);
            cl_shopping_car.setVisibility(View.VISIBLE);
            return;
        }
        userShoppingCarList.clear();
        if (userShoppingCarDivisionBean.getFailureFalse() != null) {
            userShoppingCarList.addAll(userShoppingCarDivisionBean.getFailureFalse());
            cb_all_shopping.setChecked(true);
        }
        userShoppingCarLoseList.clear();
        if (userShoppingCarDivisionBean.getFailureTrue() != null) {
            userShoppingCarLoseList.addAll(userShoppingCarDivisionBean.getFailureTrue());
        }
        adapter.notifyDataSetChanged();
        linShoppingCar.setVisibility(View.VISIBLE);
        cl_shopping_car.setVisibility(View.GONE);
        doCheckAll();
        calulateLose();
    }


    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            getPresenter().getUserShoppingListResult();
        } else {
            openLogin();
        }
    }


    @Override
    public void setUserShoppingCarDelete(String result) {
        hiddenLoading();
        toast("商品删除成功");
        getPresenter().getUserShoppingListResult();
        Intent intent = new Intent("shoppingNum");
        getContext().sendBroadcast(intent);
    }

    @Override
    public void setUserShoppingCarUpateNumber(String result) {
        hiddenLoading();
        adapter.notifyDataSetChanged();
        calulate();
        Intent intent = new Intent("shoppingNum");
        getContext().sendBroadcast(intent);
    }

    @Override
    public void setUserShoppingCarConfirm(UserShoppingCarConfirmBean result) {
        Intent intent = new Intent(getContext(), UserShoppingCarConfirmActivity.class);
        intent.putExtra("sccResult", new Gson().toJson(result));
        startActivityForResult(intent, 11);
    }

    @Override
    public void setRecommendShoppingDataResult(List<HomeShoppingSpreeBean> homeShoppingDataResult) {
        creditsExchangeAdapter.setNewData(homeShoppingDataResult);
        creditsExchangeAdapter.loadMoreEnd();
    }

    @Override
    public void addShoppingCarResult(String strResult, ImageView imageView) {
        hiddenLoading();
        if (linShoppingCar.getVisibility() == View.GONE) {
            getPresenter().getUserShoppingListResult();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.add2Cart(imageView, strResult);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == 1) {
            getPresenter().getUserShoppingListResult();
        } else if (requestCode == 1 && resultCode == 2) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.categoryselect(-1);
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
        } else if (code == 1) {
            getPresenter().getUserShoppingListResult();
        } else if (code == 405) {

        } else if (code == -10) {

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
            R.id.tv_shopping_del,
            R.id.tv_pay_goods
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_all_shopping:
                doCheckAll();
                break;
            case R.id.tv_pay_goods:
                TargetClick.targetOnClick(getContext(), "jyj://main/home/category");
                break;
            case R.id.tv_order_place:
                if (userShoppingCarList.size() == 0) {
                    toast("购物车暂无商品");
                    return;
                }
                if (EmptyUtils.isEmpty(scid) || EmptyUtils.isEmpty(scid.toString())) {
                    toast("请勾选要购买的商品");
                    return;
                }
                Log.i("aa", "onViewClicked: " + scid.toString().substring(0, scid.toString().lastIndexOf(",")));
                getPresenter().getUserShoppingCarConfirm(scid.toString().substring(0, scid.toString().lastIndexOf(",")), null, true);
                break;
            case R.id.tv_shopping_del:
                if (userShoppingCarList.size() == 0 && userShoppingCarLoseList.size() == 0) {
                    toast("购物车暂无商品");
                    return;
                }
                if (EmptyUtils.isEmpty(scid.toString()) && EmptyUtils.isEmpty(scidLose.toString())) {
                    toast("请勾选要删除的商品");
                    return;
                }
                new AlertDialog.Builder(getContext())
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
                                if (!EmptyUtils.isEmpty(scid)) {
                                    if (!EmptyUtils.isEmpty(scidLose)) {
                                        scid.append(scidLose.toString());
                                        getPresenter().getUserShoppingCarDelete(scid.toString().substring(0, scid.toString().lastIndexOf(",")));
                                    } else {
                                        getPresenter().getUserShoppingCarDelete(scid.toString().substring(0, scid.toString().lastIndexOf(",")));
                                    }
                                } else {
                                    if (!EmptyUtils.isEmpty(scidLose)) {
                                        getPresenter().getUserShoppingCarDelete(scidLose.toString().substring(0, scidLose.toString().lastIndexOf(",")));
                                    }
                                }
                            }
                        })
                        .show();
                break;
        }
    }


    public void doIncreaseRealize(int groupPosition) {
        if (userShoppingCarList.size() > groupPosition) {
            int count = userShoppingCarList.get(groupPosition).getProductNum();
            count++;
            userShoppingCarList.get(groupPosition).setProductNum(count);
            try {
                showLoading();
                getPresenter().getUserShoppingCarUpateNumber(userShoppingCarList.get(groupPosition).getProduct().getId(), count);
            } catch (Exception e) {
                Log.i("getId", "doDecrease: getId获取失败");
            }
        } else {
            toast("数据有误");
        }
    }


    public void doDecreaseRealize(final int groupPosition) {
        if (userShoppingCarList.size() > groupPosition) {
            int count = userShoppingCarList.get(groupPosition).getProductNum();
            if (count == 1) {
                new AlertDialog.Builder(getContext())
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
                                getPresenter().getUserShoppingCarDelete(userShoppingCarList.get(groupPosition).getProductId() + "");
                            }
                        })
                        .show();
                return;
            }
            count--;
            userShoppingCarList.get(groupPosition).setProductNum(count);
            try {
                showLoading();
                getPresenter().getUserShoppingCarUpateNumber(userShoppingCarList.get(groupPosition).getProductId(), count);
            } catch (Exception e) {
                Log.i("getId", "doDecrease: getId获取失败");
            }
        } else {
            toast("数据有误");
        }
    }


    public void checkChildRealize(int groupPosition, boolean isChecked) {
        if (userShoppingCarList.size() > groupPosition) {
            userShoppingCarList.get(groupPosition).setSelect(isChecked);
            adapter.notifyDataSetChanged();
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

    public void checkChildLoseRealize(int groupPosition, boolean isChecked) {
        if (userShoppingCarLoseList.size() > groupPosition) {
            userShoppingCarLoseList.get(groupPosition).setSelect(isChecked);
            calulateLose();
            adapter.notifyDataSetChanged();
        } else {
            toast("数据有误");
        }
    }


    public void childDeleteRealize(int groupPosition) {
        if (groupPosition < userShoppingCarList.size()) {
            try {
                showLoading();
                getPresenter().getUserShoppingCarDelete(userShoppingCarList.get(groupPosition).getProductId() + "");
            } catch (Exception e) {
                Log.i("getId", "doDecrease: getId获取失败");
            }
        } else {
            toast("数据有误");
        }
    }

    public void childDeleteLoseRealize(int groupPosition) {
        if (groupPosition < userShoppingCarLoseList.size()) {
            try {
                showLoading();
                getPresenter().getUserShoppingCarDelete(userShoppingCarLoseList.get(groupPosition).getProductId() + "");
            } catch (Exception e) {
                Log.i("getId", "doDecrease: getId获取失败");
            }
        } else {
            toast("数据有误");
        }
    }

    private void doCheckAll() {
        for (int i = 0; i < userShoppingCarList.size(); i++) {
            userShoppingCarList.get(i).setSelect(cb_all_shopping.isChecked());
        }
        adapter.notifyDataSetChanged();
        calulate();
    }


    private boolean isCheckAll() {
        for (UserShoppingCarBean group : userShoppingCarList) {
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
        for (int i = 0; i < userShoppingCarList.size(); i++) {
            UserShoppingCarBean group = userShoppingCarList.get(i);
            if (group.isSelect() && !EmptyUtils.isEmpty(group.getProduct())) {
                number += group.getProductNum();
                double goodsPrice = ArithmeticUtils.mul(group.getProductNum(), group.getProduct().getCurrentPrice(), 2);
                mtotalPrice += goodsPrice;
                scid.append(group.getProduct().getId() + ",");
            }
        }
        tv_pay_money.setText("￥" + String.format("%.2f", mtotalPrice) + "");
        cb_all_shopping.setText("全选(" + number + ")");
    }

    private void calulateLose() {
        scidLose = new StringBuffer();
        for (int i = 0; i < userShoppingCarLoseList.size(); i++) {
            UserShoppingCarBean group = userShoppingCarLoseList.get(i);
            if (group.isSelect() && !EmptyUtils.isEmpty(group.getProduct())) {
                scidLose.append(group.getProduct().getId() + ",");
            }
        }
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
        getContext().registerReceiver(broadcastReceiver, intentToReceiveFilter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            getContext().unregisterReceiver(broadcastReceiver);
        }
    }


    @Override
    public void addshoppingCar(int position, ImageView ivProductIcon) {
        if (creditsExchangeAdapter.getItem(position).getInventory() <= 0) {
            toast("该商品已售罄");
            return;
        }
        if ((creditsExchangeAdapter.getItem(position).getSkuList() != null && creditsExchangeAdapter.getItem(position).getSkuList().size() > 0)
                || (creditsExchangeAdapter.getItem(position).getProcessingWayList() != null && creditsExchangeAdapter.getItem(position).getProcessingWayList().size() > 0)) {
            SpecificationSelectView specificationSelectView = new SpecificationSelectView(getContext(), recyclerView, creditsExchangeAdapter.getItem(position));
            specificationSelectView.setSpecificationSelectInterface(new SpecificationSelectView.SpecificationSelectInterface() {
                @Override
                public void setConfirmOnClickListener(int processingWayId, int skuId) {
                    specificationSelectView.dismiss();
                    if (creditsExchangeAdapter.getItem(position).getSkuList() != null && creditsExchangeAdapter.getItem(position).getSkuList().size() > 0 && skuId < 0) {
                        toast("请选择商品规格");
                        return;
                    }
                    if (creditsExchangeAdapter.getItem(position).getProcessingWayList() != null && creditsExchangeAdapter.getItem(position).getProcessingWayList().size() > 0 && processingWayId < 0) {
                        toast("请选择正确的处理方式");
                        return;
                    }
                    showLoading();
                    getPresenter().addShoppingCar(creditsExchangeAdapter.getItem(position).getId(), ivProductIcon, processingWayId, skuId);
                }
            });
        } else {
            showLoading();
            getPresenter().addShoppingCar(creditsExchangeAdapter.getItem(position).getId(), ivProductIcon, 0, 0);
        }
    }
}
