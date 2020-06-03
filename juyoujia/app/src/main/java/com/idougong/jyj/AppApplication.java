package com.idougong.jyj;


import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idougong.jyj.common.net.EnvConstant;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.ipsmap.homectrl.IpsLocationSDK;
import com.ipsmap.homectrl.receiver.MyReceiverUtil;
import com.ipsmap.homectrl.utils.IpsConstants;
import com.lei.lib.java.rxcache.RxCache;
import com.lei.lib.java.rxcache.converter.GsonConverter;
import com.lei.lib.java.rxcache.mode.CacheMode;
import com.idougong.jyj.common.di.AppCommonComponent;
import com.idougong.jyj.common.di.AppCommonModule;
import com.idougong.jyj.common.di.DaggerAppCommonComponent;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.module.push.UmengNotificationService;
import com.idougong.jyj.utils.ConstUtils;
import com.idougong.jyj.utils.DataKeeper;
import com.idougong.jyj.utils.LocationService;
import com.idougong.jyj.utils.ScreenUtil;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;


import java.lang.reflect.Type;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by wujiajun on 17/4/6.
 */

public class AppApplication extends MultiDexApplication {

    AppCommonComponent aComponent;
    public LocationService locationService;
    private static AppApplication instance;
    public static String currentUserNick = "";
    LoginBean loginBean;
    public static int qhnum = 0;
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DataKeeper.init(getApplicationContext());
        aComponent = DaggerAppCommonComponent.builder().appCommonModule(new AppCommonModule(this)).build();
        locationService = new LocationService(getApplicationContext());
        UMConfigure.setLogEnabled(false);
        UMConfigure.init(this, "5e44caf9cb23d2b11c000053", "Umeng", UMConfigure.DEVICE_TYPE_PHONE,
                "");
        PlatformConfig.setWeixin(EnvConstant.WX_KEY, "28b1c45ed39af6e66cec511f91839e61");
        PlatformConfig.setSinaWeibo("", "", "http://sns.whalecloud.com");
        PlatformConfig.setQQZone("", "");
        //initUpush();
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=5da1dbd9");
        RxCache.init(this);
        new RxCache.Builder()
                .setDebug(true)   //开启debug，开启后会打印缓存相关日志，默认为true
                .setConverter(new GsonConverter())  //设置转换方式，默认为Gson转换
                .setCacheMode(CacheMode.BOTH)   //设置缓存模式，默认为二级缓存
                .setMemoryCacheSizeByMB(50)   //设置内存缓存的大小，单位是MB
                .setDiskCacheSizeByMB(100)    //设置磁盘缓存的大小，单位是MB
                .setDiskDirName("JyjCache")    //设置磁盘缓存的文件夹名称
                .build();
        SPUtils spUtils = new SPUtils(ConstUtils.CLEARUSERINFO);
        if (spUtils.getInt(ConstUtils.CLEARUSERINFOKEY, 0) == 0) {
            spUtils.put(ConstUtils.CLEARUSERINFOKEY, 1);
            SPUtils spUtils1 = new SPUtils("saveUser");
            spUtils1.remove("userInfo");
        }
        // 打开Logcat输出，上线时，一定要关闭
        StatConfig.setDebugEnable(false);
        // 注册activity生命周期，统计时长
        StatService.registerActivityLifecycleCallbacks(this);
        ScreenUtil.init(this);
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        if (EnvConstant.isInsTypes) {
            try {
                IpsLocationSDK.init(new IpsLocationSDK.Configuration.Builder(getApplicationContext())
                        .debug(true)
                        .env(EnvConstant.isDevEnv ? IpsConstants.ENV_DEEBUG : IpsConstants.ENV_RELEASE)
                        .appKey(EnvConstant.IPSMAP_APP_KEY)
                        .appSecret(EnvConstant.IPSMAP_APP_SCRITE)
                        .build());
            } catch (Exception e) {
                Log.i("IpsLocationSDK", "onCreate:初始化出错！");
            }
        }
        //ocr初始化接口
        OCR.getInstance(getBaseContext()).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                // 调用成功，返回AccessToken对象
                String token = result.getAccessToken();
                Log.i("OCR", "onCreate:"+token);
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError子类SDKError对象
            }
        }, getApplicationContext());
        //XGPushConfig.enableDebug(this, false);
    }


    private void initUpush() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        handler = new Handler(getMainLooper());

        //sdk开启通知声音
        //mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        // sdk关闭通知声音
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        //使用完全自定义处理
        mPushAgent.setPushIntentServiceClass(UmengNotificationService.class);

        //注册推送服务 每次调用register都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                Log.i("mPushAgent", "device token: " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.i("mPushAgent", "register failed: " + s + " " + s1);
            }
        });

    }

    public AppCommonComponent getAppComponent() {
        return aComponent;
    }


    public static AppApplication getInstance() {
        return instance;
    }

    public static LoginBean getUserInfo() {
        SPUtils spUtils = new SPUtils("saveUser");
        String respone = spUtils.getString("userInfo");
        if (respone != null) {
            Type type = new TypeToken<LoginBean>() {
            }.getType();
            return new Gson().fromJson(respone, type);
        } else {
            return null;
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        MyReceiverUtil.getInstance(this).utilDestory();
    }
}
