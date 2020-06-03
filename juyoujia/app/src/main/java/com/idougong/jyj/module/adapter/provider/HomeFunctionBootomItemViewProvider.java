package com.idougong.jyj.module.adapter.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idougong.jyj.R;

import me.drakeet.multitype.ItemViewBinder;

public class HomeFunctionBootomItemViewProvider extends ItemViewBinder<String, HomeFunctionBootomItemViewProvider.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        View root = layoutInflater.inflate(R.layout.item_home_bottom, viewGroup, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder viewHolder, @NonNull String s) {
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
