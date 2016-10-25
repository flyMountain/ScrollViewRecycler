package com.circlrnum.flymountain.customrecycle;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 作者 yuanfei on 2016/10/17.
 * 邮箱 yuanfei221@126.com
 */

public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
    private View root;
    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        root=itemView;
    }
    public View findView(int id){
        return root.findViewById(id);
    }
}
