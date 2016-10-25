package com.circlrnum.flymountain.customrecycle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者 yuanfei on 2016/10/18.
 * 邮箱 yuanfei221@126.com
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private int mLayout;//布局文件

    private OnItemClickListener mOnItemClickLitener;//点击事件

    private OnItemLongClickListener mOnItemLongClickListener;//长按点击事件

    private ArrayList<T> list = new ArrayList<>();//数据

    protected Context mContext;

    /**
     * 设置数据
     * @param list
     */
    public void setData(ArrayList<T> list) {
        this.list = list;
    }

    /**
     * 添加数据
     * @param list
     */
    public void addData(ArrayList<T> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public void clearData(){
        if (list!=null&&list.size()!=0)
        list.clear();
    }

    public List<T> getDataList() {
        return list;
    }


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickLitener = mOnItemClickListener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener)
    {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayout = getLayout();
        View view = LayoutInflater.from(mContext).inflate(mLayout, parent, false);
        BaseRecyclerViewHolder holder = createViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickLitener.onItemClick(v, position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    mOnItemLongClickListener.onItemLongClick(v, position);
                    return true;
                }
            });
        }
        showData(holder,position,list);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //设置布局
    public abstract int getLayout();

    /**
     * 点击事件
     * 包括点击事件和长按事件
     */
    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(View view, int position);
    }

    /**
     * 数据绑定
     * @param holder
     * @param i 位置
     * @param list 数据
     */
    public abstract void showData(BaseRecyclerViewHolder holder,int i,List<T> list);

    /**
     *
     * @param view item_frag_home_drag 的view
     * @return RecyclerViewHolderBase 基类ViewHolder
     */
    public abstract BaseRecyclerViewHolder createViewHolder(View view);
}
