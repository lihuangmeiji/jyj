package com.idougong.jyj.module.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idougong.jyj.R;
import com.idougong.jyj.model.HomeFunctionDivisionFour;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.IdentificationInfosBean;
import com.idougong.jyj.model.IdentificationInfosGListBean;
import com.idougong.jyj.widget.FlowLayoutManager;

import java.util.List;

import me.drakeet.multitype.ItemViewBinder;

public class IdentificationContentGItemViewProvider extends ItemViewBinder<IdentificationInfosGListBean,IdentificationContentGItemViewProvider.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        View root = layoutInflater.inflate(R.layout.item_user_function, viewGroup, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull IdentificationInfosGListBean identificationInfosGListBean) {
        viewHolder.setPosts(identificationInfosGListBean.getIdentificationInfosBeanList());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        private UserProfessionalAdapter adapter;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.post_list);
            recyclerView.setBackground(null);
            recyclerView.setLayoutManager(new FlowLayoutManager(itemView.getContext(),true));
            /* adapter 只负责灌输、适配数据，布局交给 LayoutManager，可复用 */
            adapter = new UserProfessionalAdapter(R.layout.item_user_professional);
            recyclerView.setAdapter(adapter);
        }

        private void setPosts(List<IdentificationInfosBean> posts) {
            adapter.getData().clear();
            adapter.addData(posts);
            adapter.notifyDataSetChanged();
        }
    }

}
