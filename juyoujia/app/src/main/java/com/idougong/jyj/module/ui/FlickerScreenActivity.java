package com.idougong.jyj.module.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.reflect.TypeToken;
import com.idougong.jyj.common.net.EnvConstant;
import com.idougong.jyj.module.ui.account.LoginActivity;
import com.idougong.jyj.utils.NetWatchdog;
import com.lei.lib.java.rxcache.RxCache;
import com.lei.lib.java.rxcache.entity.CacheResponse;
import com.lei.lib.java.rxcache.util.RxUtil;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.SimpleActivity;
import com.idougong.jyj.model.FlickerScreenBean;
import com.idougong.jyj.module.ui.chat.QuestionDetailesActivity;
import com.idougong.jyj.utils.LocationService;
import com.idougong.jyj.utils.SchemeJump;
import com.idougong.jyj.utils.SharedPreferencesHelper;
import com.idougong.jyj.utils.TargetClick;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class FlickerScreenActivity extends SimpleActivity {
    @BindView(R.id.pager_guide)
    ViewPager viewPager;
    @BindView(R.id.btn_guide)
    Button btn_guide;
    @BindView(R.id.director)
    LinearLayout director;
    @BindView(R.id.lin_fs)
    LinearLayout lin_fs;
    @BindView(R.id.tv_fs_contnet)
    TextView tv_fs_contnet;
    List<View> list;
    @BindView(R.id.iv_fs_path)
    ImageView iv_fs_path;
    @BindView(R.id.rel_bg_fs)
    RelativeLayout rel_bg_fs;

    private LocationService locationService;
    Intent intent;
    String path;
    int keyId;
    int djs = 3;
    String strTz;
    String keyContent;
    FlickerScreenBean flickerScreenBean;

    @Override
    protected int getContentView() {

        return R.layout.activity_flicker_screen;
    }

    @Override
    protected void initEventAndData() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //设置全屏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN);
        rel_bg_fs.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN);
        Intent intent1 = getIntent();
        Uri uri = intent1.getData();
        if (uri != null) {
            // 完整的url信息
            String url = uri.toString();
            // scheme部分
            String scheme = uri.getScheme();
            // host部分
            String host = uri.getHost();
            //port部分
            int port = uri.getPort();
            // 访问路径
            path = uri.getPath();
            if(path.equals("/product/details")){
                try {
                    keyId = Integer.valueOf(uri.getQueryParameter("id")).intValue();
                } catch (Exception e) {
                    keyId = 0;
                }
            }
            if(path.equals("/home/category")){
                try {
                    keyId = Integer.valueOf(uri.getQueryParameter("id")).intValue();
                } catch (Exception e) {
                    keyId = 0;
                }
            }
            if(path.equals("/home/seckill")) {
                try {
                    keyId = Integer.valueOf(uri.getQueryParameter("point")).intValue();
                } catch (Exception e) {
                    keyId = 0;
                }
            }
            if(path.equals("/search/product")) {
                keyContent = uri.getQueryParameter("key");
            }
            if (uri.getQueryParameter("lytype").equals("bd")) {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.0f;
                getWindow().setAttributes(lp);
                boolean isPeoplemgr = false;
                if (login != null) {
                    isPeoplemgr = login.isPeoplemgr();
                }
                SchemeJump.schemeJump(getBaseContext(), path, keyId, keyContent, isPeoplemgr);
                finish();
                return;
            }
        }
        FlickerScreenActivityPermissionsDispatcher.showLocationWithCheck(this);
        getFlickerScreenDataCache();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); //这一句必须的，否则Intent无法获得最新的数据
        flickerScreenBean = new FlickerScreenBean();
        Intent intent1 = getIntent();
        Uri uri = intent1.getData();
        if (uri != null) {
            // 完整的url信息
            String url = uri.toString();
            // scheme部分
            String scheme = uri.getScheme();
            // host部分
            String host = uri.getHost();
            //port部分
            int port = uri.getPort();
            // 访问路径
            path = uri.getPath();
            if(path.equals("/product/details")){
                try {
                    keyId = Integer.valueOf(uri.getQueryParameter("id")).intValue();
                } catch (Exception e) {
                    keyId = 0;
                }
            }
            if(path.equals("/home/category")){
                try {
                    keyId = Integer.valueOf(uri.getQueryParameter("id")).intValue();
                } catch (Exception e) {
                    keyId = 0;
                }
            }
            if(path.equals("/search/product")) {
                keyContent = uri.getQueryParameter("key");
            }
            if(path.equals("/home/seckill")) {
                try {
                    keyId = Integer.valueOf(uri.getQueryParameter("point")).intValue();
                } catch (Exception e) {
                    keyId = 0;
                }
            }
            openMain();
        }
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
                            openMain();
                        } else {
                            for (int i = 0; i < listCacheResponse.getData().size(); i++) {
                                Date d1 = TimeUtils.string2Date(listCacheResponse.getData().get(i).getShowBegin(), "yyyy-MM-dd");
                                Date d2 = TimeUtils.string2Date(listCacheResponse.getData().get(i).getShowEnd(), "yyyy-MM-dd");
                                Date newdata = TimeUtils.string2Date(TimeUtils.date2String(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
                                if (newdata.compareTo(d1) != -1 && newdata.compareTo(d2) != 1) {
                                    if (i == 0) {
                                        flickerScreenBean = listCacheResponse.getData().get(i);
                                    } else {
                                        if (!EmptyUtils.isEmpty(flickerScreenBean)) {
                                            if (flickerScreenBean.getArchive() < listCacheResponse.getData().get(i).getArchive()) {
                                                flickerScreenBean = listCacheResponse.getData().get(i);
                                            }
                                        } else {
                                            flickerScreenBean = listCacheResponse.getData().get(i);
                                        }
                                    }
                                }
                            }
                            if (!EmptyUtils.isEmpty(flickerScreenBean) && !EmptyUtils.isEmpty(flickerScreenBean.getImg())) {
                                strTz = flickerScreenBean.getTarget();
                                Glide.with(getBaseContext())
                                        .load(flickerScreenBean.getImg())
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                                                Display display = getWindowManager().getDefaultDisplay();
                                                Point size = new Point();
                                                display.getSize(size);
                                                Bitmap bmp = Bitmap.createScaledBitmap(bitmap, size.x, size.y, true);
                                                iv_fs_path.setImageBitmap(bmp);
                                                tv_fs_contnet.setVisibility(View.VISIBLE);
                                                tv_fs_contnet.setText(djs + " 跳过");
                                                mHandler.sendEmptyMessageDelayed(1, 1000);
                                            }
                                        });

                            } else {
                                openMain();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        openMain();
                    }
                });
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    tv_fs_contnet.setText(djs + " 跳过");
                    if (djs == 0) {
                        openMain();
                    } else {
                        djs--;
                        mHandler.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
            }
        }
    };

    @OnClick({R.id.iv_fs_path,
            R.id.tv_fs_contnet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_fs_path:
                if (!EmptyUtils.isEmpty(strTz)) {
                    //跳转到主页
                    if (strTz.contains("http://") || strTz.contains("https://") || strTz.contains("HTTP://") || strTz.contains("HTTPS://")) {

                    } else {
                        TargetClick.targetOnClick(getBaseContext(), strTz);
                    }
                }
                break;
            case R.id.tv_fs_contnet:
                //跳转到主页
                openMain();
                break;
        }
    }

    private void openMain() {
        if (EnvConstant.isInsTypes) {
            if (login != null) {
                intent = new Intent(FlickerScreenActivity.this, Main1Activity.class);
                intent.putExtra("urlPath", strTz);
                intent.putExtra("path", path);
                intent.putExtra("keyContent", keyContent);
                intent.putExtra("keyId", keyId);
                startActivity(intent);
                finish();
            } else {
                intent = new Intent(FlickerScreenActivity.this, LoginActivity.class);
                intent.putExtra("tzType", 1);
                startActivity(intent);
                finish();
            }
        } else {
            intent = new Intent(FlickerScreenActivity.this, MainActivity.class);
            intent.putExtra("urlPath", strTz);
            intent.putExtra("path", path);
            intent.putExtra("keyContent", keyContent);
            intent.putExtra("shopId", keyId);
            startActivity(intent);
            finish();
        }
    }

    /***
     * Stop location service
     */
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        if (locationService != null) {
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                if (EmptyUtils.isEmpty(SharedPreferencesHelper.getStringSF(getBaseContext(), "locality"))) {
                    System.out.println("--------反馈信息----------lat" + location.getLatitude() + "|lng" + location.getLongitude() + "|" + location.getProvince() + location.getCity());
                    String address;
                    if (!EmptyUtils.isEmpty(location.getCity()) && !EmptyUtils.isEmpty(location.getProvince())) {
                        address = location.getProvince() + location.getCity();
                    } else {
                        address = null;
                    }
                    SharedPreferencesHelper.putStringSF(FlickerScreenActivity.this, "locality", address);
                    SharedPreferencesHelper.putStringSF(FlickerScreenActivity.this, "lat", location.getLatitude() + "");
                    SharedPreferencesHelper.putStringSF(FlickerScreenActivity.this, "lng", location.getLongitude() + "");
                } else {
                    if (locationService != null) {
                        locationService.unregisterListener(mListener); //注销掉监听
                        locationService.stop(); //停止定位服务
                    }
                }
            }
        }
    };

    // 单个权限
    // @NeedsPermission(Manifest.permission.CAMERA)
    // 多个权限
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE})
    void showLocation() {
        SharedPreferencesHelper.putStringSF(FlickerScreenActivity.this, "locality", null);
        locationService = new LocationService(getApplicationContext());
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();

    }

    // 向用户说明为什么需要这些权限（可选）
    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE})
    void showRationaleForLocation(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("位置获取")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();//再次执行请求
                    }
                })
                .show();
    }

    // 用户拒绝授权回调（可选）
    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE})
    void showDeniedForLocation() {
        Toast.makeText(this, "小主，给个权限吧！", Toast.LENGTH_SHORT).show();
    }

    // 用户勾选了“不再提醒”时调用（可选）
    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE})
    void showNeverAskForLocation() {
        Toast.makeText(this, "很开心，以后不打扰你了！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 代理权限处理到自动生成的方法
        FlickerScreenActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}

