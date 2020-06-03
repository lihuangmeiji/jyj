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

import com.blankj.utilcode.util.EmptyUtils;
import com.idougong.jyj.R;
import com.idougong.jyj.model.VersionShowBean;

public class DownLoadingView extends PopupWindow {

    private DownLoadingViewInterface userPromptPopupWindowInterface;

    public DownLoadingViewInterface getUserPromptPopupWindowInterface() {
        return userPromptPopupWindowInterface;
    }

    public void setUserPromptPopupWindowInterface(DownLoadingViewInterface userPromptPopupWindowInterface) {
        this.userPromptPopupWindowInterface = userPromptPopupWindowInterface;
    }

    public DownLoadingView(Context mContext, View parent, final VersionShowBean versionShowBean) {

        View view = View.inflate(mContext, R.layout.down_loading, null);
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
        ImageView iv_dialog_cancel = view.findViewById(R.id.iv_dialog_cancel);
        TextView btn_dialog_confirm = (TextView) view.findViewById(R.id.btn_dialog_confirm);
        TextView btn_dialog_cancel = (TextView) view.findViewById(R.id.btn_dialog_cancel);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        if (!EmptyUtils.isEmpty(versionShowBean)) {
            tv_content.setText(versionShowBean.getMsg().replace("\\n", "\n"));
            if (versionShowBean.getForceUpdate() == 1) {
                iv_dialog_cancel.setVisibility(View.GONE);
            } else {
                iv_dialog_cancel.setVisibility(View.VISIBLE);
            }
            btn_dialog_confirm.setVisibility(View.VISIBLE);
        } else {
            btn_dialog_confirm.setVisibility(View.GONE);
        }
        iv_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPromptPopupWindowInterface.setCancelOnClickListener();
            }
        });
        btn_dialog_confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                userPromptPopupWindowInterface.setConfirmOnClickListener(versionShowBean.getUrl());
            }
        });
    }

    /**
     * 点击事件
     */
    public interface DownLoadingViewInterface {
        void setCancelOnClickListener();

        void setConfirmOnClickListener(String url);
    }
}
