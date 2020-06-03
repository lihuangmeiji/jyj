package com.idougong.jyj.module.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.SearchResultBean;

public class SearchResultAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

    public SearchResultAdapter(int layoutResId) {
        super(layoutResId);
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, String str) {
        TextView searchResult = baseViewHolder.getView(R.id.tv_searchresult);
        searchResult.setText(str);
    }
}
