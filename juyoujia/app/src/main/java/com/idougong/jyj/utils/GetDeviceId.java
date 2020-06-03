package com.idougong.jyj.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.security.MessageDigest;

public class GetDeviceId {
    /**
     * 获取设备唯一标识符
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        //为了统一格式对设备的唯一标识进行md5加密 最终生成32位字符串
        String m_szDevIDShort = "jyj" +
                Build.BOARD.length() % 10 +   //主板
                Build.BRAND.length() % 10 +   //品牌
                Build.CPU_ABI.length() % 10 + //CPU
                Build.DEVICE.length() % 10 +  //设备
                Build.DISPLAY.length() % 10 + //显示
                Build.HOST.length() % 10 +   //主机
                Build.ID.length() % 10 +  //id
                Build.MANUFACTURER.length() % 10 + //生产商
                Build.MODEL.length() % 10 + //终端用户可见的设备型号
                Build.PRODUCT.length() % 10 + //产品的名称
                Build.TAGS.length() % 10 +  //设备标签
                Build.TYPE.length() % 10 +  //设备版本类型
                Build.USER.length() % 10 + // 用户权限
                Build.FINGERPRINT+
                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) + //ANDROID_ID
                getIMEI(context); //IMEI
        return getMD5(m_szDevIDShort, false);
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
     * 对挺特定的 内容进行 md5 加密
     *
     * @param message   加密明文
     * @param upperCase 加密以后的字符串是是大写还是小写  true 大写  false 小写
     * @return
     */
    public static String getMD5(String message, boolean upperCase) {
        String md5str = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] input = message.getBytes();

            byte[] buff = md.digest(input);

            md5str = bytesToHex(buff, upperCase);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str;
    }

    public static String bytesToHex(byte[] bytes, boolean upperCase) {
        StringBuffer md5str = new StringBuffer();
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        if (upperCase) {
            return md5str.toString().toUpperCase();
        }
        return md5str.toString().toLowerCase();
    }

}
