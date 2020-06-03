package com.idougong.jyj.widget.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.idougong.jyj.R;

public class ReminderLoadingView extends PopupWindow {
    public ReminderLoadingView(final Context mContext, View parent, int type) {
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
            String content = "因为您关闭了居有家APP的手机存储权限,请在设置中打开!";
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
                intent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);
                }
            }
        });
    }
}
