package com.shadowsong.recyclerviewadapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.shadowsong.recyclerviewadapter.data.ListWrapper;
import com.shadowsong.recyclerviewadapter.data.OnDataPrepareListener;
import com.shadowsong.recyclerviewadapter.viewholder.RecyclerViewHolder;


/**
 * Created by zhoumo on 16/3/11.
 */
abstract public class RecyclerAdapter<T extends RecyclerViewHolder> extends RecyclerView.Adapter<T>
        implements OnDataPrepareListener {

    protected static final int HOLDER_TYPE_PLACEHOLDER = 0;
    protected static final int HOLDER_TYPE_ERROR = 1;
    protected static final int HOLDER_TYPE_LOADING_MORE = 2;
    protected static final int HOLDER_TYPE_LOADING_MORE_ERROR = 3;

    protected static final int HOLDER_INDEX_OFFSET = 4;

    protected boolean isError = false;


    protected ListWrapper adapterData;

    private Runnable loadMoreRunnable = new Runnable() {
        @Override
        public void run() {
            adapterData.loadMore();
        }
    };

    public void setData(ListWrapper data) {
        adapterData = data;
        adapterData.setOnDataPrepareListener(this);
    }

    protected boolean usePlaceholder() {
        return false;
    }

    protected boolean useErrorHolder() {
        return false;
    }

    public ListWrapper getData() {
        return adapterData;
    }

    @Override
    public void onBindViewHolder(final T holder, final int position) {
        holder.fillData(position, adapterData.dataAt(position));
        if (adapterData.hasMoreData() && position + 1 == getItemCount() && !isError) {
            holder.itemView.removeCallbacks(loadMoreRunnable);
            holder.itemView.post(loadMoreRunnable);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.onItemClick(v, position);

                if (isError) {
                    if (adapterData.hasMoreData() && position + 1 == getItemCount()) {
                        adapterData.loadMore();
                    }
                    else if (useErrorHolder() && noData()){
                        adapterData.load();
                    }
                }
                notifyDataSetChanged();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return holder.onItemLongClick(v, position);
            }
        });
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HOLDER_TYPE_PLACEHOLDER) {
            return onCreatePlaceholder(parent);
        } else if (viewType == HOLDER_TYPE_LOADING_MORE) {
            return onCreateLoadingMoreViewHolder(parent);
        } else if (viewType == HOLDER_TYPE_LOADING_MORE_ERROR) {
            return onCreateLoadingMoreErrorViewHolder(parent);
        } else if (viewType == HOLDER_TYPE_ERROR) {
            return onCreateErrorHolder(parent);
        } else {
            return onCreateContentViewHolder(parent, viewType - HOLDER_INDEX_OFFSET);
        }
    }

    /**
     * create your content
     * @param parent
     * @param viewType
     * @return
     */
    abstract protected T onCreateContentViewHolder(ViewGroup parent, int viewType);

    /**
     * used when data is loading
     * @param parent
     * @return
     */
    protected T onCreatePlaceholder(ViewGroup parent) {
        return null;
    }

    /**
     * used when having more data
     * @param parent
     * @return
     */
    protected T onCreateLoadingMoreViewHolder(ViewGroup parent) {
        return null;
    }

    /**
     * used when having more data but loaded failed
     * @param parent
     * @return
     */
    protected T onCreateLoadingMoreErrorViewHolder(ViewGroup parent) {
        return null;
    }

    /**
     * used when data loaded failed
     * @param parent
     * @return
     */
    protected T onCreateErrorHolder(ViewGroup parent) {
        return null;
    }

    protected int getPlaceholderCount() {
        return 10;
    }

    protected int getErrorHolderCount() {
        return 1;
    }

    @Override
    final public int getItemViewType(int position) {
        // 此处判断顺序必须和getItemCount一致
        if (usePlaceholder() && noData() && adapterData.status() == ListWrapper.STATUS_LOADING) {
            return HOLDER_TYPE_PLACEHOLDER;
        } else if (useErrorHolder() && isError && noData()) {
            return HOLDER_TYPE_ERROR;
        } else if (adapterData.hasMoreData() && position + 1 == getItemCount()) {
            if (isError) {
                return HOLDER_TYPE_LOADING_MORE_ERROR;
            } else {
                return HOLDER_TYPE_LOADING_MORE;
            }
        } else {
            return HOLDER_INDEX_OFFSET + itemViewType(position, adapterData.dataAt(position));
        }

    }

    private boolean noData() {
        return adapterData.size() == 0;
    }

    protected int itemViewType(int position, Object data) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (usePlaceholder() && noData() && adapterData.status() == ListWrapper.STATUS_LOADING) {
            return getPlaceholderCount();
        }

        if (useErrorHolder() && isError && noData()) {
            return getErrorHolderCount();
        }
        return adapterData.hasMoreData() ? adapterData.size() + 1 : adapterData.size();
    }

    @Override
    public void onPrepared(ListWrapper data) {
        isError = data.status() == ListWrapper.STATUS_ERROR;
        notifyDataSetChanged();
    }

    @Override
    public void onLoad(ListWrapper data) {
        isError = false;
        notifyDataSetChanged();
    }

    @Override
    public void onLoadMore(ListWrapper data) {
        isError = false;
//        notifyDataSetChanged();
    }
}
