package com.shadowsong.recyclerviewadapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhoumo on 16/3/11.
 */
public abstract class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public RecyclerViewHolder(ViewGroup parent, int res) {
        this(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));
    }

    public RecyclerViewHolder(View view) {
        super(view);
        onInitView(view);
    }

    public abstract void onInitView(View view);

    abstract public void fillData(int position, Object data);

    public void onItemClick(View view, int position) {
    }

    public boolean onItemLongClick(View view, int position) {
        return false;
    }

}
