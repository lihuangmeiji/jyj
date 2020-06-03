package com.idougong.jyj.module.adapter.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.idougong.jyj.R;
import com.idougong.jyj.model.UserFunctionBean;
import com.idougong.jyj.model.UserServiceFunctionListBean;
import com.idougong.jyj.module.adapter.UserFunctionAdapter;

import java.util.List;

import me.drakeet.multitype.ItemViewBinder;

public class UserServiceFunctionItemViewProvider extends ItemViewBinder<UserServiceFunctionListBean,UserServiceFunctionItemViewProvider.ViewHolder> {
    private UserServiceFunctionInterface userServiceFunctionInterface;

    public UserServiceFunctionInterface getUserServiceFunctionInterface() {
        return userServiceFunctionInterface;
    }

    public void setUserServiceFunctionInterface(UserServiceFunctionInterface userServiceFunctionInterface) {
        this.userServiceFunctionInterface = userServiceFunctionInterface;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        View root = layoutInflater.inflate(R.layout.item_user_service_function, viewGroup, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull UserServiceFunctionListBean userFunctionBean) {
        viewHolder.setPosts(userFunctionBean.getUserFunctionBeanList(),getUserServiceFunctionInterface());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerView;
        private UserFunctionAdapter adapter;
        private UserServiceFunctionInterface userServiceFunctionInterface;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.post_list);
            recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(),3));
            /* adapter 只负责灌输、适配数据，布局交给 LayoutManager，可复用 */
            adapter = new UserFunctionAdapter(R.layout.item_user_function_division);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    userServiceFunctionInterface.setServiceFunctionOnClickListener(adapter.getItem(i));
                }
            });
        }

        private void setPosts(List<UserFunctionBean> posts,UserServiceFunctionInterface userServiceFunctionInterface) {
            adapter.getData().clear();
            adapter.addData(posts);
            adapter.notifyDataSetChanged();
            this.userServiceFunctionInterface=userServiceFunctionInterface;
        }
    }

    /**
     * 改变数量的接口
     */
    public interface UserServiceFunctionInterface {
        void setServiceFunctionOnClickListener(UserFunctionBean userServiceFunctionBean);
    }
}
