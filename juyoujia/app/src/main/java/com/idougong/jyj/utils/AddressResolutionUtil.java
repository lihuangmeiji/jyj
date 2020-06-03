package com.idougong.jyj.utils;

import android.annotation.SuppressLint;

import com.idougong.jyj.module.ui.account.LoginActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressResolutionUtil {
    /**
     * 解析地址
     *
     * @param address
     * @return
     * @author lin
     */

    public static String addressResolution(String address) {
        String regex = "([^省]+自治区|.*?省|.*?行政区|.*?市)([^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)";
        Matcher m = Pattern.compile(regex).matcher(address);
        if (m.find()) {
            return m.group(0);
        }
        return null;
    }
}
