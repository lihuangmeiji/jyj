package com.idougong.jyj.module.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.ConvenientFunctionsBean;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.HomeMenu;
import com.idougong.jyj.model.HomeSDKStatus;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.VersionShowBean;
import com.idougong.jyj.module.adapter.UserServiceFunctionAdapter;
import com.idougong.jyj.module.contract.Main1ActivityContract;
import com.idougong.jyj.module.presenter.Main1ActivityPresenter;
import com.idougong.jyj.module.push.PushMessageDbOpenHelper;
import com.idougong.jyj.module.push.PushMessageDbOperation;
import com.idougong.jyj.module.ui.account.LoginActivity;
import com.idougong.jyj.module.ui.chat.QuestionDetailesActivity;
import com.idougong.jyj.module.ui.users.UserSettingActivity;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.ConstUtils;
import com.idougong.jyj.utils.CornerTransform;
import com.idougong.jyj.utils.DataKeeper;
import com.idougong.jyj.utils.GetDeviceId;
import com.idougong.jyj.utils.InstallApk;
import com.idougong.jyj.utils.SchemeJump;
import com.idougong.jyj.utils.SoftUpdate;
import com.idougong.jyj.utils.TargetClick;
import com.ipsmap.homectrl.IpsLocationBackgroundListener;
import com.ipsmap.homectrl.uploadlocation.DaemonEnv;
import com.ipsmap.homectrl.uploadlocation.IntentWrapper;
import com.ipsmap.homectrl.uploadlocation.bean.BackgroundData;
import com.ipsmap.homectrl.uploadlocation.impl.TraceServiceImpl;
import com.ipsmap.homectrl.utils.L;
import com.ipsmap.homectrl.utils.T;
import com.tencent.android.tpush.XGPushConfig;
import com.umeng.message.PushAgent;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class Main1Activity extends BaseActivity<Main1ActivityPresenter> implements Main1ActivityContract.View {
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    @BindView(R.id.rv_jyj_service)
    RecyclerView rvJyjService;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rel_jksb)
    RelativeLayout rel_jksb;
    @BindView(R.id.rel_jtxx)
    RelativeLayout rel_jtxx;
    @BindView(R.id.rel_sqll)
    RelativeLayout rel_sqll;

    UserServiceFunctionAdapter userServiceFunctionAdapter;

    HomeMenu homeMenu1;

    boolean isTraceServic = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_main1;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        ActivityCollector.addActivity(this);
        schemeTz();
        webTz();
        SPUtils spUtils = new SPUtils(ConstUtils.ZIQIDONGNAME);
        if (spUtils.getInt(ConstUtils.ZIQIDONGKEY, 0) == 0) {
            spUtils.put(ConstUtils.ZIQIDONGKEY, 1);
            IntentWrapper.whiteListMatters(Main1Activity.this, "轨迹跟踪服务的持续运行");
            IntentWrapper.autoStartListMatters(Main1Activity.this, "保证应用正常运行");
        }
        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        Log.i("Main1Activity", "rid: " + rid);
        getPresenter().getUpdateUserInfoResult();
        getPresenter().getFunctionDivisionOne();
        getPresenter().getHomeBannerResult();
        getPresenter().getVersionResult();
        getPresenter().getHomeMenu();
        getPresenter().getHomeSDKStatus();
        getPresenter().addPushMessageToken("", "", JPushInterface.getRegistrationID(getApplicationContext()), GetDeviceId.getDeviceId(getBaseContext()));
        status_bar_view.setVisibility(View.GONE);
        rvJyjService.setLayoutManager(new GridLayoutManager(getBaseContext(), 4));
        userServiceFunctionAdapter = new UserServiceFunctionAdapter(R.layout.item_home_function_division);
        rvJyjService.setAdapter(userServiceFunctionAdapter);
        userServiceFunctionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (userServiceFunctionAdapter.getItem(i).isStatus()) {
                    if (EmptyUtils.isEmpty(userServiceFunctionAdapter.getItem(i).getTarget())) {
                        return;
                    }
                    if (userServiceFunctionAdapter.getItem(i).isNeedLogin() == true && login == null) {
                        openLogin();
                    } else {
                        TargetClick.targetOnClick(getBaseContext(), userServiceFunctionAdapter.getItem(i).getTarget());
                    }
                } else {
                    toast("即将上线，敬请期待!");
                }
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); //这一句必须的，否则Intent无法获得最新的数据
        schemeTz();
        webTz();
    }

    public void schemeTz() {
        // 访问路径
        String path = getIntent().getStringExtra("path");
        String keyContent = getIntent().getStringExtra("keyContent");
        int keyId = getIntent().getIntExtra("keyId", 0);
        if (EmptyUtils.isEmpty(path)) {
            Log.i("path", "initEventAndData: " + path);
            return;
        }
        boolean isPeoplemgr = false;
        if (login != null) {
            isPeoplemgr = login.isPeoplemgr();
        }
        SchemeJump.schemeJump(getBaseContext(), path, keyId, keyContent, isPeoplemgr);
    }

    public void webTz() {
        String urlPath = getIntent().getStringExtra("urlPath");
        if (EmptyUtils.isEmpty(urlPath)) {
            return;
        }
        Intent intentWeb = new Intent(getBaseContext(), QuestionDetailesActivity.class);
        intentWeb.putExtra("url", urlPath);
        intentWeb.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentWeb);

    }


    @OnClick({R.id.rel_jksb,
            R.id.rel_jtxx,
            R.id.rel_sqll,
            R.id.rel_install
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rel_jksb:
                if (isTraceServic) {
                    if (!EmptyUtils.isEmpty(homeMenu1.getHealthTarget())) {
                        if (homeMenu1.getHealthTarget().contains("http") || homeMenu1.getHealthTarget().contains("https")) {
                            TargetClick.targetOnClick(getBaseContext(), homeMenu1.getHealthTarget());
                        } else {
                            toast(homeMenu1.getHealthTarget());
                        }
                    }
                } else {
                    getPresenter().getHomeSDKStatus();
                }
                break;
            case R.id.rel_jtxx:
                if (!EmptyUtils.isEmpty(homeMenu1.getFamilyTarget())) {
                    if (homeMenu1.getFamilyTarget().contains("http") || homeMenu1.getFamilyTarget().contains("https")) {
                        TargetClick.targetOnClick(getBaseContext(), homeMenu1.getFamilyTarget());
                    } else {
                        toast(homeMenu1.getFamilyTarget());
                    }
                }
                break;
            case R.id.rel_sqll:
                if (!EmptyUtils.isEmpty(homeMenu1.getImTarget())) {
                    if (homeMenu1.getImTarget().contains("http") || homeMenu1.getImTarget().contains("https")) {
                        TargetClick.targetOnClick(getBaseContext(), homeMenu1.getImTarget());
                    } else {
                        toast(homeMenu1.getImTarget());
                    }
                }
                break;
            case R.id.rel_install:
                Intent intent=new Intent(getBaseContext(),UserSettingActivity.class);
                startActivity(intent);
                break;
        }
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
    public void setHomeBannerResult(List<HomeBannerBean> homeBannerBeanList) {
        List<String> stringList = new ArrayList<>();
        if (homeBannerBeanList != null && homeBannerBeanList.size() > 0) {
            for (int i = 0; i < homeBannerBeanList.size(); i++) {
                stringList.add(homeBannerBeanList.get(i).getImg());
            }
        }
        loadTestDatas(stringList, homeBannerBeanList);
    }

    @Override
    public void setFunctionDivisionOne(List<ConvenientFunctionsBean> convenientFunctionsBeanList) {
        userServiceFunctionAdapter.addData(convenientFunctionsBeanList);
        userServiceFunctionAdapter.notifyDataSetChanged();
    }

    @Override
    public void setVersionResult(VersionShowBean versionShowBean) {
        if (versionShowBean != null) {
            if (versionShowBean.getVersion() > ConstUtils.getVersioncode(getBaseContext())) {
                new DownLoadingView(getBaseContext(), rvJyjService, versionShowBean);
            }
        }
    }

    @Override
    public void addPushMessageTokenResult(String str) {

    }

    @Override
    public void setHomeMenu(HomeMenu homeMenu) {
        if (!EmptyUtils.isEmpty(homeMenu)) {
            homeMenu1 = homeMenu;
            if (EmptyUtils.isEmpty(homeMenu1.getHealthTarget())) {
                rel_jksb.setVisibility(View.GONE);
            } else {
                rel_jksb.setVisibility(View.VISIBLE);
            }
            if (EmptyUtils.isEmpty(homeMenu1.getFamilyTarget())) {
                rel_jtxx.setVisibility(View.GONE);
            } else {
                rel_jtxx.setVisibility(View.VISIBLE);
            }
            if (EmptyUtils.isEmpty(homeMenu1.getImTarget())) {
                rel_sqll.setVisibility(View.GONE);
            } else {
                rel_sqll.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setHomeSDKStatus(HomeSDKStatus homeSDKStatus) {
        if (!EmptyUtils.isEmpty(homeSDKStatus)) {
            if (homeSDKStatus.isSDKStatus()) {
                Main1ActivityPermissionsDispatcher.showTraceServicWithCheck(this, 1);
            } else {
                isTraceServic = true;
            }
        } else {
            toast("SDK控制返回数据有误！");
        }
    }

    @Override
    public void setUpdateUserInfoResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
        } else {
            clearUserInfo();
            loadUserInfo();
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
                Intent intent = new Intent(Main1Activity.this, LoginActivity.class);
                intent.putExtra("tzType", 1);
                startActivity(intent);
                finish();
            }
            return;
        }else if (code == -2) {
            Intent intent = new Intent(Main1Activity.this, LoginActivity.class);
            intent.putExtra("tzType", 1);
            startActivity(intent);
            finish();
        }
        //toast(msg);
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hiddenLoading() {
        super.hiddenLoading();
    }

    private void loadTestDatas(List<String> stringList, final List<HomeBannerBean> homeBannerBeanList) {
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int index) {
                if (homeBannerBeanList != null && homeBannerBeanList.size() > 0 && homeBannerBeanList.get(index) != null) {
                    if (EmptyUtils.isEmpty(homeBannerBeanList.get(index).getTarget())) {
                        return;
                    }
                    if (homeBannerBeanList.get(index).isNeedLogin() == true && login == null) {
                        openLogin();
                    } else {
                        TargetClick.targetOnClick(getBaseContext(), homeBannerBeanList.get(index).getTarget());
                    }
                }
            }
        });
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context)
                        .load(path)
                        .asBitmap()
                        .skipMemoryCache(true)
                        .error(R.mipmap.homebannermr)
                        .fallback(R.mipmap.homebannermr)
                        .into(imageView);
            }
        });
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.ForegroundToBackground);
        //设置图片集合
        banner.setImages(stringList);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //防止华为机型未加入白名单时按返回键回到桌面再锁屏后几秒钟进程被杀
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        IntentWrapper.onBackPressed(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TraceServiceImpl.stopService();
    }

    public class DownLoadingView extends PopupWindow {

        public DownLoadingView(Context mContext, View parent, final VersionShowBean versionShowBean) {

            View view = View.inflate(mContext, R.layout.down_loading, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));
            // LinearLayout ll_popup = (LinearLayout) view
            // .findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
            // R.anim.push_bottom_in_1));

            setWidth(WindowManager.LayoutParams.FILL_PARENT);
            setHeight(WindowManager.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(false);
            setOutsideTouchable(false);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.CENTER, 0, 0);
            ImageView iv_dialog_cancel = view.findViewById(R.id.iv_dialog_cancel);
            TextView btn_dialog_confirm = (TextView) view.findViewById(R.id.btn_dialog_confirm);
            TextView btn_dialog_cancel = (TextView) view.findViewById(R.id.btn_dialog_cancel);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
            if (!EmptyUtils.isEmpty(versionShowBean)) {
                tv_content.setText(versionShowBean.getMsg().replace("\\n", "\n"));
                if (versionShowBean.getForceUpdate() == 1) {
                    iv_dialog_cancel.setVisibility(View.GONE);
                } else {
                    iv_dialog_cancel.setVisibility(View.VISIBLE);
                }
                btn_dialog_confirm.setVisibility(View.VISIBLE);
            } else {
                btn_dialog_confirm.setVisibility(View.GONE);
            }
            iv_dialog_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            btn_dialog_confirm.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    //String url = versionShowBean.getUrl();
                    dismiss();
                    Main1ActivityPermissionsDispatcher.showReadOrWriteWithCheck(Main1Activity.this, versionShowBean.getUrl(), 1);
                }
            });
        }
    }


    public class ReminderLoadingView extends PopupWindow {

        public ReminderLoadingView(Context mContext, View parent, int type) {

            View view = View.inflate(mContext, R.layout.confirm_loading, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));
            // LinearLayout ll_popup = (LinearLayout) view
            // .findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
            // R.anim.push_bottom_in_1));

            setWidth(WindowManager.LayoutParams.FILL_PARENT);
            setHeight(WindowManager.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(false);
            setOutsideTouchable(false);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.CENTER, 0, 0);
            TextView btn_dialog_confirm = (TextView) view.findViewById(R.id.btn_dialog_confirm);
            TextView btn_dialog_cancel = (TextView) view.findViewById(R.id.btn_dialog_cancel);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            if (type == 1) {
                String content = "因为您关闭了居有家APP的读写手机权限\n请在设置中打开居有家APP的读写手机存储权限";
                tv_content.setText(content.replace("\\n", "\n"));
                tv_title.setText("无法自动更新");
            } else if (type == 2) {
                btn_dialog_cancel.setVisibility(View.VISIBLE);
                tv_title.setText("权限开启");
                tv_content.setText("根据健康上报政策要求,需开启GPS,蓝牙,电话权限");
            }
            btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            btn_dialog_confirm.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //String url = versionShowBean.getUrl();
                    dismiss();
                    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.fromParts("package", getBaseContext().getPackageName(), null));
                    if (intent.resolveActivity(getBaseContext().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });
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
                    // 退出程序
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }
            }
        } else if (requestCode == 1 && resultCode == 1) {
            loadUserInfo();
            getPresenter().addPushMessageToken("", "", JPushInterface.getRegistrationID(getApplicationContext()), GetDeviceId.getDeviceId(getBaseContext()));
        }
    }

    // 单个权限
    // @NeedsPermission(Manifest.permission.CAMERA)
    // 多个权限
    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showReadOrWrite(String url, int type) {
        if (type == 1) {
            SoftUpdate manager = new SoftUpdate(Main1Activity.this, url);
            manager.showDownloadDialog();
        }
    }

    // 向用户说明为什么需要这些权限（可选）
    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationaleForReadOrWrite(final PermissionRequest request) {

    }

    // 用户拒绝授权回调（可选）
    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForReadOrWrite() {
        Toast.makeText(this, "小主，给个权限吧！", Toast.LENGTH_SHORT).show();
    }

    // 用户勾选了“不再提醒”时调用（可选）
    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForReadOrWrite() {
        //Toast.makeText(this, "很开心，以后不打扰你了！", Toast.LENGTH_SHORT).show();
        new ReminderLoadingView(getBaseContext(), rvJyjService, 1);
        //finish();
    }


    // 单个权限
    // @NeedsPermission(Manifest.permission.CAMERA)
    // 多个权限
    @NeedsPermission({Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showTraceServic(int type) {
        //toast("开始初始化");
        Log.i("MainActivity", "showTraceServic: 开始");
        TraceServiceImpl.setAutoTurnOnBT(true);
        //传入用户的 id
        if (login != null) {
            TraceServiceImpl.bindDevno(String.valueOf(login.getId()));
        }
        DaemonEnv.startServiceSafely(new Intent(Main1Activity.this, TraceServiceImpl.class));
        TraceServiceImpl.registerBackgroundListerner(new IpsLocationBackgroundListener() {
            @Override
            public void onReceiveBackGroundLocation(BackgroundData backgroundData) {
                L.e("dddd", backgroundData.toString());
            }
        });
        Log.i("MainActivity", "showTraceServic: 结束");
        isTraceServic = true;
    }

    // 向用户说明为什么需要这些权限（可选）
    @OnShowRationale({Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationaleForTraceServic(final PermissionRequest request) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                new ReminderLoadingView(getBaseContext(), rvJyjService, 2);
            }
        }, 800);
    }

    // 用户拒绝授权回调（可选）
    @OnPermissionDenied({Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForTraceServic() {
        Toast.makeText(this, "小主，给个权限吧！", Toast.LENGTH_SHORT).show();
    }

    // 用户勾选了“不再提醒”时调用（可选）
    @OnNeverAskAgain({Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForTraceServic() {
        //Toast.makeText(this, "很开心，以后不打扰你了！", Toast.LENGTH_SHORT).show();
        //new ReminderLoadingView(getBaseContext(), rvJyjService);
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
        Main1ActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    //双击手机返回键退出<<<<<<<<<<<<<<<<<<<<<
    private long firstTime = 0;//第一次返回按钮计时


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    toast("再按一次退出");
                    firstTime = secondTime;
                } else {//完全退出
                    moveTaskToBack(false);//应用退到后台
                    System.exit(0);
                }
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }


}