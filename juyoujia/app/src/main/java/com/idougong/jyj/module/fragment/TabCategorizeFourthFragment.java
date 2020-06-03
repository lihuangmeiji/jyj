package com.idougong.jyj.module.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.di.AppCommonModule;
import com.idougong.jyj.common.ui.BaseFragment;
import com.idougong.jyj.common.ui.BaseLazyFragment;
import com.idougong.jyj.common.ui.SimpleFragment;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserFunctionBean;
import com.idougong.jyj.model.UserFunctionListBean;
import com.idougong.jyj.model.UserServiceFunctionListBean;
import com.idougong.jyj.model.VersionShowBean;
import com.idougong.jyj.module.adapter.provider.UserFunctionItemViewProvider;
import com.idougong.jyj.module.adapter.provider.UserServiceFunctionItemViewProvider;
import com.idougong.jyj.module.contract.TabCategorizeFourthContract;
import com.idougong.jyj.module.presenter.TabCategorizeFourthPresenter;
import com.idougong.jyj.module.ui.MainActivity;
import com.idougong.jyj.module.ui.chat.QuestionDetailesActivity;
import com.idougong.jyj.module.ui.users.UserPerfectInformationActivity;
import com.idougong.jyj.utils.TargetClick;
import com.idougong.jyj.widget.CircleImageView;
import com.idougong.jyj.widget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class TabCategorizeFourthFragment extends BaseLazyFragment<TabCategorizeFourthPresenter> implements TabCategorizeFourthContract.View, UserFunctionItemViewProvider.UserFunctionInterface, UserServiceFunctionItemViewProvider.UserServiceFunctionInterface {
    @BindView(R.id.iv_userico)
    CircleImageView ivUserico;
    @BindView(R.id.tv_log_reg)
    TextView tvLogReg;
    @BindView(R.id.recycler_view_user)
    RecyclerView recyclerViewUser;

    List<UserFunctionBean> userFunctionBeanList;
    List<UserFunctionBean> userServiceFunctionBeansList;
    private Items items;
    private MultiTypeAdapter adapter;
    String zzxxUrl;

    @Override
    protected void loadLazyData() {
        userInfo(1);
    }

    @Override
    public void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        userFunctionBeanList = new ArrayList<>();
        addUserFunction("我的菜金", R.mipmap.icon_user_wallet, "jyj://main/mine/wallet", true);
        addUserFunction("我的优惠券", R.mipmap.icon_user_coupon, "jyj://main/mine/coupon", true);
        addUserFunction("我的订单", R.mipmap.icon_user_order, "jyj://main/mine/order", true);

        zzxxUrl = AppCommonModule.API_BASE_URL + "h5common/jyj-zizhi1/index.html";
        userServiceFunctionBeansList = new ArrayList<>();
        addUserServicesFunction("我的地址", R.mipmap.icon_useraddress, "jyj://main/mine/address", true);
        addUserServicesFunction("关于居友家", R.mipmap.icon_aboutjyj, "jyj://main/mine/about", false);
        addUserServicesFunction("资质信息", R.mipmap.icon_zzxx, zzxxUrl, false);
        addUserServicesFunction("联系客服", R.mipmap.icon_services, "jyj://main/mine/custser", false);
        addUserServicesFunction("版本升级", R.mipmap.icon_update_check, "bbsj", false);
        addUserServicesFunction("邀请有礼", R.mipmap.icon_invitation, "jyj://main/mine/invite", true);
        items = new Items();
        adapter = new MultiTypeAdapter(items);
        UserFunctionItemViewProvider userFunctionItemViewProvider = new UserFunctionItemViewProvider();
        userFunctionItemViewProvider.setUserFunctionInterface(this);
        adapter.register(UserFunctionListBean.class, userFunctionItemViewProvider);

        UserServiceFunctionItemViewProvider userServiceFunctionItemViewProvider = new UserServiceFunctionItemViewProvider();
        userServiceFunctionItemViewProvider.setUserServiceFunctionInterface(this);
        adapter.register(UserServiceFunctionListBean.class, userServiceFunctionItemViewProvider);

        UserFunctionListBean userFunctionListBean = new UserFunctionListBean();
        userFunctionListBean.setUserFunctionBeanList(userFunctionBeanList);
        items.add(userFunctionListBean);

        UserServiceFunctionListBean userServiceFunctionListBean = new UserServiceFunctionListBean();
        userServiceFunctionListBean.setUserFunctionBeanList(userServiceFunctionBeansList);
        items.add(userServiceFunctionListBean);
        recyclerViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewUser.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL, 20, getResources().getColor(R.color.color39)));
        recyclerViewUser.setAdapter(adapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_tab_categorize_fourth;
    }


    @OnClick({R.id.lin_login
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_login:
                if (login != null) {
                    Intent intent = new Intent(getContext(), UserPerfectInformationActivity.class);
                    startActivityForResult(intent, 2);
                } else {
                    openLogin();
                }
                break;
        }
    }

    private void addUserFunction(String name, int ico, String target, boolean needLogin) {
        UserFunctionBean userFunctionBean = new UserFunctionBean();
        userFunctionBean.setUf_ico(ico);
        userFunctionBean.setUf_name(name);
        userFunctionBean.setTarget(target);
        userFunctionBean.setNeedLogin(needLogin);
        userFunctionBeanList.add(userFunctionBean);
    }

    private void addUserServicesFunction(String name, int ico, String target, boolean needLogin) {
        UserFunctionBean userFunctionBean = new UserFunctionBean();
        userFunctionBean.setUf_ico(ico);
        userFunctionBean.setUf_name(name);
        userFunctionBean.setTarget(target);
        userFunctionBean.setNeedLogin(needLogin);
        userServiceFunctionBeansList.add(userFunctionBean);
    }

    @Override
    public void setOnClickListener(UserFunctionBean userFunctionBean) {
        String target = userFunctionBean.getTarget();
        if(EmptyUtils.isEmpty(target)){
            return;
        }
        if (userFunctionBean.isNeedLogin() && login == null) {
            openLogin();
        } else {
            TargetClick.targetOnClick(getContext(), target);
        }
    }

    @Override
    public void setServiceFunctionOnClickListener(UserFunctionBean userServiceFunctionBean) {
        String target = userServiceFunctionBean.getTarget();
        if(EmptyUtils.isEmpty(target)){
            return;
        }
        if (userServiceFunctionBean.isNeedLogin() && login == null) {
            openLogin();
        } else {
            if (target.equals("bbsj")) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.versionChecking();
            } else {
                TargetClick.targetOnClick(getContext(), target);
            }
        }
    }

    @Override
    public void setUpdateUserInfoResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            userInfo(2);
        } else {
            clearUserInfo();
            userInfo(2);
        }
    }

    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            userInfo(2);
        } else {
            clearUserInfo();
            userInfo(2);
        }
    }

    @Override
    public void setVersionResult(VersionShowBean showBean) {

    }

    public void userInfo(int cztype) {
        loadUserInfo();
        if (login != null) {
            Glide.with(getContext()).load(login.getIcon()).error(R.mipmap.user_icomr).into(ivUserico);
            tvLogReg.setText(login.getNickName());
            if (cztype == 1) {
                getPresenter().getUpdateUserInfoResult(0);
            }
        } else {
            ivUserico.setImageResource(R.mipmap.user_icomr);
            tvLogReg.setText("登录/注册");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            userInfo(2);
        }
        if (requestCode == 2 && resultCode == 1) {
            userInfo(1);
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
                clearUserInfo();
                userInfo(2);
            }
            return;
        } else if (code == -2) {
            clearUserInfo();
            userInfo(2);
            return;
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

    @Override
    public void toast(String msg) {
        super.toast(msg);
    }
}
