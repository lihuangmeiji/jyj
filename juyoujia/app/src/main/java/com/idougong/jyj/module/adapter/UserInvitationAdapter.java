package com.idougong.jyj.module.adapter;

import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.UserInvitationPeopleBean;
import com.idougong.jyj.widget.CircleImage2View;

public class UserInvitationAdapter extends BaseQuickAdapter<UserInvitationPeopleBean, BaseViewHolder> {

    public UserInvitationAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, UserInvitationPeopleBean userInvitationPeopleBean) {
        CircleImage2View iv_invitation_ico=baseViewHolder.getView(R.id.iv_invitation_ico);
        Glide.with(mContext)
                .load(userInvitationPeopleBean.getIcon())
                .asBitmap()
                .error(R.mipmap.userphotomr)
                .into(iv_invitation_ico);
        baseViewHolder.setText(R.id.tv_invitation_phone,userInvitationPeopleBean.getPhone());
        baseViewHolder.setText(R.id.tv_invitation_time, TimeUtils.date2String(TimeUtils.string2Date(userInvitationPeopleBean.getCreateAt()),"yyyy-MM-dd"));
    }
}
