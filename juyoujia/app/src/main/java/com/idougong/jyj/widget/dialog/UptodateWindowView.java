package com.idougong.jyj.widget.dialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.idougong.jyj.R;

public class UptodateWindowView extends PopupWindow {
    public UptodateWindowView(Context mContext, View parent) {

        View view = View.inflate(mContext, R.layout.uptodate_window, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));

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
        TextView uptpdateAlert = (TextView) view.findViewById(R.id.uptodate_alert);
        uptpdateAlert.setText("亲，当前已是最新版本\n" + "不用升级哦！");

        ImageView cancel_button = (ImageView) view.findViewById(R.id.iv_uptodatewindnow_cancel);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
