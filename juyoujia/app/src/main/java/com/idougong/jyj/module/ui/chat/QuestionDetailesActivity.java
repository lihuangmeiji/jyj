package com.idougong.jyj.module.ui.chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idougong.jyj.BuildConfig;
import com.idougong.jyj.R;
import com.idougong.jyj.common.net.Constant;
import com.idougong.jyj.common.net.EnvConstant;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.CardInfoBean;
import com.idougong.jyj.model.DeviceInfoBean;
import com.idougong.jyj.model.LocationInfoBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.NativeInteractionResultBean;
import com.idougong.jyj.model.UserRechargeWxBean;
import com.idougong.jyj.model.UserShareBean;
import com.idougong.jyj.model.VersionShowBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.QuestionDetailesContract;
import com.idougong.jyj.module.presenter.QuestionDetailesPresenter;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.AndroidBug5497Workaround;
import com.idougong.jyj.utils.AppMarketUtils;
import com.idougong.jyj.utils.ConstUtils;
import com.idougong.jyj.utils.DataKeeper;
import com.idougong.jyj.utils.InstallApk;
import com.idougong.jyj.utils.LocationService;
import com.idougong.jyj.utils.OpenOtherAppUtil;
import com.idougong.jyj.utils.PhotoUtils;
import com.idougong.jyj.utils.SharedPreferencesHelper;
import com.idougong.jyj.utils.SoftUpdate;
import com.idougong.jyj.utils.TargetClick;
import com.idougong.jyj.widget.SystemUtil;
import com.idougong.jyj.widget.dialog.DownLoadingView;
import com.idougong.jyj.widget.dialog.UptodateWindowView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import sign.AuthResult;
import sign.PayResult;

@RuntimePermissions
public class QuestionDetailesActivity extends BaseActivity<QuestionDetailesPresenter> implements QuestionDetailesContract.View, DownLoadingView.DownLoadingViewInterface {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView iv_right;
    @BindView(R.id.vs_showerror)
    ViewStub vs_showerror;
    @BindView(R.id.tv_question_det)
    BridgeWebView tvQuestionDet;
    String url;
    String title;
    private int myScreenWidth;
    private int myScreenHeight;
    private String latitude;
    private String longitude;
    private String city;

    private String mLastPhothPath;
    private String mCurrentPhotoPath;


    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;


    UMImage urlImage;
    UMWeb web;

    private Thread mThread;

    Context mContext;

    String acceptTypes;

    private boolean ShowTitleBar = false;

    //微信支付
    private IWXAPI api;


    //mHandler
    private static final int SDK_PAY_FLAG = 3;
    private static final int SDK_AUTH_FLAG = 4;


    //onActivityResult
    /*onShowFileChooser调用*/
    private static final int REQUEST_CODE_CAMERA = 4;
    private static final int REQUEST_CODE_ALBUM = 5;
    /*ocr*/
    private static final int CODE_GALLERY_REQUEST = 6;
    private static final int CODE_CAMERA_REQUEST = 7;
    /*bridge调用*/
    private static final int BRIDGE_TAKE_VIDEO = 8;
    private static final int REQUEST_CODE_ALBUM_ALL = 9;


    //onRequestPermissionsResult
    private static final int REQUEST_CODE_PERMISSION_CAMERA = 6;
    public static final int REQUEST_CODE_PERMISSION_VIDEO = 7;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 8;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 9;
    private static final int DOWNLOADING_REQUEST_CODE = 2;
    private File fileUri = null;
    private File fileCropUri = null;
    private Uri imageUri = null;
    private Uri cropImageUri = null;

    //正反面标识
    private int zjtype = 0;
    private String downloadurl;
    private LocationService locationService;
    private DownLoadingView downLoadingView;

    @Override
    protected int getContentView() {
        return R.layout.activity_question_detailes;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }


    @Override
    protected void initEventAndData() {
        commonTheme();
        //关键下面一句
        AndroidBug5497Workaround.assistActivity(this);
        mContext = this;
        initScreenWidth(getBaseContext());
        ActivityCollector.addActivity(this);
        title = getIntent().getStringExtra("title");
        toolbarTitle.setText(title);
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        iv_right.setVisibility(View.GONE);
        url = getIntent().getStringExtra("url");
        if (url.contains("ticketAllowance")) {
            if (url.contains("?")) {
                url = url + "&nv=1";
            } else {
                url = url + "?nv=1";
            }
        }

        if (url.contains("jv=1")) {
            toolbar.setVisibility(View.GONE);
            ShowTitleBar = true;
        }

        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(EnvConstant.WX_KEY);
        addWxPlayReceiver();

        //默认接收
        tvQuestionDet.setDefaultHandler(new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String msg = "接收到的数据：" + data;
                toast(msg);
                function.onCallBack("java默认接收完毕，并回传数据给js"); //回传数据给js
            }
        });
        //原生加载页面
        tvQuestionDet.registerHandler("nativeOpenWebview", new BridgeHandler() {
            @Override
            public void handler(String s, CallBackFunction callBackFunction) {
                String backData = "";
                if (!EmptyUtils.isEmpty(s)) {
                    backData = nativeInteractionResult(0, true, null, "打开成功");
                    TargetClick.targetOnClick(getBaseContext(), s);
                } else {
                    backData = nativeInteractionResult(1, false, "数据为空，请检查", "打开失败");
                    //toast("空数据");
                }
                callBackFunction.onCallBack(backData);
            }
        });

        tvQuestionDet.registerHandler("nativeCloseWebview", new BridgeHandler() {
            @Override
            public void handler(String s, CallBackFunction callBackFunction) {
                finish();
                callBackFunction.onCallBack(nativeInteractionResult(0, true, null, "关闭成功"));
            }
        });

        tvQuestionDet.registerHandler("nativeClosePage", new BridgeHandler() {
            @Override
            public void handler(String s, CallBackFunction callBackFunction) {
                finish();
                callBackFunction.onCallBack(nativeInteractionResult(0, true, null, "关闭成功"));
            }
        });

        //原生开启标题栏
        tvQuestionDet.registerHandler("nativeShowTitleBar", new BridgeHandler() {
            @Override
            public void handler(String s, CallBackFunction callBackFunction) {
                String backData = "";
                if (EmptyUtils.isEmpty(s)) {
                    backData = nativeInteractionResult(1, false, "数据为空，请检查......", "标题栏未隐藏");
                } else {
                    setSupportActionBar(toolbar);
                    android.support.v7.app.ActionBar actionBar = getSupportActionBar();
                    if (s.trim().equals("true")) {
                        if (actionBar != null) {
                            actionBar.setDisplayShowTitleEnabled(false);
                            actionBar.show();
                        }
                        ShowTitleBar = false;
                    } else {
                        if (actionBar != null) {
                            actionBar.hide();
                        }
                        ShowTitleBar = true;
                    }
                    backData = nativeInteractionResult(0, true, null, "标题栏已改变");
                }
                callBackFunction.onCallBack(backData);
            }
        });
        userShare();
        userWxPay();
        userAliPay();
        userOcrIDCard();
        userLocationInfo();
        userTakePhoto();
        userChooseAlbum();
        userTakeVideo();
        userVersionCheck();
        userDeviceInfo();
        userOpenLogin();
        userAppVersion();
        userCallPhone();
        userSendSms();
        userSetNavTitle();
        userOpenPageWithScheme();
        userOpenPage();
        userOpenApp();
        userOpenBrowser();
        userOpenAppstore();
        userOpenSetting();
        WebSettings ws = tvQuestionDet.getSettings();
        ws.setDefaultTextEncodingName("UTF-8");
        ws.setBuiltInZoomControls(false);//  隐藏缩放按钮
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//  排版适应屏幕
        ws.setUseWideViewPort(true);//
        ws.setLoadWithOverviewMode(true);//  setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws.setSavePassword(true);
        ws.setSaveFormData(true);//  保存表单数据
        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);// 设置可以访问文件
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setLoadsImagesAutomatically(true);//支持自动加载图片
        ws.setGeolocationEnabled(true);//  启用地理定位
        ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");//  设置定位的数据库路径
        ws.setDomStorageEnabled(true); // 开启database storage API功能
        ws.setDatabaseEnabled(true);
        ws.setAllowUniversalAccessFromFileURLs(true);
        ws.setAppCacheEnabled(false);//设置缓存模式

        latitude = SharedPreferencesHelper.getStringSF(getBaseContext(), "lat");
        longitude = SharedPreferencesHelper.getStringSF(getBaseContext(), "lng");
        city = SharedPreferencesHelper.getStringSF(getBaseContext(), "locality");
        String maptype = "BD-09";
        if (latitude == null || longitude == null) {
            maptype = "";
            latitude = "";
            longitude = "";
        }
        if (city != null) {
            try {
                city = URLEncoder.encode(city, "utf-8");
            } catch (UnsupportedEncodingException e) {
                city = "";
            }
        } else {
            city = "";
        }
        //+"|"+longitude+","+latitude+","+maptype+"|"+city
        String ua = SystemUtil.getSystemVersion() + "|okhttp 3.3.6|android," + SystemUtil.getSystemVersion() + "|" + SystemUtil.getDeviceBrand() + "," + SystemUtil.getSystemModel() + "|" + myScreenWidth + "," + myScreenHeight + "|" + SystemUtil.getAppMetaData(getBaseContext(), "jyj_channel") + "|" + longitude + "," + latitude + "," + maptype + "|" + city;
        ws.setUserAgentString(ua);

        tvQuestionDet.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.d("ANDROID_LAB", "TITLE=" + title);
                //title 就是网页的title
                if (!EmptyUtils.isEmpty(title)) {
                    if (title.contains("新冠肺炎")) {
                        toolbarTitle.setText("新冠肺炎疫情实时播报");
                    } else {
                        toolbarTitle.setText(title);
                    }
                }
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
                acceptTypes = acceptType;
                new PhotoView(getBaseContext(), tvQuestionDet, 1);
            }

            @Override
            @SuppressLint("NewApi")
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                //调用系统相机或者相册
                String[] acceptType = fileChooserParams.getAcceptTypes();
                if (acceptType.length > 0) {
                    acceptTypes = acceptType[0];
                } else {
                    acceptTypes = null;
                }
                new PhotoView(getBaseContext(), tvQuestionDet, 1);
                return true;
            }
        });
        syncCookie(getBaseContext(), url);
        tvQuestionDet.loadUrl(url);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 获取屏幕的参数，宽度和高度
     */
    private void initScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        myScreenHeight = metrics.heightPixels;
        myScreenWidth = metrics.widthPixels;
    }

    @OnClick({R.id.toolbar
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:

                break;
        }
    }

    private void userShare() {
        tvQuestionDet.registerHandler("nativeShare", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String backData;
                try {
                    Type type = new TypeToken<UserShareBean>() {
                    }.getType();
                    UserShareBean userShareBean = new Gson().fromJson(data, type);
                    if (!EmptyUtils.isEmpty(userShareBean) && !EmptyUtils.isEmpty(userShareBean.getUrl()) && !EmptyUtils.isEmpty(userShareBean.getTitle()) && !EmptyUtils.isEmpty(userShareBean.getPlatformType())) {
                        backData = nativeInteractionResult(0, true, null, "开始分享");
                        if (userShareBean.getPlatformType().equals("SINA")) {
                            ShareImage(SHARE_MEDIA.SINA, userShareBean.getUrl(), userShareBean.getTitle(), userShareBean.getContent(), userShareBean.getIcon());
                        } else if (userShareBean.getPlatformType().equals("WECHATSESSION")) {
                            ShareImage(SHARE_MEDIA.WEIXIN, userShareBean.getUrl(), userShareBean.getTitle(), userShareBean.getContent(), userShareBean.getIcon());
                        } else if (userShareBean.getPlatformType().equals("WECHATTIMELINE")) {
                            ShareImage(SHARE_MEDIA.WEIXIN_CIRCLE, userShareBean.getUrl(), userShareBean.getTitle(), userShareBean.getContent(), userShareBean.getIcon());
                        } else if (userShareBean.getPlatformType().equals("QQ")) {
                            ShareImage(SHARE_MEDIA.QQ, userShareBean.getUrl(), userShareBean.getTitle(), userShareBean.getContent(), userShareBean.getIcon());
                        } else {
                            backData = nativeInteractionResult(1, false, "分享渠道有误，请检查......", "分享失败");
                        }
                    } else {
                        backData = nativeInteractionResult(1, false, "数据为空，请检查......", "分享失败");
                    }
                } catch (Exception e) {
                    backData = nativeInteractionResult(1, false, "数据有误，请检查......", "分享失败");
                }
                function.onCallBack(backData); //回传数据给js
            }
        });
    }

    private void userWxPay() {
        tvQuestionDet.registerHandler("nativeWxPay", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String backData = "";
                try {
                    Type type = new TypeToken<UserRechargeWxBean>() {
                    }.getType();
                    UserRechargeWxBean userPayWxBean = new Gson().fromJson(data, type);
                    if (!EmptyUtils.isEmpty(userPayWxBean)) {
                        backData = nativeInteractionResult(0, true, null, "开始支付");
                        PayReq req = new PayReq();
                        //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                        req.appId = userPayWxBean.getAppId();
                        req.partnerId = EnvConstant.WX_PARTNERID;
                        req.prepayId = userPayWxBean.getPrypayId();
                        req.nonceStr = userPayWxBean.getNonceStr();
                        req.timeStamp = userPayWxBean.getTimeStamp();
                        req.packageValue = userPayWxBean.getPackageX();
                        req.sign = userPayWxBean.getPaySign();
                        api.sendReq(req);
                    } else {
                        backData = nativeInteractionResult(1, false, "数据为空，请检查", "支付失败");
                    }
                } catch (Exception e) {
                    backData = nativeInteractionResult(1, false, "数据有误，请检查", "支付失败");
                }
                function.onCallBack(backData); //回传数据给js
            }
        });
    }


    private void userAliPay() {
        tvQuestionDet.registerHandler("nativeAliPay", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String backData = null;
                if (!EmptyUtils.isEmpty(data)) {
                    backData = nativeInteractionResult(0, true, null, "开始支付");
                    pay(data);
                } else {
                    backData = nativeInteractionResult(1, false, "数据为空，请检查", "支付失败");
                }
                function.onCallBack(backData); //回传数据给js
            }
        });
    }


    private void userOcrIDCard() {
        tvQuestionDet.registerHandler("nativeOcrIDCard", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String backData = null;
                if (!EmptyUtils.isEmpty(data)) {
                    backData = nativeInteractionResult(0, true, null, "开始识别");
                    if (data.trim().equals("front")) {
                        zjtype = 1;
                    } else if (data.trim().equals("back")) {
                        zjtype = 2;
                    }
                    hideInput();
                    new PhotoView(getBaseContext(), tvQuestionDet, 2);
                } else {
                    backData = nativeInteractionResult(1, false, "数据为空，请检查", "识别失败");
                }
                function.onCallBack(backData); //回传数据给js
            }
        });
    }

    private void userLocationInfo() {
        tvQuestionDet.registerHandler("nativeLocation", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                QuestionDetailesActivityPermissionsDispatcher.showLocationWithCheck(QuestionDetailesActivity.this);
                String backData = nativeInteractionResult(0, true, null, "正在获取定位信息");
                function.onCallBack(backData); //回传数据给js
            }
        });
    }


    private void userTakePhoto() {
        tvQuestionDet.registerHandler("nativeTakePhoto", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                fileUri = new File(DataKeeper.imagePath + "photo" + System.currentTimeMillis() + ".jpg");
                autoObtainCameraPermission();
                zjtype = 3;
                String backData = nativeInteractionResult(0, true, null, "正在拍照");
                function.onCallBack(backData); //回传数据给js
            }
        });
    }

    private void userChooseAlbum() {
        tvQuestionDet.registerHandler("nativeChooseAlbum", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String backData;
                if (EmptyUtils.isEmpty(data)) {
                    backData = nativeInteractionResult(1, false, "数据为空，请检查", "文件类型为空");
                } else {
                    chooseAlbumPic1(data);
                    backData = nativeInteractionResult(0, true, null, "正在拍视频");
                }
                function.onCallBack(backData); //回传数据给js
            }
        });
    }

    private void userTakeVideo() {
        tvQuestionDet.registerHandler("nativeTakeVideo", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                takeVideo1();
                String backData = nativeInteractionResult(0, true, null, "正在拍视频");
                function.onCallBack(backData); //回传数据给js
            }
        });
    }

    private void userVersionCheck() {
        tvQuestionDet.registerHandler("nativeVersionCheck", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                getPresenter().getVersionResult();
                String backData = nativeInteractionResult(0, true, null, "正在检测");
                function.onCallBack(backData); //回传数据给js
            }
        });
    }

    private void userDeviceInfo() {
        tvQuestionDet.registerHandler("nativeDeviceInfo", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                DeviceInfoBean deviceInfoBean = new DeviceInfoBean();
                deviceInfoBean.setDeviceBrand(SystemUtil.getDeviceBrand());
                deviceInfoBean.setMyScreenHeight(myScreenHeight + "");
                deviceInfoBean.setMyScreenWidth(myScreenWidth + "");
                deviceInfoBean.setSystemModel(SystemUtil.getSystemModel());
                deviceInfoBean.setSystemVersion(SystemUtil.getSystemVersion());
                deviceInfoBean.setDeviceImei(getIMEI(getBaseContext()));
                deviceInfoBean.setDeviceImsi(getIMSI(getBaseContext()));
                String backData = nativeInteractionResult(0, true, new Gson().toJson(deviceInfoBean), "获取成功");
                function.onCallBack(backData); //回传数据给js
            }
        });
    }


    private void userOpenLogin() {
        tvQuestionDet.registerHandler("nativeLogin", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                openLogin();
                String backData = nativeInteractionResult(0, true, null, "正在呼叫");
                function.onCallBack(backData); //回传数据给js
            }
        });
    }

    private void userAppVersion() {
        tvQuestionDet.registerHandler("nativeAppVersion", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String backData = nativeInteractionResult(0, true, ConstUtils.getVersioncode(getBaseContext()) + "", "获取成功");
                function.onCallBack(backData); //回传数据给js
            }
        });
    }

    private void userCallPhone() {
        tvQuestionDet.registerHandler("nativeCallPhone", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String backData;
                if (!EmptyUtils.isEmpty(data)) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri telUri = Uri.parse("tel:" + data);
                        intent.setData(telUri);
                        startActivity(intent);
                        backData = nativeInteractionResult(0, true, null, "正在呼叫");
                    } catch (Exception e) {
                        backData = nativeInteractionResult(1, false, "电话格式不正确", "呼叫失败");
                    }
                } else {
                    backData = nativeInteractionResult(1, false, "数据为空，请检查", "呼叫失败");
                }
                function.onCallBack(backData); //回传数据给js
            }
        });
    }


    private void userSendSms() {
        tvQuestionDet.registerHandler("nativeSendSms", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String backData = null;
                if (!EmptyUtils.isEmpty(data)) {
                    if (PhoneNumberUtils.isGlobalPhoneNumber(data)) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + data));
                        intent.putExtra("sms_body", "");
                        startActivity(intent);
                    }
                    backData = nativeInteractionResult(0, true, null, "发送成功");
                } else {
                    backData = nativeInteractionResult(1, false, "数据为空，请检查", "发送失败");
                }
                function.onCallBack(backData); //回传数据给js
            }
        });
    }


    private void userSetNavTitle() {
        tvQuestionDet.registerHandler("nativeSetNavTitle", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String backData = null;
                if (!EmptyUtils.isEmpty(data)) {
                    toolbarTitle.setText(data);
                    backData = nativeInteractionResult(0, true, null, "标题设置成功");
                } else {
                    backData = nativeInteractionResult(1, false, "数据为空，请检查", "标题设置失败");
                }
                function.onCallBack(backData); //回传数据给js
            }
        });
    }

    private void userOpenPageWithScheme() {
        tvQuestionDet.registerHandler("nativeOpenPageWithScheme", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String backData = null;
                if (!EmptyUtils.isEmpty(data)) {
                    backData = nativeInteractionResult(0, true, null, "开始打开");
                    TargetClick.targetOnClick(getBaseContext(), data);
                } else {
                    backData = nativeInteractionResult(1, false, "数据为空，请检查", "打开失败");
                }
                function.onCallBack(backData); //回传数据给js
            }
        });
    }

    private void userOpenPage() {
        tvQuestionDet.registerHandler("nativeOpenPage", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String backData = null;
                if (!EmptyUtils.isEmpty(data)) {
                    ComponentName com = new ComponentName(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID + data); //package;class
                    if (EmptyUtils.isEmpty(com)) {
                        backData = nativeInteractionResult(1, false, "暂无此页面", "打开失败");
                    } else {
                        backData = nativeInteractionResult(0, true, null, "开始打开");
                        Intent intent = new Intent();
                        intent.setComponent(com);
                        startActivity(intent);
                    }
                } else {
                    backData = nativeInteractionResult(1, false, "数据为空，请检查", "打开失败");
                }
                function.onCallBack(backData); //回传数据给js
            }
        });
    }


    private void userOpenBrowser() {
        tvQuestionDet.registerHandler("nativeOpenBrowser", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String backData = null;
                if (!EmptyUtils.isEmpty(data)) {
                    backData = nativeInteractionResult(0, true, null, "开始打开");
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(data);
                    intent.setData(content_url);
                    startActivity(intent);
                } else {
                    backData = nativeInteractionResult(1, false, "数据为空，请检查", "打开网页失败");
                }
                function.onCallBack(backData); //回传数据给js
            }
        });
    }

    private void userOpenApp() {
        tvQuestionDet.registerHandler("nativeOpenApp", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String backData = null;
                if (!EmptyUtils.isEmpty(data)) {
                    if (OpenOtherAppUtil.checkPackInfo(QuestionDetailesActivity.this, data)) {
                        OpenOtherAppUtil.openPackage(QuestionDetailesActivity.this, data);
                        backData = nativeInteractionResult(0, true, null, "开始打开");
                    } else {
                        backData = nativeInteractionResult(1, false, "此应用未安装", "打开失败");
                    }
                } else {
                    backData = nativeInteractionResult(1, false, "数据为空，请检查", "打开网页失败");
                }
                function.onCallBack(backData); //回传数据给js
            }
        });
    }

    private void userOpenAppstore() {
        tvQuestionDet.registerHandler("nativeOpenAppstore", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String backData;
                try {
                    backData = nativeInteractionResult(0, true, null, "开始打开");
                    AppMarketUtils.gotoMarket(getBaseContext());
                } catch (Exception e) {
                    backData = nativeInteractionResult(1, false, "此应用未上线", "打开失败");
                }
                function.onCallBack(backData); //回传数据给js
            }
        });
    }

    private void userOpenSetting() {
        tvQuestionDet.registerHandler("nativeOpenSetting", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String backData = nativeInteractionResult(0, true, null, "开始打开");
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.fromParts("package", getBaseContext().getPackageName(), null));
                if (intent.resolveActivity(getBaseContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
                function.onCallBack(backData); //回传数据给js
            }
        });
    }

    /**
     * 给WebView同步Cookie
     *
     * @param context 上下文
     * @param url     可以使用[domain][host]
     */
    private void syncCookie(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();// 移除旧的[可以省略]
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(tvQuestionDet, true);//TODO 跨域cookie读取
        }
        SPUtils spUtils = new SPUtils(Constant.COOKIE);
        String cookie = spUtils.getString(Constant.COOKIE);
        if (!EmptyUtils.isEmpty(cookie)) {
            cookieManager.setCookie(url, cookie);
            CookieSyncManager.getInstance().sync();
        } else {
            if (login != null) {
                showLog = true;
                getPresenter().getUserLoginResult(login.getPhone(), "", "", login.getToken());
            } else {
                openLogin();
            }
        }
    }


    @Override
    public void showErrorMsg(String msg, int code) {
        super.showErrorMsg(msg, code);
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
    public void refreshUserTimeResult(BaseResponseInfo result) {
        if (result.getCode() == 0) {
            syncCookie(getBaseContext(), url);
        } else {
            openLogin();
        }
    }

    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            syncCookie(getBaseContext(), url);
            tvQuestionDet.loadUrl(url);
        } else {
            openLogin();
        }
    }


    @Override
    public void setVersionResult(VersionShowBean showBean) {
        if (showBean != null) {
            if (showBean.getVersion() > ConstUtils.getVersioncode(getBaseContext())) {
                downloadurl = showBean.getUrl();
                downLoadingView = new DownLoadingView(getBaseContext(), tvQuestionDet, showBean);
                downLoadingView.setUserPromptPopupWindowInterface(this);
            } else {
                new UptodateWindowView(getBaseContext(), tvQuestionDet);
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
        QuestionDetailesActivityPermissionsDispatcher.showReadOrWriteWithCheck(QuestionDetailesActivity.this, url);
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    takePhoto();
                    break;
                case 2:
                    takeVideo();
                    break;

                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */

                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    String msgResult;
                    int msgCode;
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        msgResult = "支付成功";
                        msgCode = 0;
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        msgResult = "取消支付";
                        msgCode = 2;
                    } else if (TextUtils.equals(resultStatus, "4000")) {
                        msgResult = "支付失败";
                        msgCode = 1;
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        msgResult = "支付异常，请在消费历史查看！";
                        msgCode = 1;
                    }
                    tvQuestionDet.callHandler("JSHNativeAliPay", nativeInteractionResult(msgCode, msgCode == 0 ? true : false, resultInfo, msgResult), new CallBackFunction() {
                        @Override
                        public void onCallBack(String data) { //处理js回传的数据
                            Log.i("Result", data);
                        }
                    });
                }
                break;
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        toast("授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()));
                    } else {
                        // 其他状态值则为授权失败
                        toast("授权失败" + String.format("authCode:%s", authResult.getAuthCode()));
                    }
                    break;
                }
                default:
                    break;

            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ALBUM || requestCode == REQUEST_CODE_CAMERA) {
            if (uploadMessage == null && uploadMessageAboveL == null) {
                return;
            }
            //取消拍照或者图片选择时
            if (resultCode != RESULT_OK) {
                //一定要返回null,否则<input file> 就是没有反应
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }
                if (uploadMessageAboveL != null) {
                    uploadMessageAboveL.onReceiveValue(null);
                    uploadMessageAboveL = null;

                }
            }
            //拍照成功和选取照片时
            if (resultCode == RESULT_OK) {
                Uri imageUri = null;
                switch (requestCode) {
                    case REQUEST_CODE_ALBUM:
                        if (data != null) {
                            imageUri = data.getData();
                        }
                        break;
                    case REQUEST_CODE_CAMERA:
                        if (!EmptyUtils.isEmpty(mCurrentPhotoPath)) {
                            File file = new File(mCurrentPhotoPath);
                            Uri localUri = Uri.fromFile(file);
                            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                            sendBroadcast(localIntent);
                            imageUri = Uri.fromFile(file);
                            mLastPhothPath = mCurrentPhotoPath;
                        }
                        break;
                }

                //上传文件
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(imageUri);
                    uploadMessage = null;
                }
                if (uploadMessageAboveL != null) {
                    uploadMessageAboveL.onReceiveValue(new Uri[]{imageUri});
                    uploadMessageAboveL = null;
                }
            }
        } else if (requestCode == 1 && resultCode == 1) {
            syncCookie(getBaseContext(), url);
            tvQuestionDet.loadUrl(url);
        } else if (requestCode == 2) {
            syncCookie(getBaseContext(), url);
            tvQuestionDet.loadUrl(url);
        } else if (requestCode == CODE_CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap bitmap = PhotoUtils.getBitmapFromUri(imageUri, this);
            if (bitmap != null) {
                if (zjtype == 1) {
                    ocr(fileUri, IDCardParams.ID_CARD_SIDE_FRONT);
                } else if (zjtype == 2) {
                    ocr(fileUri, IDCardParams.ID_CARD_SIDE_BACK);
                } else if (zjtype == 3) {
                    tvQuestionDet.callHandler("JSHNativeFilePath", nativeInteractionResult(0, true, fileUri.getPath(), "拍照成功"), new CallBackFunction() {
                        @Override
                        public void onCallBack(String data) { //处理js回传的数据
                            Log.i("shareResult", data);
                        }
                    });
                }
            }
        } else if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK) {
            if (hasSdcard()) {
                Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                fileUri = new File(newUri.getPath());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    newUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", fileUri);
                }
                Bitmap bitmap1 = PhotoUtils.getBitmapFromUri(newUri, this);
                if (bitmap1 != null) {
                    if (zjtype == 1) {
                        ocr(fileUri, IDCardParams.ID_CARD_SIDE_FRONT);
                    } else if (zjtype == 2) {
                        ocr(fileUri, IDCardParams.ID_CARD_SIDE_BACK);
                    }
                }
            } else {
                toast("设备没有SD卡！");
            }
        } else if (requestCode == BRIDGE_TAKE_VIDEO && resultCode == RESULT_OK) {
            if (!EmptyUtils.isEmpty(mCurrentPhotoPath)) {
                File file = new File(mCurrentPhotoPath);
                Uri localUri = Uri.fromFile(file);
                Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                sendBroadcast(localIntent);
                tvQuestionDet.callHandler("JSHNativeFilePath", nativeInteractionResult(0, true, mCurrentPhotoPath, "录制成功"), new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) { //处理js回传的数据
                        Log.i("Result", data);
                    }
                });
            }
        } else if (requestCode == REQUEST_CODE_ALBUM_ALL && resultCode == RESULT_OK) {
            tvQuestionDet.callHandler("JSHNativeFilePath", nativeInteractionResult(0, true, data.getData().getPath(), "文件路径"), new CallBackFunction() {
                @Override
                public void onCallBack(String data) { //处理js回传的数据
                    Log.i("Result", data);
                }
            });
        } else if (requestCode == 10012) {
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


    public String nativeInteractionResult(int code, boolean status, String infoStr, String msg) {
        Gson gson = new Gson();
        NativeInteractionResultBean userShareBackResultBean = new NativeInteractionResultBean();
        userShareBackResultBean.setCode(code);
        userShareBackResultBean.setStatus(status);
        if (code == 0) {
            userShareBackResultBean.setData(infoStr);
        } else {
            userShareBackResultBean.setDetail(infoStr);
        }
        userShareBackResultBean.setMsg(msg);
        // String backData = gson.toJson(userShareBackResultBean);
        return gson.toJson(userShareBackResultBean);
    }


    private void ShareImage(SHARE_MEDIA share_media, String shareUrl, String shareTitle, String shareContent, String shareTitleIco) {
        urlImage = new UMImage(getBaseContext(), shareTitleIco);
        web = new UMWeb(shareUrl);
        web.setTitle(shareTitle);//标题
        web.setThumb(urlImage);  //缩略图
        web.setDescription(shareContent);//描述
        new ShareAction(QuestionDetailesActivity.this)
                .withMedia(web)
                .setPlatform(share_media)
                .setCallback(umShareListener)
                .share();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            tvQuestionDet.callHandler("JSHNativeShare", nativeInteractionResult(0, true, null, "分享成功"), new CallBackFunction() {
                @Override
                public void onCallBack(String data) { //处理js回传的数据
                    Log.i("shareResult", data);
                }
            });
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            tvQuestionDet.callHandler("JSHNativeShare", nativeInteractionResult(1, false, t.toString(), "分享失败"), new CallBackFunction() {
                @Override
                public void onCallBack(String data) { //处理js回传的数据
                    Log.i("shareResult", data);
                }
            });
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            tvQuestionDet.callHandler("JSHNativeShare", nativeInteractionResult(2, false, null, "取消分享"), new CallBackFunction() {
                @Override
                public void onCallBack(String data) { //处理js回传的数据
                    Log.i("shareResult", data);
                }
            });
        }
    };


    @Override
    public void onPause() {
        super.onPause();
        tvQuestionDet.onPause();
        tvQuestionDet.pauseTimers();
    }

    @Override
    public void onResume() {
        super.onResume();
        tvQuestionDet.resumeTimers();
        tvQuestionDet.onResume();
    }


    @Override
    protected void onDestroy() {
        tvQuestionDet.destroy();
        tvQuestionDet = null;
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        mThread = null;
        mHandler = null;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (ShowTitleBar) {
                tvQuestionDet.callHandler("JSHNativeBack", nativeInteractionResult(0, true, null, "物理返回"), new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {

                    }
                });

            }
        }
        return super.onKeyDown(keyCode, event);//退出H5界面
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        QuestionDetailesActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_CAMERA:
                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    // Permission Denied
                    new AlertDialog.Builder(mContext)
                            .setTitle("无法拍照")
                            .setMessage("您未授予拍照权限")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent localIntent = new Intent();
                                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                    localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                                    startActivity(localIntent);
                                }
                            }).create().show();
                }
                break;
            case REQUEST_CODE_PERMISSION_VIDEO:
                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takeVideo();
                } else {
                    // Permission Denied
                    new AlertDialog.Builder(mContext)
                            .setTitle("无法拍视频")
                            .setMessage("您未授予拍照权限")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent localIntent = new Intent();
                                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                    localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                                    startActivity(localIntent);
                                }
                            }).create().show();
                }
                break;
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(getBaseContext(), BuildConfig.APPLICATION_ID + ".fileProvider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        toast("设备没有SD卡！");
                    }
                } else {
                    toast("请允许打开相机！！");
                }
                break;

            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {
                    toast("请允许打操作SDCard！！");
                }
                break;
            case DOWNLOADING_REQUEST_CODE:
                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    startActivityForResult(intent, 10012);
                }
                break;
            default:
        }


    }

    /**
     * 拍照
     */
    private void takePhoto() {
        StringBuilder fileName = new StringBuilder();
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        fileName.append(System.currentTimeMillis()).append("_upload.png");
        File tempFile = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mCurrentPhotoPath = tempFile.getAbsolutePath();
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }


    private void takeVideo() {
        StringBuilder fileName = new StringBuilder();
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // set the video file name
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.5);
        fileName.append(System.currentTimeMillis()).append("_upload.mp4");
        File tempFile = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mCurrentPhotoPath = tempFile.getAbsolutePath();
        //开启摄像机
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }


    private void takeVideo1() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                toast("您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(getBaseContext(), BuildConfig.APPLICATION_ID + ".fileProvider", fileUri);
                }
                StringBuilder fileName = new StringBuilder();
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                // set the video file name
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.5);
                fileName.append(System.currentTimeMillis()).append("_upload.mp4");
                File tempFile = new File(DataKeeper.videoPath, fileName.toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                } else {
                    Uri uri = Uri.fromFile(tempFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                }
                mCurrentPhotoPath = tempFile.getAbsolutePath();
                //开启摄像机
                startActivityForResult(intent, BRIDGE_TAKE_VIDEO);
            } else {
                toast("设备没有SD卡！");
            }
        }
    }

    /**
     * 选择相册照片
     */
    private void chooseAlbumPic() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*;video/*");
        startActivityForResult(Intent.createChooser(i, "文件选择"), REQUEST_CODE_ALBUM);
    }

    /**
     * 选择文件
     */
    private void chooseAlbumPic1(String filetype) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            //"image/*;video/*"
            i.setType(filetype);
            startActivityForResult(Intent.createChooser(i, "文件选择"), REQUEST_CODE_ALBUM_ALL);
        }
    }

    public class PhotoView extends PopupWindow {

        public PhotoView(Context mContext, View parent, final int openType) {

            View view = View.inflate(mContext, R.layout.toast_keynote, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));
            // LinearLayout ll_popup = (LinearLayout) view
            // .findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
            // R.anim.push_bottom_in_1));

            setWidth(ActionBar.LayoutParams.FILL_PARENT);
            setHeight(ActionBar.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            TextView bt1 = (TextView) view.findViewById(R.id.item_popupwindows_camera);
            TextView bt2 = (TextView) view.findViewById(R.id.item_popupwindows_Photo);
            TextView bt3 = (TextView) view.findViewById(R.id.item_popupwindows_cancel);
            TextView bt4 = (TextView) view.findViewById(R.id.item_popupwindows_video);
            if (openType == 1) {
                bt4.setVisibility(View.VISIBLE);
            } else {
                bt4.setVisibility(View.GONE);
            }
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //请求拍照权限
                    if (openType == 1) {
                        if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            takePhoto();
                        } else {
                            ActivityCompat.requestPermissions(QuestionDetailesActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION_CAMERA);
                        }
                    } else if (openType == 2) {
                        fileUri = new File(DataKeeper.imagePath + "photo" + System.currentTimeMillis() + ".jpg");
                        autoObtainCameraPermission();
                    }
                    dismiss();
                }
            });

            bt4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //请求拍照权限
                    if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        takeVideo();
                    } else {
                        ActivityCompat.requestPermissions(QuestionDetailesActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION_VIDEO);
                    }
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (openType == 1) {
                        chooseAlbumPic();
                    } else if (openType == 2) {
                        fileUri = new File(DataKeeper.imagePath + "photo" + System.currentTimeMillis() + ".jpg");
                        autoObtainStoragePermission();
                    }
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (openType == 1) {
                        //一定要返回null,否则<input type='file'>
                        if (uploadMessage != null) {
                            uploadMessage.onReceiveValue(null);
                            uploadMessage = null;
                        }
                        if (uploadMessageAboveL != null) {
                            uploadMessageAboveL.onReceiveValue(null);
                            uploadMessageAboveL = null;
                        }
                    }
                    dismiss();
                }
            });

        }
    }


    BroadcastReceiver broadcastReceiver;

    private void addWxPlayReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int type = intent.getIntExtra("type", 2);
                String msgResult;
                int msgCode;
                if (type == 1) {
                    msgResult = "支付成功";
                    msgCode = 0;
                } else if (type == 3) {
                    msgResult = "取消支付";
                    msgCode = 2;
                } else {
                    msgResult = "支付失败";
                    msgCode = 1;
                }
                tvQuestionDet.callHandler("JSHNativeWxPay", nativeInteractionResult(msgCode, msgCode == 0 ? true : false, null, msgResult), new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) { //处理js回传的数据
                        Log.i("shareResult", data);
                    }
                });
            }
        };
        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction("wxplay");
        registerReceiver(broadcastReceiver, intentToReceiveFilter);
    }


    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(final String ranking) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(QuestionDetailesActivity.this);
                String rankings = ranking;
                if (rankings == null) {
                } else {
                    Map<String, String> result = alipay.payV2(rankings, true);
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }


            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                toast("您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(getBaseContext(), BuildConfig.APPLICATION_ID + ".fileProvider", fileUri);
                }
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                toast("设备没有SD卡！");
            }
        }
    }

    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    public void ocr(File filePath, final String idCardSide) {
        // 通用文字识别参数设置

        IDCardParams param = new IDCardParams();
        param.setImageFile(filePath);
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(20);

        OCR.getInstance(this).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                CardInfoBean cardInfoBean = new CardInfoBean();
                if (result != null) {
                    if (idCardSide.equals(IDCardParams.ID_CARD_SIDE_FRONT)) {
                        if (!EmptyUtils.isEmpty(result.getAddress())) {
                            cardInfoBean.setAddress(result.getAddress().toString());
                        }
                        if (!EmptyUtils.isEmpty(result.getBirthday())) {
                            cardInfoBean.setBirthday(result.getBirthday().toString());
                        }
                        if (!EmptyUtils.isEmpty(result.getGender())) {
                            cardInfoBean.setGender(result.getGender().toString());
                        }
                        if (!EmptyUtils.isEmpty(result.getEthnic())) {
                            cardInfoBean.setEthnic(result.getEthnic().toString());
                        }
                        if (!EmptyUtils.isEmpty(result.getName())) {
                            cardInfoBean.setName(result.getName().toString());
                        }
                        if (!EmptyUtils.isEmpty(result.getIdNumber())) {
                            cardInfoBean.setIdCard(result.getIdNumber().toString());
                        }
                    } else if (idCardSide.equals(IDCardParams.ID_CARD_SIDE_BACK)) {
                        if (!EmptyUtils.isEmpty(result.getSignDate())) {
                            cardInfoBean.setSignDate(result.getSignDate().toString());
                        }
                        if (!EmptyUtils.isEmpty(result.getExpiryDate())) {
                            cardInfoBean.setExpiryDate(result.getExpiryDate().toString());
                        }
                        if (!EmptyUtils.isEmpty(result.getIssueAuthority())) {
                            cardInfoBean.setIssueAuthority(result.getIssueAuthority().toString());
                        }
                    }
                    tvQuestionDet.callHandler("JSHNativeOcrIDCard", nativeInteractionResult(0, true, new Gson().toJson(cardInfoBean), "OCR识别成功"), new CallBackFunction() {
                        @Override
                        public void onCallBack(String data) { //处理js回传的数据
                            Log.i("shareResult", data);
                        }
                    });
                } else {
                    tvQuestionDet.callHandler("JSHNativeOcrIDCard", nativeInteractionResult(1, false, null, "OCR识别失败"), new CallBackFunction() {
                        @Override
                        public void onCallBack(String data) { //处理js回传的数据
                            Log.i("shareResult", data);
                        }
                    });
                }
            }

            @Override
            public void onError(OCRError error) {
               /* if (idCardSide.equals(IDCardParams.ID_CARD_SIDE_FRONT)) {
                    iv_nc_identity_number_zm.setImageResource(R.mipmap.identity_number_zm);
                } else if (idCardSide.equals(IDCardParams.ID_CARD_SIDE_BACK)) {
                    iv_nc_identity_number_fm.setImageResource(R.mipmap.identity_number_fm);
                }*/
                toast("证件解析失败，请手动输入！");
            }
        });
    }


    /**
     * 获取手机IMEI
     *
     * @param context
     * @return
     */
    public static final String getIMEI(Context context) {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMEI号
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "";
            }
            String imei = telephonyManager.getDeviceId();
            //在次做个验证，也不是什么时候都能获取到的啊
            if (imei == null) {
                imei = "";
            }
            Log.i("getIMEI", "getIMEI: " + imei);
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取手机IMSI
     */
    public static String getIMSI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMSI号
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "";
            }
            String imsi = telephonyManager.getSubscriberId();
            if (null == imsi) {
                imsi = "";
            }
            return imsi;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showReadOrWrite(String downloadurl) {
        if (!EmptyUtils.isEmpty(downLoadingView)) {
            downLoadingView.dismiss();
        }
        SoftUpdate manager = new SoftUpdate(QuestionDetailesActivity.this, downloadurl);
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


    // 单个权限
    // @NeedsPermission(Manifest.permission.CAMERA)
    // 多个权限
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE})
    void showLocation() {
        locationService = new LocationService(getBaseContext());
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
        //Toast.makeText(this, "小主，给个权限吧！", Toast.LENGTH_SHORT).show();
        tvQuestionDet.callHandler("JSHNativeLocation", nativeInteractionResult(1, false, "用户拒绝授予权限", "定位信息获取失败"), new CallBackFunction() {
            @Override
            public void onCallBack(String data) { //处理js回传的数据
                Log.i("Result", data);
            }
        });
    }

    // 用户勾选了“不再提醒”时调用（可选）
    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE})
    void showNeverAskForLocation() {
        //Toast.makeText(this, "很开心，以后不打扰你了！", Toast.LENGTH_SHORT).show();
        tvQuestionDet.callHandler("JSHNativeLocation", nativeInteractionResult(1, false, "用户拒绝授予权限", "定位信息获取失败"), new CallBackFunction() {
            @Override
            public void onCallBack(String data) { //处理js回传的数据
                Log.i("Result", data);
            }
        });
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
                System.out.println("--------反馈信息----------lat" + location.getLatitude() + "|lng" + location.getLongitude() + "|" + location.getProvince() + location.getCity());
                LocationInfoBean locationInfoBean = new LocationInfoBean();
                locationInfoBean.setAddress(location.getAddrStr());
                locationInfoBean.setCity(location.getCity());
                locationInfoBean.setCountry(location.getCountry());
                locationInfoBean.setDistrict(location.getDistrict());
                locationInfoBean.setLat(location.getLatitude() + "");
                locationInfoBean.setLng(location.getLongitude() + "");
                locationInfoBean.setProvince(location.getProvince());
                locationInfoBean.setStreet(location.getStreet());
                tvQuestionDet.callHandler("JSHNativeLocation", nativeInteractionResult(0, true, new Gson().toJson(locationInfoBean), "定位信息获取成功"), new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) { //处理js回传的数据
                        if (locationService != null) {
                            locationService.stop();
                        }
                        Log.i("Result", data);
                    }
                });
            }
        }
    };
}


