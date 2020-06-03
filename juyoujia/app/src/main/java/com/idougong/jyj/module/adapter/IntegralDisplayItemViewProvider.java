package com.idougong.jyj.module.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.idougong.jyj.R;
import com.idougong.jyj.model.HomeConfigurationInformationBean;
import com.idougong.jyj.model.LoginBean;

import me.drakeet.multitype.ItemViewBinder;

public class IntegralDisplayItemViewProvider extends ItemViewBinder<String, IntegralDisplayItemViewProvider.ViewHolder> {

    private LoginBean loginBean;

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    private HomeConfigurationInformationBean homeConfigurationInformationBean;

    public HomeConfigurationInformationBean getHomeConfigurationInformationBean() {
        return homeConfigurationInformationBean;
    }

    public void setHomeConfigurationInformationBean(HomeConfigurationInformationBean homeConfigurationInformationBean) {
        this.homeConfigurationInformationBean = homeConfigurationInformationBean;
    }


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        View root = layoutInflater.inflate(R.layout.item_integral_display, viewGroup, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull String s) {
           if(getLoginBean()!=null){
               viewHolder.tvAggregateScore.setText(loginBean.getPoint()+"");
               viewHolder.tvDayScore.setText(loginBean.getToDayPoints()+"");
           }else{
               viewHolder.tvAggregateScore.setText(0+"");
               viewHolder.tvDayScore.setText(0+"");
           }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull private final TextView tvAggregateScoreTs;
        @NonNull private final TextView tvDayScoreTs;
        @NonNull private final TextView tvAggregateScore;
        @NonNull private final TextView tvDayScore;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvAggregateScore = (TextView) itemView.findViewById(R.id.tv_aggregate_score);
            this.tvDayScore=(TextView)itemView.findViewById(R.id.tv_day_score);
            this.tvAggregateScoreTs=(TextView)itemView.findViewById(R.id.tv_aggregate_score_ts);
            this.tvDayScoreTs=(TextView)itemView.findViewById(R.id.tv_day_score_ts);
        }
    }

}
