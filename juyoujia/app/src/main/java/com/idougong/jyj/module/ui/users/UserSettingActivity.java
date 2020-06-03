package com.idougong.jyj.module.ui.users;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.di.AppCommonModule;
import com.idougong.jyj.common.net.EnvConstant;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserFunctionBean;
import com.idougong.jyj.model.VersionShowBean;
import com.idougong.jyj.module.adapter.UserFunctionAdapter;
import com.idougong.jyj.module.contract.UserSettingContract;
import com.idougong.jyj.module.presenter.UserSettingPresenter;
import com.idougong.jyj.module.ui.chat.QuestionDetailesActivity;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.ConstUtils;
import com.idougong.jyj.utils.DataKeeper;
import com.idougong.jyj.utils.InstallApk;
import com.idougong.jyj.utils.SoftUpdate;
import com.idougong.jyj.widget.GridDividerItemDecoration;
import com.idougong.jyj.widget.dialog.DownLoadingView;
import com.idougong.jyj.widget.dialog.UptodateWindowView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class UserSettingActivity extends BaseActivity<UserSettingPresenter> implements UserSettingContract.View, DownLoadingView.DownLoadingViewInterface {

    @BindView(R.id.ll_usersetting)
    LinearLayout userabout;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView ivRight;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerViewUser;

    Intent intent;
    List<UserFunctionBean> userFunctionBeanList;
    UserFunctionAdapter userFunctionAdapter;
    String zzxxUrl;
    private DownLoadingView downLoadingView;

    @Override
    protected void initEventAndData() {
        commonTheme();
        userFunctionBeanList = new ArrayList<>();
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("我的");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        ivRight.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        zzxxUrl = AppCommonModule.API_BASE_URL + "h5common/jyj-zizhi/index.html?jv=1";
        if (EnvConstant.isInsTypes) {
            addUserFunction("我的钱包", R.mipmap.icon_user_wallet, "jyj://main/mine/wallet", true);
            addUserFunction("我的订单", R.mipmap.icon_user_order, "jyj://main/mine/order", true);
            addUserFunction("我的资料", R.mipmap.icon_user_profile, "jyj://main/mine/userinfo", true);
            addUserFunction("我的地址", R.mipmap.icon_useraddress, "jyj://main/mine/address", true);
            addUserFunction("关于居友家", R.mipmap.icon_aboutjyj, "jyj://main/mine/about", false);
            addUserFunction("版本升级", R.mipmap.icon_update_check, "bbsj", false);
        } else {
            addUserFunction("我的钱包", R.mipmap.icon_user_wallet, "jyj://main/mine/wallet", true);
            addUserFunction("我的订单", R.mipmap.icon_user_order, "jyj://main/mine/order", true);
            addUserFunction("我的优惠券", R.mipmap.icon_user_coupon, "jyj://main/mine/coupon", true);
            addUserFunction("我的资料", R.mipmap.icon_user_profile, "jyj://main/mine/userinfo", true);
            addUserFunction("我的地址", R.mipmap.icon_useraddress, "jyj://main/mine/address", true);
            addUserFunction("关于居友家", R.mipmap.icon_aboutjyj, "jyj://main/mine/about", false);
            addUserFunction("版本升级", R.mipmap.icon_update_check, "bbsj", false);
            addUserFunction("资质信息", R.mipmap.icon_zzxx, zzxxUrl, false);
        }
        userFunctionAdapter = new UserFunctionAdapter(R.layout.item_user_function_division);
        recyclerViewUser.setLayoutManager(new GridLayoutManager(getBaseContext(), 3));
        recyclerViewUser.addItemDecoration(new GridDividerItemDecoration(20, getResources().getColor(R.color.color39)));
        recyclerViewUser.setAdapter(userFunctionAdapter);
        userFunctionAdapter.addData(userFunctionBeanList);
        userFunctionAdapter.notifyDataSetChanged();
        userFunctionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                String target = userFunctionAdapter.getItem(i).getTarget();
                if (userFunctionAdapter.getItem(i).isNeedLogin() && login == null) {
                    openLogin();
                } else {
                    if (target.contains("jyj://")) {
                        Intent intent = null;
                        PackageManager manager = getBaseContext().getPackageManager();
                        String scheme;
                        if (target.contains("?")) {
                            scheme = target + "&lytype=bd";
                        } else {
                            scheme = target + "?lytype=bd";
                        }
                        intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(scheme));
                        List list = manager.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);
                        if (list != null && list.size() > 0) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            ToastUtils.showLongToast("请升级版本后再使用此功能");
                        }
                    } else if (target.contains("http") || target.contains("https")) {
                        Intent intentWeb = new Intent(getBaseContext(), QuestionDetailesActivity.class);
                        intentWeb.putExtra("url", target);
                        intentWeb.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentWeb);
                    } else {
                        if (target.equals("bbsj")) {
                            getPresenter().getVersionResult();
                        /*    Intent intentWeb = new Intent(getBaseContext(), QuestionDetailesActivity.class);
                            intentWeb.putExtra("url", "file:///android_asset/Hybird.html");
                            intentWeb.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentWeb);*/
                        }
                    }
                }
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_user_setting;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }


    private void addUserFunction(String name, int ico, String target, boolean needLogin) {
        UserFunctionBean userFunctionBean = new UserFunctionBean();
        userFunctionBean.setUf_ico(ico);
        userFunctionBean.setUf_name(name);
        userFunctionBean.setTarget(target);
        userFunctionBean.setNeedLogin(needLogin);
        userFunctionBeanList.add(userFunctionBean);
    }

    @Override
    public void setVersionResult(VersionShowBean showBean) {
        hiddenLoading();
        if (showBean != null) {
            if (showBean.getVersion() > ConstUtils.getVersioncode(getBaseContext())) {
                downLoadingView = new DownLoadingView(getBaseContext(), userabout, showBean);
                downLoadingView.setUserPromptPopupWindowInterface(this);
            } else {
                new UptodateWindowView(getBaseContext(), userabout);
            }
        }
    }


    @Override
    public void setCancelOnClickListener() {
        if (EmptyUtils.isEmpty(downLoadingView)) {
            return;
        }
        downLoadingView.dismiss();
    }

    @Override
    public void setConfirmOnClickListener(String url) {
        UserSettingActivityPermissionsDispatcher.showReadOrWriteWithCheck(UserSettingActivity.this, url);
    }

    @Override
    public void setUpdateUserInfoResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
        } else {
            clearUserInfo();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10012) {
            if (Build.VERSION.SDK_INT >= 26) {
                boolean b = getBaseContext().getPackageManager().canRequestPackageInstalls();
                if (b) {
                    new InstallApk(this)
                            .installApk(new File(DataKeeper.fileRootPath, SoftUpdate.str_appname));
                } else {
                    toast("请赋予权限后在操作！");
                }
            }
        }


    }

    // 单个权限
    // @NeedsPermission(Manifest.permission.CAMERA)
    // 多个权限
    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showReadOrWrite(String url) {
        if (!EmptyUtils.isEmpty(downLoadingView)) {
            downLoadingView.dismiss();
        }
        SoftUpdate manager = new SoftUpdate(UserSettingActivity.this, url);
        manager.showDownloadDialog();
    }

    // 向用户说明为什么需要这些权限（可选）
    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationaleForReadOrWrite(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setTitle("软件更新")
                .setMessage("因为您关闭了居有家APP的手机存储权限,导致无法更新，需要开启才可以使用!")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();//再次执行请求
                    }
                })
                .show();
    }

    // 用户拒绝授权回调（可选）
    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForReadOrWrite() {
        toast("关闭权限将导致无法升级！");
    }

    // 用户勾选了“不再提醒”时调用（可选）
    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForReadOrWrite() {
        toast("关闭权限将导致无法升级！");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    startActivityForResult(intent, 10012);
                }
                break;
        }
        // 代理权限处理到自动生成的方法
        UserSettingActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


}
