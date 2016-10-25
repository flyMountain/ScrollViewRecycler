package com.flymountain.scrollviewrecycle;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.circlrnum.flymountain.customrecycle.BaseRecyclerAdapter;
import com.circlrnum.flymountain.customrecycle.BaseRecyclerViewHolder;

import java.util.List;


/**
 * 作者 yuanfei on 2016/10/17.
 * 邮箱 yuanfei221@126.com
 */

public class RecyclerAdapter extends BaseRecyclerAdapter<String> implements BaseRecyclerAdapter.OnItemClickListener {
    private Context context;

    public  RecyclerAdapter(Context context){
        this.context = context;
    }
    @Override
    public int getLayout() {
        return R.layout.item_home;
    }

    /**
     * 数据绑定
     * @param holder
     * @param i
     * @param list
     */
    @Override
    public void showData(BaseRecyclerViewHolder holder, int i, List<String> list) {
        final RecycleHoldView holdView = (RecycleHoldView) holder;
        holdView.tv.setText(list.get(i));
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(View view) {
        setOnItemClickListener(this);
        return new RecycleHoldView(view);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.e("position",position+"");
    }


    class RecycleHoldView extends BaseRecyclerViewHolder{

        TextView tv;

        public RecycleHoldView(View itemView) {
            super(itemView);
            tv = (TextView) findView(R.id.tv);
        }
    }
}
