package com.wjj.easy.easyandroid.http;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.wjj.easy.easyandroid.R;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * HTTP网络配置
 * 1.OkHttpClient配置
 * 2.Retrofit配置
 * 3.Builder构建初始化数据
 * Created by wujiajun on 17/4/5.
 */
public class Http {

    /*BuildConfig.DEBUG*/
    private static final boolean DEBUG = true;

    private HttpBuilder mBuilder;
    private OkHttpClient mClient;
    private Retrofit mRetrofit;
    SSLSocketFactory sslSocketFactory=null;
    private static final String CLIENT_KET_PASSWORD = "gyj9527666";

    Context context;
    public Http(HttpBuilder builder, Context context) {
        this.context=context;
        mBuilder = builder;
        configOKHttp();
        configRetrofit();
    }

    private void configOKHttp() {
        try {
            SSLContext sslContext =  trustManagerForCertificates(trustedCertificatesInputStream(R.raw.gyj));
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (DEBUG) {
            builder.addInterceptor(loggingInterceptor);
        }
        for (Interceptor interceptor : mBuilder.interceptors) {
            builder.addInterceptor(interceptor);
        }
        mClient = builder.retryOnConnectionFailure(true)
                .connectTimeout(mBuilder.timeout, TimeUnit.SECONDS)
                .sslSocketFactory(sslSocketFactory)
                .build();

        //cookieJar(mBuilder.cookieJar)
    }

    private void configRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(mBuilder.baseUrl)
                .client(mClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * HTTP 构建类
     */
    public static final class HttpBuilder {
        private String baseUrl;
        private ClearableCookieJar cookieJar;
        private long timeout;
        private ArrayList<Interceptor> interceptors = new ArrayList<Interceptor>();

        public HttpBuilder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public HttpBuilder setCookieJar(ClearableCookieJar cookieJar) {
            this.cookieJar = cookieJar;
            return this;
        }

        public HttpBuilder setTimeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        public HttpBuilder addInterceptor(Interceptor interceptor) {
            interceptors.add(interceptor);
            return this;
        }

        public Http build(Context context) {
            return new Http(this,context);
        }
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public OkHttpClient getClient() {
        return mClient;
    }


    private InputStream trustedCertificatesInputStream(int rrId) {
        return context.getResources().openRawResource(rrId) ;
    }


    private SSLContext trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException, IOException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        // Put the certificates a key store.
        char[] password = CLIENT_KET_PASSWORD.toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        //  keyStore.load(in,CLIENT_KET_PASSWORD.toCharArray());
        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }

        SSLContext ssContext = SSLContext.getInstance("SSL");
        ssContext.init(keyManagerFactory.getKeyManagers(),trustManagers,null);
        //return (X509TrustManager) trustManagers[0];
        return  ssContext;
    }

    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
