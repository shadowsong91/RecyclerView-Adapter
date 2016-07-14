package com.shadowsong.recyclerviewadapter.data;

/**
 * Created by zhoumo on 16/3/11.
 */
public interface OnDataPrepareListener {

  void onPrepared(ListWrapper data);

  void onLoad(ListWrapper data);

  void onLoadMore(ListWrapper data);

}
