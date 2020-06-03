
package com.idougong.jyj.module.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.AdvertiseInfoBean;
import com.idougong.jyj.model.FlickerScreenBean;
import com.idougong.jyj.model.HomeConfigurationInformationBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.TabBean;
import com.idougong.jyj.model.VersionShowBean;
import com.idougong.jyj.module.adapter.HomePageAdapter;
import com.idougong.jyj.module.contract.MainContract;
import com.idougong.jyj.module.fragment.TabCategorizeFirstFragment;
import com.idougong.jyj.module.fragment.TabCategorizeFourthFragment;
import com.idougong.jyj.module.fragment.TabCategorizeSecondFragment;
import com.idougong.jyj.module.fragment.TabCategorizeThirdFragment;
import com.idougong.jyj.module.presenter.MainPresenter;
import com.idougong.jyj.module.push.PushMessageDbOpenHelper;
import com.idougong.jyj.module.push.PushMessageDbOperation;
import com.idougong.jyj.module.ui.account.LoginActivity;
import com.idougong.jyj.module.ui.chat.QuestionDetailesActivity;
import com.idougong.jyj.module.ui.home.CreditsExchangeActivity;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.ConstUtils;
import com.idougong.jyj.utils.DataKeeper;
import com.idougong.jyj.utils.GetDeviceId;
import com.idougong.jyj.utils.InstallApk;
import com.idougong.jyj.utils.NetWatchdog;
import com.idougong.jyj.utils.SchemeJump;
import com.idougong.jyj.utils.SoftUpdate;
import com.idougong.jyj.utils.TargetClick;
import com.idougong.jyj.widget.CircleImage1View;
import com.idougong.jyj.widget.CircleImageView;
import com.idougong.jyj.widget.CustomizeTabLayout;
import com.idougong.jyj.widget.ImageView_286_383;
import com.idougong.jyj.widget.NoScrollViewPager;
import com.idougong.jyj.widget.dialog.DownLoadingView;
import com.idougong.jyj.widget.dialog.UptodateWindowView;
import com.lei.lib.java.rxcache.RxCache;
import com.lei.lib.java.rxcache.entity.CacheResponse;
import com.lei.lib.java.rxcache.util.RxUtil;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;
import com.umeng.message.PushAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.functions.Consumer;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * 主页
 *
 * @author wujiajun
 */
@RuntimePermissions
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View, DownLoadingView.DownLoadingViewInterface {
    private final static String TAG = "MainActivity";
    @BindView(R.id.tabLayout)
    CustomizeTabLayout tabLayout;
    @BindView(R.id.iv_tab_icon3)
    ImageView iv_tab_icon3;
    @BindView(R.id.tv_shoppingcart_number)
    TextView tvShoppingCartnumber;
    @BindView(R.id.vp_home)
    NoScrollViewPager vp_home;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    @BindView(R.id.rel_main)
    RelativeLayout rel_main;
    private ArrayList<Fragment> fragments;
    private ArrayList<TabBean> mTabbeans = new ArrayList<>();

    private String[] mTitles = {"首页", "分类", "菜篮", "我的"};

    private int[] mUnSelectIcons = {
            R.mipmap.tab1wx, R.mipmap.tab2wx, R.mipmap.tab3wx, R.mipmap.tab4wx};
    private int[] mSelectIcons = {
            R.mipmap.tab1yx, R.mipmap.tab2yx, R.mipmap.tab3yx, R.mipmap.tab4yx};

    private String[] mUnSelectUrl = new String[4];
    private String[] mSelectUrl = new String[4];
    HomeConfigurationInformationBean hcShow;
    private int mSelectColor = R.color.color37;
    private int mUnSelectColor = R.color.color51;

    private String tabBarNormalTextColor;
    private String tabBarSelectedTextColor;

    private final int REQUEST_PERMISION_CODE_CAMARE = 0;

    BroadcastReceiver broadcastReceiver;
    private int mCurrentTab;
    private PathMeasure mPathMeasure;
    private float[] mCurrentPosition = new float[2];


    Intent intent;
    private DownLoadingView downLoadingView;

    NetWatchdog netWatchdog;

    int openType = 0;

    @Override
    protected void initEventAndData() {
        commonTheme();
        status_bar_view.setVisibility(View.GONE);
        ActivityCollector.addActivity(this);
        schemeTz();
        webTz();
        addShoppingDateReceiver();
        addUpdateInfoReceiver();
        getHomeConfigurationInformationDataCache();
        getPresenter().getVersionResult();
        netWatchdog = new NetWatchdog(getBaseContext());
        netWatchdog.startWatch();
        netWatchdog.setNetConnectedListener(new NetWatchdog.NetConnectedListener() {
            @Override
            public void onReNetConnected(boolean isReconnect) {
                getPresenter().getShoppingNumber();
                getFlickerScreenDataCache();
            }

            @Override
            public void onNetUnConnected() {
                toast("请检查您的网络!");
            }
        });
        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        Log.i(TAG, "rid: " + rid);
        getPresenter().addPushMessageToken(XGPushConfig.getToken(MainActivity.this), PushAgent.getInstance(MainActivity.this).getRegistrationId(), JPushInterface.getRegistrationID(getApplicationContext()), GetDeviceId.getDeviceId(getBaseContext()));
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); //这一句必须的，否则Intent无法获得最新的数据
        //schemeTz();
        //webTz();
        String path = intent.getStringExtra("path");
        if (!EmptyUtils.isEmpty(path)) {
            if (path.equals("/home/category")) {
                int categoryid = intent.getIntExtra("categoryid", 0);
                categoryselect(1);
                Intent intent1 = new Intent("categorysp");
                intent1.putExtra("categoryid", categoryid);
                sendBroadcast(intent1);
            } else if (path.equals("/cart")) {
                categoryselect(2);
            } else if (path.equals("/mine")) {
                categoryselect(3);
            }
        }
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
        SchemeJump.schemeJump(getBaseContext(), path, keyId, keyContent, false);
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

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }


    public void getHomeConfigurationInformationDataCache() {
        Type type = new TypeToken<List<HomeConfigurationInformationBean>>() {
        }.getType();
        RxCache.getInstance()
                .<List<HomeConfigurationInformationBean>>get("pagingInformationBeanList", false, type)
                .compose(RxUtil.<CacheResponse<List<HomeConfigurationInformationBean>>>io_main())
                .subscribe(new Consumer<CacheResponse<List<HomeConfigurationInformationBean>>>() {
                    @Override
                    public void accept(CacheResponse<List<HomeConfigurationInformationBean>> listCacheResponse) throws Exception {
                        if (!EmptyUtils.isEmpty(listCacheResponse.getData()) && listCacheResponse.getData().size() > 0) {
                            for (int i = 0; i < listCacheResponse.getData().size(); i++) {
                                Date d1 = TimeUtils.string2Date(listCacheResponse.getData().get(i).getShowBegin(), "yyyy-MM-dd");
                                Date d2 = TimeUtils.string2Date(listCacheResponse.getData().get(i).getShowEnd(), "yyyy-MM-dd");
                                Date newdata = TimeUtils.string2Date(TimeUtils.date2String(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
                                if (newdata.compareTo(d1) != -1 && newdata.compareTo(d2) != 1) {
                                    if (i == 0) {
                                        hcShow = listCacheResponse.getData().get(i);
                                    } else {
                                        if (!EmptyUtils.isEmpty(hcShow)) {
                                            if (hcShow.getArchive() < listCacheResponse.getData().get(i).getArchive()) {
                                                hcShow = listCacheResponse.getData().get(i);
                                            }
                                        } else {
                                            hcShow = listCacheResponse.getData().get(i);
                                        }
                                    }
                                }
                            }
                            if (!EmptyUtils.isEmpty(hcShow)) {
                                mSelectUrl[0] = hcShow.getHomeSelected();
                                mUnSelectUrl[0] = hcShow.getHomeNormal();
                                mSelectUrl[1] = hcShow.getCategorySelected();
                                mUnSelectUrl[1] = hcShow.getCategoryNormal();
                                mSelectUrl[2] = hcShow.getShopCartSelected();
                                mUnSelectUrl[2] = hcShow.getShopCartNormal();
                                mSelectUrl[3] = hcShow.getMeSelected();
                                mUnSelectUrl[3] = hcShow.getMeNormal();
                                tabBarNormalTextColor = hcShow.getTabBarNormalTextColor();
                                tabBarSelectedTextColor = hcShow.getTabBarSelectedTextColor();
                            }
                        }
                        initFragments();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        initFragments();
                    }
                });
    }

    /**
     * 初始化主页5个fragment
     */
    private void initFragments() {
        fragments = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            addTab(i);
        }
        fragments.add(instantiateFragment(vp_home, 0, new TabCategorizeFirstFragment()));
        fragments.add(instantiateFragment(vp_home, 1, new TabCategorizeSecondFragment()));
        fragments.add(instantiateFragment(vp_home, 2, new TabCategorizeThirdFragment()));
        fragments.add(instantiateFragment(vp_home, 3, new TabCategorizeFourthFragment()));
        //设置viewpager的适配器
        vp_home.setAdapter(new HomePageAdapter(getSupportFragmentManager(), fragments));
        //viewpager会初始化左右两边各3个
        vp_home.setOffscreenPageLimit(1);
        tabLayout.setTabDate(mTabbeans);
        tabLayout.setmListener(new CustomizeTabLayout.OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 3) {
                    mCurrentTab = position;
                    status_bar_view.setBackgroundColor(getBaseContext().getResources().getColor(R.color.color37));
                    status_bar_view.setVisibility(View.VISIBLE);
                    vp_home.setCurrentItem(position, false);
                } else if (position == 2) {
                    status_bar_view.setBackgroundColor(getBaseContext().getResources().getColor(R.color.white));
                    status_bar_view.setVisibility(View.VISIBLE);
                    vp_home.setCurrentItem(position, false);
                }else  if(position==0){
                    mCurrentTab = position;
                    status_bar_view.setVisibility(View.GONE);
                    vp_home.setCurrentItem(position, false);
                }else {
                    mCurrentTab = position;
                    status_bar_view.setBackgroundColor(getBaseContext().getResources().getColor(R.color.white));
                    status_bar_view.setVisibility(View.VISIBLE);
                    vp_home.setCurrentItem(position, false);
                }
            }

            /**
             * 连续点击调用此方法
             */
            @Override
            public void onTabReselect(int position) {

            }
        });

    }

    private Fragment instantiateFragment(ViewPager viewPager, int position, Fragment defaultResult) {
        String tag = "android:switcher:" + viewPager.getId() + ":" + position;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        return fragment == null ? defaultResult : fragment;
    }

    public void addTab(int index) {
        TabBean tabBean1 = new TabBean();
        tabBean1.setTitle(mTitles[index]);
        tabBean1.setSelectUrl(mSelectUrl[index]);
        tabBean1.setUnSelectUrl(mUnSelectUrl[index]);
        if (EmptyUtils.isEmpty(tabBarSelectedTextColor)) {
            tabBean1.setmSelectColor(getBaseContext().getResources().getColor(mSelectColor));
        } else {
            try {
                tabBean1.setmSelectColor(Color.parseColor(tabBarSelectedTextColor));
            } catch (Exception e) {
                tabBean1.setmSelectColor(getBaseContext().getResources().getColor(mSelectColor));
            }
        }
        if (EmptyUtils.isEmpty(tabBarNormalTextColor)) {
            tabBean1.setmUnSelectColor(getBaseContext().getResources().getColor(mUnSelectColor));
        } else {
            try {
                tabBean1.setmUnSelectColor(Color.parseColor(tabBarNormalTextColor));
            } catch (Exception e) {
                tabBean1.setmUnSelectColor(getBaseContext().getResources().getColor(mUnSelectColor));
            }
        }
        tabBean1.setSelectIcons(mSelectIcons[index]);
        tabBean1.setUnSelectIcon(mUnSelectIcons[index]);
        mTabbeans.add(tabBean1);
    }


    private void goScanner() {

    }


    public void categoryselect(int position) {
        if (position == 3) {
            status_bar_view.setBackgroundColor(getBaseContext().getResources().getColor(R.color.color37));
            status_bar_view.setVisibility(View.VISIBLE);
        }else if(position==0){
            status_bar_view.setVisibility(View.GONE);
        }else {
            status_bar_view.setBackgroundColor(getBaseContext().getResources().getColor(R.color.white));
            status_bar_view.setVisibility(View.VISIBLE);
        }
        if (position == -1) {
            vp_home.setCurrentItem(mCurrentTab);
            tabLayout.setCurrentTab(mCurrentTab);
        } else {
            vp_home.setCurrentItem(position);
            tabLayout.setCurrentTab(position);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 2) {
            categoryselect(-1);
        }

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
        }
    }

    @Override
    public void showErrorMsg(String msg, int code) {
        super.showErrorMsg(msg, code);
        if (code == -1) {
            clearUserInfo();
            return;
        }
        toast(msg);
    }

    @Override
    public void setVersionResult(VersionShowBean versionShowBean) {
        if (versionShowBean != null) {
            if (versionShowBean.getVersion() > ConstUtils.getVersioncode(getBaseContext())) {
                downLoadingView = new DownLoadingView(getBaseContext(), vp_home, versionShowBean);
                downLoadingView.setUserPromptPopupWindowInterface(this);
            } else {
                if (openType == 1) {
                    openType = 0;
                    new UptodateWindowView(getBaseContext(), tabLayout);
                } else {
                    getPresenter().getAdvertiseInfo();
                }
            }
        } else {
            if (openType == 1) {
                openType = 0;
                new UptodateWindowView(getBaseContext(), tabLayout);
            } else {
                getPresenter().getAdvertiseInfo();
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
        MainActivityPermissionsDispatcher.showReadOrWriteWithCheck(MainActivity.this, url, 2);
    }

    @Override
    public void setUpdateUserInfoResult(LoginBean loginBean) {
        if (loginBean != null) {
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            userInfo(2);
        } else {
            clearUserInfo();
            userInfo(2);
        }
    }

    @Override
    public void addPushMessageTokenResult(String str) {
        //String content = "因为您关闭了居有家APP的读写手机权限\n请在设置中打开居有家APP的读写手机存储权限";
    }

    @Override
    public void setAdvertiseWindow(List<AdvertiseInfoBean> advertiseInfoBeans) {
        try {
            if (advertiseInfoBeans != null && advertiseInfoBeans.size() > 0 && !EmptyUtils.isEmpty(advertiseInfoBeans.get(0).getImg())) {
                new AdvertiseWindows(getBaseContext(), vp_home, advertiseInfoBeans.get(0));
            }
        } catch (Exception e) {
            Log.i(TAG, "setAdvertiseWindow: 获取数据失败！");
        }
    }

    @Override
    public void setShoppingNumber(String str) {
        if (EmptyUtils.isEmpty(str)) {
            tvShoppingCartnumber.setVisibility(View.GONE);
        } else {
            try {
                if (Integer.valueOf(str).intValue() == 0) {
                    tvShoppingCartnumber.setVisibility(View.GONE);
                } else {
                    tvShoppingCartnumber.setVisibility(View.VISIBLE);
                    tvShoppingCartnumber.setText(str);
                }
            } catch (Exception e) {
                Log.i(TAG, "setShoppingNumber: 非数字格式");
            }
        }
    }

    @Override
    public void setFlickerScreenResult(List<FlickerScreenBean> flickerScreenBeans) {
        if (flickerScreenBeans != null && flickerScreenBeans.size() > 0) {
            addFlickerScreenDataCache(flickerScreenBeans);
        }
    }

    public void userInfo(int cztype) {
        loadUserInfo();
    }


    // 单个权限
    // @NeedsPermission(Manifest.permission.CAMERA)
    // 多个权限
    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showReadOrWrite(String url, int type) {
        if (type == 1) {

        } else if (type == 2) {
            if (!EmptyUtils.isEmpty(downLoadingView)) {
                downLoadingView.dismiss();
            }
            SoftUpdate manager = new SoftUpdate(MainActivity.this, url);
            manager.showDownloadDialog();
        }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISION_CODE_CAMARE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goScanner();
                }
                return;
            case 2:
                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    startActivityForResult(intent, 10012);
                }
                break;
        }
        // 代理权限处理到自动生成的方法
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    private void addUpdateInfoReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadUserInfo();
                getPresenter().addPushMessageToken(XGPushConfig.getToken(getBaseContext()), PushAgent.getInstance(getBaseContext()).getRegistrationId(), JPushInterface.getRegistrationID(getBaseContext()), GetDeviceId.getDeviceId(getBaseContext()));
                getPresenter().getShoppingNumber();
            }
        };
        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction("userupdate");
        registerReceiver(broadcastReceiver, intentToReceiveFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }

        if (broadcastReceiverAddShopping != null) {
            unregisterReceiver(broadcastReceiverAddShopping);
        }

        if (netWatchdog != null) {
            netWatchdog.stopWatch();
        }
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


    public class AdvertiseWindows extends PopupWindow {

        public AdvertiseWindows(Context mContext, View parent, final AdvertiseInfoBean advertiseInfoBean) {
            View view = View.inflate(mContext, R.layout.popupwindow_advertise, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));

            setWidth(WindowManager.LayoutParams.FILL_PARENT);
            setHeight(WindowManager.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(false);
            setOutsideTouchable(false);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb2000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.CENTER, 0, 0);
            ImageView_286_383 advertiseImage = view.findViewById(R.id.advertiseImage);
            Glide.with(getBaseContext()).load(advertiseInfoBean.getImg()).diskCacheStrategy(DiskCacheStrategy.ALL).into(advertiseImage);
            advertiseImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (EmptyUtils.isEmpty(advertiseInfoBean.getTarget())) {
                        return;
                    }
                    if (advertiseInfoBean.isNeedLogin() == true && login == null) {
                        openLogin();
                    } else {
                        TargetClick.targetOnClick(getBaseContext(), advertiseInfoBean.getTarget());
                    }
                    dismiss();
                }
            });
            ImageView advertiseCloseimage = view.findViewById(R.id.advertise_closeimage);
            advertiseCloseimage.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                        }
                    }
            );
        }
    }

    public void versionChecking() {
        openType = 1;
        getPresenter().getVersionResult();
    }


    public void add2Cart(ImageView ivProductIcon, String scNumber) {
        try {
            if (Integer.valueOf(scNumber).intValue() == 0) {
                tvShoppingCartnumber.setVisibility(View.GONE);
            } else {
                tvShoppingCartnumber.setText(scNumber);
                tvShoppingCartnumber.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.i(TAG, "setShoppingNumber: 非数字格式");
            getPresenter().getShoppingNumber();
        }
        final CircleImage1View imageView = new CircleImage1View(MainActivity.this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(30, 30);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageDrawable(ivProductIcon.getDrawable());
        // 将执行动画的图片添加到开始位置。
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(160, 160);
        rel_main.addView(imageView, params);

        // 二、计算动画开始/结束点的坐标的准备工作
        // 得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = new int[2];

        rel_main.getLocationInWindow(parentLocation);
        // 得到商品图片的坐标（用于计算动画开始的坐标）
        int[] startLoc = new int[2];
        ivProductIcon.getLocationInWindow(startLoc);
        // 得到购物车图片的坐标(用于计算动画结束后的坐标)
        int[] endLoc = new int[2];
        iv_tab_icon3.getLocationInWindow(endLoc);
        // 三、计算动画开始结束的坐标
        // 开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = startLoc[0] - parentLocation[0] + ivProductIcon.getWidth() / 2;
        float startY = startLoc[1] - parentLocation[1] + ivProductIcon.getHeight() / 2;
        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLocation[0] + iv_tab_icon3.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1];

        // 四、计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);

        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        mPathMeasure = new PathMeasure(path, false);
        //★★★属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        imageView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(1000);
        // 匀速线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // ★★★★★获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                imageView.setTranslationX(mCurrentPosition[0]);
                imageView.setTranslationY(mCurrentPosition[1]);
            }
        });
        //   五、 开始执行动画
        valueAnimator.start();
        //   六、动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            //当动画结束后：
            @Override
            public void onAnimationEnd(Animator animation) {
                // 购物车的数量加1
                // 把移动的图片imageview从父布局里移除
                imageView.setLayerType(View.LAYER_TYPE_NONE, null);
                rel_main.removeView(imageView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    BroadcastReceiver broadcastReceiverAddShopping;

    private void addShoppingDateReceiver() {
        broadcastReceiverAddShopping = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String scNumber = intent.getStringExtra("numberShop");
                try {
                    if (Integer.valueOf(scNumber).intValue() == 0) {
                        tvShoppingCartnumber.setVisibility(View.GONE);
                    } else {
                        tvShoppingCartnumber.setVisibility(View.VISIBLE);
                        tvShoppingCartnumber.setText(scNumber);
                    }
                } catch (Exception e) {
                    Log.i(TAG, "setShoppingNumber: 非数字格式");
                    getPresenter().getShoppingNumber();
                }
            }
        };
        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction("shoppingNum");
        registerReceiver(broadcastReceiverAddShopping, intentToReceiveFilter);
    }


    public void addFlickerScreenDataCache(List<FlickerScreenBean> flickerScreenBeanList) {
        RxCache.getInstance()
                .put("flickerScreenBeanList", flickerScreenBeanList, 24 * 60 * 60 * 1000)  //key:缓存的key data:具体的数据 time:缓存的有效时间
                .compose(RxUtil.<Boolean>io_main()) //线程调度
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) Log.d("flickerScreenBeanList", "cache successful!");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    public void getFlickerScreenDataCache() {
        Type type = new TypeToken<List<FlickerScreenBean>>() {
        }.getType();
        RxCache.getInstance()
                .<List<FlickerScreenBean>>get("flickerScreenBeanList", false, type)
                .compose(RxUtil.<CacheResponse<List<FlickerScreenBean>>>io_main())
                .subscribe(new Consumer<CacheResponse<List<FlickerScreenBean>>>() {
                    @Override
                    public void accept(CacheResponse<List<FlickerScreenBean>> listCacheResponse) throws Exception {
                        if (EmptyUtils.isEmpty(listCacheResponse.getData()) || listCacheResponse.getData().size() == 0) {
                            getPresenter().getFlickerScreenInfo();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getPresenter().getFlickerScreenInfo();
                    }
                });
    }
}


