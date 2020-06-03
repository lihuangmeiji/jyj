package com.idougong.jyj.module.ui.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idougong.jyj.R;
import com.idougong.jyj.common.net.Constant;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserShareBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.NativeWebDealWithContract;
import com.idougong.jyj.module.presenter.NativeWebDealWithPresenter;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.SharedPreferencesHelper;
import com.idougong.jyj.widget.SystemUtil;
import com.umeng.socialize.UMShareAPI;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.OnClick;

public class NativeWebDealWithActivity extends BaseActivity<NativeWebDealWithPresenter> implements NativeWebDealWithContract.View {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView iv_right;
    @BindView(R.id.vs_showerror)
    ViewStub vs_showerror;
    @BindView(R.id.wv_content)
    WebView wvContent;
    String url;
    String title;
    private int myScreenWidth;
    private int myScreenHeight;
    private String latitude;
    private String longitude;
    private String city;


    @Override
    protected int getContentView() {
        return R.layout.activity_native_web_deal_with;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }


    @Override
    protected void initEventAndData() {
        commonTheme();
        initScreenWidth(getBaseContext());
        ActivityCollector.addActivity(this);
        title = getIntent().getStringExtra("title");
        toolbarTitle.setText(title);
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        iv_right.setVisibility(View.GONE);
        url = getIntent().getStringExtra("url");
        WebSettings ws = wvContent.getSettings();
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
        wvContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.d("ANDROID_LAB", "TITLE=" + title);
                //title 就是网页的title
                if (!EmptyUtils.isEmpty(title)) {
                    toolbarTitle.setText(title);
                }
            }
        });
        wvContent.setWebViewClient(new MyWebViewClient());
        syncCookie(getBaseContext(), url);
        wvContent.loadUrl(url);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wvContent.canGoBack()) {
                    wvContent.goBack();//返回上个页面
                    return;
                } else {
                    finish();
                }
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
            cookieManager.setAcceptThirdPartyCookies(wvContent, true);//TODO 跨域cookie读取
        }
        SPUtils spUtils = new SPUtils(Constant.COOKIE);
        String cookie = spUtils.getString(Constant.COOKIE);
        if (!EmptyUtils.isEmpty(cookie)) {
            cookieManager.setCookie(url, cookie);
            CookieSyncManager.getInstance().sync();
        }
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        // 在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边。这个函数我们可以做很多操作，比如我们读取到某些特殊的URL，于是就可以不打开地址，取消这个操作，进行预先定义的其他操作，这对一个程序是非常必要的。
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 判断url链接中是否含有某个字段，如果有就执行指定的跳转（不执行跳转url链接），如果没有就加载url链接
            return false;
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
        }else if (code == -2) {
            openLogin();
            return;
        }  else if (code == -10) {
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
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            syncCookie(getBaseContext(), url);
            wvContent.loadUrl(url);
        } else {
            openLogin();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            syncCookie(getBaseContext(), url);
            wvContent.loadUrl(url);
        }
        if (requestCode == 2) {
            syncCookie(getBaseContext(), url);
            wvContent.loadUrl(url);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        wvContent.onPause();
        wvContent.pauseTimers();
    }

    @Override
    public void onResume() {
        super.onResume();
        wvContent.resumeTimers();
        wvContent.onResume();
    }

    @Override
    protected void onDestroy() {
        wvContent.destroy();
        wvContent = null;
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wvContent.canGoBack()) {
            wvContent.goBack();//返回上个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);//退出H5界面
    }
}
